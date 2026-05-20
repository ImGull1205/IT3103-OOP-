package com.ecosystem.sim.entity.concrete;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.ecosystem.sim.entity.Animal;
import com.ecosystem.sim.entity.AnimalState;
import com.ecosystem.sim.entity.behavior.IPrey;
import com.ecosystem.sim.map.MapManager;

/**
 * Lớp Thỏ - Động vật ăn cỏ, là con mồi của sói
 * Thỏ chạy nhanh nhưng có cơ thể nhỏ (bodySize = 1)
 * Có thể lách qua bụi rậm nhỏ mà sói không vào được
 * Màu sắc: Xanh lá
 */
public class Rabbit extends Animal implements IPrey {
    private float fleeSpeed;
    private float fleeDistance; // Khoảng cách tối thiểu từ kẻ thù
    private Animal threatDetected;
    
    public Rabbit(float x, float y, MapManager mapManager) {
        // Gọi constructor cha với màu xanh lá
        super(x, y, new Color(0, 1, 0, 1), mapManager, 14, 14); // 16x16 pixels, GREEN
        
        // Thuộc tính thỏ
        this.speed = 80f;           // Tốc độ bình thường
        this.fleeSpeed = 150f;      // Tốc độ chạy thoát
        this.bodySize = 1;          // Thỏ nhỏ - lách được qua bụi rậm
        this.dominance = 10;        // Thấp nhất - phải nhường đường
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
        this.thirstRate = 3f;
        
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
    protected void updateAIState(float deltaTime) {
        super.updateAIState(deltaTime);
        specificBehavior(deltaTime);
    }

    @Override
    protected AnimalState makeDecision() {
        // ƯUTIEN: Nếu phát hiện được kẻ thù gần, chạy thoát
        if (threatDetected != null && 
            position.dst(threatDetected.getPosition()) < fleeDistance) {
            return AnimalState.FLEEING;
        }
        
        // Quy trình quyết định bình thường
        return super.makeDecision();
    }

    @Override
    protected void move(float deltaTime) {
        // Nếu đang chạy thoát, dùng tốc độ cao hơn
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
        this.targetAnimal = predator;
        this.currentState = AnimalState.FLEEING;
        
        // Di chuyển ngược lại từ kẻ thù
        Vector2 fleeDirection = new Vector2(position).sub(predator.getPosition()).nor();
        targetPosition.set(position).add(fleeDirection.scl(fleeDistance));
    }

    @Override
    protected void onStateChanged(AnimalState oldState, AnimalState newState) {
        if (oldState == AnimalState.FLEEING && newState != AnimalState.FLEEING) {
            threatDetected = null;
            targetAnimal = null;
        }
    }

    @Override
    public Animal detectThreat(java.util.List<Animal> potentialThreats) {
        for (Animal threat : potentialThreats) {
            float distance = position.dst(threat.getPosition());
            
            // Phát hiện sói, hổ, hoặc bất kỳ ăn thịt nào
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
    protected Vector2 getSearchDirection(boolean searchWater) {
        // Thỏ tìm cỏ (ở mặt đất) hoặc nước
        if (!searchWater) {
            // Tìm cỏ - đi dạo để tìm
            return getWanderingDirection();
        }
        return super.getSearchDirection(searchWater);
    }

    @Override
    public void specificBehavior(float deltaTime) {
        // Thỏ tìm thức ăn từ cỏ trên bản đồ
        // TODO: Integrate với tile-based food sources
    }
}
