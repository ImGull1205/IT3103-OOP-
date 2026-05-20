# 🌍 Ecosystem Simulation - Architecture Documentation

## 📋 Overview

Kiến trúc phân cấp cho hệ sinh thái mô phỏng với các loài động vật và thực vật tương tác

---

## 🏗️ Core Architecture

### 1. **Entity Hierarchy** (Phân cấp Thực thể)

```
Entity (Abstract)
├── Animal (Abstract)
│   ├── Rabbit (Thỏ) - IPrey
│   ├── Deer (Hươu) - IPrey
│   ├── Wolf (Sói) - IPredator
│   └── Tiger (Hổ) - IPredator
└── Plant (Abstract)
    ├── Grass (Cỏ)
    └── Tree (Cây)
```

### 2. **Entity Class** - Lớp Cơ Sở Tối Cao

- **Thuộc tính**:
  - `position` (Vector2): Vị trí x, y
  - `velocity` (Vector2): Vận tốc
  - `age` (float): Tuổi tác
  - `isAlive` (boolean): Trạng thái sống chết
  - `width, height`: Kích thước bounding box

- **Phương thức**:
  - `update(deltaTime)`: Cập nhật mỗi khung hình
  - `render(SpriteBatch)`: Vẽ lên màn hình
  - `collidesWith()`: Kiểm tra va chạm
  - `die()`: Kết thúc cuộc sống

---

## 🎯 Animal System (Hệ Thống Động Vật)

### 1. **Survival Stats** (Chỉ Số Sinh Tồn)

```java
health       // Máu (0-100) - Mất khi thiếu energy/hydration
energy       // Năng lượng (0-100) - Mất 15/s, phục hồi khi ăn
hydration    // Nước (0-100) - Mất 20/s, phục hồi khi uống
```

**Cơ chế Chết:**

- Nếu `health <= 0` → Chết
- Nếu `age > 300s` → Chết từ già
- Nếu `energy < 20` hoặc `hydration < 20` → `health -= 5/s`

### 2. **Physical Attributes** (Thuộc Tính Thể Chất)

```java
bodySize    // 1 (Thỏ), 2 (Sói), 3 (Hổ) - quyết định xuyên vật cản
dominance   // 10 (Thỏ) → 100 (Voi) - quyết định nhường đường
speed       // Tốc độ di chuyển bình thường
senseRadius // Tầm nhìn phát hiện côn mồi/kẻ thù
```

### 3. **State Machine** (Máy Trạng Thái)

```
IDLE → WANDERING → SEARCHING_WATER → DRINKING → EATING → RESTING
    ↓
    FLEEING ↔ HUNTING

AnimalState enum:
- IDLE: Đứng yên
- WANDERING: Đi dạo tìm kiếm
- SEARCHING_WATER: Tìm nước (hydration < 30%)
- SEARCHING_FOOD: Tìm thức ăn (energy < 40%)
- HUNTING: Truy đuổi con mồi (Predator)
- FLEEING: Chạy thoát (Prey)
- EATING: Đang ăn
- DRINKING: Đang uống
- RESTING: Nghỉ ngơi phục hồi
- DEAD: Chết
```

### 4. **Decision Making** (Quyết Định)

Cứ **0.5 giây**, động vật quyết định trạng thái tiếp theo:

```
if (hydration < 30%) → SEARCHING_WATER
else if (energy < 40%) → SEARCHING_FOOD
else if (isPredatorNearby) → FLEEING (Prey)
else if (isPreyNearby) → HUNTING (Predator)
else → WANDERING
```

---

## 🎭 Behavior Interfaces (Giao Diện Hành Vi)

### IPrey (Ăn Cây - Thỏ, Hươu)

```java
void flee(Animal predator)          // Chạy thoát khỏi kẻ thù
Animal detectThreat(List<Animal>)   // Phát hiện kẻ thù
float getAwarenessRange()           // Tầm nhìn
```

**Hành vi**: Chạy khỏi bất kỳ con vật nào có `dominance > dominance của mình`

### IPredator (Ăn Thịt - Sói, Hổ)

```java
void hunt(Animal prey)              // Truy đuổi con mồi
Animal detectPrey(List<Animal>)     // Phát hiện con mồi
float getHuntingRange()             // Tầm săn bắt
```

