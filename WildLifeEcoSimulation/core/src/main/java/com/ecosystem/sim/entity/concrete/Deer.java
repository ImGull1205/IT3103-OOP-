package com.ecosystem.sim.entity.concrete;

import com.badlogic.gdx.graphics.Color;
import com.ecosystem.sim.entity.Animal;
import com.ecosystem.sim.entity.AnimalState;
import com.ecosystem.sim.entity.behavior.IPrey;
import com.ecosystem.sim.map.MapManager;

/**
 * Lớp Hươu - Động vật ăn cỏ, nhanh hơn thỏ nhưng cơ thể lớn hơn
 * Chuẩn mực trung bình: không nhỏ như thỏ nhưng không lớn như sói
 * Màu sắc: Vàng nhạt (tan)
 */
public class Deer extends Animal implements IPrey {
    private float fleeSpeed;
    private float fleeDistance;
    private Animal threatDetected;
    
    public Deer(float x, float y, MapManager mapManager) {
        // Gọi constructor cha với màu vàng nhạt
        super(x, y, new Color(1, 1, 0.7f, 1), mapManager, 14, 14); // 20x20 pixels, TAN
        
        // Thuộc tính hươu
        this.speed = 75f;
        this.fleeSpeed = 130f;
        this.bodySize = 1;        // Hươu cũng nhỏ, có thể lách bụi rậm
        this.dominance = 25;      // Cao hơn thỏ, thấp hơn sói
        this.senseRadius = 160f;
        
        // Sinh lý hươu
        this.health = 90;
        this.maxHealth = 90;
        this.energy = 100;
        this.maxEnergy = 100;
        this.hydration = 100;
        this.maxHydration = 100;
        
        this.hungerRate = 3f;
        this.thirstRate = 4f;
        
        this.fleeDistance = 200f;
        this.threatDetected = null;
    }

    @Override
    protected AnimalState makeDecision() {
        if (threatDetected != null && 
            position.dst(threatDetected.getPosition()) < fleeDistance) {
            return AnimalState.FLEEING;
        }
        return super.makeDecision();
    }

    @Override
    protected void move(float deltaTime) {
        if (currentState == AnimalState.FLEEING) {
            float tempSpeed = this.speed;
            this.speed = fleeSpeed;
            super.move(deltaTime);
            this.speed = tempSpeed;
        } else {
            super.move(deltaTime);
        }
    }

    @Override
    public void flee(Animal predator) {
        this.threatDetected = predator;
        this.currentState = AnimalState.FLEEING;
    }

    @Override
    public Animal detectThreat(java.util.List<Animal> potentialThreats) {
        for (Animal threat : potentialThreats) {
            float distance = position.dst(threat.getPosition());
            if (distance < senseRadius && threat.getDominance() > this.dominance) {
                return threat;
            }
        }
        return null;
    }

    @Override
    public float getAwarenessRange() {
        return senseRadius;
    }

    @Override
    public void specificBehavior(float deltaTime) {
        // Hươu tìm cỏ và nước
    }
}
