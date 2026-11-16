package com.gerald.frontend.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.gerald.frontend.objects.enemies.Boss;
import com.gerald.frontend.objects.enemies.Enemy;
import com.gerald.frontend.objects.enemies.Fairy;
import com.gerald.frontend.objects.items.Item;
import com.gerald.frontend.objects.items.ItemType;
import com.gerald.frontend.objects.patterns.ShootingPattern;
import com.gerald.frontend.objects.patterns.bulletStrategy.HomingBulletMovement;
import com.gerald.frontend.objects.patterns.entityStrategy.EntityMovementPattern;
import com.gerald.frontend.objects.patterns.entityStrategy.FixedMovement;
import com.gerald.frontend.objects.patterns.shootingStrategy.CompositeShootingPattern;
import com.gerald.frontend.objects.patterns.shootingStrategy.FrontShot;
import com.gerald.frontend.objects.patterns.shootingStrategy.SideShot;
import com.gerald.frontend.objects.patterns.shootingStrategy.SpreadShot;
import com.gerald.frontend.observers.GameObserver;
import com.gerald.frontend.systems.AssetManager;
import com.gerald.frontend.systems.AudioManager;
import com.gerald.frontend.systems.BulletManager;

import java.util.List;
import java.util.ArrayList;

public class Player extends EntityShooter {
    private String name;
    private int hp;
    private int lives = 3;
    private int power;
    private int spellCards;
    private long score;
    private int grazeCount = 0;
    private boolean focused = false;

    private float shootCooldown = 5f / 60f; // Authentic Touhou 6 EoSD: 5 frames at 60 FPS (12 shots/sec = 0.0833s)
    private float shootTimer = shootCooldown;
    private float homingCooldown = 30f / 60f; // Authentic Touhou 6 EoSD: 30 frames (0.5s) or 15 frames (0.25s)
    private float homingShootTimer = homingCooldown;

    private ShootingPattern mainShootingPattern;
    private ShootingPattern sideShootingPattern;

    private int currentDir = 0; // -1: Left, 0: Idle, 1: Right

    // Observer Pattern Subject
    private List<GameObserver> observers = new ArrayList<>();

    public Player(String name, int hp, int power, int spellCards) {
        super(200, 50, 32, 48, 240f, Color.RED);
        this.name = name;
        this.hp = hp;
        this.power = power;
        this.spellCards = spellCards;
        this.score = 0;
        setMovementPattern(new FixedMovement());
        updatePowerStrategy();
    }

    public Player(float x, float y, String name, int hp, int power, int spellCards) {
        super(x, y, 32, 48, 240f, Color.RED);
        this.name = name;
        this.hp = hp;
        this.power = power;
        this.spellCards = spellCards;
        this.score = 0;
        setMovementPattern(new FixedMovement());
        updatePowerStrategy();
    }

    public Player(float x, float y, String name, int hp, int power, int spellCards,
                  ShootingPattern shootingPattern, EntityMovementPattern movementPattern) {
        super(x, y, 32, 48, 240f, Color.RED, shootingPattern, movementPattern);
        this.name = name;
        this.hp = hp;
        this.power = power;
        this.spellCards = spellCards;
        this.score = 0;
        if (shootingPattern == null) {
            updatePowerStrategy();
        } else {
            this.mainShootingPattern = shootingPattern;
        }
    }

