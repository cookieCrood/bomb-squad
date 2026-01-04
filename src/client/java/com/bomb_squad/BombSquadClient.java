package com.bomb_squad;

import net.fabricmc.api.ClientModInitializer;

import com.bomb_squad.items.HeldItemObserver;

public class BombSquadClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		HeldItemObserver.register();
	}
}