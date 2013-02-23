package me.KeybordPiano459.RemoteJukebox;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;

public class JukeboxFile implements Listener {
    RemoteJukebox plugin;
    public JukeboxFile(RemoteJukebox plugin) {
        this.plugin = plugin;
        folder = plugin.folder;
    }
    
    private File jukeboxFile;
    private FileConfiguration jukeboxConfig;
    private File folder;
    
    public void createJukeboxFile() {
        if (jukeboxFile == null) {
            jukeboxFile = new File(folder, "jukebox.yml");
            try {
                jukeboxFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    public void reloadJukebox() {
        if (!jukeboxFile.exists()) jukeboxFile = new File(folder, "jukebox.yml");
        jukeboxConfig = YamlConfiguration.loadConfiguration(jukeboxFile);
        
        InputStream defConfigStream = plugin.getResource("jukebox.yml");
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            jukeboxConfig.setDefaults(defConfig);
        }
    }
    
    public FileConfiguration getJukebox() {
        if (jukeboxConfig == null) reloadJukebox();
        return jukeboxConfig;
    }
    
    public void saveJukebox() {
        if (jukeboxConfig == null || jukeboxFile == null) {
            return;
        }
        
        try {
            jukeboxConfig.save(jukeboxFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Could not save the stats file to the disk!");
        }
    }
}