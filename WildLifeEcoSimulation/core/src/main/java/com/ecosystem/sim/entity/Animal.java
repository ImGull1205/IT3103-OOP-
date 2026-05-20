package com.ecosystem.sim.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.Gdx;
import com.ecosystem.sim.map.MapManager;

/**
 * Lớp cơ sở cho tất cả các loài động vật
 * Quản lý các chỉ số sinh tồn, hành vi và tương tác với môi trường
 */
public abstract class Animal extends Entity {
    // === BIẾN LỌC SỰ SỐNG ===
    protected float health;           // Máu (0-100)
    protected float energy;           // Năng lượng (0-100)
    protected float hydration;        // Độ ẩm (0-100)
    protected float temperatureHeat;  // Độ nóng từ môi trường (0-100)
    
    // === THUỘC TÍNH THỂ CHẤT ===
    protected float speed;            // Tốc độ di chuyển (pixels/s)
    protected int bodySize;           // Kích thước cơ thể: Thỏ=1, Sói=2, Voi=4
    protected int dominance;          // Độ uy quyền: Voi=100, Hổ=80, Sói=60, Thỏ=10
    protected float senseRadius;      // Bán kính tầm nhìn
    
    // === HỆ THỐNG GRAPHIC ===
    protected Color color;            // Màu sắc để vẽ động vật
    
    // === THAM CHIẾU ===
    protected MapManager mapManager;
    
    // === TRẠNG THÁI AI ===
    protected AnimalState currentState;
    protected AnimalState previousState;
    protected float stateTimer;       // Thời gian ở trạng thái hiện tại
    protected Vector2 targetPosition; // Vị trí mục tiêu
    protected Animal targetAnimal;    // Con vật mục tiêu (con mồi hoặc kẻ thù)
    
    // === THAM SỐ SỐNG ===
    protected float maxHealth;
    protected float maxEnergy;
    protected float maxHydration;
    protected float hungerRate;       // Tốc độ mất energy (per second)
    protected float thirstRate;       // Tốc độ mất hydration (per second)
    protected float healthDecayRate;  // Tốc độ mất health khi thiếu các yếu tố
    
    // === TOÁN THỜI GIAN ===
    protected float stateChangeTimer; // Timer để đổi trạng thái
    protected float decisionTimer;    // Timer để quyết định hành động mới

    public Animal(float x, float y, Color color, MapManager mapManager,
                  float width, float height) {
        super(x, y, width, height);
        
        this.color = color;
        this.mapManager = mapManager;
        
        // Khởi tạo chỉ số sinh tồn mặc định
        this.health = 100;
        this.maxHealth = 100;
        this.energy = 100;
        this.maxEnergy = 100;
        this.hydration = 100;
        this.maxHydration = 100;
        this.temperatureHeat = 0;
        
        // Khởi tạo các tham số mặc định
        this.hungerRate = 1.5f;    // Tốc độ đói
        this.thirstRate = 2.0f;    // Tốc độ khát
        this.healthDecayRate = 5f; // -5 máu/s nếu đói/khát kiệt quệ
        
        // Trạng thái AI - khởi chạy ở WANDERING để animals di chuyển ngay
        this.currentState = AnimalState.WANDERING;
        this.previousState = null;
        this.stateTimer = 0;
        this.targetPosition = new Vector2();
        this.stateChangeTimer = 0;
        this.decisionTimer = 0;
        
        // Khởi tạo velocity ngẫu nhiên để tránh kẹt ở (0, 0)
        float randomAngle = com.badlogic.gdx.math.MathUtils.random(360);
        velocity = new Vector2(1, 0).setAngleDeg(randomAngle);
    }

    @Override
    public void init(float x, float y) {
        super.init(x, y);
        this.health = maxHealth;
        this.energy = maxEnergy;
        this.hydration = maxHydration;
        this.temperatureHeat = 0;
        
        this.currentState = AnimalState.WANDERING;
        this.previousState = null;
        this.stateTimer = 0;
        this.stateChangeTimer = 0;
        this.decisionTimer = 0;
        this.targetAnimal = null;
        this.targetPosition.set(0, 0);
        
        float randomAngle = com.badlogic.gdx.math.MathUtils.random(360);
        velocity.set(1, 0).setAngleDeg(randomAngle);
    }

