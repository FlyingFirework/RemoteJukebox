package me.KeybordPiano459.RemoteJukebox;

import java.util.logging.Logger;
import org.bukkit.ChatColor;
import org.bukkit.block.Jukebox;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CommandJukebox implements CommandExecutor {
    RemoteJukebox plugin;
    public CommandJukebox(RemoteJukebox plugin) {
        this.plugin = plugin;
        prefix = plugin.prefix;
    }
    
    private String prefix;
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (cmd.getName().equalsIgnoreCase("jukebox")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (args.length == 0) {
                    player.performCommand("jukebox help");
                } else if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("eject")) {
                        if (plugin.jukebox.get(player) == null) player.sendMessage(prefix + "You haven't set your jukebox yet! Type /jukebox [set]");
                        else {
                            Jukebox jukebox = (Jukebox) plugin.jukebox.get(player).getState();
                            jukebox.eject();
                            player.sendMessage(prefix + "Your jukebox has been ejected.");
                        }
                    } else if (args[0].equalsIgnoreCase("help")) {
                        player.sendMessage(ChatColor.GOLD + "------- [" + ChatColor.GREEN + "RemoteJukebox" + ChatColor.GOLD + "] -------");
                        player.sendMessage("- " + ChatColor.GREEN + "/jukebox [eject] - Ejects the current disc");
                        player.sendMessage("- " + ChatColor.GREEN + "/jukebox [help] - Display this help screen");
                        player.sendMessage("- " + ChatColor.GREEN + "/jukebox [play] - Play the item in your hand");
                        player.sendMessage("- " + ChatColor.GREEN + "/jukebox [set] - Set the jukebox you control");
                    } else if (args[0].equalsIgnoreCase("play")) {
                        if (plugin.jukebox.get(player) == null) player.sendMessage(prefix + "You haven't set your jukebox yet! Type /jukebox [set]");
                        else {
                            Jukebox jukebox = (Jukebox) plugin.jukebox.get(player).getState();
                            ItemStack disc = player.getItemInHand();
                            if (disc.getTypeId() >= 2256 && disc.getTypeId() <= 2267) {
                                jukebox.eject();
                                player.getInventory().setItemInHand(null);
                                jukebox.setPlaying(disc.getType());
                                player.sendMessage(prefix + "Your jukebox is now playing the " + plugin.discToName(disc.getTypeId()) + " disc");
                            } else player.sendMessage(prefix + "You must be holding a music disc!");
                        }
                    } else if (args[0].equalsIgnoreCase("set")) {
                        player.sendMessage(prefix + "Left-click the jukebox you want to set as yours");
                        plugin.set.add(player);
                    } else {
                        player.sendMessage(prefix + "Incorrect usage! Type /jukebox for help");
                    }
                }
            } else {
                Logger.getLogger("Minecraft").info("You can't use this command from console!");
            }
        }
        return false;
    }
}