package com.bx.magicSmp.patch;

import me.xyr0.astralsmpcore.systems.sell.SellMenus;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;

/** Protects MagicSMP /sell category/multiplier display slots from extraction. */
public final class SellGuiProtectionListener implements Listener {
    private boolean isSellGui(Inventory top) {
        if (top == null || top.getSize() < 54) return false;
        // Current MagicSMP sell GUI exposes category/display slots through SellMenus.
        for (int slot = 45; slot <= 53 && slot < top.getSize(); slot++) {
            if (SellMenus.isCategorySlot(slot)) return true;
        }
        return false;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
    public void onClick(InventoryClickEvent event) {
        Inventory top = event.getView().getTopInventory();
        if (!isSellGui(top)) return;

        int raw = event.getRawSlot();
        // Never allow direct interaction with display/category/multiplier row.
        if (raw >= 45 && raw <= 53) {
            event.setCancelled(true);
            return;
        }

        // Prevent collection/swap actions from pulling GUI display items indirectly.
        switch (event.getAction()) {
            case COLLECT_TO_CURSOR, HOTBAR_SWAP, HOTBAR_MOVE_AND_READD -> event.setCancelled(true);
            default -> { }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
    public void onDrag(InventoryDragEvent event) {
        Inventory top = event.getView().getTopInventory();
        if (!isSellGui(top)) return;
        for (int raw : event.getRawSlots()) {
            if (raw >= 45 && raw <= 53) {
                event.setCancelled(true);
                return;
            }
        }
    }
}
