# 🔧 Troubleshooting & Integration Checklist

## Pre-Integration Checklist

Before integrating, ensure:

- [ ] LibGDX project is properly configured (gradle builds successfully)
- [ ] `ecosystem.tmx` map file exists in `assets/`
- [ ] MapManager.java is working correctly
- [ ] Camera zoom/pan controls work
- [ ] Can render simple shapes on screen

---

## Common Integration Issues & Solutions

### Issue 1: "Cannot find symbol: class Animal"

**Cause**: Java not seeing new entity package

```bash
❌ WRONG: import com.ecosystem.sim.Entity;
✅ RIGHT: import com.ecosystem.sim.entity.Animal;
         import com.ecosystem.sim.entity.concrete.Rabbit;
```

**Fix**:

- Check package names match exactly
- Run `gradle clean build`
- Invalidate IDE cache (if using IntelliJ)

---

### Issue 2: "NullPointerException when creating Animal"

**Cause**: Texture is null or mapManager is null

```java
❌ WRONG:
Texture tex = new Texture("rabbit.png");  // File not found → null
Rabbit rabbit = new Rabbit(x, y, tex, mapManager);

✅ RIGHT:
try {
    Texture tex = new Texture("rabbit.png");
    if (tex == null) throw new RuntimeException("Texture not found");
    Rabbit rabbit = new Rabbit(x, y, tex, mapManager);
} catch (Exception e) {
    System.err.println("Failed to load rabbit texture: " + e);
}
```

**Fix**:

- Verify texture files exist in `assets/` folder
- Check texture file paths are correct
- Ensure mapManager is initialized before creating animals

---

### Issue 3: Animals not rendering

**Cause**: Batch not using correct projection matrix or entities not drawn

```java
❌ WRONG:
batch.setProjectionMatrix(camera.combined);
mapManager.render();  // No batch begin/end!
entityManager.render(batch);

✅ RIGHT:
batch.setProjectionMatrix(camera.combined);
mapManager.render();
batch.begin();
entityManager.render(batch);
batch.end();
```

**Fix**:

- Ensure `batch.begin()` before rendering sprites
- Ensure `batch.end()` after rendering
- Verify camera.combined is set correctly
- Check ZOrder (render plants before animals, animals before UI)

---

### Issue 4: Animals moving out of bounds

**Cause**: Obstacle checking not working correctly

```java
// In MapManager:
public boolean isObstacle(float worldX, float worldY) {
    int tileX = (int)(worldX / (16 * unitScale));
    int tileY = (int)(worldY / (16 * unitScale));

    TiledMapTileLayer obstacleLayer = (TiledMapTileLayer) map.getLayers().get("Tile Layer 2");

    if (obstacleLayer != null && obstacleLayer.getCell(tileX, tileY) != null) {
        return true;
    }
    return false;
}
```

**Fix**:

- Verify layer name matches your Tiled map ("Tile Layer 2")
- Ensure obstacle tiles have correct IDs
- Test manually with: `System.out.println(mapManager.isObstacle(x, y))`

---

### Issue 5: No predator-prey interactions

**Cause**: EntityManager not updating interactions or ranges too small

```java
✅ Verify in EntityManager.updateInteractions():
// Sói phát hiện thỏ
if (wolf.detectPrey(animals) != null) {
    // Should be true if prey in range
}

// Check ranges:
System.out.println("Wolf hunting range: " + wolf.getHuntingRange());
System.out.println("Distance to rabbit: " + wolf.getPosition().dst(rabbit.getPosition()));
```

**Fix**:

- Increase hunting ranges in concrete animals
- Check that detect methods are implemented correctly
- Verify dominance values are set (Rabbit=10, Wolf=60)
- Make sure EntityManager.update() is called every frame

---

### Issue 6: Memory leak / FPS dropping over time

**Cause**: Entities not being disposed or too many being created

```java
✅ Fix 1: Ensure dispose() is called
@Override
public void dispose() {
    entityManager.dispose();  // Must call this!
    hud.dispose();
    batch.dispose();
}

✅ Fix 2: Limit entity spawning
if (entityManager.getAnimalCount() > MAX_ANIMALS) {
    return;  // Don't spawn more
}

✅ Fix 3: Remove dead entities immediately
entities.removeIf(e -> !e.isAlive());
```

---

### Issue 7: Camera dragging feels weird

**Cause**: Input handling overwriting each other or wrong delta calculation

```java
✅ Correct mouse dragging:
private boolean isDragging = false;
private int lastMouseX = 0;
private int lastMouseY = 0;

@Override
public boolean touchDragged(int screenX, int screenY, int pointer) {
    if (isDragging) {
        int deltaX = screenX - lastMouseX;
        int deltaY = screenY - lastMouseY;

        camera.position.x -= deltaX * camera.zoom;
        camera.position.y += deltaY * camera.zoom;

        lastMouseX = screenX;
        lastMouseY = screenY;
    }
    return true;
}
```

---

### Issue 8: HUD rendering incorrectly or off-screen

**Cause**: Projection matrix not reset for HUD or wrong batch mode

```java
✅ Correct HUD rendering:
// HUD should use screen coordinates, not world coordinates
public void render(SpriteBatch batch) {
    batch.begin();

    float x = 10;  // Screen pixels, not world units
    float y = Gdx.graphics.getHeight() - 10;

    font.draw(batch, "Animals: " + count, x, y);

    batch.end();
}

// In main render:
batch.setProjectionMatrix(camera.combined);
entityManager.render(batch);  // World coordinates

batch.setProjectionMatrix(uiCamera.combined);  // Or reset
hud.render(batch);  // Screen coordinates
```

---

## Performance Troubleshooting

### Symptom: FPS drops below 30

