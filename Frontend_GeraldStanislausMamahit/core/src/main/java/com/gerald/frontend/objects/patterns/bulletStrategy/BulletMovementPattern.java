package com.gerald.frontend.objects.patterns.bulletStrategy;

import com.gerald.frontend.objects.bullets.Bullet;

public interface BulletMovementPattern {
    void move(Bullet bullet, float delta);
}
