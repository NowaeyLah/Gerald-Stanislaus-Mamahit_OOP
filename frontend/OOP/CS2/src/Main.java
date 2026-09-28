package com.netlab.frontend;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import java.util.ArrayList;
import java.util.List;

public class Main extends ApplicationAdapter {
    private ShapeRenderer shapeRenderer;

    // TODO 1: Declare fields for Player, Fairy, Boss, Items, and List<GameObject>

    
    @Override
    public void create() {
        shapeRenderer = new ShapeRenderer();
        gameObjects = new ArrayList<>();

        // TODO 2: Instantiate Player (Red square) at (280, 40)


        // TODO 3: Instantiate Fairy (Pink square) at (150, 380)


        // TODO 4: Instantiate Boss (Blue square) at (380, 400)


        // TODO 5: Instantiate Items (White squares) with downward speeds


        // TODO 6: Add all entities into the gameObjects list polymorphically

    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();

        // 1. Polymorphic Update Loop: Items move downward automatically via Item.update(delta)
        for (GameObject obj : gameObjects) {
            obj.update(delta);
        }

        // 2. Clear Screen
        ScreenUtils.clear(0.1f, 0.1f, 0.15f, 1f);

        // 3. Polymorphic Render Loop: Draw hitboxes with ShapeRenderer
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (GameObject obj : gameObjects) {
            obj.render(shapeRenderer);
        }
        shapeRenderer.end();
    }

    @Override
    public void dispose() {
        if (shapeRenderer != null) {
            shapeRenderer.dispose();
        }
    }
}




// ==========================================
// MODULE 2: ENCAPSULATION, INHERITANCE & SCORE SYSTEM
// ==========================================
System.out.println("\n\n=== TOUHOU OOP PRACTICUM - MODULE 2: ENCAPSULATION, INHERITANCE & SCORE SYSTEM ===");

// Instantiating polymorphic objects
Player reimu2 = new Player("Reimu Hakurei", 100, 15, 3);
Fairy fairy = new Fairy("Stage 1 Fairy", 20);
Boss cirno = new Boss("Cirno (Stage 2 Boss)", 150);
Item pointItem = new Item(200, 450, 12, 12, 120f, "Point Item", 1000L);

System.out.println("\n--- Testing Encapsulation & Inheritance ---");
System.out.println("Player: " + reimu2.getName() + " | Position: (" + reimu2.getX() + ", " + reimu2.getY() + ")");
System.out.println("Fairy:  " + fairy.getName() + " | Defeat Worth: " + fairy.getScoreValue() + " pts");
System.out.println("Boss:   " + cirno.getName() + " | Defeat Worth: " + cirno.getScoreValue() + " pts | Size: " + cirno.getWidth() + "x" + cirno.getHeight());
System.out.println("Item:   " + pointItem.getItemType() + " | Value: " + pointItem.getScoreValue() + " pts | Speed: " + pointItem.getSpeed());

System.out.println("\n--- Testing Item Movement Update ---");
System.out.println("Initial Item Y: " + pointItem.getY());
pointItem.update(0.5f);
System.out.println("Item Y after 0.5s update: " + pointItem.getY() + " (linear downward movement)");

System.out.println("\n--- Testing Scoring System ---");
System.out.println("Initial Score: " + reimu2.getScore());
reimu2.shoot(fairy);
reimu2.collectItem(pointItem);
reimu2.shoot(cirno);
System.out.println("Final Score: " + reimu2.getScore() + " pts");

System.out.println("\n=== Module 2 Test Completed Successfully ===");


public void setWidth(float width) {
    if (width > 0) this.width = width;
    this.width = width;
}
public void getWidth(){
    return this.width;
}
public void setHeight(float height) {
    if (height > 0) this.height = height;
    this.height = height;
}
public void getHeight(){
    return this.height;
}

public void setSpeed(float speed) {
    if (speed >= 0) this.speed = speed;
    this.speed = speed;
}
public void getSpeed(){
    return this.speed;
}

public class player{

public void setHp(int hp) {
    this.hp = Math.max(0, hp);
    this.hp = hp;
}
public void getHp(){
    return this.hp;
}

public void setName(string name){
    this.name = name;
}
public void getName(){
    return this.name;
}
public void setPower(float power){
    this.power = Math.max;
    this.power = power;
}
public void getPower(){
    return this
}

public void setSpellCards(float spellCards){
    this.spellCards = Math.max;
    this spellCards = spellCards;
}
public void getSpellCards(){
    return this.spellCards;
}
public void getScore(){
    return this.score;
}
}

public class enemy{

public void setHp(int hp) {
    this.hp = Math.max(0, hp);
    this.hp = hp;
}
public void getHp(){
    return this.hp;
}
public void getMaxHp(){
    return this.maxHp;
}
public void setName(string name){
    this.name = name;
}
public void getName(){
    return this.name;
}
public void setScoreValue(float scoreValue){
    this.scoreValue = scoreValue;
}
public void getScoreValue(){
    return this.score;
}
}