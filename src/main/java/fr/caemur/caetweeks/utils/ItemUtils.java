package fr.caemur.caetweeks.utils;

import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.item.ToolMaterials;

public class ItemUtils {
    public static boolean shouldProtectTool(ItemStack item) {
        if (item == null) return false;

        if (item.getItem() instanceof ToolItem) {
            ToolItem tool = (ToolItem) item.getItem();

            // durability
            if (item.getMaxDamage() - item.getDamage() > 2)
                return false;

            // any diamond or netherite
            if (tool.getMaterial() == ToolMaterials.DIAMOND || tool.getMaterial() == ToolMaterials.NETHERITE)
                return true;

            // enchanted iron
            return tool.getMaterial() == ToolMaterials.IRON && item.hasEnchantments();
        }

        return false;
    }
}
