package me.KeybordPiano459.RemoteJukebox;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class RemoteJukebox extends JavaPlugin {

    public HashMap<Player, Block> jukebox = new HashMap<>();
    public ArrayList<Player> set = new ArrayList<>();
    public String prefix = "[" + ChatColor.GOLD + "RemoteJukebox" + ChatColor.RESET + "] ";
    public File folder = new File("plugins" + File.separator + "RemoteJukebox");
    private JukeboxFile jukeboxfile;

    @Override
    public void onEnable() {
        getLogger().info("RemoteJukebox v1.0 has been enabled!");
        getServer().getPluginCommand("jukebox").setExecutor(new CommandJukebox(this));
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        jukeboxfile = new JukeboxFile(this);
        folder.mkdirs();
        jukeboxfile.createJukeboxFile();
        loadJukeboxes();
        
        try {
            BukkitMetrics metrics = new BukkitMetrics(this);
            metrics.start();
        } catch (IOException e) {
            // Could not send data :-(
        }
    }

    @Override
    public void onDisable() {
        getLogger().info("RemoteJukebox v1.0 has been disabled.");
        saveJukeboxes();
    }

    public FileConfiguration getJukeboxFile() {
        return jukeboxfile.getJukebox();
    }

    public String discToName(int id) {
        if (id == 2256) return "13";
        else if (id == 2257) return "cat";
        else if (id == 2258) return "blocks";
        else if (id == 2259) return "chirp";
        else if (id == 2260) return "far";
        else if (id == 2261) return "mall";
        else if (id == 2262) return "mellohi";
        else if (id == 2263) return "stal";
        else if (id == 2264) return "strad";
        else if (id == 2265) return "ward";
        else if (id == 2266) return "11";
        else if (id == 2267) return "wait";
        else return null;
    }
    
    public String discToName(Material mat) {
        int id = mat.getId();
        return discToName(id);
    }
    
    private void loadJukeboxes() {
        try {
            for (String key : getJukeboxFile().getConfigurationSection("jukebox.").getKeys(false))
                jukebox.put(getServer().getPlayer(key), stringToBlock(getJukeboxFile().getString("jukebox." + key)));
        } catch (NullPointerException e) {}
    }
    
    private void saveJukeboxes() {
        try {
            for (Entry<Player, Block> entry : jukebox.entrySet()) {
                Player player = entry.getKey();
                Block block = entry.getValue();
                getJukeboxFile().set("jukebox." + player.getName(), blockToString(block));
                jukeboxfile.saveJukebox();
            }
        } catch (NullPointerException e) {}
    }

    public String blockToString(Block block) {
        return block.getWorld().getName() + "_" + block.getX() + "_" + block.getY() + "_" + block.getZ();
    }

    public Block stringToBlock(String str) {
        String s[] = str.split("_");
        if (s.length < 4) return null;
        int x = Integer.parseInt(s[s.length - 3]);
        int y = Integer.parseInt(s[s.length - 2]);
        int z = Integer.parseInt(s[s.length - 1]);
        StringBuilder worldName = new StringBuilder(12);
        for (int i = 0; i < s.length - 3; i++) {
            if (i != 0) worldName.append('_');
            worldName.append(s[i]);
        }

        World world = Bukkit.getServer().getWorld(worldName.toString());
        if (world == null) return null;
        return world.getBlockAt(x, y, z);
    }
}