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
    /**
     * Tìm đường đến một loại tile mục tiêu sử dụng BFS đã tối ưu hóa (Không cấp phát String/Map, Zero-Allocation)
     * @param startPos Vị trí bắt đầu (world coordinates)
     * @param targetType Loại tile mục tiêu (ví dụ: "water")
     * @param mapManager Manager bản đồ
     * @param maxDistance Khoảng cách tìm kiếm tối đa
     * @return Hướng di chuyển tiếp theo, hoặc Vector2(0,0) nếu không tìm thấy
     */
    public static Vector2 findPathToTileType(Vector2 startPos, String targetType, 
                                             MapManager mapManager, float maxDistance) {
        int startTileX = (int)(startPos.x / TILE_SIZE);
        int startTileY = (int)(startPos.y / TILE_SIZE);
        
        // Đảm bảo nằm trong ranh giới bản đồ 50x50
        startTileX = Math.max(0, Math.min(49, startTileX));
        startTileY = Math.max(0, Math.min(49, startTileY));
        
        // Hàng đợi lưu trữ chỉ mục phẳng: index = y * 50 + x để tránh tạo object int[] mới
        Queue<Integer> queue = new LinkedList<>();
        boolean[] visited = new boolean[2500];
        int[] parent = new int[2500];
        Arrays.fill(parent, -1);
        
        int startIdx = startTileY * 50 + startTileX;
        queue.add(startIdx);
        visited[startIdx] = true;
        
        int targetIdx = -1;
        int maxTiles = (int)(maxDistance / TILE_SIZE);
        
        // Hướng di chuyển (4 hướng thẳng, 4 hướng chéo)
        int[] dx = {1, -1, 0, 0, 1, -1, 1, -1};
        int[] dy = {0, 0, 1, -1, 1, 1, -1, -1};
        
        // BFS tìm kiếm
        while (!queue.isEmpty()) {
            int currentIdx = queue.poll();
            int cx = currentIdx % 50;
            int cy = currentIdx / 50;
            
            // Kiểm tra xem tile này có phải là mục tiêu không
            if (isTileType(cx, cy, targetType, mapManager)) {
                targetIdx = currentIdx;
                break;
            }
            
            // Nếu vượt quá khoảng cách, bỏ qua
            if (Math.abs(cx - startTileX) + Math.abs(cy - startTileY) > maxTiles) {
                continue;
            }
            
            for (int i = 0; i < 8; i++) {
                int nx = cx + dx[i];
                int ny = cy + dy[i];
                
                if (nx >= 0 && nx < 50 && ny >= 0 && ny < 50) {
                    int neighborIdx = ny * 50 + nx;
                    if (!visited[neighborIdx] && !mapManager.isTileObstacle(nx, ny)) {
                        
                        // Chống cắt góc chéo (Corner Cutting) để tránh kẹt
                        if (i >= 4) {
                            int cx1 = cx + dx[i];
                            int cy1 = cy;
                            int cx2 = cx;
                            int cy2 = cy + dy[i];
                            if (mapManager.isTileObstacle(cx1, cy1) || mapManager.isTileObstacle(cx2, cy2)) {
                                continue;
                            }
                        }
                        
                        visited[neighborIdx] = true;
                        parent[neighborIdx] = currentIdx;
                        queue.add(neighborIdx);
                    }
                }
            }
        }
        
        // Nếu tìm thấy, truy ngược lại bước tiếp theo
        if (targetIdx != -1) {
            int nextStepIdx = targetIdx;
            int prevIdx = parent[nextStepIdx];
            
            while (prevIdx != -1 && prevIdx != startIdx) {
                nextStepIdx = prevIdx;
                prevIdx = parent[nextStepIdx];
            }
            
            int nX = nextStepIdx % 50;
            int nY = nextStepIdx / 50;
            
            Vector2 nextPos = new Vector2(
                nX * TILE_SIZE + TILE_SIZE / 2f,
                nY * TILE_SIZE + TILE_SIZE / 2f
            );
            
            return nextPos.sub(startPos).nor();
        }
        
        return new Vector2(0, 0); // Không tìm thấy
    }

    /**
     * Kiểm tra xem một tile có phải loại mục tiêu không
     */
    private static boolean isTileType(int tileX, int tileY, String type, MapManager mapManager) {
        float worldX = tileX * TILE_SIZE + TILE_SIZE / 2f;
        float worldY = tileY * TILE_SIZE + TILE_SIZE / 2f;
        
        if ("water".equals(type)) {
            return mapManager.isWater(worldX, worldY);
        }
        return false;
    }

    /**
     * Tìm vị trí trống gần nhất
     * Dùng để dùng động vật nhường đường cho nhau
     */
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

    /**
     * Lớp Node hỗ trợ thuật toán tìm đường A*
     */
    private static class AStarNode implements Comparable<AStarNode> {
        int x, y;
        float g; // Chi phí từ điểm bắt đầu
        float h; // Chi phí ước lượng tới điểm đích (Heuristic)
        float f; // Tổng chi phí (g + h)
        AStarNode parent;

        AStarNode(int x, int y, float g, float h, AStarNode parent) {
            this.x = x;
            this.y = y;
            this.g = g;
            this.h = h;
            this.f = g + h;
            this.parent = parent;
        }

        @Override
        public int compareTo(AStarNode o) {
            return Float.compare(this.f, o.f);
        }
    }

    /**
     * Tìm đường tối ưu từ điểm bắt đầu tới điểm đích bằng thuật toán A* (A-Star)
     * Tránh đi xuyên qua góc tường hoặc chui vào bụi cây vẽ sẵn trong file .tmx
     */
    public static List<Vector2> findAStarPath(Vector2 startPos, Vector2 targetPos, MapManager mapManager) {
        return findAStarPath(startPos, targetPos, mapManager, false);
    }

    /**
     * Tìm đường tối ưu từ điểm bắt đầu tới điểm đích bằng thuật toán A* (A-Star), hỗ trợ tùy chọn lội nước
     */
    public static List<Vector2> findAStarPath(Vector2 startPos, Vector2 targetPos, MapManager mapManager, boolean ignoreWater) {
        int startTileX = (int) (startPos.x / TILE_SIZE);
        int startTileY = (int) (startPos.y / TILE_SIZE);
        int targetTileX = (int) (targetPos.x / TILE_SIZE);
        int targetTileY = (int) (targetPos.y / TILE_SIZE);

        // Giới hạn trong kích thước bản đồ 50x50
        startTileX = Math.max(0, Math.min(49, startTileX));
        startTileY = Math.max(0, Math.min(49, startTileY));
        targetTileX = Math.max(0, Math.min(49, targetTileX));
        targetTileY = Math.max(0, Math.min(49, targetTileY));

        List<Vector2> path = new ArrayList<>();

        if (startTileX == targetTileX && startTileY == targetTileY) {
            path.add(new Vector2(targetPos));
            return path;
        }

        PriorityQueue<AStarNode> openSet = new PriorityQueue<>();
        boolean[][] closedSet = new boolean[50][50];
        float[][] gScores = new float[50][50];
        for (int i = 0; i < 50; i++) {
            Arrays.fill(gScores[i], Float.MAX_VALUE);
        }

        // Sử dụng Manhattan Distance làm heuristic
        float startH = Math.abs(startTileX - targetTileX) + Math.abs(startTileY - targetTileY);
        AStarNode startNode = new AStarNode(startTileX, startTileY, 0, startH, null);
        openSet.add(startNode);
        gScores[startTileX][startTileY] = 0;

        AStarNode targetNode = null;
        int maxIterations = 1000; // Giới hạn số lượt quét để tránh đơ game
        int iterations = 0;

        // 8 hướng di chuyển (4 hướng thẳng, 4 hướng chéo)
        int[][] directions = {
            {0, 1}, {0, -1}, {1, 0}, {-1, 0},     // Cardinal
            {1, 1}, {-1, 1}, {1, -1}, {-1, -1}    // Diagonal
        };

        while (!openSet.isEmpty() && iterations++ < maxIterations) {
            AStarNode current = openSet.poll();

            if (closedSet[current.x][current.y]) continue;
            closedSet[current.x][current.y] = true;

            if (current.x == targetTileX && current.y == targetTileY) {
                targetNode = current;
                break;
            }

            for (int i = 0; i < directions.length; i++) {
                int nx = current.x + directions[i][0];
                int ny = current.y + directions[i][1];

                if (nx < 0 || nx >= 50 || ny < 0 || ny >= 50) continue;
                
                boolean isObstacle = ignoreWater ? mapManager.isTileObstacleIgnoreWater(nx, ny) : mapManager.isTileObstacle(nx, ny);
                if (isObstacle) continue;

                // NGĂN CHẶN CẮT GÓC (Corner Cutting):
                // Nếu đi chéo, góc kề kề 2 bên của hướng đi không được là vật cản
                if (i >= 4) {
                    int cx1 = current.x + directions[i][0];
                    int cy1 = current.y;
                    int cx2 = current.x;
                    int cy2 = current.y + directions[i][1];
                    
                    boolean obs1 = ignoreWater ? mapManager.isTileObstacleIgnoreWater(cx1, cy1) : mapManager.isTileObstacle(cx1, cy1);
                    boolean obs2 = ignoreWater ? mapManager.isTileObstacleIgnoreWater(cx2, cy2) : mapManager.isTileObstacle(cx2, cy2);
                    if (obs1 || obs2) {
                        continue; // Bỏ qua nếu có vật cản sát góc để tránh kẹt
                    }
                }

                float weight = (i < 4) ? 1.0f : 1.414f;
                float tentativeG = current.g + weight;

                if (tentativeG < gScores[nx][ny]) {
                    gScores[nx][ny] = tentativeG;
                    float h = Math.abs(nx - targetTileX) + Math.abs(ny - targetTileY);
                    AStarNode neighborNode = new AStarNode(nx, ny, tentativeG, h, current);
                    openSet.add(neighborNode);
                }
            }
        }

        if (targetNode != null) {
            AStarNode curr = targetNode;
            while (curr != null) {
                // Thêm tọa độ thế giới (tâm của ô tile 16x16)
                path.add(0, new Vector2(curr.x * TILE_SIZE + TILE_SIZE / 2f, curr.y * TILE_SIZE + TILE_SIZE / 2f));
                curr = curr.parent;
            }

            // Thay thế điểm cuối cùng của path bằng tọa độ đích chính xác
            if (!path.isEmpty()) {
                path.set(path.size() - 1, new Vector2(targetPos));
            }
        }
        return path;
    }
}
