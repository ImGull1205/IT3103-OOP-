package com.ecosystem.sim.entity.strategy;

import java.util.Random;

/**
 * Chiến lược di chuyển cho thú săn (Sói, Hổ, v.v.)
 * - Di chuyển nhanh hơn con mồi
 * - Có khả năng "tìm kiếm" target (simulate với random patrol)
 * - Tốc độ cao hơn PassiveStrategy
 */
public class HunterStrategy implements MoveStrategy {
    private static final float SPEED = 100f; // pixel/s - nhanh gấp đôi PassiveStrategy
    private Random random;
    private float moveTimer = 0;
    private float changeDirectionTime = 1.5f; // Đổi hướng nhanh hơn - 1.5 giây
    private float currentDirX = 1;
    private float currentDirY = 0;
    private float targetX = -1;
    private float targetY = -1;

    public HunterStrategy() {
        this.random = new Random();
        randomizeDirection();
    }

    @Override
    public float[] move(float x, float y, float deltaTime) {
        moveTimer += deltaTime;
        
        // Đổi hướng nhanh hơn để simulate tìm kiếm
        if (moveTimer >= changeDirectionTime) {
            // 70% di chuyển random, 30% nếu có target thì hướng tới target
            if (targetX >= 0 && random.nextDouble() < 0.3) {
                moveTowardTarget(x, y);
            } else {
                randomizeDirection();
            }
            moveTimer = 0;
        }

        float newX = x + currentDirX * SPEED * deltaTime;
        float newY = y + currentDirY * SPEED * deltaTime;

        return new float[]{newX, newY};
    }

    @Override
    public float getSpeed() {
        return SPEED;
    }

    /**
     * Đặt target để con vật săn tìm
     * (Sẽ được dùng sau khi có hệ thống phát hiện mục tiêu)
     */
    public void setTarget(float targetX, float targetY) {
        this.targetX = targetX;
        this.targetY = targetY;
    }

    /**
     * Xóa target
     */
    public void clearTarget() {
        this.targetX = -1;
        this.targetY = -1;
    }

    private void moveTowardTarget(float currentX, float currentY) {
        float dx = targetX - currentX;
        float dy = targetY - currentY;
        float distance = (float) Math.sqrt(dx * dx + dy * dy);
        
        if (distance > 0) {
            currentDirX = dx / distance;
            currentDirY = dy / distance;
        }
    }

    private void randomizeDirection() {
        float angle = (float) (random.nextDouble() * 2 * Math.PI);
        currentDirX = (float) Math.cos(angle);
        currentDirY = (float) Math.sin(angle);
    }
}
