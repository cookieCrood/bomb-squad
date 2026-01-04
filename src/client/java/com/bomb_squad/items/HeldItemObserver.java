package com.bomb_squad.items;

import com.bomb_squad.chat.Chat;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

public class HeldItemObserver {

    private static final Map<UUID, ItemStack> previousMainHand = new HashMap<>();

    private static final Pattern ITEM_MODEL_ID_PATTERN = Pattern.compile("^[a-z0-9/:.+_-]+$");

    public static void register() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.level == null) return;

            for (Player player : client.level.players()) {
                UUID id = player.getUUID();
                ItemStack main = player.getMainHandItem();
                ItemStack prevMain = previousMainHand.get(id);

                if (!(prevMain == null)) {
                    if (stacksAreIdentical(main, prevMain)) {
                        continue;
                    }
                }

                previousMainHand.put(id, main.copy());
                onMainHandChanged(player, main);

            }
        });
    }

    private static void onMainHandChanged(Player player, ItemStack stack) {
        try {
            Identifier itemModel = stack.get(DataComponents.ITEM_MODEL);
            if (itemModel == null) return;

            String itemModelString = itemModel.toString();

            if (!isValidItemModelID(itemModelString) || itemModelString.equalsIgnoreCase("minecraft:null")) {
                Chat.send("§c§lATTENTION! §eThe player §6" + player.getName().getString() + " §eis holding a §cmalformed §eitem!");
            }
        } catch (Exception e) {
            Chat.send(e.getMessage());
        }
    }

    public static boolean stacksAreIdentical(ItemStack a, ItemStack b) {
        if (a == null || b == null) return false;
        if (!a.is(b.getItem())) return false;
        if (a.getCount() != b.getCount()) return false;
        if (!ItemStack.matches(a, b)) return false;

        return true;
    }

    public static boolean isValidItemModelID(String itemModelID) {
        return ITEM_MODEL_ID_PATTERN.matcher(itemModelID).matches();
    }

}

