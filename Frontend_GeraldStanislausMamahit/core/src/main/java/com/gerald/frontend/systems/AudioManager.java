package com.gerald.frontend.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

import java.util.HashMap;
import java.util.Map;

public class AudioManager {
    private static AudioManager instance;
    private final Map<String, Sound> sounds = new HashMap<>();
    private Music bgm;
    private AudioManager() {}

    public static AudioManager getInstance() {
        if (instance == null) {
            instance = new AudioManager();
        }
        return instance;
    }

    public void init() {
        registerSound("bulletShoot", "audio/bulletShoot.mp3");
        registerSound("enemyDead", "audio/enemyDead.mp3");
        registerSound("itemPickup", "audio/itemPickup.mp3");

        bgm = Gdx.audio.newMusic(Gdx.files.internal("audio/BGM.mp3"));
        bgm.setLooping(true);
        bgm.setVolume(0.5f);
    }

    private void registerSound(String key, String path) {
        sounds.put(key, Gdx.audio.newSound(Gdx.files.internal(path)));
    }

    public void playBgm() {
        if (bgm != null && !bgm.isPlaying()) bgm.play();
    }

    public void playSfx(String key) {
        Sound s = sounds.get(key);
        if (s != null) s.play(0.8f);
    }

    public void dispose() {
        for (Sound s : sounds.values()) s.dispose();
        sounds.clear();
        if (bgm != null) bgm.dispose();
        instance = null;
    }
}
