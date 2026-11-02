package com.gerald.frontend.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import java.util.HashMap;
import java.util.Map;

public class AssetManager {
    // 1. Instans tunggal untuk Singleton
    private static AssetManager instance;

    // 2. Cache untuk Flyweight Pattern
    private Map<String, TextureRegion> textureRegionMap;
    private Map<String, Animation<TextureRegion>> animationMap;
    private Map<String, Texture> textureMap;

    // Cari tahu: kenapa Constructor AssetManager bersifat *private*?
    private AssetManager() {
        // TODO: Inisialisasi ketiga Map di atas sebagai HashMap kosong
        textureRegionMap = new HashMap<>();
        animationMap = new HashMap<>();
        textureMap = new HashMap<>();
    }

    // Titik akses global untuk mendapatkan instans tunggal
    public static AssetManager getInstance() {
        // TODO: Jika instance masih null, buat instance baru (Lazy Initialization)
        if(instance == null){
            instance = new AssetManager();
        }
        // Kembalikan referensi instance
        return instance;
    }

    public Texture loadTexture(String filename) {
        // TODO:
        // 1. Periksa apakah filename sudah ada di dalam textureMap.
        if(textureMap.containsKey(filename)){
            return textureMap.get(filename);
        }
        // 2. Jika belum:
        //    - Pastikan Gdx.files != null dan file tersebut ada via Gdx.files.internal(filename).exists()
        //    - Buat Texture baru: new Texture(Gdx.files.internal(filename))
        //    - Simpan ke dalam textureMap dengan key filename
        //    - Jika file tidak ada / Gdx.files belum siap, kembalikan null
        // 3. Kembalikan Texture yang tersimpan
        if (Gdx.files != null && Gdx.files.internal(filename).exists()) {
            Texture tex = new Texture(Gdx.files.internal(filename));
            textureMap.put(filename, tex);
            return tex;
        }
        return null;
    }

    // ========================================================================
// Register the Region Textures
// ========================================================================

    // Mendaftarkan region tunggal
    public void registerRegion(String key, TextureRegion region) {
        textureRegionMap.put(key, region);
    }

    // Memotong satu petak tertentu dari sprite sheet pada koordinat [row][col]
    public void registerRegionFromSheet(String key, String filename, int tileWidth, int tileHeight, int row, int col) {
        Texture tex = loadTexture(filename);
        if (tex != null) {
            TextureRegion[][] grid = TextureRegion.split(tex, tileWidth, tileHeight);
            // TODO: Simpan potongan grid[row][col] ke textureRegionMap dengan key ini
            textureRegionMap.put(key, grid[row][col]);

        }
    }

    // Overload praktis: mendaftarkan animasi mulai dari kolom 0 dengan PlayMode.LOOP
    public void registerAnimationFromSheet(String key, String filename, int tileWidth, int tileHeight, int row, int numFrames, float frameDuration) {
        registerAnimationFromSheet(key, filename, tileWidth, tileHeight, row, 0, numFrames, frameDuration, Animation.PlayMode.LOOP);
    }

    // Mendaftarkan serangkaian frame horizontal sebagai objek Animation
    public void registerAnimationFromSheet(String key, String filename, int tileWidth, int tileHeight, int row, int startCol, int numFrames, float frameDuration, Animation.PlayMode playMode) {
        Texture tex = loadTexture(filename);
        if (tex != null) {
            TextureRegion[][] grid = TextureRegion.split(tex, tileWidth, tileHeight);

            // TODO:
            // 1. Buat array TextureRegion[] sepanjang numFrames
            // 2. Isi array dengan grid[row][startCol + i]
            // 3. Buat Animation<TextureRegion> anim = new Animation<>(frameDuration, frames);
            // 4. Set play mode animasi: anim.setPlayMode(playMode);
            // 5. Simpan anim ke animationMap dengan key
            // 6. Simpan frame pertama (frames[0]) ke textureRegionMap dengan key yang sama (sebagai sprite default)
            TextureRegion[] frames = new TextureRegion[numFrames];
            for (int i = 0; i < numFrames; i++) {
                frames[i] = grid[row][startCol + i];
            }

            Animation<TextureRegion> anim = new Animation<>(frameDuration, frames);
            anim.setPlayMode(playMode);

            animationMap.put(key, anim);
            textureRegionMap.put(key, frames[0]);
        }
    }

