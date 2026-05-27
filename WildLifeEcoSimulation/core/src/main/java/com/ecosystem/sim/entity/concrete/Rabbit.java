package com.ecosystem.sim.entity.concrete;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.ecosystem.sim.entity.Animal;
import com.ecosystem.sim.entity.Herbivore;
import com.ecosystem.sim.entity.AnimalState;
import com.ecosystem.sim.entity.behavior.IPrey;
import com.ecosystem.sim.map.MapManager;
import com.ecosystem.sim.util.EntityManager;

/**
 * Lớp Thỏ - Động vật ăn cỏ, là con mồi của sói
 * Thỏ chạy nhanh nhưng có cơ thể nhỏ (bodySize = 1)
 * Có thể lách qua bụi rậm nhỏ mà sói không vào được
 * Màu sắc: Xanh lá
 */
public class Rabbit extends Herbivore implements IPrey {
    public Rabbit(float x, float y, MapManager mapManager) {
        // Gọi constructor cha với màu xanh lá
        super(x, y, new Color(0, 1, 0, 1), mapManager, 12, 12); // 12x12 pixels, GREEN
        
        // Thuộc tính thỏ
        this.speed = 80f;           // Tốc độ bình thường
        this.fleeSpeed = 150f;      // Tốc độ chạy thoát
        this.bodySize = 1;          // Thỏ nhỏ - lách được qua bụi rậm
        this.dominance = 20;        // Uy quyền 20
        this.senseRadius = 150f;    // Tầm nhìn 150 pixels
        
        // Sinh lý thỏ
        this.health = 80;
        this.maxHealth = 80;
        this.energy = 100;
        this.maxEnergy = 100;
        this.hydration = 100;
        this.maxHydration = 100;
        
        // Thỏ ăn cỏ nên tiêu tốn ít năng lượng hơn sói
        this.hungerRate = 2f;
        this.thirstRate = 4f;
        
        this.fleeDistance = 200f;
        this.threatDetected = null;
    }

    @Override
    public void init(float x, float y) {
        super.init(x, y);
        this.threatDetected = null;
        this.speed = 80f; // Tốc độ bình thường
    }

    @Override
    public void reset() {
        super.reset();
        this.threatDetected = null;
    }



    @Override
    protected AnimalState makeDecision() {
        // ƯUTIEN: Nếu phát hiện được kẻ thù gần, chạy thoát
        if (threatDetected != null && threatDetected.isAlive() &&
            position.dst(threatDetected.getPosition()) < fleeDistance) {
            return AnimalState.FLEEING;
        }
        
        // Quy trình quyết định bình thường (bao gồm kiểm tra đói ăn trong Herbivore)
        return super.makeDecision();
    }


    @Override
    public void cloneSelf() {
        EntityManager em = EntityManager.getInstance();
        if (em != null) {
            em.spawnRabbit(position.x, position.y);
        }
    }


}
