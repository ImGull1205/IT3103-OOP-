package com.ecosystem.sim.entity.concrete;

import com.badlogic.gdx.graphics.Color;
import com.ecosystem.sim.entity.Plant;
import com.ecosystem.sim.map.MapManager;

/**
 * Lớp Cỏ - Thực vật đơn giản, nguồn thức ăn cho thỏ
 * Cỏ phát triển theo giai đoạn: Hạt -> Mầm -> Trưởng thành -> Héo
 * Màu sắc: Xanh lá nhạt
 */
public class Grass extends Plant {
    
    public Grass(float x, float y, MapManager mapManager) {
        // Gọi constructor cha với màu xanh lá nhạt (yellowish green)
        super(x, y, new Color(0.5f, 1, 0.5f, 1), mapManager, 14, 14);
        
        // Cỏ phát triển nhanh
        this.growthRate = 25f; // 25%/s => mất 4 giây từ hạt đến trưởng thành
        this.nutritionalValue = 25f;
        
        // Cỏ sống lâu hơn các cây khác
        this.maxAge = 150f; // 2.5 phút
        this.wiltheringAge = 100f;
    }

    @Override
    public void init(float x, float y) {
        super.init(x, y);
        this.growthRate = 25f;
    }

    @Override
    public void specificBehavior(float deltaTime) {
        // Cỏ không có hành vi đặc biệt
        // Chỉ phát triển và chờ bị ăn
    }
}
