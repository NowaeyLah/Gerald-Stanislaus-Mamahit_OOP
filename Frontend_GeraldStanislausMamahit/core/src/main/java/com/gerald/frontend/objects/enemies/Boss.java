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
    public Boss(String name, int hp) {
        super(380, 400, 64, 64, Color.BLUE, name, hp, 5000L);
        // TODO (2 poin): Suntikkan strategi bawaan Boss via setter
        // 1. Tetapkan Shooting Pattern sebagai Spread Shot dengan speed 200f, 3 peluru, dan 30f spreadAngleDegerees).
        shootingPattern = new SpreadShot(200f, 3, 30f);
        // 2. Tetapkan Movement Pattern dengan Fixed Movement.
        movementPattern = new FixedMovement();
    }

    public Boss(float x, float y, String name, int hp) {
        super(x, y, 64, 64, Color.BLUE, name, hp, 5000L);
        // TODO (2 poin): Lakukan penyuntikan strategi yang sama pada konstruktor koordinat kustom
        shootingPattern = new SpreadShot(200f, 3, 30f);
        movementPattern = new FixedMovement();
    }
    @Override
    public void onCollision(Collidable other) {
        if (other instanceof Player) {
            System.out.println("Player touches boss");
        }
    }
}
