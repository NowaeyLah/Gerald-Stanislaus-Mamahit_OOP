package com.gerald.frontend;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input; // Tambahan import Input
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import java.util.List;
import com.gerald.frontend.objects.Player;
import com.gerald.frontend.objects.enemies.Boss;
import com.gerald.frontend.objects.GameObject;
import com.gerald.frontend.objects.enemies.Fairy;
import com.gerald.frontend.objects.items.Item;
import com.gerald.frontend.objects.items.ItemType;
import java.util.ArrayList;
import java.util.Iterator; // Tambahan import Iterator

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
        shapeRenderer = new ShapeRenderer();
        entities = new ArrayList<>();

        // 1. Player: Red square (movable with W/A/S/D or Arrows)
        player = new Player(280, 40, "Reimu Hakurei", 100, 15, 3);

        // 2. Fairy: Pink square (stationary)
        fairy = new Fairy(150, 380, "Stage 1 Fairy", 20);

        // 3. Boss: Blue square (stationary, larger size)
        boss = new Boss(380, 400, "Cirno", 150);

        // 4. Items: White squares (moving downwards linearly)
        powerItem = new Item(200, 450, 16, 16, 80f, ItemType.POWER, 500L);
        pointItem = new Item(320, 480, 12, 12, 120f, ItemType.POINT, 1000L);

        entities.add(player);
        entities.add(fairy);
        entities.add(boss);
        entities.add(powerItem);
        entities.add(pointItem);
    }

    // Soal 2: Membuat Generic Method updateAndClean()
    public <T extends GameObject> void updateAndClean(List<T> list, float delta, float screenWidth, float screenHeight) {
        // 1. Dapatkan Iterator<T> dari list yang diberikan.
        Iterator<T> iterator = list.iterator();

        // 2. Selama masih ada elemen berikutnya (hasNext()):
        while (iterator.hasNext()) {
            // a. Ambil elemen saat ini menggunakan next()
            T element = iterator.next();

            // b. Panggil update(delta)
            element.update(delta);

            // c. Jika elemen isOffScreen ATAU isDestroyed
            if (element.isOffScreen(screenWidth, screenHeight) || element.isDestroyed()) {
                System.out.println("Removed via Generic Iterator: " + element.getClass().getSimpleName());

                // Hapus elemen ini dari list menggunakan method milik Iterator
                iterator.remove();
            }
        }
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();

        // TODO 1: Jika tombol Z baru saja ditekan, tambahkan bullet baru
        if (Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
            entities.add(player.shootBullet());
        }

        // TODO 2: Panggil updateAndClean
        updateAndClean(entities, delta, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // 3. Collision detection antar entity (skip entity yang sudah destroyed)
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
            // TODO 3: Pengecekan apakah entity belum hancur (!entity.isDestroyed())
            if (!entity.isDestroyed()) {
                entity.render(shapeRenderer);
            }
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
