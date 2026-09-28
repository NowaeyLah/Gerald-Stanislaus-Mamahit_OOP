package com.gerald.frontend;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import java.util.ArrayList;
import java.util.List;
import com.gerald.frontend.objects.Player;
import com.gerald.frontend.objects.enemies.Boss;
import com.gerald.frontend.objects.enemies.Fairy;
import com.gerald.frontend.objects.enemies.Enemy;
import com.gerald.frontend.objects.items.Item;
import com.gerald.frontend.objects.items.ItemType;
import com.gerald.frontend.objects.GameObject;
import com.gerald.frontend.objects.Collidable;
import com.badlogic.gdx.math.Rectangle;

public class Main extends ApplicationAdapter {


    // TODO 1: Declare fields for Player, Fairy, Boss, Items, and List<GameObject>
    private Player player;
    private Fairy fairy;
    private Boss boss;
    private Item pointItem;
    private Item powerItem;
    private List<GameObject> entities;
    private ShapeRenderer shapeRenderer;

    @Override
    public void create() {
        shapeRenderer = new ShapeRenderer();
        entities = new ArrayList<>();

        // TODO 2: Instantiate Player (Red square) at (280, 40)
        player = new Player(280, 40, "Reimu Hakurei", 100, 15, 3);

        // TODO 3: Instantiate Fairy (Pink square) at (150, 380)
        fairy = new Fairy(150, 380, "Stage 1 Fairy", 20);

        // TODO 4: Instantiate Boss (Blue square) at (380, 400)
        boss = new Boss(380, 400, "Cirno (Stage 2 Boss)", 150);

        // TODO 5: Instantiate Items (White squares) with downward speeds
        powerItem = new Item(200, 450, 16, 16, 80f, ItemType.POWER, 500L);
        pointItem = new Item(320, 480, 12, 12, 120f, ItemType.POINT, 1000L);


        // TODO 6: Add all entities into the gameObjects list polymorphically
        entities.add(player);
        entities.add(fairy);
        entities.add(boss);
        entities.add(pointItem);
        entities.add(powerItem);
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();

        // 1. Polymorphic Update Loop: Items move downward automatically via Item.update(delta)
        for (GameObject obj : entities) {
            obj.update(delta);
        }
        // AABB Collision detection antara setiap pasangan unik entity
        for (int i = 0; i < entities.size(); i++) {
            for (int j = i + 1; j < entities.size(); j++) {
                GameObject a = entities.get(i);
                GameObject b = entities.get(j);

                if (a.getCoreHitbox().overlaps(b.getCoreHitbox())){
                    a.onCollision(a);
                    b.onCollision(b);
                }
                // TODO: Cek apakah getCoreHitbox() milik a dan b saling overlap (gunakan method .overlaps() milik Rectangle)
                if()
                // TODO: Panggil a.onCollision(b) dan b.onCollision(a)
            }
        }


        // 2. Clear Screen
        ScreenUtils.clear(0.1f, 0.1f, 0.15f, 1f);

        // 3. Polymorphic Render Loop: Draw hitboxes with ShapeRenderer
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (GameObject obj : entities) {
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
