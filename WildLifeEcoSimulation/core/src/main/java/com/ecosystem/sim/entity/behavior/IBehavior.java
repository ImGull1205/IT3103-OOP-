package com.ecosystem.sim.entity.behavior;

import com.badlogic.gdx.math.Vector2;

/**
 * Giao diện cho hành vi của động vật
 * Các động vật có thể triển khai hành vi cụ thể
 */
public interface IBehavior {
    /**
     * Cập nhật hành vi dựa trên các điều kiện hiện tại
     */
    void update(float deltaTime);

    /**
     * Trả về hướng di chuyển tiếp theo
     */
    Vector2 getNextDirection();

    /**
     * Trả về tốc độ di chuyển
     */
    float getSpeed();
}