    // Overload untuk animasi yang dibalik arahnya (misal menghadap kiri dengan flip horizontal)
    public void registerFlippedAnimationFromSheet(String key, String filename, int tileWidth, int tileHeight, int row, int numFrames, float frameDuration, boolean flipX, boolean flipY) {
        registerFlippedAnimationFromSheet(key, filename, tileWidth, tileHeight, row, 0, numFrames, frameDuration, Animation.PlayMode.LOOP, flipX, flipY);
    }

    public void registerFlippedAnimationFromSheet(String key, String filename, int tileWidth, int tileHeight, int row, int startCol, int numFrames, float frameDuration, Animation.PlayMode playMode, boolean flipX, boolean flipY) {
        Texture tex = loadTexture(filename);
        if (tex != null) {
            TextureRegion[][] grid = TextureRegion.split(tex, tileWidth, tileHeight);

            // TODO: Sama seperti registerAnimationFromSheet, tetapi setiap frame
            // di-copy via 'new TextureRegion(...)' lalu panggil 'frame.flip(flipX, flipY)'
            TextureRegion[] frames = new TextureRegion[numFrames];
            for (int i = 0; i < numFrames; i++) {
                frames[i] = new TextureRegion(grid[row][startCol + i]);
                frames[i].flip(flipX, flipY);
            }

            Animation<TextureRegion> anim = new Animation<>(frameDuration, frames);
            anim.setPlayMode(playMode);

            animationMap.put(key, anim);
            textureRegionMap.put(key, frames[0]);
        }
    }

    public TextureRegion getTextureRegion(String key) {
        return textureRegionMap.get(key);
    }

    public TextureRegion getRegion(String key) {
        return getTextureRegion(key);
    }

    public Animation<TextureRegion> getAnimation(String key) {
        return animationMap.get(key);
    }

    public void init() {
        registerAnimationFromSheet("player_idle", "assets/player.png", 32, 48, 0, 4, 0.1f);

        registerAnimationFromSheet("boss_idle", "assets/rumia.png", 64, 64, 0, 4, 0.15f);

        registerAnimationFromSheet("fairy_idle", "assets/fairy.png", 32, 32, 0, 3, 0.1f);

        registerRegionFromSheet("bullet_amulet", "assets/amulet_reimu.png", 16, 16, 0, 0);
        registerRegionFromSheet("bullet_amulet_homing", "assets/amulet_reimu.png", 16, 16, 1, 0);
        registerRegionFromSheet("bullet_danmaku", "assets/bullets_small.png", 16, 16, 2, 3);

        registerRegionFromSheet("item_power", "assets/items.png", 16, 16, 0, 0);
        registerRegionFromSheet("item_point", "assets/items.png", 16, 16, 0, 1);
        registerRegionFromSheet("item_bomb",  "assets/items.png", 16, 16, 0, 3);
        registerRegionFromSheet("item_life",  "assets/items.png", 16, 16, 0, 5);

        registerAnimationFromSheet("player_idle",  "player.png", 32, 48, 0, 0, 8, 0.125f, Animation.PlayMode.LOOP);
        registerAnimationFromSheet("player_left",  "player.png", 32, 48, 1, 0, 4, 0.12f,  Animation.PlayMode.LOOP);
        registerAnimationFromSheet("player_right", "player.png", 32, 48, 2, 0, 4, 0.12f,  Animation.PlayMode.LOOP);
        registerAnimationFromSheet("boss_idle",    "rumia.png",  64, 64, 0, 0, 4, 0.2f,   Animation.PlayMode.LOOP);
        registerAnimationFromSheet("boss_left",    "rumia.png",  64, 64, 1, 0, 4, 0.15f,  Animation.PlayMode.REVERSED);
        registerAnimationFromSheet("boss_right",   "rumia.png",  64, 64, 2, 0, 4, 0.15f,  Animation.PlayMode.NORMAL);
        registerAnimationFromSheet("fairy_idle_red",  "fairy.png", 32, 32, 1, 0, 8, 0.125f, Animation.PlayMode.LOOP);
        registerAnimationFromSheet("fairy_idle_blue", "fairy.png", 32, 32, 0, 0, 8, 0.125f, Animation.PlayMode.LOOP);




    }

    public void dispose() {
        for (Texture tex : textureMap.values()) {
            tex.dispose();
        }
        textureMap.clear();
        textureRegionMap.clear();
        animationMap.clear();
    }

}

