package me.justmicha.elementalapi.item;

import me.justmicha.elementalapi.utils.Common;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ItemBuilder {

    private ItemStack is;
    private ItemMeta im;

    public ItemBuilder(String displayName, Material material, int amount) {
        this.is = new ItemStack(material, amount);
        this.im = is.getItemMeta();
        this.im.setDisplayName(Common.colorize(displayName));
    }

    public ItemBuilder(ItemStack itemStack) {
        this.is = itemStack;
        this.im = itemStack.getItemMeta();
    }

    public ItemBuilder setDisplayName(String displayName) {
        this.im.setDisplayName(Common.colorize(displayName));
        return this;
    }

    public ItemBuilder setAmount(int amount) {
        this.is.setAmount(amount);
        return this;
    }

    public ItemBuilder setMaterial(Material material) {
        this.is.setType(material);
        return this;
    }

    public ItemBuilder setSkullOwner(UUID uuid) {
        if (uuid != null && this.is.getType() == Material.PLAYER_HEAD) {
            SkullMeta sm = (SkullMeta) this.im;
            sm.setOwningPlayer(Bukkit.getOfflinePlayer(uuid));
            this.im = sm;
        } else {
            Common.warn( "[" + Common.getPlugin().getName() + "]" + "Improper usage of #setSkullOwner()! The item is not a player head.");
        }

        return this;
    }

    public ItemBuilder setLore(String... lores) {
        if (lores == null)
            return this;

        List<String> finalLore = new ArrayList<>();

        for (String lore : lores)
            finalLore.add(Common.colorize(lore));

        this.im.setLore(finalLore);
        return this;
    }

    public ItemBuilder addEnchantment(Enchantment enchantment, int level) {
        this.is.addUnsafeEnchantment(enchantment, level);
        return this;
    }

    public ItemBuilder addItemFlags(ItemFlag... flags) {
        this.im.addItemFlags(flags);
        return this;
    }

    public ItemBuilder unbreakable() {
        this.im.setUnbreakable(true);
        return this;
    }

    public ItemBuilder glow() {
        if (!is.getType().equals(Material.TRIDENT))
            return addEnchantment(Enchantment.CHANNELING, 1).addItemFlags(ItemFlag.HIDE_ENCHANTS);
        else
            return addEnchantment(Enchantment.PROTECTION_EXPLOSIONS, 1).addItemFlags(ItemFlag.HIDE_ENCHANTS);
    }

    public ItemStack build() {
        this.is.setItemMeta(im);
        return is;
    }
}
