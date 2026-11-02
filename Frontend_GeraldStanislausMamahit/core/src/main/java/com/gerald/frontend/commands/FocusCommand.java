package com.gerald.frontend.commands;

import com.gerald.frontend.objects.Player;
import com.gerald.frontend.systems.BulletManager;

public class FocusCommand implements com.gerald.frontend.commands.Command {
    private boolean focused;

    public FocusCommand(boolean focused) {
        this.focused = focused;
    }

    @Override
    public void execute(Player player, BulletManager bulletManager) {
        player.setFocused(focused);
    }
}
