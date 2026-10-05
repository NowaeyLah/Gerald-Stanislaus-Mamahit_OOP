package com.gerald.frontend.systems;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.gerald.frontend.objects.Player;
import com.gerald.frontend.objects.bullets.Bullet;
import com.gerald.frontend.objects.BulletType;
import com.gerald.frontend.objects.enemies.Boss;
import com.gerald.frontend.objects.enemies.Fairy;
import com.gerald.frontend.objects.items.Item;
import com.gerald.frontend.objects.items.ItemType;

public class EntityFactory {

    // Membuat Player dan memasang animasi 'player_idle' dari AssetManager
    public static Player createPlayer(float x, float y, String name, int hp, int power, int spellCards) {
        Player player = new Player(x, y, name, hp, power, spellCards);
        Animation<TextureRegion> anim = AssetManager.getInstance().getAnimation("player_idle");
        player.setAnimation(anim);
        return player;
    }
    public static Boss createBoss(float x, float y, String name, int hp) {
        // TODO:
        // 1. Buat Boss baru dengan x, y, name, dan hp dari parameter;
        Boss boss = new Boss(x, y, name, hp);
        //    simpan pada variabel lokal bernama `boss`.
        // 2. Ambil animasi "boss_idle" melalui getAnimation(...)
        //    dari AssetManager.getInstance(). Simpan hasilnya pada
        //    variabel lokal bernama `idleAnim`.
        Animation<TextureRegion> idleAnim = AssetManager.getInstance().getAnimation("boss_idle");
        // 3. Pasang idleAnim pada boss melalui boss.setAnimation(...).
        boss.SetAnimation(idleAnim);
        // 4. Kembalikan boss.
        return boss;
    }

    public static Fairy createFairy(float x, float y, String name, int hp) {
        // TODO:
        // 1. Buat Fairy baru dengan x, y, name, dan hp dari parameter;
        //    simpan pada variabel lokal bernama `fairy`.
        Fairy fairy = new Fairy(x, y, name, hp);
        // 2. Ambil animasi "fairy_idle_red" melalui getAnimation(...)
        //    dari AssetManager.getInstance(). Simpan hasilnya pada
        //    variabel lokal bernama `idleAnim`.
        Animation<TextureRegion> idleAnim = AssetManager.getInstance().getAnimation("fairy_idle_red");
        // 3. Pasang idleAnim pada fairy melalui fairy.setAnimation(...).
        fairy.SetAnimation(idleAnim);
        // 4. Kembalikan fairy.
        return fairy;
    }

    public static Fairy createFairy(float x, float y, String name, int hp, String keyString) {
        // TODO:
        // 1. Buat Fairy baru dengan x, y, name, dan hp dari parameter;
        //    simpan pada variabel lokal bernama `fairy`.
        Fairy fairy = new Fairy(x, y, name, hp, keyString);

        // 2. Ambil animasi untuk keyString melalui getAnimation(...)
        //    dari AssetManager.getInstance(). Simpan hasilnya pada
        //    variabel lokal bernama `idleAnim`.
        Animation<TextureRegion> idleAnim = AssetManager.getInstance().getAnimation("fairy_idle_red");

        // 3. Pasang idleAnim pada fairy melalui fairy.setAnimation(...).
        fairy.SetAnimation(idleAnim);

        // 4. Kembalikan fairy.
        return fairy;

    }


    // Membuat Item dan memasang sprite sesuai jenis ItemType dari AssetManager
    public static Item createItem(float x, float y, ItemType itemType) {
        Item item = new Item(x, y, itemType);
        String key = switch (itemType) {
            case POWER -> "item_power";
            case POINT -> "item_point";
            case BOMB  -> "item_bomb";
            case LIFE  -> "item_life";
        };
        TextureRegion sprite = AssetManager.getInstance().getTextureRegion(key);
        item.setSprite(sprite);
        return item;
    }

    // Membuat peluru musuh (tipe DANMAKU, speed 0f, sprite bullet_danmaku)
    public static Bullet createEnemyBullet(float x, float y, int damage) {
        Bullet bullet = new Bullet(x, y, 0f, BulletType.DANMAKU, damage);
        TextureRegion sprite = AssetManager.getInstance().getTextureRegion("bullet_danmaku");
        bullet.setSprite(sprite);
        return bullet;
    }
}
