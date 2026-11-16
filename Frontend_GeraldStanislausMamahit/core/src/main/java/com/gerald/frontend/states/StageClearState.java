package com.gerald.frontend.states;

import com.gerald.frontend.objects.enemies.Boss;
import com.gerald.frontend.systems.LevelWaveManager;

public class StageClearState implements WaveState {
    // TODO: Implementasikan @Override method dari WaveState:
    // 1. Buatlah agar ketika masuk state, cetak pesan stage clear, lalu ambil boss dari manager.
    //    (HINT: Gunakan Helper yang ada di LevelManager!)
    //    1.1. Cek! jika `boss != null && !boss.isDestroyed()`
    //    1.2. Maka, hancurkan boss tersebut (persiapan saat bos ditambahkan di CS).
    // 2. Biarkan update(manager, delta) kosong aja.
    @Override
    public void onEnter(LevelWaveManager manager) {
        System.out.println("[StageClearState] Stage 1 Clear!");
        Boss boss = manager.getBoss();
        if (boss != null && !boss.isDestroyed()) {
            boss.destroy();
        }
    }

    @Override
    public void update(LevelWaveManager manager, float delta) {
        // kosong
    }
}
