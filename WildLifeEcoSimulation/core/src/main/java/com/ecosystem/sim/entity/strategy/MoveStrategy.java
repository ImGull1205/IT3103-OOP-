package com.ecosystem.sim.entity.strategy;

public interface MoveStrategy {
    /**
     * Di chuyển dựa trên chiến lược khác nhau
     * 
     * @param x vị trí x hiện tại
     * @param y vị trí y hiện tại
     * @param deltaTime thời gian delta (để tính vận tốc)
     * @return mảng [newX, newY] vị trí mới sau di chuyển
     */
    float[] move(float x, float y, float deltaTime);

    /**
     * Lấy tốc độ di chuyển của chiến lược này
     */
    float getSpeed();
}
