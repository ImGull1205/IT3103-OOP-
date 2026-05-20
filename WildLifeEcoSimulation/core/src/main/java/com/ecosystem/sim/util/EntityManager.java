package com.ecosystem.sim.util;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import com.ecosystem.sim.entity.Animal;
import com.ecosystem.sim.entity.Entity;
import com.ecosystem.sim.entity.Plant;
import com.ecosystem.sim.entity.concrete.Rabbit;
import com.ecosystem.sim.entity.concrete.Wolf;
import com.ecosystem.sim.entity.concrete.Grass;
import com.ecosystem.sim.entity.concrete.Tree;
import com.ecosystem.sim.entity.behavior.IPredator;
import com.ecosystem.sim.entity.behavior.IPrey;
import com.ecosystem.sim.map.MapManager;

import java.util.*;

/**
 * Quản lý tất cả các thực thể trong thế giới
 */
public class EntityManager {
    private List<Entity> entities;
    private List<Animal> animals;
    private List<Plant> plants;
    private MapManager mapManager;
    private ZoneManager zoneManager;
    
    // Object Pools
    private Pool<Rabbit> rabbitPool;
    private Pool<Wolf> wolfPool;
    private Pool<Grass> grassPool;
    private Pool<Tree> treePool;
    
    private static final float SPAWN_COOLDOWN = 5.0f; // Thời gian giữa các lần spawn
    private float spawnTimer;
    
    public EntityManager(MapManager mapManager) {
        this.entities = new ArrayList<>();
        this.animals = new ArrayList<>();
        this.plants = new ArrayList<>();
        this.spawnTimer = 0;
        this.mapManager = mapManager;
        this.zoneManager = new ZoneManager();
        
        // Khởi tạo các Pools
        this.rabbitPool = new Pool<Rabbit>() {
            @Override protected Rabbit newObject() { return new Rabbit(0, 0, mapManager); }
        };
        this.wolfPool = new Pool<Wolf>() {
            @Override protected Wolf newObject() { return new Wolf(0, 0, mapManager); }
        };
        this.grassPool = new Pool<Grass>() {
            @Override protected Grass newObject() { return new Grass(0, 0, mapManager); }
        };
        this.treePool = new Pool<Tree>() {
            @Override protected Tree newObject() { return new Tree(0, 0, mapManager); }
        };
    }

    /**
     * Spawn thực thể dùng Object Pool
     */
    public Rabbit spawnRabbit(float x, float y) {
        Rabbit rabbit = rabbitPool.obtain();
        rabbit.init(x, y);
        addEntity(rabbit);
        return rabbit;
    }

    public Wolf spawnWolf(float x, float y) {
        Wolf wolf = wolfPool.obtain();
        wolf.init(x, y);
        addEntity(wolf);
        return wolf;
    }

    public Grass spawnGrass(float x, float y) {
        Grass grass = grassPool.obtain();
        grass.init(x, y);
        addEntity(grass);
        return grass;
    }

    public Tree spawnTree(float x, float y) {
        Tree tree = treePool.obtain();
        tree.init(x, y);
        addEntity(tree);
        return tree;
    }

    /**
     * Thêm một thực thể vào quản lý (chỉ dùng nội bộ hoặc nếu không dùng Pool)
     */
    public void addEntity(Entity entity) {
        entities.add(entity);
        
        if (entity instanceof Animal) {
            animals.add((Animal) entity);
        } else if (entity instanceof Plant) {
            plants.add((Plant) entity);
        }
    }

