package com.ecosystem.sim.entity.behavior;

import com.ecosystem.sim.entity.Animal;
import com.ecosystem.sim.entity.Entity;

/**
 * Giao diện cho các động vật ăn thịt (Sói, Hổ, Hổ báo, v.v.)
 */
public interface IPredator {
    /**
     * Bắt đầu truy đuổi một con mồi
     */
    void hunt(Animal prey);

    /**
     * Kiểm tra xem có con mồi nào trong tầm nhìn không
     */
    Animal detectPrey(java.util.List<Entity> potentialPrey);

    /**
     * Trả về bán kính tầm nhìn của con thú ăn thịt
     */
    float getHuntingRange();
}
