package com.ecosystem.sim.entity.concrete;

import com.badlogic.gdx.graphics.Color;
import com.ecosystem.sim.entity.Animal;
import com.ecosystem.sim.entity.AnimalState;
import com.ecosystem.sim.entity.behavior.IPredator;
import com.ecosystem.sim.map.MapManager;

/**
 * Lớp Hổ - Động vật ăn thịt mạnh nhất, ít xuất hiện, cơ thể lớn
 * Hổ chạy chậm hơn sói nhưng mạnh hơn và có tầm nhìn xa hơn
 * Màu sắc: Cam (orange)
 */
public class Tiger extends Animal implements IPredator {
    private float huntSpeed;
    private float huntingRange;
    private Animal preyDetected;
    private int killCount; // Số lần săn thành công
    
    public Tiger(float x, float y, MapManager mapManager) {
        // Gọi constructor cha với màu cam
        super(x, y, new Color(1, 0.6f, 0, 1), mapManager, 14, 14); // 32x32 pixels, ORANGE
        
        // Thuộc tính hổ
        this.speed = 85f;
        this.huntSpeed = 120f;
        this.bodySize = 3;           // Hổ rất to - bị chặn bởi bụi rậm
        this.dominance = 80;         // Cao - chỉ kém voi
        this.senseRadius = 200f;     // Tầm nhìn xa nhất
        this.huntingRange = 220f;
        
        // Sinh lý hổ
        this.health = 120;
        this.maxHealth = 120;
        this.energy = 100;
        this.maxEnergy = 100;
        this.hydration = 100;
        this.maxHydration = 100;
        
        // Hổ ăn rất nhiều
        this.hungerRate = 4f;
        this.thirstRate = 4f;
        
        this.preyDetected = null;
        this.killCount = 0;
    }

    @Override
    protected void updateAIState(float deltaTime) {
        super.updateAIState(deltaTime);
        
        // Hổ có thể vào trạng thái RESTING sau khi ăn
        if (currentState == AnimalState.EATING && stateTimer > 3.0f) {
            currentState = AnimalState.RESTING;
        }
    }

    @Override
    protected AnimalState makeDecision() {
        // Hổ phục hồi sau khi ăn
        if (health < maxHealth * 0.7f) {
            return AnimalState.RESTING;
        }
        
        if (preyDetected != null && 
            position.dst(preyDetected.getPosition()) < huntingRange) {
            return AnimalState.HUNTING;
        }
        
        return super.makeDecision();
    }

    @Override
    protected void move(float deltaTime) {
        if (currentState == AnimalState.HUNTING && preyDetected != null) {
            float tempSpeed = this.speed;
            this.speed = huntSpeed;
            setTargetPosition(preyDetected.getPosition());
            
            super.move(deltaTime);
            this.speed = tempSpeed;
            
            // Bắt được con mồi
            if (position.dst(preyDetected.getPosition()) < width + preyDetected.getWidth()) {
                consumeAnimal(preyDetected);
                preyDetected = null;
                killCount++;
            }
        } else {
            super.move(deltaTime);
        }
    }

    @Override
    public void hunt(Animal prey) {
        this.preyDetected = prey;
        this.currentState = AnimalState.HUNTING;
        setTargetPosition(prey.getPosition());
    }

    @Override
    public Animal detectPrey(java.util.List<Animal> potentialPrey) {
        // Hổ có thể ăn Thỏ, Hươu, và thậm chí Sói
        Animal bestPrey = null;
        float closestDistance = huntingRange;
        
        for (Animal prey : potentialPrey) {
            float distance = position.dst(prey.getPosition());
            
            // Hổ không ăn hổ khác
            if (prey instanceof Tiger) continue;
            
            if (distance < closestDistance && prey.getDominance() < this.dominance) {
                bestPrey = prey;
                closestDistance = distance;
            }
        }
        
        return bestPrey;
    }

    @Override
    public float getHuntingRange() {
        return huntingRange;
    }

    @Override
    public void specificBehavior(float deltaTime) {
        // Hổ tuần tra khu vực tìm kiếm con mồi
    }

    public int getKillCount() {
        return killCount;
    }
}