    @Override
    public void reset() {
        super.reset();
        this.health = 0;
        this.energy = 0;
        this.hydration = 0;
        this.currentState = AnimalState.IDLE;
        this.targetAnimal = null;
    }

    @Override
    public void update(float deltaTime) {
        if (!isAlive) return;
        
        // 1. Cập nhật tuổi tác
        age += deltaTime;
        
        // 2. Giảm các chỉ số sinh tồn theo thời gian
        updateSurvivalStats(deltaTime);
        
        // 3. Kiểm tra tình trạng sống chết
        if (shouldDie()) {
            die();
            return;
        }
        
        // 4. Cập nhật trạng thái AI
        updateAIState(deltaTime);
        
        // 5. Di chuyển dựa trên hành vi hiện tại
        move(deltaTime);
        
        // 6. Gọi logic đặc trưng của từng loài (Đa hình)
        specificBehavior(deltaTime);
    }

    /**
     * Cập nhật các chỉ số sinh tồn: energy, hydration, health
     */
    protected void updateSurvivalStats(float deltaTime) {
        energy -= hungerRate * deltaTime;
        energy = Math.max(0, energy);
        
        hydration -= thirstRate * deltaTime;
        hydration = Math.max(0, hydration);
        
        // Sức khỏe giảm nếu đói hoặc khát nghiêm trọng (dưới 20)
        if (energy < 20) health -= healthDecayRate * deltaTime;
        if (hydration < 20) health -= healthDecayRate * deltaTime;
        
        health = MathUtils.clamp(health, 0, maxHealth);
    }

    /**
     * Cập nhật trạng thái AI của động vật sử dụng State Machine
     */
    protected void updateAIState(float deltaTime) {
        stateTimer += deltaTime;
        decisionTimer += deltaTime;
        
        // Đưa ra quyết định mới mỗi 0.5 giây để tránh lag thuật toán
        if (decisionTimer > 0.5f) {
            decisionTimer = 0;
            AnimalState newState = makeDecision();
            
            if (newState != currentState) {
                previousState = currentState;
                currentState = newState;
                stateTimer = 0;
                onStateChanged(previousState, newState);
            }
        }
    }

    /**
     * Quyết định trạng thái tiếp theo dựa trên các điều kiện (Có thể ghi đè)
     */
    protected AnimalState makeDecision() {
        if (currentState == AnimalState.EATING || currentState == AnimalState.DRINKING) {
            if (stateTimer < 2.0f) return currentState; // Đang ăn/uống thì ăn nốt 2 giây
        }
        
        // Tạm thời: Chỉ cho WANDERING để test movement
        // TODO: Sau này mới thêm food finding logic
        return AnimalState.WANDERING;
    }

    protected void onStateChanged(AnimalState oldState, AnimalState newState) {}

    /**
     * Di chuyển động vật dựa trên trạng thái hiện tại
     */
    protected void move(float deltaTime) {
        Vector2 direction = new Vector2(0, 0);
        
        switch (currentState) {
            case WANDERING:
                direction = getWanderingDirection();
                break;
            case SEARCHING_WATER:
                direction = getSearchDirection(true);
                break;
            case SEARCHING_FOOD:
                direction = getSearchDirection(false);
                break;
            case HUNTING:
                if (targetAnimal != null) {
                    direction = getDirectionToTarget(targetAnimal.getPosition());
                }
                break;
            case FLEEING:
                if (targetAnimal != null) {
                    direction = getDirectionToTarget(targetAnimal.getPosition()).scl(-1f); // Chạy ngược hướng
                }
                break;
            case IDLE:
            case EATING:
            case DRINKING:
            case RESTING:
                direction.set(0, 0);
                break;
            default:
                break;
        }
        
        if (direction.len() > 0) {
            direction.nor();
            direction.scl(speed * deltaTime);
            
            Vector2 newPos = new Vector2(position).add(direction);
            if (!isObstructed(newPos)) {
                position.add(direction);
            } else {
                // Nếu va chạm với cây hoặc đá, đổi hướng ngẫu nhiên ngay
                if (currentState == AnimalState.WANDERING) {
                    stateChangeTimer = 3.1f;
                }
            }
        }
        
        // Giới hạn animals trong bounds của map (800x800 = 50x50 tiles)
        clampPositionToMapBounds();
        
        velocity.set(direction).scl(1f / deltaTime);
    }