    // Dynamic Strategy Swapping based on Player Power Level & Focus Mode (Shift key)
    public void updatePowerStrategy() {
        float spreadAngle = focused ? 4f : 18f;     // Narrow stream in Focus Mode (4° vs 18°)
        float dualSpreadAngle = focused ? 3f : 12f; // Narrow stream in Focus Mode (3° vs 12°)

        // Authentic Touhou 6 EoSD Side Shot Positions & Angles:
        // Unfocused: +/-24px horizontal offset, -16px vertical, launched outward at 120° / 60° (flared V-shape)
        // Focused:   +/-8px horizontal offset, +16px vertical, launched forward at 100° / 80° (tight focus)
        float sideHOffset = focused ? 8f : 24f;
        float sideVOffset = focused ? 16f : -16f;
        float leftAngle = focused ? 100f : 120f;
        float rightAngle = focused ? 80f : 60f;

        // Authentic Touhou 6 EoSD Speed: Normal = 720 px/s (12 px/f), Homing = 600 px/s (10 px/f)
        float normalSpeed = 720f;
        float homingSpeed = 500f;

        // Authentic Touhou 6 (EoSD) 9-Rank Power System from BulletData.cpp (g_CharacterPowerDataReimuA):
        if (power < 8) {
            // Rank 1 (Power 0 - 7): 1 Center Straight Amulet
            mainShootingPattern = new FrontShot(normalSpeed, 48);
            sideShootingPattern = null;
            homingCooldown = 30f / 60f;
        } else if (power < 16) {
            // Rank 2 (Power 8 - 15): 1 Center Straight Amulet + 2 Side Homing Amulets (Cadence: every 30 frames / 0.5s)
            mainShootingPattern = new FrontShot(normalSpeed, 48);
            sideShootingPattern = new SideShot(homingSpeed, 14, sideHOffset, sideVOffset, leftAngle, rightAngle, true,
                new HomingBulletMovement(360f), "bullet_amulet_homing");
            homingCooldown = 30f / 60f;
        } else if (power < 32) {
            // Rank 3 (Power 16 - 31): 2 Center Straight Amulets (spaced 8px) + 2 Side Homing Amulets (Cadence: 30 frames)
            mainShootingPattern = new SpreadShot(normalSpeed, 2, dualSpreadAngle, 30, 8f, null, null, null);
            sideShootingPattern = new SideShot(homingSpeed, 14, sideHOffset, sideVOffset, leftAngle, rightAngle, true,
                new HomingBulletMovement(360f), "bullet_amulet_homing");
            homingCooldown = 30f / 60f;
        } else if (power < 48) {
            // Rank 4 (Power 32 - 47): 3 Center Spread Amulets + 2 Side Homing Amulets (Cadence: 30 frames)
            mainShootingPattern = new SpreadShot(normalSpeed, 3, spreadAngle, 26);
            sideShootingPattern = new SideShot(homingSpeed, 14, sideHOffset, sideVOffset, leftAngle, rightAngle, true,
                new HomingBulletMovement(360f), "bullet_amulet_homing");
            homingCooldown = 30f / 60f;
        } else if (power < 64) {
            // Rank 5 (Power 48 - 63): 3 Center Spread Amulets + 2 Side Homing Amulets (Cadence accelerated to 15 frames / 0.25s!)
            mainShootingPattern = new SpreadShot(normalSpeed, 3, spreadAngle, 26);
            sideShootingPattern = new SideShot(homingSpeed, 12, sideHOffset, sideVOffset, leftAngle, rightAngle, true,
                new HomingBulletMovement(360f), "bullet_amulet_homing");
            homingCooldown = 15f / 60f;
        } else if (power < 80) {
            // Rank 6 (Power 64 - 79): 3 Center Spread Amulets + 4 Side Homing Amulets (Cadence: 15 frames)
            mainShootingPattern = new SpreadShot(normalSpeed, 3, spreadAngle, 26);
            sideShootingPattern = new CompositeShootingPattern(
                new SideShot(homingSpeed, 9, sideHOffset, sideVOffset, leftAngle, rightAngle, true,
                    new HomingBulletMovement(360f), "bullet_amulet_homing"),
                new SideShot(homingSpeed, 12, sideHOffset + 4f, sideVOffset, leftAngle + 30f, rightAngle - 30f, true,
                    new HomingBulletMovement(360f), "bullet_amulet_homing")
            );
            homingCooldown = 15f / 60f;
        } else if (power < 128) {
            // Rank 7 & 8 (Power 80 - 127): 3 Center Spread Amulets + 4 Side Homing Amulets (Cadence: 15 frames)
            mainShootingPattern = new SpreadShot(normalSpeed, 3, spreadAngle, 26);
            sideShootingPattern = new CompositeShootingPattern(
                new SideShot(homingSpeed, 8, sideHOffset, sideVOffset, leftAngle - 10f, rightAngle + 10f, true,
                    new HomingBulletMovement(360f), "bullet_amulet_homing"),
                new SideShot(homingSpeed, 8, sideHOffset + 4f, sideVOffset, leftAngle + 10f, rightAngle - 10f, true,
                    new HomingBulletMovement(360f), "bullet_amulet_homing")
            );
            homingCooldown = 15f / 60f;
        } else {
            // Rank 9 (Power 128 MAX): 4 Center Spread Amulets (Spaced) + 6 Side Homing Amulets (Cadence: 15 frames)
            mainShootingPattern = new SpreadShot(normalSpeed, 4, spreadAngle * 1.2f, 24, 16f, null, null, null);
            sideShootingPattern = new CompositeShootingPattern(
                new SideShot(homingSpeed, 8, sideHOffset, sideVOffset, leftAngle - 10f, rightAngle + 10f, true,
                    new HomingBulletMovement(360f), "bullet_amulet_homing"),
                new SideShot(homingSpeed, 8, sideHOffset + 4f, sideVOffset, leftAngle + 10f, rightAngle - 10f, true,
                    new HomingBulletMovement(360f), "bullet_amulet_homing"),
                new SideShot(homingSpeed, 8, sideHOffset + 8f, sideVOffset, leftAngle + 30f, rightAngle - 30f, true,
                    new HomingBulletMovement(360f), "bullet_amulet_homing")
            );
            homingCooldown = 15f / 60f;
        }

        // Keep this.shootingPattern complete so EntityShooter and observers see the full pattern
        if (sideShootingPattern != null) {
            setShootingPattern(new CompositeShootingPattern(mainShootingPattern, sideShootingPattern));
        } else {
            setShootingPattern(mainShootingPattern);
        }
    }

