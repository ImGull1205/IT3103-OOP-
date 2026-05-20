# ⚡ Quick Start Guide - 5 Minute Setup

## 🚀 Get Running in 5 Minutes

### Step 1: Copy Files (1 minute)

Copy these folders into your project:

```
core/src/main/java/com/ecosystem/sim/
├── entity/          ← All new class files
├── ui/HUD.java
└── util/            ← All utility classes
```

### Step 2: Update EcoSim.java (2 minutes)

Replace your `EcoSim.java` create() method with this:

```java
@Override
public void create() {
    // Camera setup (keep your existing code)
    camera = new OrthographicCamera();
    camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    float mapCenterX = (50 * 16) / 2f;
    float mapCenterY = (50 * 16) / 2f;
    camera.position.set(mapCenterX, mapCenterY, 0);
    camera.zoom = 1.0f;
    camera.update();

    mapManager = new MapManager("ecosystem.tmx", camera);
    Gdx.input.setInputProcessor(this);

    // === NEW: Entity system ===
    entityManager = new EntityManager();
    batch = new SpriteBatch();
    hud = new HUD(entityManager);

    // === Load textures (use placeholder colors if no images) ===
    Texture rabbitTex = new Texture("rabbit.png");
    Texture wolfTex = new Texture("wolf.png");
    Texture grassTex = new Texture("grass.png");

    // === Spawn initial entities ===
    for (int i = 0; i < 10; i++) {
        float x = (float)Math.random() * 600 + 100;
        float y = (float)Math.random() * 600 + 100;
        entityManager.addEntity(new Rabbit(x, y, rabbitTex, mapManager));
    }

    for (int i = 0; i < 3; i++) {
        float x = (float)Math.random() * 600 + 100;
        float y = (float)Math.random() * 600 + 100;
        entityManager.addEntity(new Wolf(x, y, wolfTex, mapManager));
    }

    for (int i = 0; i < 50; i++) {
        float x = (float)Math.random() * 700;
        float y = (float)Math.random() * 700;
        entityManager.addEntity(new Grass(x, y, grassTex, mapManager));
    }
}
```

### Step 3: Update render() method (1 minute)

Add these 3 lines to your render() method:

```java
@Override
public void render() {
    Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    float deltaTime = Gdx.graphics.getDeltaTime();

    // === ADD THESE 3 LINES ===
    entityManager.update(deltaTime);
    applyBounds();
    camera.update();

    batch.setProjectionMatrix(camera.combined);
    mapManager.render();

    // === ADD THESE 3 LINES ===
    batch.begin();
    entityManager.render(batch);
    batch.end();
    hud.render(batch);
}
```

### Step 4: Add fields (1 minute)

Add these fields to your EcoSim class:

```java
private EntityManager entityManager;
private SpriteBatch batch;
private HUD hud;
```

### Step 5: Add dispose() cleanup

```java
@Override
public void dispose() {
    batch.dispose();
    mapManager.dispose();
    entityManager.dispose();
    hud.dispose();
}
```

---

## ✅ Test It Works

Run your project and you should see:

- ✅ 10 Rabbits spawning and wandering
- ✅ 3 Wolves spawning and hunting rabbits
- ✅ 50 grass patches growing
- ✅ Wolves chasing and eating rabbits
- ✅ HUD showing counts in top-left
- ✅ Camera zoom/drag working

---

## 🎮 Controls

| Key/Action       | Effect      |
| ---------------- | ----------- |
| **ESC**          | Exit        |
| **SPACE**        | Reset zoom  |
| **Mouse Drag**   | Pan camera  |
| **Mouse Scroll** | Zoom in/out |

---

## 📊 What's Happening

```
Every Frame (60x/second):
1. entityManager.update(deltaTime)
   - Animals lose energy (-15/s)
   - Animals lose hydration (-20/s)
   - Animals make decisions (every 0.5s)
   - Animals move based on state
   - Plants grow
   - Collisions detected

2. Interactions:
   - Wolves see rabbits → HUNTING
   - Rabbits see wolves → FLEEING
   - Collisions → Eating
   - Dead entities removed

3. Render:
   - Map tiles drawn
   - Sprites drawn (animals, plants)
   - HUD displayed
```

---

## 🎨 Customize Spawn Amounts

In `create()`:

```java
// Change these numbers to spawn different amounts:
for (int i = 0; i < 20; i++)      // More rabbits (was 10)
for (int i = 0; i < 5; i++)       // More wolves (was 3)
for (int i = 0; i < 100; i++)     // More grass (was 50)
```

---

## 🐛 If Something's Wrong

1. **Blank screen?**
   - Check textures exist in `assets/rabbit.png`, etc.
   - Check console for errors

2. **Crash on startup?**
   - Verify `ecosystem.tmx` exists
   - Check MapManager is initialized
   - Look at stack trace

3. **Animals not moving?**
   - Verify `entityManager.update()` is called
   - Check MapManager.isObstacle() returns correct values

4. **No interactions?**
   - Verify EntityManager.updateInteractions() is called
   - Check console for debug output

---

## 📖 Next Steps

After this works, explore:

- **BEST_PRACTICES.md** - How to add new species
- **ARCHITECTURE.md** - Deep dive into system design
- **TROUBLESHOOTING.md** - Fix specific issues

---

## 🎉 You're Done!

Your ecosystem is now running. Animals are:

- ✅ Being born (spawned)
- ✅ Living (updating stats)
- ✅ Hunting (predator-prey)
- ✅ Eating (plant/animal consumption)
- ✅ Dying (when health=0)

**Time to see evolution in action!** 🌍
