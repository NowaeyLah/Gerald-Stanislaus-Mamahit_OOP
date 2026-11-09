package com.gerald.frontend.systems;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gerald.frontend.objects.bullets.BulletType;
import com.gerald.frontend.objects.GameObject;
import com.gerald.frontend.objects.bullets.Bullet;
import com.gerald.frontend.objects.items.ItemType;
import com.gerald.frontend.objects.items.Item;
import com.gerald.frontend.objects.patterns.bulletStrategy.FantasySealMovement;
import com.gerald.frontend.objects.patterns.bulletStrategy.HomingBulletMovement;
import com.gerald.frontend.objects.patterns.shootingStrategy.CompositeShootingPattern;
import com.gerald.frontend.objects.patterns.shootingStrategy.RingShot;

import java.util.*;

public class BulletManager {
    private List<Bullet> activePlayerBullets;
    private List<Bullet> activeEnemyBullets;
    private Queue<Bullet> playerBulletPool;
    private Queue<Bullet> enemyBulletPool;
    private List<GameObject> entities;


    public BulletManager() {
        activePlayerBullets = new ArrayList<>();
        activeEnemyBullets = new ArrayList<>();
        playerBulletPool = new LinkedList<>();
        enemyBulletPool = new LinkedList<>();
    }

    // Object Pool Spawning for Player Bullets via EntityFactory
    public Bullet spawnPlayerBullet(float x, float y, float vx, float vy, int damage) {
        return spawnPlayerBullet(x, y, vx, vy, damage, "bullet_amulet");
    }


    public Bullet spawnPlayerBullet(float x, float y, float vx, float vy, int damage, String spriteKey) { // Tambahin String spriteKey sebagai parameter
        String key = (spriteKey != null) ? spriteKey : "bullet_amulet"; // Tambahkan variabel String lokal key
        Bullet bullet;
        if (!playerBulletPool.isEmpty()) {
            bullet = playerBulletPool.poll();
            bullet.init(x, y, vx, vy, BulletType.AMULET, damage, true);
            bullet.setSprite(AssetManager.getInstance().getTextureRegion(key)); // Tambahkan setSprite menggunakan key
        } else {
            bullet = EntityFactory.createPlayerBullet(x, y, damage, key); // Tambahkan key sebagai argumen ke-4
            bullet.init(x, y, vx, vy, BulletType.AMULET, damage, true);
        }
        activePlayerBullets.add(bullet);
        return bullet;
    }

    // Object Pool Spawning for Enemy Bullets via EntityFactory
    public Bullet spawnEnemyBullet(float x, float y, float vx, float vy, int damage) {
        return spawnEnemyBullet(x, y, vx, vy, damage, "bullet_danmaku");
    }

    public Bullet spawnEnemyBullet(float x, float y, float vx, float vy, int damage, String spriteKey) { // Tambahin String spriteKey sebagai parameter
        String key = (spriteKey != null) ? spriteKey : "bullet_danmaku"; // Tambahkan variabel String lokal key
        Bullet bullet;
        if (!enemyBulletPool.isEmpty()) {
            bullet = enemyBulletPool.poll();
            bullet.init(x, y, vx, vy, BulletType.DANMAKU, damage, false);
            bullet.setSprite(AssetManager.getInstance().getTextureRegion(key)); // Tambahkan setSprite menggunakan key
        } else {
            bullet = EntityFactory.createEnemyBullet(x, y, damage, key); // Tambahkan key sebagai argumen ke-4
            bullet.init(x, y, vx, vy, BulletType.DANMAKU, damage, false);
        }
        activeEnemyBullets.add(bullet);
        return bullet;
    }

