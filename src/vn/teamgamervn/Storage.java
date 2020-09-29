package vn.teamgamervn;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Storage {
    static Plugin plugin;
    static FileConfiguration config;
    static ItemStack item;
    static List<String> ilore;

    public static void createItemStack()
    {
        item = getCustomTextureHead(config.getString("Head.Value"));
    }

    public static void setup(Plugin p)
    {
        plugin = p;
        plugin.saveDefaultConfig();
        config = plugin.getConfig();
    }

    public static boolean contains(Material material)
    {
        return config.contains("Block." + material.name().toUpperCase());
    }

    public static double getPercentage(Material material)
    {
        return config.getDouble("Block." + material.name().toUpperCase() + ".Chance");
    }

    public static void reloadConfig()
    {
        plugin.reloadConfig();
        config = plugin.getConfig();
        createItemStack();
    }

    public static ItemStack getCustomTextureHead(String value) {
        ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), "");
        profile.getProperties().put("textures", new Property("textures", value));
        Field profileField;
        try {
            profileField = meta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(meta, profile);
        } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
        }
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&',config.getString("Head.Name")));
        List<String> lore = config.getStringList("Head.Lore");
        List<String> newlore = new ArrayList<>();
        for (String s : lore)
            newlore.add(ChatColor.translateAlternateColorCodes('&', s));
        ilore = newlore;
        meta.setLore(newlore);
        head.setItemMeta(meta);
        return head;
    }
}
