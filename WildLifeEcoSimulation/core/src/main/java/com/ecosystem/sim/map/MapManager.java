package com.ecosystem.sim.map;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.MathUtils;

public class MapManager {
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private OrthographicCamera camera;
    private float unitScale = 1.0f; // Unit scale cho renderer
    private float cameraZoom = 1.0f; // Zoom level của camera
    private float minZoom = 0.5f;
    private float maxZoom = 3.0f;
    private float mapWidth = 800f;  // 50 tiles × 16px
    private float mapHeight = 800f; // 50 tiles × 16px

    public MapManager(String mapPath, OrthographicCamera camera) {
        this.map = new TmxMapLoader().load(mapPath);
        this.renderer = new OrthogonalTiledMapRenderer(map, unitScale);
        this.camera = camera;
    }

    public void render() {
        renderer.setView(camera);
        renderer.render();
    }

    /**
     * Kiểm tra xem một tọa độ có phải là vật cản không
     * Dùng để logic Sói/Thỏ không đi xuyên tường
     */
    public boolean isObstacle(float worldX, float worldY) {
        // Chuyển tọa độ thế giới sang tọa độ ô (tile)
        int tileX = (int) (worldX / (16 * unitScale));
        int tileY = (int) (worldY / (16 * unitScale));

        // Lấy lớp "Tile Layer 2" (nơi bạn vẽ ID 520)
        TiledMapTileLayer obstacleLayer = (TiledMapTileLayer) map.getLayers().get("Tile Layer 2");
        
        if (obstacleLayer != null && obstacleLayer.getCell(tileX, tileY) != null) {
            return true; // Có vật cản ở đây
        }
        return false;
    }

    /**
     * Kiểm tra xem một tọa độ có phải là nước không
     */
    public boolean isWater(float worldX, float worldY) {
        int tileX = (int) (worldX / (16 * unitScale));
        int tileY = (int) (worldY / (16 * unitScale));

        TiledMapTileLayer layer1 = (TiledMapTileLayer) map.getLayers().get("Tile Layer 1");
        if (layer1 != null) {
            TiledMapTileLayer.Cell cell = layer1.getCell(tileX, tileY);
            if (cell != null && cell.getTile() != null) {
                int id = cell.getTile().getId();
                // Các ID của nước theo yêu cầu (60, 3, 4, 5, 59, 61...) bao gồm cả viền bờ hồ phía Nam (115, 116, 117)
                if (id == 60 || id == 3 || id == 4 || id == 5 || id == 59 || id == 61 || id == 115 || id == 116 || id == 117) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Kiểm tra xem một tọa độ có phải là cỏ (ID 6) không
     */
    public boolean isGrass(float worldX, float worldY) {
        int tileX = (int) (worldX / (16 * unitScale));
        int tileY = (int) (worldY / (16 * unitScale));

        TiledMapTileLayer layer1 = (TiledMapTileLayer) map.getLayers().get("Tile Layer 1");
        if (layer1 != null) {
            TiledMapTileLayer.Cell cell = layer1.getCell(tileX, tileY);
            if (cell != null && cell.getTile() != null) {
                int id = cell.getTile().getId();
                return id == 6; // Cỏ có ID là 6 trong tileset
            }
        }
        return false;
    }

    /**
     * Kiểm tra xem một vị trí có phải là land hợp lệ (phải trên cỏ, không có vật cản)
     */
    public boolean isValidSpawnLocation(float worldX, float worldY) {
        return isValidSpawnLocation(worldX, worldY, 14f, 14f);
    }

    /**
     * Kiểm tra xem một thực thể với kích thước cụ thể có thể spawn tại vị trí đó không
     */
    public boolean isValidSpawnLocation(float worldX, float worldY, float width, float height) {
        // Kiểm tra bounds
        if (worldX < 0 || worldX + width >= mapWidth || worldY < 0 || worldY + height >= mapHeight) {
            return false;
        }
        
        // Kiểm tra va chạm đa điểm ở 4 góc của thực thể để đảm bảo nằm trọn trên cỏ
        float[] xs = {worldX, worldX + width};
        float[] ys = {worldY, worldY + height};

        for (float x : xs) {
            for (float y : ys) {
                if (!isGrass(x, y) || isObstacle(x, y)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Tìm vị trí spawn ngẫu nhiên hợp lệ trên cỏ cho thực thể kích thước mặc định (14x14)
     */
    public Vector2 findRandomSpawnLocation() {
        return findRandomSpawnLocation(14f, 14f);
    }

    /**
     * Tìm vị trí spawn ngẫu nhiên hợp lệ trên cỏ cho thực thể có kích thước cụ thể
     */
    public Vector2 findRandomSpawnLocation(float width, float height) {
        Vector2 spawnPos = new Vector2();
        int maxAttempts = 100;
        int attempts = 0;
        
        // Thử tìm vị trí hợp lệ tối đa 100 lần
        while (attempts < maxAttempts) {
            float randomX = MathUtils.random(0, mapWidth - width);
            float randomY = MathUtils.random(0, mapHeight - height);
            
            if (isValidSpawnLocation(randomX, randomY, width, height)) {
                spawnPos.set(randomX, randomY);
                return spawnPos;
            }
            
            attempts++;
        }
        
        // Fallback: Nếu không tìm được thì trả về vị trí gốc ngẫu nhiên trên vùng cỏ an toàn phía Bắc (hàng 38 đến 48 trong LibGDX)
        float safeX = MathUtils.random(16, mapWidth - 32);
        float safeY = MathUtils.random(38 * 16, 48 * 16);
        return new Vector2(safeX, safeY);
    }

    /**
     * Điều chỉnh mức zoom với camera
     */
    public void adjustZoom(float zoomDelta) {
        cameraZoom += zoomDelta;
        if (cameraZoom < minZoom) cameraZoom = minZoom;
        if (cameraZoom > maxZoom) cameraZoom = maxZoom;
        
        camera.zoom = cameraZoom;
        camera.update();
    }

    /**
     * Đặt mức zoom cụ thể
     */
    public void setZoom(float zoom) {
        cameraZoom = zoom;
        if (cameraZoom < minZoom) cameraZoom = minZoom;
        if (cameraZoom > maxZoom) cameraZoom = maxZoom;
        
        camera.zoom = cameraZoom;
        camera.update();
    }

    /**
     * Lấy mức zoom hiện tại
     */
    public float getZoom() {
        return cameraZoom;
    }

    public void dispose() {
        map.dispose();
        renderer.dispose();
    }
}