    /**
     * Giữ animals trong ranh giới của map
     */
    private void clampPositionToMapBounds() {
        float mapWidth = 800f;   // 50 tiles × 16px
        float mapHeight = 800f;
        
        position.x = Math.max(0, Math.min(mapWidth - width, position.x));
        position.y = Math.max(0, Math.min(mapHeight - height, position.y));
    }

    protected Vector2 getWanderingDirection() {
        // Đổi hướng ngẫu nhiên sau mỗi 3 giây
        if (stateChangeTimer > 3.0f) {
            stateChangeTimer = 0;
            float angle = MathUtils.random(360);
            velocity = new Vector2(1, 0).setAngleDeg(angle);
        } else {
            stateChangeTimer += Gdx.graphics.getDeltaTime();
        }
        
        // Trả về hướng hiện tại (đã normalize)
        Vector2 direction = new Vector2(velocity);
        if (direction.len() > 0) {
            direction.nor();
        } else {
            // Fallback nếu velocity = (0,0)
            float angle = MathUtils.random(360);
            direction = new Vector2(1, 0).setAngleDeg(angle);
            velocity.set(direction);
        }
        return direction;
    }

    protected Vector2 getSearchDirection(boolean searchWater) {
        // TODO: Triển khai BFS/A* ở các phần sau để tìm tài nguyên thực sự
        return getWanderingDirection();
    }

    protected Vector2 getDirectionToTarget(Vector2 target) {
        Vector2 direction = new Vector2(target).sub(position);
        if (direction.len() > 0) {
            direction.nor();
        }
        return direction;
    }

    protected boolean isObstructed(Vector2 newPos) {
        // Kiểm tra va chạm đa điểm dựa trên kích thước thực thể
        return mapManager.isObstacle(newPos.x, newPos.y) ||
               mapManager.isObstacle(newPos.x + width, newPos.y) ||
               mapManager.isObstacle(newPos.x, newPos.y + height) ||
               mapManager.isObstacle(newPos.x + width, newPos.y + height);
    }

    protected boolean shouldDie() {
        return health <= 0 || age > 300f; // Chết khi hết máu hoặc sống quá 5 phút tuổi
    }

    public void eat(float foodValue) {
        energy = Math.min(maxEnergy, energy + foodValue);
        currentState = AnimalState.EATING;
        stateTimer = 0;
    }

    public void drink(float waterValue) {
        hydration = Math.min(maxHydration, hydration + waterValue);
        currentState = AnimalState.DRINKING;
        stateTimer = 0;
    }

    public void takeDamage(float damage) {
        health = Math.max(0, health - damage);
    }

    public void consumeAnimal(Animal prey) {
        if (prey != null && prey.isAlive()) {
            float meatValue = prey.bodySize * 20f;
            eat(meatValue);
            prey.die();
        }
    }

    @Override
    public void render(ShapeRenderer shapeRenderer) {
        if (isAlive) {
            shapeRenderer.setColor(color);
            // Vẽ hình tròn tại vị trí
            shapeRenderer.circle(position.x + width / 2, position.y + height / 2, width / 2);
        }
    }

    public abstract void specificBehavior(float deltaTime);

    // ============= GETTERS & SETTERS =============
    public float getHealth() { return health; }
    public float getEnergy() { return energy; }
    public float getHydration() { return hydration; }
    public float getSpeed() { return speed; }
    public int getBodySize() { return bodySize; }
    public int getDominance() { return dominance; }
    public float getSenseRadius() { return senseRadius; }
    public AnimalState getCurrentState() { return currentState; }
    public Animal getTargetAnimal() { return targetAnimal; }
    public Vector2 getTargetPosition() { return targetPosition; }

    public void setTargetAnimal(Animal target) { this.targetAnimal = target; }
    public void setTargetPosition(Vector2 pos) { this.targetPosition.set(pos); }
    public void setHealth(float value) { this.health = Math.min(maxHealth, value); }
    public void setEnergy(float value) { this.energy = Math.min(maxEnergy, value); }
    public void setHydration(float value) { this.hydration = Math.min(maxHydration, value); }
} // Dấu đóng class chính xác nằm ở đây!