# 🚀 Integration Guide - Integrering Architecture Vào EcoSim

## Các Bước Cài Đặt

### 1. Cập Nhật EcoSim.java

```java
package com.ecosystem.sim;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.ecosystem.sim.entity.concrete.Rabbit;
import com.ecosystem.sim.entity.concrete.Wolf;
import com.ecosystem.sim.entity.concrete.Grass;
import com.ecosystem.sim.entity.concrete.Deer;
import com.ecosystem.sim.entity.concrete.Tiger;
import com.ecosystem.sim.entity.concrete.Tree;
import com.ecosystem.sim.map.MapManager;
import com.ecosystem.sim.ui.HUD;
import com.ecosystem.sim.util.EntityManager;
import com.ecosystem.sim.util.ResourceTracker;

public class EcoSim extends ApplicationAdapter implements InputProcessor {
    private OrthographicCamera camera;
    private MapManager mapManager;
    private EntityManager entityManager;
    private ResourceTracker resourceTracker;
    private HUD hud;
    private SpriteBatch batch;

    // Camera dragging
    private int lastMouseX = 0;
    private int lastMouseY = 0;
    private boolean isDragging = false;

    @Override
    public void create() {
        // === SETUP CAMERA ===
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        float mapCenterX = (50 * 16) / 2f;
        float mapCenterY = (50 * 16) / 2f;
        camera.position.set(mapCenterX, mapCenterY, 0);
        camera.zoom = 1.0f;
        camera.update();

        // === SETUP MAP ===
        mapManager = new MapManager("ecosystem.tmx", camera);

        // === SETUP ENTITY SYSTEMS ===
        entityManager = new EntityManager();
        resourceTracker = new ResourceTracker();
        batch = new SpriteBatch();

        // === SETUP HUD ===
        hud = new HUD(entityManager);

        // === LOAD TEXTURES ===
        Texture rabbitTexture = new Texture("rabbit.png");
        Texture wolfTexture = new Texture("wolf.png");
        Texture deerTexture = new Texture("deer.png");
        Texture tigerTexture = new Texture("tiger.png");
        Texture grassTexture = new Texture("grass.png");
        Texture treeTexture = new Texture("tree.png");

        // === SPAWN INITIAL ENTITIES ===
        // Thỏ
        for (int i = 0; i < 10; i++) {
            float x = (float) Math.random() * 600 + 100;
            float y = (float) Math.random() * 600 + 100;
            entityManager.addEntity(new Rabbit(x, y, rabbitTexture, mapManager));
        }

        // Sói
        for (int i = 0; i < 3; i++) {
            float x = (float) Math.random() * 600 + 100;
            float y = (float) Math.random() * 600 + 100;
            entityManager.addEntity(new Wolf(x, y, wolfTexture, mapManager));
        }

        // Hươu
        for (int i = 0; i < 5; i++) {
            float x = (float) Math.random() * 600 + 100;
            float y = (float) Math.random() * 600 + 100;
            entityManager.addEntity(new Deer(x, y, deerTexture, mapManager));
        }

        // Hổ (hiếm)
        entityManager.addEntity(new Tiger(300, 300, tigerTexture, mapManager));

        // Cỏ
        for (int i = 0; i < 50; i++) {
            float x = (float) Math.random() * 700;
            float y = (float) Math.random() * 700;
            entityManager.addEntity(new Grass(x, y, grassTexture, mapManager));
        }

        // Cây
        for (int i = 0; i < 15; i++) {
            float x = (float) Math.random() * 700;
            float y = (float) Math.random() * 700;
            entityManager.addEntity(new Tree(x, y, treeTexture, mapManager));
        }

        // === REGISTER INPUT ===
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // === UPDATE ===
        float deltaTime = Gdx.graphics.getDeltaTime();
        entityManager.update(deltaTime);
        resourceTracker.update(deltaTime);

        // === APPLY CAMERA BOUNDS ===
        applyBounds();
        camera.update();

        // === RENDER ===
        batch.setProjectionMatrix(camera.combined);

        mapManager.render();

        batch.begin();
        entityManager.render(batch);
        batch.end();

        hud.render(batch);
    }

    private void applyBounds() {
        float mapWidth = 50 * 16;
        float mapHeight = 50 * 16;
        float viewWidth = camera.viewportWidth * camera.zoom;
        float viewHeight = camera.viewportHeight * camera.zoom;

        float minX = viewWidth / 2;
        float maxX = mapWidth - viewWidth / 2;
        float minY = viewHeight / 2;
        float maxY = mapHeight - viewHeight / 2;

        camera.position.x = Math.max(minX, Math.min(camera.position.x, maxX));
        camera.position.y = Math.max(minY, Math.min(camera.position.y, maxY));
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
    }

    @Override
    public void dispose() {
        batch.dispose();
        mapManager.dispose();
        entityManager.dispose();
        hud.dispose();
    }

    // ============= INPUT HANDLERS =============

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.ESCAPE) {
            Gdx.app.exit();
        }
        if (keycode == Input.Keys.SPACE) {
            camera.zoom = 1.0f; // Reset zoom
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        isDragging = true;
        lastMouseX = screenX;
        lastMouseY = screenY;
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        isDragging = false;
        return true;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        isDragging = false;
        return true;
    }

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

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        mapManager.adjustZoom(amountY * 0.1f);
        return true;
    }
}
```