**Hành vi**: Truy đuổi con vật có `dominance < dominance của mình`

---

## 🧬 Concrete Animals (Động Vật Cụ Thể)

### Rabbit (Thỏ)

| Thuộc tính  | Giá trị  | Ghi Chú                   |
| ----------- | -------- | ------------------------- |
| bodySize    | 1        | Lách qua bụi rậm nhỏ      |
| dominance   | 10       | Thấp nhất, hay chạy thoát |
| speed       | 80 px/s  | Bình thường               |
| fleeSpeed   | 150 px/s | Chạy thoát nhanh          |
| senseRadius | 150 px   | Tầm nhìn vừa phải         |
| health      | 80       | Khỏe manh vừa phải        |

**Hành vi**: IPrey → Chạy thoát khỏi Sói, Hổ

### Deer (Hươu)

| Thuộc tính  | Giá trị  | Ghi Chú           |
| ----------- | -------- | ----------------- |
| bodySize    | 1        | Lách qua bụi rậm  |
| dominance   | 25       | Cao hơn Thỏ       |
| speed       | 75 px/s  | Chậm hơn Thỏ      |
| fleeSpeed   | 130 px/s | Chạy thoát nhanh  |
| senseRadius | 160 px   | Tầm nhìn xa       |
| health      | 90       | Khỏe mạnh hơn Thỏ |

**Hành vi**: IPrey → Chạy thoát khỏi Sói, Hổ

### Wolf (Sói)

| Thuộc tính  | Giá trị  | Ghi Chú                     |
| ----------- | -------- | --------------------------- |
| bodySize    | 2        | **Bị chặn bởi bụi rậm nhỏ** |
| dominance   | 60       | Cao, có quyền lực           |
| speed       | 90 px/s  | Nhanh hơn Thỏ               |
| huntSpeed   | 140 px/s | Tốc độ truy đuổi            |
| senseRadius | 180 px   | Tầm nhìn tốt                |
| health      | 100      | Bình thường                 |
| hungerRate  | 20/s     | Ăn nhiều                    |

**Hành vi**: IPredator → Truy đuổi Thỏ, Hươu

### Tiger (Hổ)

| Thuộc tính  | Giá trị  | Ghi Chú                         |
| ----------- | -------- | ------------------------------- |
| bodySize    | 3        | **Rất to, bị chặn bớn bụi rậm** |
| dominance   | 80       | Rất cao, chỉ kém Voi            |
| speed       | 85 px/s  | Chậm hơn Sói                    |
| huntSpeed   | 120 px/s | Đủ nhanh để bắt mồi             |
| senseRadius | 200 px   | Tầm nhìn xa nhất                |
| health      | 120      | Mạnh nhất                       |
| hungerRate  | 25/s     | Ăn rất nhiều                    |

**Hành vi**: IPredator → Truy đuổi tất cả các loài (trừ Hổ khác)

---

## 🌱 Plant System (Hệ Thống Thực Vật)

### Growth Stages (Giai Đoạn Phát Triển)

```
SEED (20%)  →  SPROUT (50%)  →  MATURE (100%)  →  WITHERED (30%)  →  DIE
```

### Grass (Cỏ)

- `growthRate`: 25%/s → 4 giây từ hạt đến trưởng thành
- `maxAge`: 150s → 2.5 phút
- `nutritionalValue`: 25

### Tree (Cây)

- `growthRate`: 15%/s → 6.7 giây từ hạt đến trưởng thành
- `maxAge`: 200s → 3.3 phút
- `nutritionalValue`: 40 (Nhiều dinh dưỡng hơn)

**Ăn**: Chỉ ăn được khi `stage == MATURE`

---

## 🔄 Game Loop / Update Order

```
EcoSim.render() (mỗi frame)
├─ mapManager.render()              // Vẽ bản đồ
├─ entityManager.update(deltaTime)  // CẬP NHẬT LOGIC
│  ├─ updateSurvivalStats()         // Giảm stats
│  ├─ updateAIState()               // Quyết định trạng thái
│  ├─ move()                        // Di chuyển
│  └─ updateInteractions()          // Tương tác
├─ entityManager.render(batch)      // Vẽ thực thể
└─ hud.render(batch)                // Vẽ HUD
```

---

## 🚀 Interaction System (Hệ Thống Tương Tác)