**Check these in order:**

1. **Entity count?**

   ```java
   System.out.println("Entities: " + entityManager.getAnimalCount() +
                      " + " + entityManager.getPlantCount());
   ```

   If > 300 total, reduce spawn rate or increase max age.

2. **Update time?**

   ```java
   long start = System.nanoTime();
   entityManager.update(deltaTime);
   long elapsed = (System.nanoTime() - start) / 1_000_000;  // ms
   System.out.println("Update: " + elapsed + "ms");
   ```

   If > 16ms, optimize using ZoneManager.

3. **Collision detection?**

   ```java
   // Disable interactions temporarily
   // entityManager.updateInteractions();  // Comment out
   ```

   If FPS improves, collision detection is bottleneck.

4. **Rendering?**
   ```java
   // Disable rendering
   // entityManager.render(batch);  // Comment out
   ```
   If FPS improves, rendering is bottleneck.

---

## Testing Checklist

### Basic Functionality

- [ ] Can spawn Rabbit at position (100, 100)
- [ ] Can spawn Wolf at position (200, 200)
- [ ] Both render on screen
- [ ] Both update position each frame
- [ ] Both lose energy over time

### Interactions

- [ ] Wolf can chase Rabbit
- [ ] Rabbit flees from Wolf
- [ ] Wolf catches Rabbit after time
- [ ] Wolf's energy increases after eating
- [ ] Rabbit dies on being eaten

### Plants

- [ ] Grass spawns and renders
- [ ] Grass grows through stages (Seed → Mature)
- [ ] Rabbit can eat Mature grass
- [ ] Grass disappears after being eaten
- [ ] New grass can grow

### AI

- [ ] Animals wander when idle
- [ ] Animals search for food when energy < 40%
- [ ] Animals search for water when hydration < 30%
- [ ] Animals flee when threatened
- [ ] Animals hunt when prey detected

### UI/UX

- [ ] HUD displays animal count
- [ ] HUD displays plant count
- [ ] HUD displays FPS
- [ ] HUD updates every frame
- [ ] Camera zoom works
- [ ] Camera drag works

---

## Debugging Tips

### Print state changes:

```java
@Override
protected void onStateChanged(AnimalState oldState, AnimalState newState) {
    System.out.println(this.getClass().getSimpleName() +
                       ": " + oldState + " → " + newState);
}
```

### Monitor specific animal:

```java
Rabbit targetRabbit = null;

@Override
public void render() {
    // Find first rabbit
    if (targetRabbit == null) {
        for (Entity e : entityManager.getEntities()) {
            if (e instanceof Rabbit) {
                targetRabbit = (Rabbit) e;
                break;
            }
        }
    }

    if (targetRabbit != null) {
        System.out.println("Rabbit - Health: " + targetRabbit.getHealth() +
                          " | Energy: " + targetRabbit.getEnergy() +
                          " | State: " + targetRabbit.getCurrentState());
    }
}
```

### Visualize collision boxes:

```java
public void renderDebug(ShapeRenderer shapeRenderer) {
    shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
    shapeRenderer.setColor(1, 0, 0, 1);  // Red

    for (Entity e : entityManager.getEntities()) {
        shapeRenderer.rect(e.getX(), e.getY(), e.getWidth(), e.getHeight());
    }

    shapeRenderer.end();
}

// In render():
Gdx.gl.glEnable(GL20.GL_BLEND);
ShapeRenderer shapeRenderer = new ShapeRenderer();
shapeRenderer.setProjectionMatrix(camera.combined);
renderDebug(shapeRenderer);
shapeRenderer.dispose();
```

---

## Step-by-Step Integration

### Phase 1: Get code compiling

1. Add all `.java` files to `src/main/java/com/ecosystem/sim/`
2. Run `gradle clean build`
3. Fix any import errors
4. Verify no compilation errors

### Phase 2: Basic rendering

1. Create EntityManager in EcoSim.create()
2. Spawn 1 Rabbit and 1 Wolf
3. Call entityManager.render() in render loop
4. Verify they appear on screen
5. Verify they move

### Phase 3: Add interactions

1. Call entityManager.update() in render loop
2. Watch them interact
3. Verify hunts happen
4. Verify eating happens

### Phase 4: Scale up

1. Spawn 10 Rabbits, 3 Wolves
2. Monitor FPS
3. Add HUD
4. Test various scenarios
5. Tune spawn rates

### Phase 5: Polish

1. Integrate custom textures
2. Add sounds (future)
3. Add UI controls
4. Save/load state (future)

---

## Final Verification Checklist

```
Before deployment:
- [ ] Game compiles without errors
- [ ] Game runs at 60 FPS
- [ ] Animals spawn correctly
- [ ] Predator-prey interactions work
- [ ] Plant growth works
- [ ] No memory leaks (check after 5 min)
- [ ] HUD displays correct stats
- [ ] Controls (zoom, drag) work
- [ ] No console errors/warnings
- [ ] Documentation is clear
- [ ] Code is well-commented
- [ ] All classes have javadoc
```

---

## Getting Help

### If stuck, check:

1. **Console output** - Read error messages carefully
2. **Stack trace** - Click on first red line for root cause
3. **Documentation files**:
   - `ARCHITECTURE.md` - System design
   - `INTEGRATION_GUIDE.md` - Step-by-step setup
   - `BEST_PRACTICES.md` - Code patterns

### Common search terms for issues:

- "LibGDX null pointer exception"
- "Gradle dependency resolution"
- "Java import errors"
- "Memory leak detection Java"

---

**Good luck! 🚀**

If you encounter an issue not listed here, check the console output first - it usually tells you exactly what's wrong.

Last Updated: May 19, 2026