    /**
     * Cập nhật tất cả thực thể
     */
    public void update(float deltaTime) {
        // Cập nhật tất cả thực thể
        for (Entity entity : entities) {
            if (entity.isAlive()) {
                entity.update(deltaTime);
            }
        }
        
        // Cập nhật ZoneManager
        zoneManager.clear();
        for (Entity entity : entities) {
            if (entity.isAlive()) {
                ZoneManager.Zone zone = zoneManager.getZone(entity.getX(), entity.getY());
                if (entity instanceof Animal) {
                    zone.animals.add((Animal) entity);
                } else if (entity instanceof Plant) {
                    zone.plants.add((Plant) entity);
                }
            }
        }
        
        // Phát hiện va chạm và tương tác dùng ZoneManager
        updateInteractions();
        
        // Xóa các thực thể chết và trả về Pool
        for (int i = entities.size() - 1; i >= 0; i--) {
            Entity e = entities.get(i);
            if (!e.isAlive()) {
                if (e instanceof Rabbit) rabbitPool.free((Rabbit) e);
                else if (e instanceof Wolf) wolfPool.free((Wolf) e);
                else if (e instanceof Grass) grassPool.free((Grass) e);
                else if (e instanceof Tree) treePool.free((Tree) e);
                entities.remove(i);
            }
        }
        animals.removeIf(a -> !a.isAlive());
        plants.removeIf(p -> !p.isAlive());
        
        // Spawn động vật và thực vật mới (nếu cần)
        updateSpawning(deltaTime);
    }

    /**
     * Cập nhật tương tác giữa các thực thể dùng Spatial Partitioning (ZoneManager)
     */
    private void updateInteractions() {
        for (Animal animal : animals) {
            if (!animal.isAlive()) continue;

            List<ZoneManager.Zone> nearbyZones = zoneManager.getAdjacentZones(animal.getX(), animal.getY());
            
            // Lấy danh sách animals và plants gần kề
            List<Animal> nearbyAnimals = new ArrayList<>();
            List<Plant> nearbyPlants = new ArrayList<>();
            for (ZoneManager.Zone zone : nearbyZones) {
                nearbyAnimals.addAll(zone.animals);
                nearbyPlants.addAll(zone.plants);
            }

            // Sói/Hổ săn bắt
            if (animal instanceof IPredator) {
                IPredator hunter = (IPredator) animal;
                Animal prey = hunter.detectPrey(nearbyAnimals);
                
                if (prey != null && prey instanceof IPrey) {
                    ((IPrey) prey).flee(animal);
                    hunter.hunt(prey);
                }
            }
            
            // Thỏ tìm kiếm kẻ thù
            if (animal instanceof IPrey) {
                IPrey herbivore = (IPrey) animal;
                Animal threat = herbivore.detectThreat(nearbyAnimals);
                
                if (threat != null) {
                    herbivore.flee(threat);
                }
            }
            
            // Động vật ăn thực vật
            for (Plant plant : nearbyPlants) {
                if (!plant.isAlive()) continue;
                if (animal.collidesWith(plant.getX(), plant.getY(), plant.getWidth(), plant.getHeight())) {
                    float foodValue = plant.getNutritionalValue();
                    if (foodValue > 0) {
                        animal.eat(foodValue);
                        plant.beEaten();
                    }
                }
            }
            
            // Xử lý dominance (nhường đường) với các con vật gần
            for (Animal other : nearbyAnimals) {
                if (animal == other || !other.isAlive()) continue;
                if (animal.collidesWith(other.getX(), other.getY(), other.getWidth(), other.getHeight())) {
                    if (animal.getDominance() < other.getDominance()) {
                        Vector2 away = PathFinding.findNearbyEmptySpot(animal.getPosition(), mapManager, 32);
                        animal.setTargetPosition(away);
                    }
                }
            }
        }
    }

    /**
     * Spawn động vật và thực vật mới
     */
    private void updateSpawning(float deltaTime) {
        spawnTimer += deltaTime;
        
        if (spawnTimer >= SPAWN_COOLDOWN) {
            spawnTimer = 0;
            // TODO: Logic sinh sản tự động nếu cần thiết
        }
    }

    /**
     * Vẽ tất cả thực thể
     */
    public void render(ShapeRenderer shapeRenderer) {
        for (Entity entity : entities) {
            if (entity.isAlive()) {
                entity.render(shapeRenderer);
            }
        }
    }

    // ============= GETTERS =============

    public List<Entity> getEntities() { return new ArrayList<>(entities); }
    public List<Animal> getAnimals() { return new ArrayList<>(animals); }
    public List<Plant> getPlants() { return new ArrayList<>(plants); }
    
    public int getAnimalCount() { return animals.size(); }
    public int getPlantCount() { return plants.size(); }
}