### 1. **Predator-Prey** (Ăn Thịt-Ăn Cây)

```java
// Sói tìm kiếm Thỏ
if (wolf.detectPrey(allAnimals) != null) {
    wolf.hunt(prey);        // IPredator
    prey.flee(wolf);        // IPrey

    // Khi bắt được:
    wolf.consumeAnimal(prey);  // Wolf lấy nutrition
    prey.die();                // Thỏ chết
}
```

### 2. **Dominance Behavior** (Hành Vi Uy Quyền)

```
if (animal1 va chạm animal2) {
    if (animal1.dominance < animal2.dominance) {
        animal1 → tìm ô trống kế cạnh
    } else if (animal2.dominance < animal1.dominance) {
        animal2 → tìm ô trống kế cạnh
    }
}
```

### 3. **Feeding** (Ăn Uống)

```java
// Động vật ăn thực vật
if (animal.collidesWith(plant)) {
    float foodValue = plant.getNutritionalValue();
    animal.eat(foodValue);
    plant.beEaten();
}

// Động vật ăn thịt
if (wolf.collidesWith(rabbit)) {
    wolf.consumeAnimal(rabbit);  // Phục hồi energy
}
```

---

## 📊 Performance Optimization

### ZoneManager - Spatial Partitioning

- Chia bản đồ thành lưới **64×64 zones**
- Chỉ kiểm tra tương tác trong cùng zone + 8 zone kế cạnh
- **Giảm 90% kiểm tra tương tác**

### EntityManager

- Quản lý tất cả entities (`List<Animal>`, `List<Plant>`)
- Xóa entities chết mỗi frame
- Spawn entities mới dựa trên điều kiện

---

## 🎓 Adding New Species

Để thêm loài mới, kế thừa từ `Animal` hoặc `Plant`:

```java
public class Elephant extends Animal implements IPredator {
    public Elephant(float x, float y, Texture texture, MapManager mapManager) {
        super(x, y, texture, mapManager, 40, 40);

        this.bodySize = 4;        // Rất to
        this.dominance = 100;     // Cao nhất
        this.speed = 60;          // Chậm
        this.senseRadius = 250;   // Tầm nhìn xa
    }

    @Override
    protected AnimalState makeDecision() {
        // Logic đặc biệt cho Voi
        return super.makeDecision();
    }

    @Override
    public void specificBehavior(float deltaTime) {
        // Hành vi đặc biệt
    }
}
```

---

## 📝 TODO / Future Enhancements

1. **Breeding System**
   - Hai động vật cùng loài gặp nhau → sinh con
   - Di truyền chỉ số sinh tồn

2. **Advanced Pathfinding**
   - BFS để tìm nước, thức ăn
   - A\* để tránh vật cản hiệu quả

3. **Day/Night Cycle**
   - Một số loài hoạt động ban ngày, một số ban đêm
   - Thay đổi hành vi theo thời gian

4. **Weather System**
   - Mưa → thêm nước
   - Nắng → mất hydration nhanh hơn
   - Lạnh → mất energy nhanh hơn

5. **Sounds & Particles**
   - Âm thanh bước chân, ăn, chết
   - Hạt máu khi bị ăn
   - Hạt lá khi động vật đi ngang

6. **UI Enhancements**
   - Click vào động vật xem chi tiết
   - Đồ thị thống kê số lượng
   - Pause/Resume/Speed controls

---

## 🔗 Class Diagram

```
Entity (Abstract)
├── update(deltaTime)
├── render(batch)
├── collidesWith()
└── die()
    │
    ├─→ Animal (Abstract)
    │   ├── updateSurvivalStats()
    │   ├── updateAIState()
    │   ├── move()
    │   ├── makeDecision()
    │   └── specificBehavior()
    │       │
    │       ├─→ Rabbit (IPrey)
    │       ├─→ Deer (IPrey)
    │       ├─→ Wolf (IPredator)
    │       └─→ Tiger (IPredator)
    │
    └─→ Plant (Abstract)
        ├── updateGrowth()
        ├── advanceGrowthStage()
        └── specificBehavior()
            │
            ├─→ Grass
            └─→ Tree
```

---

**Version**: 1.0  
**Last Updated**: May 19, 2026  
**Author**: AI Assistant  
**Framework**: LibGDX
