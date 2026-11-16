package com.gerald.frontend.states;

import com.gerald.frontend.systems.LevelWaveManager;

public interface WaveState {
    void onEnter(LevelWaveManager manager);
    void update(LevelWaveManager manager, float delta);
}
