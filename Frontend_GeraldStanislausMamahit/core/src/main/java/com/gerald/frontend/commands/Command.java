package com.gerald.frontend.commands;

import com.gerald.frontend.objects.Player;
import com.gerald.frontend.systems.BulletManager;

public interface Command {
    void execute(Player player, BulletManager bulletManager);
}

