package taewookim.bag;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public final class Bag extends JavaPlugin {

    public static NamespacedKey InventoryNamed;

    @Override
    public void onEnable() {
        Bukkit.getPluginCommand("bag").setExecutor(new Command());
        InventoryNamed = new NamespacedKey(this, "Inventory");
    }

    @Override
    public void onDisable() {

    }
}
