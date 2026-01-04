package com.bomb_squad.chat;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;


public class Chat {
    public static void send(String message) {
        Player p = Minecraft.getInstance().player;
        if (p != null) {
            p.displayClientMessage(Component.literal("§8[BS] §r" + message),
                    false
            );
        }
    }
}
