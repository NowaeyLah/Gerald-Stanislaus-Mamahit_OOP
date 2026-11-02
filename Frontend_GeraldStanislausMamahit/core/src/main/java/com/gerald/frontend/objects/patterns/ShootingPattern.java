package com.gerald.frontend.objects.patterns;

import com.gerald.frontend.systems.BulletManager;

public interface ShootingPattern {
    void execute(float originX, float originY, BulletManager bulletManager, boolean isPlayer);
}
