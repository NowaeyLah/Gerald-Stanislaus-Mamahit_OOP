package com.gerald.frontend.objects.enemies;

import com.badlogic.gdx.graphics.Color;
import com.gerald.frontend.objects.GameObject;
import com.gerald.frontend.objects.Player;

public class Enemy extends GameObject {
    private String name;
    private int hp;
    private int maxHp;
    protected long scoreValue;

    public Enemy(String name, int hp) {
        super(200, 380, 24, 24, 0, Color.PINK);
        this.name = name;
        this.hp = hp;
        this.maxHp = hp;
        this.scoreValue = 100;
    }

    public Enemy(float x, float y, float width, float height, Color color, String name, int hp, long scoreValue) {
        super(x, y, width, height, 0, color);
        this.name = name;
        this.hp = hp;
        this.maxHp = hp;
        this.scoreValue = scoreValue;
    }

    public String getName() { return this.name; }
    public void setName(String name) { this.name = name; }
    public int getHp() { return this.hp; }
    public void setHp(int hp) { this.hp = Math.max(0, hp); }
    public int getMaxHp() { return this.maxHp; }
    public long getScoreValue() { return this.scoreValue; }
    public void setScoreValue(long scoreValue) { this.scoreValue = scoreValue; }

    public boolean takeDamage(int damage) {
        if (getHp() <= 0) return false;
        setHp(getHp() - damage);

        if (getHp() <= 0) {
            System.out.println(getName() + " took " + damage + " damage! HP: 0/" + getMaxHp());
            System.out.println(getName() + " was defeated!");
            return true;
        } else {
            System.out.println(getName() + " took " + damage + " damage! HP: " + getHp() + "/" + getMaxHp());
            return false;
        }
    }

    public void attack(Player playerName, int damage) {
        System.out.println(getName() + " unleashes bullet barrage on " + playerName.getName() + "!");
        playerName.takeDamage(damage);
    }

    public boolean enemyIsAlive() { return getHp() > 0; }
}
