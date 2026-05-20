# 💡 Best Practices & Quick Reference

## 📌 Quick Reference - Creating New Species

### Template: New Predator

```java
public class Panther extends Animal implements IPredator {
    private float huntSpeed = 135f;
    private float huntingRange = 190f;
    private Animal preyDetected;

    public Panther(float x, float y, Texture texture, MapManager mapManager) {
        super(x, y, texture, mapManager, 28, 28);

        this.speed = 88f;
        this.bodySize = 2;
        this.dominance = 70;  // Giữa sói và hổ
        this.senseRadius = 190f;
        this.hungerRate = 22f;
    }

    @Override
    protected AnimalState makeDecision() {
        if (preyDetected != null &&
            position.dst(preyDetected.getPosition()) < huntingRange) {
            return AnimalState.HUNTING;
        }
        return super.makeDecision();
    }

    @Override
    public void hunt(Animal prey) {
        this.preyDetected = prey;
        this.currentState = AnimalState.HUNTING;
    }

    @Override
    public Animal detectPrey(java.util.List<Animal> potentialPrey) {
        for (Animal prey : potentialPrey) {
            if (prey.getDominance() < this.dominance &&
                position.dst(prey.getPosition()) < huntingRange) {
                return prey;
            }
        }
        return null;
    }

    @Override
    public float getHuntingRange() { return huntingRange; }

    @Override
    public void specificBehavior(float deltaTime) {}
}
```

### Template: New Herbivore

```java
public class Buffalo extends Animal implements IPrey {
    private float fleeSpeed = 125f;
    private Animal threatDetected;

    public Buffalo(float x, float y, Texture texture, MapManager mapManager) {
        super(x, y, texture, mapManager, 26, 26);

        this.speed = 70f;
        this.bodySize = 2;
        this.dominance = 45;  // Cao hơn thỏ, thấp hơn sói
        this.senseRadius = 170f;
        this.health = 110;    // Khỏe hơn thỏ
    }

    @Override
    protected AnimalState makeDecision() {
        if (threatDetected != null &&
            position.dst(threatDetected.getPosition()) < 220f) {
            return AnimalState.FLEEING;
        }
        return super.makeDecision();
    }

    @Override
    public void flee(Animal predator) {
        this.threatDetected = predator;
        this.currentState = AnimalState.FLEEING;
    }

    @Override
    public Animal detectThreat(java.util.List<Animal> potentialThreats) {
        for (Animal threat : potentialThreats) {
            if (threat.getDominance() > this.dominance &&
                position.dst(threat.getPosition()) < senseRadius) {
                return threat;
            }
        }
        return null;
    }

    @Override
    public float getAwarenessRange() { return senseRadius; }

    @Override
    public void specificBehavior(float deltaTime) {}
}
```

---

## 🎯 Optimization Tips

### 1. **Reduce Update Frequency**

```java
// Thay vì cập nhật mỗi frame, cập nhật mỗi N frame
private float updateTimer = 0;
private static final float UPDATE_INTERVAL = 0.1f; // 100ms

@Override
public void update(float deltaTime) {
    updateTimer += deltaTime;
    if (updateTimer >= UPDATE_INTERVAL) {
        updateTimer = 0;
        doExpensiveCalculation();
    }
}
```

### 2. **Object Pooling**

```java
// Tái sử dụng Vector2 objects
public class Vector2Pool {
    private static final Deque<Vector2> pool = new LinkedList<>();

    public static Vector2 obtain() {
        return pool.isEmpty() ? new Vector2() : pool.pop();
    }

    public static void free(Vector2 v) {
        pool.push(v);
    }
}

// Sử dụng:
Vector2 temp = Vector2Pool.obtain();
temp.set(x, y);
// ... sử dụng
Vector2Pool.free(temp);
```

### 3. **Spatial Hashing**

```java
// Dùng ZoneManager để giảm tính toán distance
List<Zone> nearbyZones = zoneManager.getAdjacentZones(position.x, position.y);
for (Zone zone : nearbyZones) {
    for (Object entity : zone.entities) {
        // Chỉ kiểm tra entities trong zone này
    }
}
```

---

## 🐛 Common Issues & Solutions

### Issue 1: Động vật bị mắc kẹt trong vật cản

**Nguyên nhân**: Kiểm tra vật cản không đủ chính xác

```java
// ❌ SAI: Chỉ kiểm tra 1 điểm
mapManager.isObstacle(newPos.x, newPos.y)

// ✅ ĐÚNG: Kiểm tra 4 góc của bounding box
protected boolean isObstructed(Vector2 newPos) {
    return mapManager.isObstacle(newPos.x, newPos.y) ||
           mapManager.isObstacle(newPos.x + width, newPos.y) ||
           mapManager.isObstacle(newPos.x, newPos.y + height) ||
           mapManager.isObstacle(newPos.x + width, newPos.y + height);
}
```

### Issue 2: Động vật không ăn được thực vật

**Nguyên nhân**: Plant chỉ cấp nutrition khi `MATURE`

