package com.ecosystem.sim.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.ecosystem.sim.util.EntityManager;

/**
 * Hệ thống HUD (Heads-Up Display) để hiển thị thông tin mô phỏng
 * Hiển thị:
 * - Tổng số động vật
 * - Tổng số thực vật
 * - Tốc độ khung hình (FPS)
 * - Trạng thái sinh thái
 */
public class HUD {
    private BitmapFont font;
    private EntityManager entityManager;
    
    // Màu sắc
    private static final float TEXT_COLOR_R = 1.0f;
    private static final float TEXT_COLOR_G = 1.0f;
    private static final float TEXT_COLOR_B = 1.0f;
    private static final float TEXT_COLOR_A = 1.0f;
    
    public HUD(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.font = new BitmapFont();
        this.font.setColor(TEXT_COLOR_R, TEXT_COLOR_G, TEXT_COLOR_B, TEXT_COLOR_A);
    }

    /**
     * Vẽ HUD lên màn hình
     */
    public void render(SpriteBatch batch) {
        int animalCount = entityManager.getAnimalCount();
        int plantCount = entityManager.getPlantCount();
        int fps = Gdx.graphics.getFramesPerSecond();
        
        batch.begin();
        
        // Vẽ thông tin ở góc trên bên trái
        float x = 10;
        float y = Gdx.graphics.getHeight() - 10;
        
        font.draw(batch, "Animals: " + animalCount, x, y);
        y -= 20;
        
        font.draw(batch, "Plants: " + plantCount, x, y);
        y -= 20;
        
        font.draw(batch, "FPS: " + fps, x, y);
        y -= 20;
        
        // Trạng thái sinh thái
        String ecoStatus = getEcosystemStatus(animalCount, plantCount);
        font.draw(batch, "Status: " + ecoStatus, x, y);
        
        batch.end();
    }

    /**
     * Xác định trạng thái của hệ sinh thái
     */
    private String getEcosystemStatus(int animals, int plants) {
        if (animals == 0) {
            return "Extinct";
        }
        
        float ratio = animals > 0 ? (float) plants / animals : 0;
        
        if (ratio > 5) {
            return "Balanced";
        } else if (ratio > 2) {
            return "Stable";
        } else if (ratio > 1) {
            return "Stressed";
        } else {
            return "Critical";
        }
    }

    /**
     * Dọn dẹp tài nguyên
     */
    public void dispose() {
        if (font != null) {
            font.dispose();
        }
    }
}
