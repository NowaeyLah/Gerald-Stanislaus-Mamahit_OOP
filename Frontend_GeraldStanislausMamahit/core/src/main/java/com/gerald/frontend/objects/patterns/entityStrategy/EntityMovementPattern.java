package com.gerald.frontend.objects.patterns.entityStrategy;

import com.gerald.frontend.objects.GameObject;

public interface EntityMovementPattern {
    // TODO (4 poin): Deklarasikan method `move` dengan kontrak dan parameter yang dijelaskan di atas.
    void move(GameObject entity, float delta);

}