    public void update(float delta, float screenWidth, float screenHeight) {
        // Safe update & recycling of player bullets using Iterator
        Iterator<Bullet> pIter = activePlayerBullets.iterator();
        while (pIter.hasNext()) {
            Bullet bullet = pIter.next();
            if (entities != null && bullet.getMovementPattern() instanceof HomingBulletMovement homing && homing.getEntities() == null) {
                homing.setEntities(entities);
            }
            bullet.update(delta);

            if (bullet.isOffScreen(screenWidth, screenHeight) || bullet.isDestroyed()) {
                bullet.destroy();
                pIter.remove();
                playerBulletPool.offer(bullet); // Return to pool!
            }
        }

        // Safe update & recycling of enemy bullets using Iterator
        Iterator<Bullet> eIter = activeEnemyBullets.iterator();
        while (eIter.hasNext()) {
            Bullet bullet = eIter.next();
            bullet.update(delta);

            if (bullet.isOffScreen(screenWidth, screenHeight) || bullet.isDestroyed()) {
                bullet.destroy();
                eIter.remove();
                enemyBulletPool.offer(bullet); // Return to pool!
            }
        }
    }

    public void render(SpriteBatch batch) {
        for (Bullet b : activePlayerBullets) {
            if (!b.isDestroyed()) b.render(batch);
        }
        for (Bullet b : activeEnemyBullets) {
            if (!b.isDestroyed()) b.render(batch);
        }
    }

    // Bomb / Spell Card execution: Clear active enemy bullets and convert to drop items!
    public void clearEnemyBullets(List<GameObject> entities) {
        Iterator<Bullet> eIter = activeEnemyBullets.iterator();
        while (eIter.hasNext()) {
            Bullet bullet = eIter.next();
            if (entities != null) {
                Item item = EntityFactory.createItem(bullet.getX(), bullet.getY(), ItemType.POINT);
                entities.add(item);
            }
            bullet.destroy();
            eIter.remove();
            enemyBulletPool.offer(bullet); // Recycles back to pool!
        }
    }

    public void clearEnemyBullets() {
        clearEnemyBullets(null);
    }

    public void clearPlayerBullets() {
        Iterator<Bullet> pIter = activePlayerBullets.iterator();
        while (pIter.hasNext()) {
            Bullet bullet = pIter.next();
            bullet.destroy();
            pIter.remove();
            playerBulletPool.offer(bullet);
        }
    }

    public void spawnBombOrbs(float originX, float originY, GameObject targetEnemy, List<GameObject> entities) {
        int orbCount = 8;
        int orbDamage = 35; // 35 damage x 8 orbs = 280 total spell card damage potential!

        List<GameObject> targetEntities = (entities != null) ? entities : this.entities;
        // TODO (2 poin): Terapkan Strategy Composition — buat objek RingShot dengan bulletSpeed 150f, jumlah peluru orbCount, damage orbDamage, dan strategi pergerakan peluru berupa FantasySealMovement(targetEnemy, targetEntities), lalu panggil execute(originX, originY, this, true)
        new CompositeShootingPattern(new RingShot(150f, orbCount, orbDamage, FantasySealMovement(targetEnemy, targetEntities), n)
        System.out.println("[BulletManager] Spawned 8 Bomb Spirit Orbs via RingShot composed with FantasySealMovement!");
    }

    public void spawnBombOrbs(float originX, float originY, GameObject targetEnemy) {
        spawnBombOrbs(originX, originY, targetEnemy, null);
    }


    // Dynamic Getters & Clean Recycling Methods for Testing
    public List<Bullet> getActivePlayerBullets() { return activePlayerBullets; }
    public List<Bullet> getActiveEnemyBullets() { return activeEnemyBullets; }
    public Queue<Bullet> getPlayerBulletPool() { return playerBulletPool; }
    public Queue<Bullet> getEnemyBulletPool() { return enemyBulletPool; }
    public int getPlayerPoolSize() { return playerBulletPool.size(); }
    public int getEnemyPoolSize() { return enemyBulletPool.size(); }
    public List<GameObject> getEntities() { return entities; }
    public void setEntities(List<GameObject> entities) { this.entities = entities; }
}
