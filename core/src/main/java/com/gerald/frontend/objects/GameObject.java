package com.gerald.frontend.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

public abstract class GameObject implements Collidable {
    protected float x;
    protected float y;
    protected float width;
    protected float height;
    protected float speed;
    protected Color color;

    public GameObject(float x, float y, float width, float height, float speed, Color color) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.speed = speed;
        this.color = color;
    }

    public void update(float delta) {
        // Base update method
    }

    public void render(ShapeRenderer shapeRenderer) {
        if (shapeRenderer != null && color != null && this.active == true) {
            shapeRenderer.setColor(color);
            shapeRenderer.rect(x, y, width, height);
        }
    }


    @Override
    public Rectangle getCoreHitbox() {
        return new Rectangle(x, y, width, height);
    }

    @Override
    public Rectangle getGrazeHitbox() {
        // Graze hitbox is slightly larger than core hitbox (+10px padding)
        return new Rectangle(x - 10, y - 10, width + 20, height + 20);
    }

    @Override
    public void onCollision(Collidable other) {
        // Base collision handler (can be overridden by subclasses)
    }

    // Encapsulation: Getters and Setters
    public float getX() { return x; }
    public void setX(float x) { this.x = x; }

    public float getY() { return y; }
    public void setY(float y) { this.y = y; }

    public float getWidth() { return width; }
    public void setWidth(float width) {
        if (width > 0) this.width = width;
    }

    public float getHeight() { return height; }
    public void setHeight(float height) {
        if (height > 0) this.height = height;
    }

    public float getSpeed() { return speed; }
    public void setSpeed(float speed) {
        if (speed >= 0) this.speed = speed;
    }

    public Color getColor() { return color; }
    public void setColor(Color color) { this.color = color; }

    protected boolean active = true;

    public boolean isDestroyed() {
        // TODO: kembalikan true jika object TIDAK aktif (active == false)
        if (active == false){
            return true;
        }else{
            return false;
        }
    }

    public void destroy() {
        // TODO: tandai object ini sebagai tidak aktif
        this.active = false;
    }

    public boolean isOffScreen(float screenWidth, float screenHeight) {
        // TODO: kembalikan true jika posisi x atau y sudah keluar dari batas layar
        // Gunakan margin toleransi 50px di setiap sisi, supaya objek yang baru
        float margin = 50f;
        // sedikit melewati tepi layar tidak langsung dianggap hilang.
        if (x + width < -margin || x > screenWidth + margin ||
            y + height < -margin || y > screenHeight + margin) {
            return true;
        } else {
            return false;
        }
    }

}
