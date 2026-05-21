package com.ecosystem.sim.entity.concrete;

import com.badlogic.gdx.graphics.Color;
import com.ecosystem.sim.entity.Herbivore;
import com.ecosystem.sim.map.MapManager;

/**
 * Lớp Voi (Elephant) - Động vật ăn cỏ (kích thước 12x12)
 * Có Uy quyền cao nhất bản đồ (dominance = 100).
 * Có thể lội qua nước (ignoreWater = true).
 * Chỉ ăn cây to (ediblePlantType = Tree.class).
 * Màu sắc: Xám (Color.GRAY).
 */
public class Elephant extends Herbivore {

    public Elephant(float x, float y, MapManager mapManager) {
        super(x, y, Color.GRAY, mapManager, 12, 12);
        
        // Thuộc tính vật lý voi
        this.speed = 50f;            // Di chuyển chậm rãi
        this.bodySize = 4;           // Voi to lớn
        this.dominance = 100;        // Uy quyền tuyệt đối
        this.senseRadius = 150f;     // Tầm nhìn
        this.ignoreWater = true;     // Lội nước thoải mái
        this.ediblePlantType = Tree.class; // Chỉ ăn cây
        
        // Sinh lý voi
        this.health = 150;
        this.maxHealth = 150;
        this.energy = 100;
        this.maxEnergy = 100;
        this.hydration = 100;
        this.maxHydration = 100;
        
        this.hungerRate = 2.5f;
        this.thirstRate = 4.0f;     // Động vật ăn cỏ khát nhanh hơn ăn thịt
    }

    @Override
    public void init(float x, float y) {
        super.init(x, y);
        this.speed = 50f;
    }

    @Override
    public void reset() {
        super.reset();
    }

    @Override
    public void cloneSelf() {
        com.ecosystem.sim.util.EntityManager em = com.ecosystem.sim.util.EntityManager.getInstance();
        if (em != null) {
            em.spawnElephant(position.x, position.y);
        }
    }

    @Override
    public void specificBehavior(float deltaTime) {
        super.specificBehavior(deltaTime);
    }
}
