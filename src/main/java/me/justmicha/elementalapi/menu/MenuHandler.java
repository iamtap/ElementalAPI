package me.justmicha.elementalapi.menu;

import me.justmicha.elementalapi.utils.Common;
import me.justmicha.elementalapi.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerLoadEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class MenuHandler implements Listener {

    private Map<Player, Menu> openInventoryMap;
    private Map<Player, Menu> prevMenuMap;

    @EventHandler
    public void onServerLoad(ServerLoadEvent e) {
        if (e.getType().equals(ServerLoadEvent.LoadType.STARTUP))
            if (openInventoryMap == null)
                openInventoryMap = new HashMap<>();
            if (prevMenuMap == null)
                prevMenuMap = new HashMap<>();
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        Player player = (Player) event.getPlayer();
        InventoryHolder holder = event.getInventory().getHolder();

        if (holder instanceof Menu) {
            Menu menu = (Menu) holder;
            Menu prevMenu = getPrevMenu(player);

            menu.onMenuOpen();

            if (prevMenu != null) {
                menu.createButton("prevMenuBtn", menu.getSlots() - 9, new ItemBuilder("&cGo Back", Material.BARRIER, 1).build());
            }

            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1F, 1F);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = ((Player) event.getPlayer());
        Inventory inventory = event.getInventory();
        InventoryHolder holder = inventory.getHolder();

        if (holder instanceof Menu) {
            prevMenuMap.put(player, ((Menu) holder));
        }


    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory inventory = event.getClickedInventory();
        InventoryHolder holder = inventory.getHolder();
        Player player = (Player) event.getWhoClicked();
        ItemStack itemStack = event.getCurrentItem();

        if (holder instanceof Menu) {
            Menu menu = (Menu) holder;
            ItemStack item = event.getCurrentItem();

            if (item == null)
                return;

            menu.handleClick(Common.retrieveValue(item, "buttonId"), player, item, event);
            player.playSound(player.getLocation(), Sound.BLOCK_DISPENSER_DISPENSE, 1F, 1F);

            if (itemStack != null && Common.hasKey(itemStack, "buttonId")) {
                String buttonId = Common.retrieveValue(itemStack, "buttonId");

                if (buttonId.equals("prevMenuBtn"))
                    player.openInventory(getPrevMenu(player).getInventory());
            }

            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        openInventoryMap.remove(event.getPlayer());
    }

    private Menu getPrevMenu(Player player) {
        if (prevMenuMap.containsKey(player))
            prevMenuMap.get(player);
        return null;
    }
}
