package org.brithgtex.plugin.highlight;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class HighlightFile {
    private final Highlight plugin;

    public HighlightFile(Highlight plugin){ this.plugin = plugin; }

    public boolean Save_Player(Player player) {
        String UUID = player.getUniqueId().toString();
        if (!this.plugin.Blocked_player.contains(UUID)) {
            this.plugin.Blocked_player.add(UUID);
            try {
                File player_file = new File(this.plugin.getDataFolder() + "_blocked_player.yml");
                YamlConfiguration config = YamlConfiguration.loadConfiguration(player_file);
                if (!config.getList("Player_List").contains(UUID)) {
                    config.set("Player_List", this.plugin.Blocked_player);
                    config.save(player_file);
                    return true;
                } else {
                    this.plugin.getLogger().info("Highlight plugin tried to save a player already present in the Blocked Player file");
                    return false;
                }
            } catch (IOException e) {
                this.plugin.getLogger().info("An error occured while saving player in Highlight file");
                return false;
            }
        } else {
            player.sendMessage("You're already on highlight mode");
            return false;
        }
    }

    public boolean Remove_Player(Player player) {
        String UUID = player.getUniqueId().toString();
        if (this.plugin.Blocked_player.contains(UUID)) {
            this.plugin.Blocked_player.remove(UUID);
            try {
                File player_file = new File(this.plugin.getDataFolder() + "_blocked_player.yml");
                YamlConfiguration config = YamlConfiguration.loadConfiguration(player_file);
                if (config.getList("Player_List").contains(UUID)) {
                    config.set("Player_List", this.plugin.Blocked_player);
                    config.save(player_file);
                    return true;
                } else {
                    this.plugin.getLogger().info("Highlight plugin tried to remove a player not present in the Blocked Player file");
                    return false;
                }
            } catch (IOException e) {
                this.plugin.getLogger().info("An error occured while saving player in Highlight file");
                return false;
            }
        } else {
            this.plugin.getLogger().info("Highlight plugin tried to remove a player not present in the Blocked Player list");
            return false;
        }
    }

    public boolean CheckHighlightMode(Player player) {
        if (this.plugin.Blocked_player.contains(player.getUniqueId().toString())) {
            return true;
        } else {
            return false;
        }
    }
}
