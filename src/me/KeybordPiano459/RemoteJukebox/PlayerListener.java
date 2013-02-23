package me.KeybordPiano459.RemoteJukebox;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Jukebox;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerListener implements Listener {
    RemoteJukebox plugin;
    public PlayerListener(RemoteJukebox plugin) {
        this.plugin = plugin;
        prefix = plugin.prefix;
    }
    
    private String prefix;
    
    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
            Block block = event.getClickedBlock();
            if (plugin.set.contains(player)) {
                if (block.getType() == Material.JUKEBOX) {
                    plugin.jukebox.put(player, block);
                    player.sendMessage(prefix + "You have set that jukebox as yours!");
                    plugin.set.remove(player);
                } else {
                    player.sendMessage(prefix + "That is not a jukebox! Type /jukebox [set] to try again.");
                    plugin.set.remove(player);
                }
            }
        }
    }
}