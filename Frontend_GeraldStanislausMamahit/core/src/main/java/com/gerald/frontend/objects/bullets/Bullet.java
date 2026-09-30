package com.gerald.frontend.objects.bullets;

import com.gerald.frontend.objects.Collidable;
import com.gerald.frontend.objects.GameObject;
import com.gerald.frontend.objects.BulletType;
import com.badlogic.gdx.graphics.Color;
import com.gerald.frontend.objects.enemies.Enemy;

public class Bullet extends GameObject {
    private BulletType bulletType;
    private int damage;

    public Bullet(float x, float y, BulletType bulletType, int damage) {
        super(x, y, 8, 16, 400f, Color.YELLOW);
        this.bulletType = bulletType;
        this.damage = damage;
    }

    public Bullet(float x, float y, float speed, BulletType bulletType, int damage) {
        super(x, y, 8, 16, speed, Color.YELLOW);
        this.bulletType = bulletType;
        this.damage = damage;
    }

    @Override
    public void update(float delta) {
        // Posisi y bertambah sebesar speed * delta (bullet bergerak ke atas)
        y += speed * delta;
    }

    public BulletType getBulletType() { return bulletType; }
    public void setBulletType(BulletType bulletType) { this.bulletType = bulletType; }

    public int getDamage() { return damage; }
    public void setDamage(int damage) { this.damage = damage; } // Typo camelCase diperbaiki

    @Override
    public void onCollision(Collidable other) {
        if (other instanceof Enemy enemy) {
            // 1. Tampilkan pesan bahwa Bullet mengenai Enemy
            System.out.println("Bullet hit " + enemy.getName() + " for " + damage + " DMG!");

            // 2. Panggil takeDamage() milik Enemy
            enemy.takeDamage(damage);

            // 3. Bikin si bullet hancur (destroy)
            destroy();
        }
    }
}
