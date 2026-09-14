package com.gerald.frontend.objects;

import com.badlogic.gdx.graphics.Color;
import com.gerald.frontend.objects.enemies.Enemy;
import com.gerald.frontend.objects.items.Item;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.gerald.frontend.objects.items.ItemType;

public class Player extends GameObject {
    private String name;
    private int hp;
    private int power;
    private int spellCards;
    protected long score;

    public Player(String name, int hp, int power, int spellCards) {
        super(280, 40, 32, 32, 0, Color.RED);
        this.name = name;
        this.hp = hp;
        this.power = power;
        this.spellCards = spellCards;
        this.score = 0;
    }

    public Player(float x, float y, String name, int hp, int power, int spellCards) {
        super(x, y, 32, 32, 0, Color.RED);
        this.name = name;
        this.hp = hp;
        this.power = power;
        this.spellCards = spellCards;
        this.score = 0;
    }

    public String getName() { return this.name; }
    public void setName(String name) { this.name = name; }
    public int getHp() { return this.hp; }
    public void setHp(int hp) { this.hp = Math.max(0, hp); }
    public int getPower() { return this.power; }
    public void setPower(int power) { this.power = power; }
    public int getSpellCards() { return this.spellCards; }
    public void setSpellCards(int spellCards) { this.spellCards = spellCards; }
    public long getScore() { return this.score; }

    public void takeDamage(int damage) {
        setHp(getHp() - damage);
        if (getHp() > 0) {
            System.out.println(getName() + " took " + damage + " damage! Remaining HP: " + getHp());
        } else {
            System.out.println(getName() + " took " + damage + " damage! Remaining HP: 0");
            System.out.println(getName() + " was defeated (Pichuun~)! ");
        }
    }

    public void shoot(Enemy target) {
        int damage = 10 + getPower();
        System.out.println(getName() + " shoots " + target.getName() + " dealing " + damage + " DMG!");
        boolean isDefeated = target.takeDamage(damage);
        if (isDefeated) {
            addScore(target.getScoreValue());
        }
    }

    public boolean playerIsAlive() {
        return getHp() > 0;
    }

    public void addScore(long points) {
        if (points > 0) {
            this.score += points;
            System.out.println(getName() + " gained " + points + " pts! Total Score: " + this.score);
        }
    }

    public void collectItem(Item item) {
        System.out.println(getName() + " collected " + item.getItemType() + "!");
        if (item.getScoreValue() > 0) {
            addScore(item.getScoreValue());
        }
    }
    public void collectItem(Item item) {
        ItemType type = item.getItemTypeEnum();
        if (type != null) {
            switch (type) {
                case POWER -> {
                    // 1. Tambahkan power sebesar type.getPowerBonus() lewat this.power
                    power += type.getPowerBonus();
                    // 2. Tambahkan score sebesar item.getScoreValue() lewat addScore() (addScore() sudah otomatis mencetak "gained X pts!")
                    addScore(ItemType.POWER.getScoreValue());
                    // 3. Cetak: [name] collected POWER item! Power increased to [power]
                    System.out.println(Player + " collected POWER item! Power increased to " + power);

                }
                case POINT -> {
                    // 1. Tambahkan score sebesar item.getScoreValue() lewat addScore()
                    addScore(ItemType.POINT.getScoreValue());
                    // 2. Cetak: [name] collected POINT item!
                    System.out.println(Player + " collected POINT item!");
                }
                case BOMB -> {
                    // 1. Tambahkan spellCards sebesar 1
                    spellCards +=1;
                    // 2. Tambahkan score sebesar item.getScoreValue() lewat addScore()
                    addScore(ItemType.BOMB.getScoreValue());
                    // 3. Cetak: [name] collected BOMB item! SpellCards: [spellCards]
                    System.out.println(Player + " collected BOMB item! SpellCards: " + spellCards);
                }
                case LIFE -> {
                    // 1. Tambahkan hp sebesar 20
                    hp+= 20;
                    // 2. Tambahkan score sebesar item.getScoreValue() lewat addScore()
                    addScore(ItemType.LIFE.getScoreValue());
                    // 3. Cetak: [name] collected LIFE item! HP: [hp]
                    System.out.println(Player + " collected LIFE item! HP: " + hp);
                }
            }
        } else {
            addScore(item.getScoreValue());
            System.out.println(name + " collected " + item.getItemType() + "!");
        }
    }


    @Override
    public void update(float delta) {
        if (Gdx.input != null) {
            // TODO: Cek input W / UP   → y += speed * delta
            if (Gdx.input.isKeyPressed(Input.Keys.W)){
                y += speed * delta;
            }
            // TODO: Cek input S / DOWN → y -= speed * delta
            if (Gdx.input.isKeyPressed(Input.Keys.S)){
                y -= speed * delta;
            }
            // TODO: Cek input A / LEFT → x -= speed * delta
            if (Gdx.input.isKeyPressed(Input.Keys.A)){
                x -= speed * delta;
            }
            // TODO: Cek input D / RIGHT → x += speed * delta
            if (Gdx.input.isKeyPressed(Input.Keys.D)){
                x += speed * delta;
            }
        }
    }
    @Override
    public void onCollision(Collidable other) {
        // TODO: Cek apakah other yang diterima method ini adalah Item
        if(other.getClass() == Item.class){
            System.out.println("Player touches items");
            collectItem((Item) other);
        }
        // TODO: Cetak "Player touches items" lalu panggil collectItem((Item) other)
    }


}
