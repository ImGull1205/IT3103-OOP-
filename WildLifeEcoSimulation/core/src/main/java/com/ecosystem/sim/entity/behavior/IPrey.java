package com.ecosystem.sim.entity.behavior;

import com.ecosystem.sim.entity.Animal;

/**
 * Giao diện cho các động vật bị săn (Thỏ, Hươu, v.v.)
 */
public interface IPrey {
    /**
     * Bắt đầu chạy thoát khỏi một kẻ thù
     */
    void flee(Animal predator);

    /**
     * Kiểm tra xem có kẻ thù nào trong tầm nhìn không
     */
    Animal detectThreat(java.util.List<Animal> potentialThreats);

    /**
     * Trả về bán kính tầm nhìn của con vật bị săn
     */
    float getAwarenessRange();
}
