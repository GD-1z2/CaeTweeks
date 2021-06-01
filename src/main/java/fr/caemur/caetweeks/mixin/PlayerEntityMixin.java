package fr.caemur.caetweeks.mixin;

import fr.caemur.caetweeks.CaeTweeks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.item.ToolMaterials;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {

    @Inject(at = @At("HEAD"), method = "isBlockBreakingRestricted", cancellable = true)
    public void isBlockBreakingRestricted(World world, BlockPos pos, GameMode gameMode, CallbackInfoReturnable<Boolean> info) {
        final ItemStack item = MinecraftClient.getInstance().player.getMainHandStack();

        if (world.isClient && CaeTweeks.getConfig().isAntiBreakEnabled() && shouldProtectItem(item)) {
            MinecraftClient.getInstance().player.sendMessage(new TranslatableText("message.caetweeks.brokentool",
                    item.getMaxDamage() - item.getDamage()), true);
            info.setReturnValue(true);
        }
    }

    private boolean shouldProtectItem(ItemStack item) {
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
