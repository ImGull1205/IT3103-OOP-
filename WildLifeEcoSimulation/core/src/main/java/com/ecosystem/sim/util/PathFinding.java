package com.ecosystem.sim.util;

import com.badlogic.gdx.math.Vector2;
import com.ecosystem.sim.map.MapManager;
import java.util.*;

/**
 * Công cụ tìm đường sử dụng BFS (Breadth-First Search)
 * Được sử dụng để tìm nước, thức ăn, hoặc lối thoát
 */
public class PathFinding {
    
    private static final int TILE_SIZE = 16; // Kích thước tile
    
    /**
     * Tìm đường đến một loại tile mục tiêu sử dụng BFS
     * @param startPos Vị trí bắt đầu (world coordinates)
     * @param targetType Loại tile mục tiêu (ví dụ: "water", "food")
     * @param mapManager Manager bản đồ
     * @param maxDistance Khoảng cách tìm kiếm tối đa
     * @return Hướng di chuyển tiếp theo, hoặc Vector2(0,0) nếu không tìm thấy
     */
    public static Vector2 findPathToTileType(Vector2 startPos, String targetType, 
                                             MapManager mapManager, float maxDistance) {
        int startTileX = (int)(startPos.x / TILE_SIZE);
        int startTileY = (int)(startPos.y / TILE_SIZE);
        
        Queue<int[]> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();
        Map<String, int[]> parent = new HashMap<>();
        
        queue.add(new int[]{startTileX, startTileY});
        visited.add(startTileX + "," + startTileY);
        parent.put(startTileX + "," + startTileY, null);
        
        int[] targetTile = null;
        int maxTiles = (int)(maxDistance / TILE_SIZE);
        
        // BFS
        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int x = current[0];
            int y = current[1];
            
            // Kiểm tra xem tile này có phải là mục tiêu không
            if (isTileType(x, y, targetType, mapManager)) {
                targetTile = current;
                break;
            }
            
            // Nếu vượt quá khoảng cách, bỏ qua
            if (Math.abs(x - startTileX) + Math.abs(y - startTileY) > maxTiles) {
                continue;
            }
            
            // Thêm các tile kế cạnh
            int[][] neighbors = {
                {x + 1, y}, {x - 1, y}, {x, y + 1}, {x, y - 1},
                {x + 1, y + 1}, {x - 1, y + 1}, {x + 1, y - 1}, {x - 1, y - 1}
            };
            
            for (int[] neighbor : neighbors) {
                String key = neighbor[0] + "," + neighbor[1];
                
                if (!visited.contains(key) && !mapManager.isObstacle(
                    neighbor[0] * TILE_SIZE, neighbor[1] * TILE_SIZE)) {
                    
                    visited.add(key);
                    parent.put(key, current);
                    queue.add(neighbor);
                }
            }
        }
        
        // Nếu tìm thấy, trả về hướng
        if (targetTile != null) {
            // Truy ngược lại bước tiếp theo
            int[] nextStep = targetTile;
            int[] previous = parent.get(nextStep[0] + "," + nextStep[1]);
            
            while (previous != null && previous[0] != startTileX && previous[1] != startTileY) {
                nextStep = previous;
                previous = parent.get(nextStep[0] + "," + nextStep[1]);
            }
            
            Vector2 nextPos = new Vector2(
                nextStep[0] * TILE_SIZE + TILE_SIZE / 2f,
                nextStep[1] * TILE_SIZE + TILE_SIZE / 2f
            );
            
            return nextPos.sub(startPos).nor();
        }
        
        return new Vector2(0, 0); // Không tìm thấy
    }

    /**
     * Kiểm tra xem một tile có phải loại mục tiêu không
     */
    private static boolean isTileType(int tileX, int tileY, String type, MapManager mapManager) {
        // TODO: Triển khai dựa trên property của layer trong Tiled
        // Ví dụ: Kiểm tra layer "Water", "Food", vv
        
        // Tạm thời: Trả về false
        return false;
    }

    /**
     * Tìm vị trí trống gần nhất
     * Dùng để dùng động vật nhường đường cho nhau
     */
    public static Vector2 findNearbyEmptySpot(Vector2 currentPos, MapManager mapManager, 
                                               float searchRadius) {
        for (int i = 0; i < 8; i++) {
            float angle = (i / 8f) * 360;
            Vector2 offset = new Vector2(searchRadius, 0).rotateRad((float) Math.toRadians(angle));
            Vector2 testPos = new Vector2(currentPos).add(offset);
            
            if (!mapManager.isObstacle(testPos.x, testPos.y)) {
                return testPos;
            }
        }
        
        return currentPos; // Không tìm thấy, ở lại chỗ cũ
    }
}
