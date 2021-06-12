package fr.caemur.caetweeks.mixin;

import fr.caemur.caetweeks.utils.ItemUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.TranslatableText;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerInteractionManager.class)
public class ClientPlayerInteractionManagerMixin {

    @Shadow
    @Final
    private MinecraftClient client;

    @Inject(at = @At("HEAD"), method = "attackBlock", cancellable = true)
    private void attackBlock(CallbackInfoReturnable<Boolean> info) {
        if (client.player != null) {
            final ItemStack item = client.player.getMainHandStack();

            if (ItemUtils.shouldProtectTool(item)) {
                client.player.sendMessage(new TranslatableText("message.caetweeks.brokentool",
                        item.getMaxDamage() - item.getDamage()), true);

                info.setReturnValue(false);
                info.cancel();
            }
        }
    }

    @Inject(at = @At("HEAD"), method = "attackEntity", cancellable = true)
    private void attackEntity(PlayerEntity player, Entity target, CallbackInfo info) {
        final ItemStack item = player.getMainHandStack();

        if (ItemUtils.shouldProtectTool(item)) {
            player.sendMessage(new TranslatableText("message.caetweeks.brokentool",
                    item.getMaxDamage() - item.getDamage()), true);

            info.cancel();
        }
    }
}
