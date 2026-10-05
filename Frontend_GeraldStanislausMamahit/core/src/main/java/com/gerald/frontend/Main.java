package com.gerald.frontend;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import java.util.List;
import com.gerald.frontend.objects.Player;
import com.gerald.frontend.objects.enemies.Boss;
import com.gerald.frontend.objects.GameObject;
import com.gerald.frontend.objects.enemies.Fairy;
import com.gerald.frontend.objects.items.Item;
import com.gerald.frontend.objects.items.ItemType;
import com.gerald.frontend.systems.AssetManager;
import com.gerald.frontend.systems.EntityFactory;

import java.util.ArrayList;
import java.util.Iterator;

public class Main extends ApplicationAdapter {
    private ShapeRenderer shapeRenderer;

    private Player player;
    private Fairy fairy;
    private Boss boss;
    private Item powerItem;
    private Item pointItem;
    private List<GameObject> entities;

    @Override
    public void create() {
        // TODO 1:
        // Pada inisialisasi renderer, buat SpriteBatch dan simpan ke batch.
        // Hint LibGDX: new SpriteBatch().
        SpriteBatch batch = new SpriteBatch();

        // TODO 2:
        // Inisialisasi list fairy dan entities sebagai ArrayList kosong.
        List<Fairy> fairy = new ArrayList<>();
        List<GameObject> entities = new ArrayList<>();

        // TODO 3:
        // Sebelum membuat entitas, ambil instance AssetManager dan panggil init().
        AssetManager.getInstance().init();

        // TODO 4:
        // Ubah pembuatan semua entitas! Ikuti tabel dan buat Player, Fairy, Boss, dan Item
        // agar memakai metode EntityFactory yang benar.
        // Untuk kedua Fairy, masukkan mereka ke list fairy menggunakan add(...).
        Player player = EntityFactory.createPlayer(280, 40, "Reimu Hakurei", 100, 15, 3);
        Boss boss = EntityFactory.createBoss(380, 400, "Rumia", 150);
        Item powerItem = EntityFactory.createItem(200, 450, ItemType.POWER);
        Item pointItem = EntityFactory.createItem(320, 480, ItemType.POINT);
        fairy.add(EntityFactory.createFairy(150, 380, "Red Fairy", 20));
        fairy.add(EntityFactory.createFairy(250, 380, "Blue Fairy", 20, "fairy_idle_blue"));
        // TODO 5:
        // Masukkan semua objek yang baru saja kita buat ke dalam entities.
        entities.add(Player);
        entities.add(Boss);
        entities.add(powerItem);
        entities.add(pointItem);
    }


    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();

        // 1. Check Player Bullet shooting input (Key Z)
        if (Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
            entities.add(player.shootBullet());
        }

        // 2. Generic update & safe removal of off-screen/destroyed entities using non-static instance method
        updateAndClean(entities, delta, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // 3. Collision detection between active entities
        for (int i = 0; i < entities.size(); i++) {
            for (int j = i + 1; j < entities.size(); j++) {
                GameObject a = entities.get(i);
                GameObject b = entities.get(j);

                if (!a.isDestroyed() && !b.isDestroyed()) {
                    if (a.getCoreHitbox().overlaps(b.getCoreHitbox())) {
                        a.onCollision(b);
                        b.onCollision(a);
                    }
                }
            }
        }

        // 4. Clear screen
        ScreenUtils.clear(0.1f, 0.1f, 0.15f, 1f);

        // 5. Render filled hitboxes with ShapeRenderer
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (GameObject entity : entities) {
            if (!entity.isDestroyed()) {
                entity.render(shapeRenderer);
            }
        }
        shapeRenderer.end();

        // ... Bagian atas tetap sama
        ScreenUtils.clear(0.1f, 0.1f, 0.15f, 1f);

        batch.begin();
        for (GameObject entity : entities) {
            if (!entity.isDestroyed()) {
                // TODO: Buat agar setiap entity melakukan method .render() dengan mengoper parameter SpriteBatch.

            }
        }
        batch.end();
    }



    // Non-static (Instance) Generic Method with Bounded Type Parameter <T extends GameObject>
    public <T extends GameObject> void updateAndClean(List<T> list, float delta, float screenWidth, float screenHeight) {
        Iterator<T> iterator = list.iterator();
        while (iterator.hasNext()) {
            T entity = iterator.next();
            entity.update(delta);

            if (entity.isOffScreen(screenWidth, screenHeight) || entity.isDestroyed()) {
                System.out.println("Removed via Generic Iterator: " + entity.getClass().getSimpleName());
                iterator.remove(); // Safe removal using Iterator!
            }
        }
    }

    @Override
    public void dispose() {
        if (batch != null) {
            batch.dispose();
        }

        // TODO: Panggil dispose untuk AssetManager agar Texture yang dimuat juga dilepas.
        AssetManager.dispose();
    }

}