### 2. Thêm Textures vào Assets

Tạo các file texture (PNG) trong folder `assets/`:

- `rabbit.png` (16×16)
- `wolf.png` (24×24)
- `deer.png` (20×20)
- `tiger.png` (32×32)
- `grass.png` (16×16)
- `tree.png` (24×24)

Hoặc sử dụng `libgdx-texture-packer` để tạo sprite sheets.

### 3. Cập Nhật Gradle Dependencies

Thêm vào `core/build.gradle`:

```gradle
dependencies {
    // Đã có libgdx
    api "com.badlogicgames.gdx:gdx:$gdxVersion"
    // Thêm nếu cần
}
```

---

## 🎮 Chế Độ Chơi

### Keyboard Controls

- **ESC**: Thoát
- **SPACE**: Reset zoom
- **Mouse Drag**: Di chuyển camera
- **Mouse Scroll**: Zoom in/out

### HUD Display

- Số lượng động vật
- Số lượng thực vật
- FPS
- Trạng thái hệ sinh thái (Extinct, Stable, Stressed, etc.)

---

## 📊 Statistics & Analysis

### In-Game Metrics

```
Ecosystem Status:
- Total Animals: X
- Total Plants: X
- Predator/Prey Ratio: X%
- Food/Animal Ratio: X%
```

### File Logging (TODO)

```java
// Ghi log mỗi 5 giây vào file
"logs/ecosystem_[timestamp].csv"
Time, Rabbits, Wolves, Deer, Tigers, Grass, Trees, Status
0.0,  10,      3,      5,    1,      50,    15,    Balanced
5.0,  12,      3,      6,    1,      45,    15,    Balanced
10.0, 14,      4,      7,    1,      40,    14,    Stable
```

---

## 🔧 Debugging Tips

### 1. Render Debug Info

```java
// Thêm vào HUD.render()
font.draw(batch, "Debug: X=" + player.x + " Y=" + player.y, 10, 100);
```

### 2. Pause Simulation

```java
boolean isPaused = false;

@Override
public void render() {
    if (!isPaused) {
        entityManager.update(deltaTime);
    }
}

@Override
public boolean keyDown(int keycode) {
    if (keycode == Input.Keys.P) isPaused = !isPaused;
    return true;
}
```

### 3. Monitor Entity Count

```java
System.out.println("Animals: " + entityManager.getAnimalCount() +
                   " | Plants: " + entityManager.getPlantCount());
```

---

## 🎯 Next Steps

### Phase 1: Basic (Hoàn thành)

- ✅ Entity System
- ✅ Animal State Machine
- ✅ Predator-Prey Interaction
- ✅ Plant Growth

### Phase 2: Enhancement (TODO)

- [ ] BFS Pathfinding
- [ ] Breeding System
- [ ] Food Chain Balancing
- [ ] Save/Load System

### Phase 3: Advanced (TODO)

- [ ] Day/Night Cycle
- [ ] Weather System
- [ ] Genetic Algorithm
- [ ] Network Multiplayer
- [ ] Sound & Music

---

**Setup Time**: ~2 hours  
**Complexity**: Medium  
**Maintainability**: High (Modular Architecture)
