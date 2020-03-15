package org.brithgtex.plugin.highlight;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.MagmaCube;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Highlight extends JavaPlugin {

    public List<String> Blocked_player = new ArrayList<>();
    public final HighlightListener Listener = new HighlightListener(this);
    public final HighlightFile FileSaver = new HighlightFile(this);
    public Configuration config;

    @Override
    public void onEnable() {
        this.getCommand("highlight").setExecutor(new HighlightCommandExecutor(this));
        getServer().getPluginManager().registerEvents(this.Listener, this);
        saveDefaultConfig();
        this.config = this.getConfig();

        try {
            File player_file = new File(this.getDataFolder() + "_blocked_player.yml");
            if (player_file.createNewFile()) {
                getLogger().info("New file created :" + player_file.getName());
                YamlConfiguration config = YamlConfiguration.loadConfiguration(player_file);
                config.set("Player_List", Blocked_player);
                config.save(player_file);
            } else {
                YamlConfiguration config = YamlConfiguration.loadConfiguration(player_file);
                this.Blocked_player = (List<String>) config.getList("Player_List");
            }
        } catch (IOException e) {
            getLogger().info("An error occured while getting Highlight plugin players file");
        }
    }

    @Override
    public void onDisable() {
    }

    public void DespawnAfterTime(MagmaCube entity, long time){
    BukkitScheduler scheduler = getServer().getScheduler();
    scheduler.scheduleSyncDelayedTask(this, new Runnable() {
        @Override
        public void run() {
            entity.remove();
        }
    }, time);
    }

    public void Entity_Highlighter(MagmaCube entity) {
        PotionEffect invisibility = new PotionEffect(PotionEffectType.INVISIBILITY, 12000, 1, false, false);
        entity.setGlowing(true);
        entity.setInvulnerable(true);
        entity.setAI(false);
        entity.setCollidable(false);
        entity.setSize(1);
        entity.addPotionEffect(invisibility);
    }

    public void MarkChest(Material material, Player player) {
        FileConfiguration config = this.getConfig();
        World world = player.getWorld();
        Location location = player.getLocation().toCenterLocation();
        double x0 = location.getX();
        double y0 = location.getY();
        double z0 = location.getZ();
        Class<? extends Entity> MagmacubeClass = MagmaCube.class;
        for (double r = 1; r < config.getInt("research_radius"); r++) {
            double step = Math.PI / (6 * (r));
            for (double delta0 = -Math.PI / 2; delta0 <= Math.PI / 2; delta0 = delta0 + Math.PI) {
                double y_delta0 = r * Math.sin(delta0) + y0;
                Block block_delta0 = world.getBlockAt((int) x0, (int) y_delta0, (int) z0);
                if (block_delta0.getState() instanceof InventoryHolder) {
                    if (block_delta0.getLocation().getNearbyEntities(0.1, 0.1, 0.1).isEmpty()) {
                        if (((InventoryHolder) block_delta0.getState()).getInventory().contains(material)) {
                            MagmaCube entity = (MagmaCube) world.spawnEntity(block_delta0.getLocation().toCenterLocation().subtract(0, 0.3, 0), EntityType.MAGMA_CUBE);
                            Entity_Highlighter(entity);
                            DespawnAfterTime(entity, 200L);
                        }
                    }
                }
            }
            for (double theta = -Math.PI; theta <= Math.PI; theta = theta + step) {
                for (double delta = -Math.PI / 2. + step; delta <= Math.PI / 2. - step; delta = delta + step) {
                    double z = Math.round(r * Math.cos(delta) * Math.cos(theta) + z0);
                    double x = Math.round(r * Math.cos(delta) * Math.sin(theta) + x0);
                    double y = Math.round(r * Math.sin(delta) + y0);
                    Block block = world.getBlockAt((int) x, (int) y, (int) z);
                    if (block.getState() instanceof InventoryHolder) {
                        if (block.getLocation().getNearbyEntitiesByType(MagmacubeClass ,1 ).isEmpty()) {
                            if (((InventoryHolder) block.getState()).getInventory().contains(material)) {
                                MagmaCube entity = (MagmaCube) world.spawnEntity(block.getLocation().toCenterLocation().subtract(0, 0.3, 0), EntityType.MAGMA_CUBE);
                                Entity_Highlighter(entity);
                                DespawnAfterTime(entity, config.getLong("glow_effect_duration"));
                            }
                        }
                    }

                }
            }
        }
    }
}