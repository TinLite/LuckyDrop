package vn.teamgamervn;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class Events implements Listener {
    @EventHandler
    public void onBreak(BlockBreakEvent event)
    {
        Player player = event.getPlayer();
        Material block = event.getBlock().getType();
        if (!Storage.contains(block)) return;
        if (Math.random() < (Storage.getPercentage(block) / 100)) {
            ItemStack item = Storage.item.clone();
            int random = ThreadLocalRandom.current().nextInt(9999);
            item.getItemMeta().setDisplayName(ChatColor.translateAlternateColorCodes('&', Storage.config.getString("Head.Name").replaceAll("%id%", String.valueOf(random))));
            event.getPlayer().getInventory().addItem();
            if (Storage.config.getBoolean("Broadcast"))
                Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', Storage.config.getString("BroadcastMsg").replaceAll("%player%", event.getPlayer().getName()).replaceAll("%block%", block.name())));
            if (Storage.config.getBoolean("PlayGlobalSound")) {
                for (Player p : Bukkit.getOnlinePlayers())
                    if (!(player.getName().equals(p.getName())))
                        p.playSound(player.getLocation(), Sound.valueOf(Storage.config.getString("GlobalSound")), 1, 1);
            }
            if (Storage.config.getBoolean("PlaySingleSound"))
                player.playSound(player.getLocation(), Sound.valueOf(Storage.config.getString("SingleSound")), 1, 1);
            if (Storage.config.getBoolean("Block." + block.name() + ".RemoveBaseItem")) {
                event.getBlock().setType(Material.AIR);
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onClick(PlayerInteractEvent e) {
        if (e.getItem() == null) return;
        if (e.getItem().getItemMeta().getLore().equals(Storage.item.getItemMeta().getLore())
                && e.getItem().getType().equals(Storage.item.getType())) {
            // Lấy danh sách những thứ cần Random
            Set<String> set = Storage.config.getConfigurationSection("Commands").getKeys(false);
            int index = ThreadLocalRandom.current().nextInt(set.size()); // Random thread, đề phòng nhiều người dùng 1 lúc
            int i = 0;
            String rnd = null;
            for (String s : set) {
                if (index == i) {
                    rnd = s;
                    break;
                }
                i++;
            }
            if (rnd == null) {
                e.getPlayer().sendMessage(ChatColor.RED + "Có lỗi đã xảy ra. Hãy liên hệ Admin để báo lỗi này.");
                e.getPlayer().sendMessage(ChatColor.RED + "Thông tin lỗi: PlayerInteract -> rnd -> Null.");
                return;
            }

            // Gỡ 1 Item
            ItemStack hand = e.getPlayer().getInventory().getItemInHand();
            if (hand.getAmount() == 1) e.getPlayer().getInventory().setItemInHand(new ItemStack(Material.AIR));
            else {
                hand.setAmount(hand.getAmount() - 1);
                e.getPlayer().getInventory().setItemInHand(hand);
            }

            // Kiểm tra xem cái list chó má kia có Empty ko
            if (!Storage.config.getStringList("Commands." + rnd + ".commands").isEmpty()) {
                // Thực hiện lệnh đã random
                for (String s : Storage.config.getStringList("Commands." + rnd + ".commands"))
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), s.replaceAll("%player%", e.getPlayer().getName()));
            }
            // Tin nhắn người chơi sử dụng
            if (!Storage.config.getString("Commands." + rnd + ".message").isEmpty())
                e.getPlayer().sendMessage(
                        ChatColor.translateAlternateColorCodes(
                                '&',
                                Storage.config.getString("Commands." + rnd + ".message")
                                        .replaceAll("%player%", e.getPlayer().getName())));
            // Broadcast toàn thế giới
            if (!Storage.config.getString("Commands." + rnd + ".broadcast").isEmpty())
                Bukkit.broadcastMessage(
                        ChatColor.translateAlternateColorCodes(
                                '&',
                                Storage.config.getString("Commands." + rnd + ".broadcast")
                                        .replaceAll("%player%", e.getPlayer().getName())));
            // Play sound.
            if (!Storage.config.getString("Commands." + rnd + ".sound").isEmpty())
                for (Player p : Bukkit.getOnlinePlayers())
                    p.playSound(p.getLocation(), Sound.valueOf(Storage.config.getString("Commands." + rnd + ".sound")), 1, 1);
            e.setCancelled(true);
        }
    }
}
