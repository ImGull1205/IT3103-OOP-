package com.ecosystem.sim.entity.strategy;

import java.util.Random;

/**
 * Chiến lược di chuyển cho động vật ăn cỏ (Thỏ, Hươu, v.v.)
 * - Di chuyển random quanh khu vực
 * - Tốc độ chậm hơn thú săn
 * - Ưu tiên tìm thức ăn gần nhất
 */
public class PassiveStrategy implements MoveStrategy {
    private static final float SPEED = 50f; // pixel/s
    private Random random;
    private float moveTimer = 0;
    private float changeDirectionTime = 2.0f; // Đổi hướng mỗi 2 giây
    private float currentDirX = 1;
    private float currentDirY = 0;

    public PassiveStrategy() {
        this.random = new Random();
        randomizeDirection();
    }

    @Override
    public float[] move(float x, float y, float deltaTime) {
        moveTimer += deltaTime;
        
        // Đổi hướng mỗi khoảng thời gian
        if (moveTimer >= changeDirectionTime) {
            randomizeDirection();
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

    private void randomizeDirection() {
        // Random hướng trong vòng tròn (radian)
        float angle = (float) (random.nextDouble() * 2 * Math.PI);
        currentDirX = (float) Math.cos(angle);
        currentDirY = (float) Math.sin(angle);
    }
}
