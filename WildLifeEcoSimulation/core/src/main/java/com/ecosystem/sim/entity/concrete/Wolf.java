package com.ecosystem.sim.entity.concrete;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.ecosystem.sim.entity.Animal;
import com.ecosystem.sim.entity.AnimalState;
import com.ecosystem.sim.entity.behavior.IPredator;
import com.ecosystem.sim.map.MapManager;

/**
 * Lớp Sói - Động vật ăn thịt, là nỗi sợ hãi của thỏ
 * Sói chạy nhanh, cơ thể lớn (bodySize = 2) nên không lách được qua bụi rậm nhỏ
 * Có chiến lược săn bắt thông minh
 * Màu sắc: Đỏ
 */
public class Wolf extends Animal implements IPredator {
    private float huntSpeed;
    private float huntingRange;
    private Animal preyDetected;
    
    public Wolf(float x, float y, MapManager mapManager) {
        // Gọi constructor cha với màu đỏ
        super(x, y, new Color(1, 0, 0, 1), mapManager, 14, 14); // 24x24 pixels, RED
        
        // Thuộc tính sói
        this.speed = 90f;           // Tốc độ bình thường
        this.huntSpeed = 140f;      // Tốc độ truy đuổi
        this.bodySize = 2;          // Sói to - bị chặn bởi bụi rậm nhỏ
        this.dominance = 60;        // Trung bình - có quyền lực
        this.senseRadius = 180f;    // Tầm nhìn tốt
        this.huntingRange = 200f;
        
        // Sinh lý sói
        this.health = 100;
        this.maxHealth = 100;
        this.energy = 100;
        this.maxEnergy = 100;
        this.hydration = 100;
        this.maxHydration = 100;
        
        // Sói ăn thịt nên cần nhiều năng lượng khi chạy
        this.hungerRate = 3f;       // Giảm từ 20 xuống 3
        this.thirstRate = 3f;       // Giảm từ 18 xuống 3
        
        this.preyDetected = null;
    }

    @Override
    public void init(float x, float y) {
        super.init(x, y);
        this.preyDetected = null;
        this.speed = 90f; // Tốc độ bình thường
    }

    @Override
    public void reset() {
        super.reset();
        this.preyDetected = null;
    }

    @Override
    protected void updateAIState(float deltaTime) {
        super.updateAIState(deltaTime);
        specificBehavior(deltaTime);
    }

    @Override
    protected AnimalState makeDecision() {
        // ƯU TIÊN: Nếu phát hiện con mồi, truy đuổi
        if (preyDetected != null && 
            position.dst(preyDetected.getPosition()) < huntingRange) {
            return AnimalState.HUNTING;
        }
        
        // Quy trình quyết định bình thường
        return super.makeDecision();
    }

    @Override
    protected void move(float deltaTime) {
        // Nếu đang truy đuổi, dùng tốc độ cao hơn
        if (currentState == AnimalState.HUNTING && preyDetected != null) {
            float tempSpeed = this.speed;
            this.speed = huntSpeed;
            
            // Cập nhật mục tiêu
            setTargetPosition(preyDetected.getPosition());
            
            super.move(deltaTime);
            this.speed = tempSpeed;
            
            // Kiểm tra xem có bắt được con mồi không
            if (position.dst(preyDetected.getPosition()) < width) {
                consumeAnimal(preyDetected);
                preyDetected = null;
            }
        } else {
            super.move(deltaTime);
        }
    }

    @Override
    public void hunt(Animal prey) {
        this.preyDetected = prey;
        this.targetAnimal = prey;
        this.currentState = AnimalState.HUNTING;
        setTargetPosition(prey.getPosition());
    }

    @Override
    public Animal detectPrey(java.util.List<Animal> potentialPrey) {
        for (Animal prey : potentialPrey) {
            float distance = position.dst(prey.getPosition());
            
            // Phát hiện thỏ, hươu, hoặc bất kỳ con vật nào có dominance thấp
            if (distance < huntingRange && prey.getDominance() < this.dominance) {
                return prey;
            }
        }
        return null;
    }

    @Override
    public float getHuntingRange() {
        return huntingRange;
    }

    @Override
    protected Vector2 getSearchDirection(boolean searchWater) {
        if (!searchWater) {
            // Sói tìm con mồi - tuần tra
            return getWanderingDirection();
        }
        return super.getSearchDirection(searchWater);
    }

    @Override
    protected void onStateChanged(AnimalState oldState, AnimalState newState) {
        if (newState == AnimalState.HUNTING) {
            // Bắt đầu truy đuổi
        } else if (oldState == AnimalState.HUNTING && newState != AnimalState.HUNTING) {
            // Dừng truy đuổi
            preyDetected = null;
            targetAnimal = null;
        }
    }

    @Override
    public void specificBehavior(float deltaTime) {
        // Sói tìm kiếm con mồi trong khu vực
        // TODO: Implement pack behavior (sói tuần tra thành bầy)
    }
}
