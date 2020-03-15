package org.brithgtex.plugin.highlight;

import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HighlightCommandExecutor implements CommandExecutor {
    private final Highlight plugin;

    public HighlightCommandExecutor(Highlight plugin){ this.plugin = plugin; }

    @Override
    public boolean onCommand (CommandSender sender, Command cmd, String label, String[] args){
        if(cmd.getName().equalsIgnoreCase("highlight")) {
            if (sender instanceof Player) {
                if (plugin.FileSaver.Save_Player(((Player) sender))){
                    ((Player) sender).setGameMode(GameMode.CREATIVE);
                }
            }
        }
        return false;
    }
}
