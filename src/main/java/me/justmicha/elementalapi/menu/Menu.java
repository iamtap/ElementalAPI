package me.justmicha.elementalapi.menu;

import me.justmicha.elementalapi.utils.Common;
import me.justmicha.elementalapi.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public abstract class Menu implements InventoryHolder {

    private Menu parent;
    protected Inventory inventory;

    public abstract String getTitle();

    public abstract int getSlots();

    public abstract void handleClick(String buttonId, Player player, ItemStack item, InventoryClickEvent event);

    public abstract void createItems();

    public void onMenuOpen() {
    }

    @Override
    public Inventory getInventory() {
        inventory = Bukkit.createInventory(this, getSlots(), Common.colorize(getTitle()));
        createItems();
        return inventory;
    }

    public void createButton(String buttonId, int slot, ItemStack itemStack) {
        itemStack = new ItemBuilder(itemStack).addItemFlags(ItemFlag.HIDE_ATTRIBUTES).build(); // Auto-Hide Attributes
        Common.assignKey(itemStack, "buttonId", buttonId);
        inventory.setItem(slot, itemStack);
    }

    public void displaySlots() {
        for (int slot = 0; slot < inventory.getContents().length; slot++) {
            if (inventory.getItem(slot) == null)
                inventory.setItem(slot, new ItemBuilder("&bSlot " + slot, Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1)
                        .addItemFlags(ItemFlag.HIDE_ATTRIBUTES).build());
        }
    }

    public void fillEmptySlots() {
        for (int slot = 0; slot < inventory.getContents().length; slot++) {
            if (inventory.getItem(slot) == null)
                inventory.setItem(slot, new ItemBuilder("&f", Material.BLACK_STAINED_GLASS_PANE, 1)
                        .addItemFlags(ItemFlag.HIDE_ATTRIBUTES).build());
        }
    }

    public void open(Player player) {
        player.openInventory(getInventory());
    }

    public void open(CommandSender sender) {
        if (sender instanceof Player)
            ((Player) sender).openInventory(getInventory());
        else
            sender.sendMessage(Common.colorize("&cYou must be a player to do this."));
    }

    public static Menu createMenu(String title, int slots, ItemStack[] items) {
        return new Menu() {
            @Override
            public String getTitle() {
                return title;
            }

            @Override
            public int getSlots() {
                return slots;
            }

            @Override
            public void handleClick(String buttonId, Player player, ItemStack item, InventoryClickEvent event) {
            }

            @Override
            public void createItems() {
                inventory.setContents(items);
            }
        };
    }

    public void setParent(Menu parent) {
        this.parent = parent;
    }

    public Menu getParent() {
        return parent;
    }
}
