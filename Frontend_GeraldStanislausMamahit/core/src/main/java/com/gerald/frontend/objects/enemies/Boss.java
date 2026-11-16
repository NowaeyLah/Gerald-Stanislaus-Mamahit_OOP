package com.gerald.frontend.objects.enemies;

import com.badlogic.gdx.graphics.Color;
import com.gerald.frontend.objects.Collidable;
import com.gerald.frontend.objects.Player;
import com.gerald.frontend.objects.patterns.ShootingPattern;
import com.gerald.frontend.objects.patterns.shootingStrategy.SpreadShot;
import com.gerald.frontend.systems.BulletManager;
import com.badlogic.gdx.graphics.Color;
import com.gerald.frontend.objects.patterns.shootingStrategy.SpreadShot;
import com.gerald.frontend.objects.patterns.entityStrategy.FixedMovement;

public class Boss extends Enemy {
    private boolean survivesAtZeroHp = false;

    public Boss(String name, int hp) {
        super(380, 400, 64, 64, Color.BLUE, name, hp, 5000L);
        // TODO (2 poin): Suntikkan strategi bawaan Boss via setter
        // 1. Tetapkan Shooting Pattern sebagai Spread Shot dengan speed 200f, 3 peluru, dan 30f spreadAngleDegerees).
        setShootingPattern(new SpreadShot(200f, 3, 30f));
        setMovementPattern(new FixedMovement());
    }

    public Boss(float x, float y, String name, int hp) {
        super(x, y, 64, 64, Color.BLUE, name, hp, 5000L);
        // TODO (2 poin): Lakukan penyuntikan strategi yang sama pada konstruktor koordinat kustomsetShootingPattern(new SpreadShot(200f, 3, 30f));
        setShootingPattern(new SpreadShot(200f, 3, 30f));
        setMovementPattern(new FixedMovement());
    }
    @Override
    public void onCollision(Collidable other) {
        if (other instanceof Player) {
            System.out.println("Player touches boss");
        }
    }

    public void setLife(int life) {
        this.hp = Math.max(0, life);
        this.maxHp = this.hp;
    }

    @Override
    public boolean takeDamage(int damage) {
        if (!damageable) {
            return false;
        }
        if (!survivesAtZeroHp) {
            return super.takeDamage(damage);
        }
        this.hp = Math.max(0, this.hp - damage);
        System.out.println(name + " took " + damage + " damage! HP: " + this.hp + "/" + this.maxHp);
        return false;
    }


    public boolean isSurvivesAtZeroHp() { return survivesAtZeroHp; }
    public void setSurvivesAtZeroHp(boolean survivesAtZeroHp) { this.survivesAtZeroHp = survivesAtZeroHp; }
}
