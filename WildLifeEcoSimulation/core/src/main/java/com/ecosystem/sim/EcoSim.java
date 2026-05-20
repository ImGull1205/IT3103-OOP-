package com.ecosystem.sim;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.ecosystem.sim.map.MapManager;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all
 * platforms.
 */
public class EcoSim extends ApplicationAdapter implements InputProcessor {
    private OrthographicCamera camera;
    private MapManager mapManager;
    private ShapeRenderer shapeRenderer;
    private com.ecosystem.sim.util.EntityManager entityManager;
    // Camera dragging
    private int lastMouseX = 0;
    private int lastMouseY = 0;
    private boolean isDragging = false;
    
    // Spawn qua thời gian thay vì một lúc
    private float spawnTimer = 0;
    private float spawnInterval = 0.1f; // Spawn 1 entity mỗi 0.1 giây
    private int rabbitCount = 0;
    private int wolfCount = 0;
    private int grassCount = 0;
    private int treeCount = 0;
    
    private static final int MAX_RABBITS = 20;    // Giảm từ 50
    private static final int MAX_WOLVES = 10;     // Giảm từ 36
    private static final int MAX_GRASS = 30;      // Giảm từ 100
    private static final int MAX_TREES = 2;

    @Override
    public void create() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Bản đồ: 50x50 tiles, mỗi tile 16px => 800x800 pixels
        float mapCenterX = (50 * 16) / 2f; // 400
        float mapCenterY = (50 * 16) / 2f; // 400
        camera.position.set(mapCenterX, mapCenterY, 0);
        camera.zoom = 1.0f;
        camera.update();

        mapManager = new MapManager("ecosystem.tmx", camera);
        
        // --- KHỞI TẠO HỆ THỐNG RENDERING ---
        shapeRenderer = new ShapeRenderer();
        entityManager = new com.ecosystem.sim.util.EntityManager(mapManager);
        
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Áp dụng bounds checking cho camera
        applyBounds();

        camera.update();
        mapManager.render();

        float deltaTime = Gdx.graphics.getDeltaTime();
        
        // Time-sliced Spawning
        spawnTimer += deltaTime;
        if (spawnTimer >= spawnInterval) {
            spawnTimer = 0;
            
            // Spawn vài con mỗi frame nếu chưa đủ
            if (rabbitCount < MAX_RABBITS) {
                com.badlogic.gdx.math.Vector2 pos = mapManager.findRandomSpawnLocation();
                entityManager.spawnRabbit(pos.x, pos.y);
                rabbitCount++;
            }
            if (wolfCount < MAX_WOLVES) {
                com.badlogic.gdx.math.Vector2 pos = mapManager.findRandomSpawnLocation();
                entityManager.spawnWolf(pos.x, pos.y);
                wolfCount++;
            }
            if (grassCount < MAX_GRASS) {
                com.badlogic.gdx.math.Vector2 pos = mapManager.findRandomSpawnLocation();
                entityManager.spawnGrass(pos.x, pos.y);
                grassCount++;
            }
            if (treeCount < MAX_TREES) {
                com.badlogic.gdx.math.Vector2 pos = mapManager.findRandomSpawnLocation();
                entityManager.spawnTree(pos.x, pos.y);
                treeCount++;
            }
        }
        
        // --- CẬP NHẬT VÀ VẼ CÁC ĐỘNG VẬT ---
        
        // Cho phép các con vật suy nghĩ và di chuyển
        entityManager.update(deltaTime); 
        
        // Thiết lập ShapeRenderer vẽ tại vị trí camera
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        
        // Vẽ các con vật và thực vật lên bản đồ
        entityManager.render(shapeRenderer);
        
        shapeRenderer.end();
    }

    /**
     * Giới hạn camera không đi ra ngoài bản đồ
     */
    private void applyBounds() {
        // Bản đồ: 50x50 tiles × 16 pixels = 800×800
        float mapWidth = 50 * 16;
        float mapHeight = 50 * 16;

        // Tính kích thước viewport (phần nhìn thấy)
        float viewportWidth = Gdx.graphics.getWidth() * camera.zoom;
        float viewportHeight = Gdx.graphics.getHeight() * camera.zoom;

        // Clamp X - Chỉ clamp khi viewport <= map
        float minX = viewportWidth / 2;
        float maxX = mapWidth - viewportWidth / 2;
        
        if (minX <= maxX) {
            // Viewport nhỏ hơn hoặc bằng map: clamp bình thường
            camera.position.x = Math.max(minX, Math.min(camera.position.x, maxX));
        }
        // Nếu viewport > map: không clamp, camera di chuyển tự do

        // Clamp Y - Chỉ clamp khi viewport <= map
        float minY = viewportHeight / 2;
        float maxY = mapHeight - viewportHeight / 2;
        
        if (minY <= maxY) {
            // Viewport nhỏ hơn hoặc bằng map: clamp bình thường
            camera.position.y = Math.max(minY, Math.min(camera.position.y, maxY));
        }
        // Nếu viewport > map: không clamp, camera di chuyển tự do
    }

    @Override
    public void dispose() {
        mapManager.dispose();
        shapeRenderer.dispose();
    }

    // ===== InputProcessor Implementation =====

    @Override
    public boolean keyDown(int keycode) {
        if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT) || Gdx.input.isKeyPressed(Input.Keys.CONTROL_RIGHT)) {
            if (keycode == Input.Keys.PLUS || keycode == Input.Keys.EQUALS) {
                mapManager.adjustZoom(-0.1f); // Phóng to (camera zoom nhỏ hơn = phóng to)
                return true;
            } else if (keycode == Input.Keys.MINUS) {
                mapManager.adjustZoom(0.1f); // Thu nhỏ (camera zoom lớn hơn = thu nhỏ)
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        // Chỉ cho phép drag với chuột trái (button 0)
        if (button == 0) {
            isDragging = true;
            lastMouseX = screenX;
            lastMouseY = screenY;
            return true;
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (button == 0) {
            isDragging = false;
            return true;
        }
        return false;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        isDragging = false;
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (isDragging) {
            // Lấy vị trí chuột hiện tại trong tọa độ thế giới (World)
            Vector3 lastPos = camera.unproject(new Vector3(lastMouseX, lastMouseY, 0));
            Vector3 currPos = camera.unproject(new Vector3(screenX, screenY, 0));

            // Tính khoảng cách dịch chuyển thực tế
            float deltaX = currPos.x - lastPos.x;
            float deltaY = currPos.y - lastPos.y;

            // Di chuyển camera ngược hướng với hướng kéo chuột để tạo cảm giác "cầm map kéo
            // đi"
            camera.position.x -= deltaX;
            camera.position.y -= deltaY;

            lastMouseX = screenX;
            lastMouseY = screenY;
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        // Mouse scroll up = positive (phóng to), scroll down = negative (thu nhỏ)
        if (amountY > 0) {
            mapManager.adjustZoom(-0.1f); // Cuộn lên = phóng to (zoom nhỏ hơn)
        } else if (amountY < 0) {
            mapManager.adjustZoom(0.1f); // Cuộn xuống = thu nhỏ (zoom lớn hơn)
        }
        return true;
    }
}