    // Touhou Hitboxes: Graze Hitbox is full sprite size (32x48), Core Hitbox is small centered dot (8x8)
    @Override
    public Rectangle getGrazeHitbox() {
        return new Rectangle(x, y, width, height); // Full sprite size
    }

    @Override
    public Rectangle getCoreHitbox() {
        return new Rectangle(x + width / 2f - 4f, y + height / 2f - 4f, 8f, 8f); // Small centered 8x8 hurtbox
    }

    // Observer Registration & Notification
    public void registerObserver(GameObserver observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
            // Immediately notify initial state
            observer.onScoreChanged(score);
            observer.onHpChanged(hp);
            observer.onSpellCardsChanged(spellCards);
            observer.onPowerChanged(power);
            observer.onGrazeChanged(grazeCount);
            observer.onLivesChanged(lives);
        }
    }

    public void removeObserver(GameObserver observer) {
        observers.remove(observer);
    }

    private void notifyScoreChanged() {
        for (GameObserver obs : observers) obs.onScoreChanged(score);
    }

    private void notifyHpChanged() {
        for (GameObserver obs : observers) obs.onHpChanged(hp);
    }

    private void notifySpellCardsChanged() {
        for (GameObserver obs : observers) obs.onSpellCardsChanged(spellCards);
    }

    private void notifyPowerChanged() {
        for (GameObserver obs : observers) obs.onPowerChanged(power);
    }

    private void notifyGrazeChanged() {
        for (GameObserver obs : observers) obs.onGrazeChanged(grazeCount);
    }

    public void notifyLivesChanged() {
        for (GameObserver obs : observers) obs.onLivesChanged(lives);
    }

    private float invulnerableTimer = 0f;

    public boolean isInvulnerable() {
        return invulnerableTimer > 0f;
    }

    public float getInvulnerableTimer() {
        return invulnerableTimer;
    }
    @Override
    public void update(float delta) {
        super.update(delta); // Advances stateTime and movementPattern
        shootTimer += delta;
        homingShootTimer += delta;

        // Authentic Touhou 6 (EoSD): When idle (> 0.15s / 9 frames), prime timers so next press fires frame 0 immediately
        if (shootTimer > 0.15f) {
            shootTimer = shootCooldown;
            homingShootTimer = homingCooldown;
        }

        if (invulnerableTimer > 0f) {
            invulnerableTimer -= delta;
            if (invulnerableTimer < 0f) invulnerableTimer = 0f;
        }
    }

    // Updates character animation state based on horizontal movement direction (dx)
    public void updateAnimationState(float dx) {
        AssetManager assets = AssetManager.getInstance();
        if (dx < 0) { // Moving Left
            if (currentDir != -1) {
                currentDir = -1;
                Animation<TextureRegion> anim = assets.getAnimation("player_left");
                if (anim != null) setAnimation(anim);
            }
        } else if (dx > 0) { // Moving Right
            if (currentDir != 1) {
                currentDir = 1;
                Animation<TextureRegion> anim = assets.getAnimation("player_right");
                if (anim != null) setAnimation(anim);
            }
        } else { // Idle / Stationary
            if (currentDir != 0) {
                currentDir = 0;
                Animation<TextureRegion> anim = assets.getAnimation("player_idle");
                if (anim != null) setAnimation(anim);
            }
        }
    }


    // Command Pattern Movement execution with Playfield Clamping
    public void move(float dx, float dy) {
        this.x += dx * speed;
        this.y += dy * speed;
        clampToPlayfield();
    }

    private void clampToPlayfield() {
        // Playfield bounds: X: 32..416 (Width 384), Y: 16..464 (Height 448) - Authentic 6:7 aspect ratio!
        float minX = 32;
        float maxX = 416 - width;
        float minY = 16;
        float maxY = 464 - height;

        if (x < minX) x = minX;
        if (x > maxX) x = maxX;
        if (y < minY) y = minY;
        if (y > maxY) y = maxY;
    }

    // Command Pattern Bullet Shooting (The ZUN way: Player natively coordinates main and homing cadences)
    @Override
    public void shootBullet(BulletManager bulletManager) {
        if (bulletManager == null) return;

        float originX = x + width / 2f - 8f;
        float originY = y + height;

        // 1. Center / Main Shot (Fires every 5 frames = 0.0833s)
        if (shootTimer >= shootCooldown) {
            if (mainShootingPattern != null) {
                mainShootingPattern.execute(originX, originY, bulletManager, true);
            } else if (shootingPattern != null) {
                shootingPattern.execute(originX, originY, bulletManager, true);
            } else {
                bulletManager.spawnPlayerBullet(originX, originY, 0, 400f, 10 + power);
            }
            shootTimer = 0f;
        }

        // 2. Side / Homing Shot (Fires every 30 frames = 0.5s or 15 frames = 0.25s)
        if (sideShootingPattern != null && homingShootTimer >= homingCooldown) {
            sideShootingPattern.execute(originX, originY, bulletManager, true);
            homingShootTimer = 0f;
        }
    }

    // Command Pattern Bomb / Spell Card execution (Spirit Sign "Fantasy Seal" - 霊符「夢想封印」)
    public void useBomb(BulletManager bulletManager, List<GameObject> entities) {
        if (spellCards > 0) {
            spellCards--;
            invulnerableTimer = 5.0f; // 5 seconds invulnerability during Fantasy Seal!
            notifySpellCardsChanged();
            if (bulletManager != null) {
                bulletManager.clearEnemyBullets(entities); // Clears all enemy bullets on screen & converts to drop items!

                // Find active target enemy to home into (Boss or Fairy)
                GameObject target = null;
                if (entities != null) {
                    for (GameObject obj : entities) {
                        if (obj instanceof Enemy enemy && !enemy.isDestroyed()) {
                            target = enemy;
                            break;
                        }
                    }
                }

                // Spawn 8 giant homing Fantasy Seal spirit orbs surrounding Reimu!
                bulletManager.spawnBombOrbs(x + width / 2f, y + height / 2f, target, entities);
            }
            System.out.println(name + " unleashes SPELL CARD: Spirit Sign Fantasy Seal (霊符「夢想封印」)! All enemy bullets cleared & 8 homing orbs launched!");
        } else {
            System.out.println("No Spell Cards (Bombs) remaining!");
        }
    }

    public void useBomb(BulletManager bulletManager) {
        useBomb(bulletManager, null);
    }

    public void addGraze() {
        this.grazeCount++;
        addScore(50);
        notifyGrazeChanged();
    }

    @Override
    public void render(SpriteBatch batch) {
        if (batch != null && active && !isDestroyed()) {
            // Authentic Touhou respawn / spellcard invulnerability high-frequency flicker
            if (invulnerableTimer > 0f) {
                boolean visible = ((int) (invulnerableTimer * 16)) % 2 == 0;
                if (!visible) return; // Skip frame draw to create fast blinking
            }

            if (focused) {
                batch.setColor(1f, 1f, 1f, 0.5f); // 50% opacity in Focus Mode
            }
            super.render(batch);
            if (focused) {
                batch.setColor(1f, 1f, 1f, 1.0f); // Reset back to 100% opacity
            }
        }
    }

    // Renders visual core hurtbox indicator dot when Focus Mode (Shift) is active
    public void renderFocusIndicator(ShapeRenderer shapeRenderer) {
        if (focused && shapeRenderer != null && active) {
            Rectangle core = getCoreHitbox();
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(Color.RED);
            shapeRenderer.circle(core.x + core.width / 2f, core.y + core.height / 2f, 4f);
            shapeRenderer.setColor(Color.WHITE);
            shapeRenderer.circle(core.x + core.width / 2f, core.y + core.height / 2f, 2f);
            shapeRenderer.end();
        }
    }

    @Override
    public void onCollision(Collidable other) {
        if (other instanceof Fairy) {
            System.out.println("Player touches fairy");
        } else if (other instanceof Boss) {
            System.out.println("Player touches boss");
        } else if (other instanceof Item item) {
            System.out.println("Player touches items");
            collectItem(item);
        }
    }

    public void shoot(Enemy target) {
        int damage = 10 + power;
        System.out.println(name + " shoots " + target.getName() + " dealing " + damage + " DMG!");
        boolean defeated = target.takeDamage(damage);
        if (defeated) {
            addScore(target.getScoreValue());
        }
    }

    public void collectItem(Item item) {
        if (item.isDestroyed()) return;

        ItemType type = item.getItemTypeEnum();
        if (type != null) {
            switch (type) {
                case POWER -> {
                    this.power += type.getPowerBonus();
                    updatePowerStrategy(); // Dynamic Strategy Swap on Power Increase!
                    notifyPowerChanged();
                    addScore(item.getScoreValue());
                    System.out.println(name + " collected POWER item! Power increased to " + power);
                }
                case POINT -> {
                    addScore(item.getScoreValue());
                    System.out.println(name + " collected POINT item!");
                }
                case BOMB -> {
                    this.spellCards++;
                    notifySpellCardsChanged();
                    addScore(item.getScoreValue());
                    System.out.println(name + " collected BOMB item! SpellCards: " + spellCards);
                }
                case LIFE -> {
                    this.lives++;
                    notifyLivesChanged();
                    this.hp = 100;
                    notifyHpChanged();
                    addScore(item.getScoreValue());
                    System.out.println(name + " collected LIFE item! Remaining Lives: " + lives);
                }
            }
        } else {
            addScore(item.getScoreValue());
            System.out.println(name + " collected " + item.getItemType() + "!");
        }

        item.destroy();
    }

    public void takeDamage(int damage) {
        takeDamage(damage, null, null);
    }

    public void takeDamage(int damage, BulletManager bulletManager, List<GameObject> entities) {
        if (isInvulnerable() || isDestroyed()) {
            if (isInvulnerable()) {
                System.out.println(name + " is INVULNERABLE during Spell Card! (0 damage taken)");
            }
            return;
        }
        this.hp -= damage;
        if (this.hp < 0) {
            this.hp = 0;
        }
        notifyHpChanged();
        System.out.println(name + " took " + damage + " damage! Remaining HP: " + this.hp);
        if (this.hp == 0) {
            System.out.println(name + " was defeated (Pichuun~)! ");
            loseLife(bulletManager, entities);
        }
    }

    public void loseLife(BulletManager bulletManager, List<GameObject> entities) {
        this.lives--;
        notifyLivesChanged();

        if (bulletManager != null) {
            bulletManager.clearEnemyBullets();
        }

        if (this.lives > 0) {
            if (bulletManager != null) {
                System.out.println("[Player] " + name + " lost a life! Remaining Lives: " + this.lives + ". Respawning at (200, 50)...");
            }
            respawn();
        } else {
            if (bulletManager != null) {
                System.out.println("[Player] GAME OVER! " + name + " has run out of lives!");
            }
            destroy();
        }
    }

    public void respawn() {
        this.active = true;
        this.x = 200;
        this.y = 50;
        this.hp = 100;
        this.invulnerableTimer = 3.0f; // 3 seconds respawn invulnerability!
        this.power = Math.max(10, this.power - 16); // Touhou death penalty
        this.spellCards = 3; // Reset bombs to 3 on respawn
        updatePowerStrategy();
        notifyHpChanged();
        notifyPowerChanged();
        notifySpellCardsChanged();
    }

    public void resetGame() {
        this.lives = 3;
        this.hp = 100;
        this.power = 0;
        this.spellCards = 3;
        this.score = 0;
        this.grazeCount = 0;
        this.active = true;
        this.x = 200;
        this.y = 50;
        this.invulnerableTimer = 3.0f;
        updatePowerStrategy();
        notifyLivesChanged();
        notifyHpChanged();
        notifyPowerChanged();
        notifySpellCardsChanged();
        notifyScoreChanged();
        notifyGrazeChanged();
    }

    public void addScore(long points) {
        if (points > 0) {
            this.score += points;
            notifyScoreChanged();
            System.out.println(name + " gained " + points + " pts! Total Score: " + this.score);
        }
    }

    public boolean isAlive() {
        return this.hp > 0 && this.lives > 0 && !isDestroyed();
    }

    public boolean isGameOver() {
        return this.lives <= 0;
    }

    public int getLives() { return lives; }
    public void setLives(int lives) {
        this.lives = lives;
        notifyLivesChanged();
    }

    public boolean isFocused() { return focused; }
    public void setFocused(boolean focused) {
        if (this.focused != focused) {
            this.focused = focused;
            updatePowerStrategy(); // Dynamic Strategy Swap: Narrows bullet stream in Focus Mode!
        }
    }

    public int getGrazeCount() { return grazeCount; }

    // Encapsulation getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getHp() { return hp; }
    public void setHp(int hp) {
        this.hp = Math.max(0, hp);
        notifyHpChanged();
    }

    public int getPower() { return power; }
    public void setPower(int power) {
        this.power = power;
        updatePowerStrategy(); // Dynamic Strategy Swap on Power Setter!
        notifyPowerChanged();
    }

    public int getSpellCards() { return spellCards; }
    public void setSpellCards(int spellCards) {
        this.spellCards = spellCards;
        notifySpellCardsChanged();
    }

    public long getScore() { return score; }

    public ShootingPattern getMainShootingPattern() { return mainShootingPattern; }
    public ShootingPattern getSideShootingPattern() { return sideShootingPattern; }
    public float getShootCooldown() { return shootCooldown; }
    public float getHomingCooldown() { return homingCooldown; }
}
