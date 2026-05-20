package com.ecosystem.sim.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.ecosystem.sim.map.MapManager;

/**
 * Lớp cơ sở cho tất cả các loài thực vật
 * Thực vật phát triển theo thời gian và cung cấp thức ăn
 */
public abstract class Plant extends Entity {
    
    public enum GrowthStage {
        SEED(0.2f),           // Hạt - 20% kích thước
        SPROUT(0.5f),         // Mầm - 50% kích thước
        MATURE(1.0f),         // Trưởng thành - 100% kích thước
        WITHERED(0.3f);       // Héo - 30% kích thước
        
        public final float sizeMultiplier;
        
        GrowthStage(float sizeMultiplier) {
            this.sizeMultiplier = sizeMultiplier;
        }
    }
    
    protected GrowthStage growthStage;
    protected float growthProgress;    // 0-100%, tiến độ tới giai đoạn tiếp theo
    protected float growthRate;        // Tốc độ phát triển (%/s)
    protected float nutritionalValue;  // Giá trị nutrition khi được ăn
    protected Color color;             // Màu sắc để vẽ thực vật
    
    // === THAM CHIẾU ===
    protected MapManager mapManager;
    
    // === THAM SỐ SỐNG ===
    protected float maxAge;           // Tuổi thọ tối đa (giây)
    protected float wiltheringAge;    // Tuổi khi bắt đầu héo
    
    public Plant(float x, float y, Color color, MapManager mapManager,
                 float width, float height) {
        super(x, y, width, height);
        
        this.color = color;
        this.mapManager = mapManager;
        
        // Khởi tạo trạng thái phát triển
        this.growthStage = GrowthStage.SEED;
        this.growthProgress = 0;
        this.growthRate = 20f; // 20%/s
        this.nutritionalValue = 30f; // Giá trị nutrition mặc định
        
        // Tham số sống
        this.maxAge = 120f; // 2 phút
        this.wiltheringAge = 80f; // Bắt đầu héo sau 80 giây
    }

    @Override
    public void init(float x, float y) {
        super.init(x, y);
        this.growthStage = GrowthStage.SEED;
        this.growthProgress = 0;
        this.growthRate = 20f;
    }

    @Override
    public void reset() {
        super.reset();
        this.growthStage = GrowthStage.SEED;
        this.growthProgress = 0;
    }

    @Override
    public void update(float deltaTime) {
        if (!isAlive) return;
        
        age += deltaTime;
        
        // Cập nhật giai đoạn phát triển
        updateGrowth(deltaTime);
        
        // Kiểm tra điều kiện sống chết
        if (age >= maxAge) {
            die();
        }
    }

    /**
     * Cập nhật tiến độ phát triển
     */
    protected void updateGrowth(float deltaTime) {
        growthProgress += growthRate * deltaTime;
        
        // Chuyển sang giai đoạn tiếp theo
        if (growthProgress >= 100) {
            growthProgress = 0;
            advanceGrowthStage();
        }
        
        // Kiểm tra điều kiện héo
        if (age >= wiltheringAge && growthStage != GrowthStage.WITHERED) {
            growthStage = GrowthStage.WITHERED;
            growthProgress = 0;
            growthRate = -10f; // Héo nhanh hơn
        }
    }

    /**
     * Chuyển sang giai đoạn phát triển tiếp theo
     */
    protected void advanceGrowthStage() {
        switch (growthStage) {
            case SEED:
                growthStage = GrowthStage.SPROUT;
                break;
            case SPROUT:
                growthStage = GrowthStage.MATURE;
                break;
            case MATURE:
                // Ở lại trưởng thành cho tới khi héo
                growthProgress = 0;
                growthRate = 0; // Không phát triển thêm
                break;
            case WITHERED:
                die();
                break;
        }
    }

    /**
     * Được gọi khi một động vật ăn thực vật này
     */
    public void beEaten() {
        die();
    }

    /**
     * Trả về giá trị nutrition nếu được ăn
     */
    public float getNutritionalValue() {
        // Chỉ cho ăn nếu đã trưởng thành
        if (growthStage == GrowthStage.MATURE) {
            return nutritionalValue;
        }
        return 0;
    }

    @Override
    public void render(ShapeRenderer shapeRenderer) {
        if (isAlive) {
            shapeRenderer.setColor(color);
            float sizeMultiplier = growthStage.sizeMultiplier;
            float actualWidth = width * sizeMultiplier;
            float actualHeight = height * sizeMultiplier;
            
            // Căn giữa hình chữ nhật
            float centerX = position.x + (width - actualWidth) / 2;
            float centerY = position.y + (height - actualHeight) / 2;
            
            // Vẽ hình vuông
            shapeRenderer.rect(centerX, centerY, actualWidth, actualHeight);
        }
    }

    /**
     * Phương thức trừu tượng: Các lớp con phải triển khai
     */
    public abstract void specificBehavior(float deltaTime);

    // ============= GETTERS & SETTERS =============

    public GrowthStage getGrowthStage() { return growthStage; }
    public float getGrowthProgress() { return growthProgress; }
    public float getNutritional() { return nutritionalValue; }
}
