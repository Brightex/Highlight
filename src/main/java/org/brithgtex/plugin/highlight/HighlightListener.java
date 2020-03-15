package org.brithgtex.plugin.highlight;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.event.player.*;

public class HighlightListener implements Listener {

    private final Highlight plugin;

    public HighlightListener(Highlight plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (this.plugin.FileSaver.CheckHighlightMode(e.getPlayer())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerMovement(PlayerMoveEvent e) {
        if (this.plugin.FileSaver.CheckHighlightMode(e.getPlayer())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteractatEntity(PlayerInteractAtEntityEvent e) {
        if (this.plugin.FileSaver.CheckHighlightMode(e.getPlayer())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent e) {
        if (this.plugin.FileSaver.CheckHighlightMode(e.getPlayer())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerCreativeInventoryClick(InventoryCreativeEvent e) {
        if (e.getWhoClicked() instanceof Player) {
            Player player = (Player) e.getWhoClicked();
            if (this.plugin.FileSaver.CheckHighlightMode((Player) e.getWhoClicked())) {
                if (e.getClick().isCreativeAction()) {
                    if (e.getClickedInventory() != null) {
                        this.plugin.MarkChest(e.getCursor().getType(), player);
                        player.closeInventory();
                        if (this.plugin.FileSaver.Remove_Player(player)) {
                            player.setGameMode(GameMode.SURVIVAL);
                            e.setCancelled(true);
                            return;
                        }
                    } else {
                        e.setCancelled(true);
                    }
                }
            }
        }
    }
}