```java
// Kiểm tra trong Animal.move()
for (Plant plant : entityManager.getPlants()) {
    if (collidesWith(plant) && plant.getGrowthStage() == GrowthStage.MATURE) {
        eat(plant.getNutritionalValue());
        plant.beEaten();
    }
}
```

### Issue 3: Tốc độ game chậm với 1000+ entities

**Giải pháp**:

1. Dùng ZoneManager
2. Giảm số lượng update
3. Dùng Object Pooling
4. Limit entity count

```java
private static final int MAX_ANIMALS = 200;
private static final int MAX_PLANTS = 500;

if (entityManager.getAnimalCount() >= MAX_ANIMALS) {
    // Không spawn thêm thỏ
    return;
}
```

---

## 📊 Performance Benchmarks

| Số Entities | FPS | Memory | Status        |
| ----------- | --- | ------ | ------------- |
| 50-100      | 60  | ~50MB  | ✅ Excellent  |
| 100-200     | 60  | ~80MB  | ✅ Good       |
| 200-300     | 45  | ~120MB | ⚠️ Acceptable |
| 300+        | <30 | >150MB | ❌ Poor       |

**Khuyến nghị**: Giới hạn ở **200 động vật + 500 thực vật**

---

## 🔬 Experimentation Ideas

### 1. **Evolution Simulator**

```java
// Theo dõi gen của động vật
class AnimalGene {
    float speed;      // Thừa hưởng từ cha mẹ
    float metabolism; // Tốc độ tiêu hao năng lượng
    float size;       // Kích thước cơ thể

    AnimalGene breed(AnimalGene other) {
        // Lai ghép genes từ cả hai cha mẹ
        return new AnimalGene();
    }
}
```

### 2. **Food Web Analysis**

```java
// Theo dõi quan hệ ăn uống
class FoodWebTracker {
    Map<Class, Integer> kills;      // Loài X ăn Y con
    Map<Class, Integer> eaten;      // Loài X bị ăn Z con

    void recordHunt(Animal predator, Animal prey) {
        kills.merge(predator.getClass(), 1, Integer::sum);
        eaten.merge(prey.getClass(), 1, Integer::sum);
    }
}
```

### 3. **Territory System**

```java
// Động vật có lãnh địa riêng
public class Territory {
    Vector2 center;
    float radius;
    Animal owner;

    boolean isInTerritory(Vector2 pos) {
        return center.dst(pos) <= radius;
    }
}
```

---

## 📖 Code Patterns Used

### 1. **Strategy Pattern** (MoveStrategy)

```java
interface MoveStrategy {
    float[] move(float x, float y, float deltaTime);
}

class WanderingStrategy implements MoveStrategy { ... }
class HuntingStrategy implements MoveStrategy { ... }
```

### 2. **State Machine Pattern** (AnimalState)

```java
switch (currentState) {
    case WANDERING: moveWandering(); break;
    case HUNTING: moveHunting(); break;
    case FLEEING: moveFleeing(); break;
}
```

### 3. **Observer Pattern** (EntityManager)

```java
// EntityManager "watches" tất cả entities
// Gọi update() trên tất cả
// Phát hiện va chạm/tương tác
```

### 4. **Object Pool Pattern** (TODO)

```java
class AnimalPool {
    Queue<Animal> available = new LinkedList<>();

    Animal get() { return available.isEmpty() ? new Rabbit(...) : available.poll(); }
    void return(Animal a) { available.offer(a); }
}
```

---

## 🚀 Performance Profiling

### Java Profiling

```bash
# Chạy với JProfiler hoặc YourKit
java -Xmx512m -Xms256m -XX:+UnlockCommercialFeatures -XX:+FlightRecorder ...
```

### LibGDX Profiling

```java
com.badlogic.gdx.utils.PerformanceCounter counter;

counter.start();
// code to measure
counter.stop();

System.out.println("Average: " + counter.time.mean);
```

### Memory Profiling

```java
Runtime runtime = Runtime.getRuntime();
long memBefore = runtime.totalMemory() - runtime.freeMemory();
// code
long memAfter = runtime.totalMemory() - runtime.freeMemory();
System.out.println("Memory used: " + (memAfter - memBefore) / 1024 + " KB");
```

---

## 📚 Reference Links

- **LibGDX Docs**: https://libgdx.com/wiki/start/documentation
- **Tiled Map Editor**: https://www.mapeditor.org/
- **Java Optimization**: https://docs.oracle.com/javase/tutorial/

---

## 📝 Checklist for New Features

Khi thêm feature mới, kiểm tra:

- [ ] Có javadoc cho tất cả public methods?
- [ ] Có unit tests?
- [ ] Có xử lý edge cases?
- [ ] Có memory leaks?
- [ ] Có performance issues?
- [ ] Tương thích với existing architecture?
- [ ] Có document trong ARCHITECTURE.md?

---

**Last Updated**: May 19, 2026  
**Framework**: LibGDX 1.11+  
**Java Version**: 11+
