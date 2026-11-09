package com.gerald.frontend.objects;

import com.badlogic.gdx.graphics.Color;
import com.gerald.frontend.objects.patterns.ShootingPattern;
import com.gerald.frontend.objects.patterns.entityStrategy.EntityMovementPattern;
import com.gerald.frontend.systems.BulletManager;

public abstract class EntityShooter extends GameObject {
    protected ShootingPattern shootingPattern;
    protected EntityMovementPattern movementPattern;

    public EntityShooter(float x, float y, float width, float height, float speed, Color color) {
        super(x, y, width, height, speed, color);
    }

    public EntityShooter(float x, float y, float width, float height, float speed, Color color,
                         ShootingPattern shootingPattern, EntityMovementPattern movementPattern) {
        super(x, y, width, height, speed, color);
        this.shootingPattern = shootingPattern;
        this.movementPattern = movementPattern;
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        updateMovement(delta);
    }

    // TODO (3 poin): Delegasikan pergerakan ke movementPattern
    // - Lakukan validasi null-check pada movementPattern dan pastikan active bernilai true
    // - Panggil method move() pada movementPattern dengan meneruskan objek ini (this) dan delta
    public void updateMovement(float delta) {
        if (movementPattern != null && active) {
            movementPattern.move(this, delta);
        }

    }

    // TODO (3 poin): Delegasikan penembakan peluru seragam ke shootingPattern
    // - Lakukan validasi null-check pada shootingPattern, pastikan active bernilai true, dan !isDestroyed()
    // - Evaluasi apakah entitas ini instance dari Player: boolean isPlayer = (this instanceof Player);
    // - Hitung koordinat spawn: originX = x + width / 2f - 8f, originY = y + (isPlayer ? height : 0f)
    // - Panggil method execute() pada shootingPattern
    public void shootBullet(BulletManager bulletManager) {
        if (shootingPattern != null && active && !isDestroyed()) {
            boolean isPlayer = (this instanceof Player);
            float originX = x + width / 2f - 8f;
            float originY = y + (isPlayer ? height : 0f);
            shootingPattern.execute(originX, originY, bulletManager, isPlayer);
        }
    }

    // TODO (2 poin): Implementasikan getter & setter untuk shootingPattern dan movementPattern
    public ShootingPattern getShootingPattern() { return shootingPattern; }
    public void setShootingPattern(ShootingPattern shootingPattern) { this.shootingPattern = shootingPattern; }

    public EntityMovementPattern getMovementPattern() { return movementPattern; }
    public void setMovementPattern(EntityMovementPattern movementPattern) { this.movementPattern = movementPattern; }
}
