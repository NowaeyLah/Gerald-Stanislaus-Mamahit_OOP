package com.gerald.frontend;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.gerald.frontend.commands.InputHandler;
import com.gerald.frontend.objects.GameObject;
import com.gerald.frontend.objects.Player;
import com.gerald.frontend.objects.enemies.Boss;
import com.gerald.frontend.objects.enemies.Fairy;
import com.gerald.frontend.objects.items.Item;
import com.gerald.frontend.objects.items.ItemType;
import com.gerald.frontend.systems.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import com.gerald.frontend.systems.CollisionReferee;
import com.gerald.frontend.systems.BulletManager;
import com.gerald.frontend.ui.GameHUD;

public class Main extends ApplicationAdapter {
    private SpriteBatch batch;

    private Player player;
    private List<Fairy> fairy;
    private Boss boss;
    private Item powerItem;
    private Item pointItem;
    private List<GameObject> entities;
    private BulletManager bulletManager;
    private CollisionReferee collisionReferee;
    private float enemyShootTimer = 0f;
    private BulletManager bulletManager;
    private CollisionReferee collisionReferee;
    private InputHandler inputHandler;
    private GameHUD gameHUD;

    @Override
    public void create() {
        batch = new SpriteBatch();

        fairy = new ArrayList<>();
        entities = new ArrayList<>();

        AssetManager.getInstance().init();
        AudioManager.getInstance().init();
        AudioManager.getInstance().playBgm();

        bulletManager = new BulletManager();
        collisionReferee = new CollisionReferee();

        AssetManager.getInstance().init();

        player = EntityFactory.createPlayer(200, 50, "Reimu Hakurei", 100, 15, 3);
        fairy.add(EntityFactory.createFairy(150, 380, "Red Fairy", 20));
        fairy.add(EntityFactory.createFairy(250, 380, "Blue Fairy", 20, "fairy_idle_blue"));
        boss = EntityFactory.createBoss(250, 400, "Rumia", 150);
        powerItem = EntityFactory.createItem(180, 450, ItemType.POWER);
        pointItem = EntityFactory.createItem(220, 480, ItemType.POINT);

        player.registerObserver(gameHUD);

        entities.add(player);
        entities.addAll(fairy);
        entities.add(boss);
        entities.add(powerItem);
        entities.add(pointItem);

        inputHandler.setEntities(entities);
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();

        if (Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
            player.shootBullet(bulletManager);
        }

        if(Gdx.input.isKeyPressed(Input.Keys.R)){
            restartGame();
        }
        updateEnemyScheduler(delta);

        updateAndClean(entities, delta, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        bulletManager.update(delta, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        collisionReferee.resolveCollisions(player, entities, bulletManager);

        ScreenUtils.clear(0.1f, 0.1f, 0.15f, 1f);

        batch.begin();
        for (GameObject entity : entities) {
            if (!entity.isDestroyed()) {
                entity.render(batch);
            }
        }

        bulletManager.render(batch);
        batch.end();
    }

    private void updateEnemyScheduler(float delta) {
        enemyShootTimer += delta;
        if (enemyShootTimer >= 1.5f) {
            boss.shootBullet(bulletManager);
            enemyShootTimer = 0f;
        }
    }

    private void restartGame() {
        System.out.println("[Main] Restarting game...");
        entities.clear();
        bulletManager.clearEnemyBullets();
        bulletManager.clearPlayerBullets();
        entities.add(player);
        player.resetGame();

        fairy.clear();
        fairy.add(EntityFactory.createFairy(150, 380, "Red Fairy", 20));
        fairy.add(EntityFactory.createFairy(250, 380, "Blue Fairy", 20, "fairy_idle_blue"));
        boss = EntityFactory.createBoss(250, 400, "Rumia", 150);
        powerItem = EntityFactory.createItem(180, 450, ItemType.POWER);
        pointItem = EntityFactory.createItem(220, 480, ItemType.POINT);

        entities.addAll(fairy);
        entities.add(boss);
        entities.add(powerItem);
        entities.add(pointItem);
    }


    public <T extends GameObject> void updateAndClean(List<T> list, float delta, float screenWidth, float screenHeight) {
        Iterator<T> iterator = list.iterator();
        while (iterator.hasNext()) {
            T entity = iterator.next();
            entity.update(delta);

            if (entity.isOffScreen(screenWidth, screenHeight) || entity.isDestroyed()) {
                System.out.println("Removed via Generic Iterator: " + entity.getClass().getSimpleName());
                iterator.remove();
            }
        }
    }

    @Override
    public void dispose() {
        if (batch != null) {
            batch.dispose();
        }
        AssetManager.getInstance().dispose();

    }
}
