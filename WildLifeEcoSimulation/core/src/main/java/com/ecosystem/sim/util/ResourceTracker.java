package com.ecosystem.sim.util;

import com.badlogic.gdx.math.Vector2;

/**
 * Hệ thống quản lý tài nguyên tự nhiên trên bản đồ
 * Theo dõi vị trí nước, thực vật để động vật có thể tìm kiếm
 */
public class ResourceTracker {
    private Vector2[] waterSources;    // Vị trí các nguồn nước
    private Vector2[] foodSources;     // Vị trí các nguồn thức ăn
    private int maxWaterSources = 20;
    private int maxFoodSources = 50;
    
    public ResourceTracker() {
        this.waterSources = new Vector2[maxWaterSources];
        this.foodSources = new Vector2[maxFoodSources];
    }

    /**
     * Tìm nguồn nước gần nhất
     */
    public Vector2 findNearestWater(Vector2 position) {
        Vector2 nearest = null;
        float minDistance = Float.MAX_VALUE;
        
        for (Vector2 water : waterSources) {
            if (water == null) continue;
            
            float distance = position.dst(water);
            if (distance < minDistance) {
                minDistance = distance;
                nearest = water;
            }
        }
        
        return nearest;
    }

    /**
     * Tìm thức ăn gần nhất
     */
    public Vector2 findNearestFood(Vector2 position) {
        Vector2 nearest = null;
        float minDistance = Float.MAX_VALUE;
        
        for (Vector2 food : foodSources) {
            if (food == null) continue;
            
            float distance = position.dst(food);
            if (distance < minDistance) {
                minDistance = distance;
                nearest = food;
            }
        }
        
        return nearest;
    }

    /**
     * Đăng ký một nguồn nước mới
     */
    public void addWaterSource(float x, float y) {
        for (int i = 0; i < waterSources.length; i++) {
            if (waterSources[i] == null) {
                waterSources[i] = new Vector2(x, y);
                return;
            }
        }
    }

    /**
     * Đăng ký một nguồn thức ăn mới
     */
    public void addFoodSource(float x, float y) {
        for (int i = 0; i < foodSources.length; i++) {
            if (foodSources[i] == null) {
                foodSources[i] = new Vector2(x, y);
                return;
            }
        }
    }

    /**
     * Loại bỏ một nguồn thức ăn (đã bị ăn hết)
     */
    public void removeFoodSource(Vector2 position) {
        for (int i = 0; i < foodSources.length; i++) {
            if (foodSources[i] != null && 
                foodSources[i].dst(position) < 5) {
                foodSources[i] = null;
            }
        }
    }

    /**
     * Cập nhật tất cả tài nguyên
     */
    public void update(float deltaTime) {
        // TODO: Cập nhật vị trí tài nguyên nếu cần
    }
}
