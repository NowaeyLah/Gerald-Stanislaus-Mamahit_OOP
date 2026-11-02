package com.gerald.frontend.commands;

import com.gerald.frontend.objects.GameObject;
import com.gerald.frontend.objects.Player;
import com.gerald.frontend.systems.BulletManager;

import java.util.List;

public class BombCommand implements com.gerald.frontend.commands.Command {
    private List<GameObject> entities;

    public BombCommand() {}

    public BombCommand(List<GameObject> entities) {
        this.entities = entities;
    }

    public void setEntities(List<GameObject> entities) {
        this.entities = entities;
    }

    @Override
    public void execute(Player player, BulletManager bulletManager) {
        if (player != null) {
            player.useBomb(bulletManager, entities);
        }
    }
}
