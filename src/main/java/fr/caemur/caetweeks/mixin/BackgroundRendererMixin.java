package fr.caemur.caetweeks.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import fr.caemur.caetweeks.CaeTweeks;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.FluidState;
import net.minecraft.tag.FluidTags;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BackgroundRenderer.class)
public class BackgroundRendererMixin {
    private static boolean playingAnimation = false, wasWaterEnabled = false, wasLavaEnabled = false;
    private static int animationTicks = 0;
    private static long lastUpdate = 0, currentTime = 0;

    @Inject(at = @At("TAIL"), method = "applyFog")
    private static void applyFog(Camera camera, BackgroundRenderer.FogType fogType, float viewDistance, boolean thickFog, CallbackInfo info) throws Exception {
        FluidState fluidState = camera.getSubmergedFluidState();
        if ((CaeTweeks.getConfig().isClearWaterEnabled() && fluidState.isIn(FluidTags.WATER))
                || (CaeTweeks.getConfig().isClearLavaEnabled() && fluidState.isIn(FluidTags.LAVA))) {

            if ((fluidState.isIn(FluidTags.WATER) && !wasWaterEnabled)
                    || (fluidState.isIn(FluidTags.LAVA) && !wasLavaEnabled)) {
                playingAnimation = true;
                animationTicks = 0;
            }

            if (fluidState.isIn(FluidTags.WATER)) {
                float fogDensity = 0f;

                if (playingAnimation) {
                    Entity entity = camera.getFocusedEntity();
                    float s = 0.05F;
                    if (entity instanceof ClientPlayerEntity) {
                        ClientPlayerEntity clientPlayerEntity = (ClientPlayerEntity) entity;
                        s -= clientPlayerEntity.getUnderwaterVisibility() * clientPlayerEntity.getUnderwaterVisibility() * 0.03F;
                        Biome biome = clientPlayerEntity.world.getBiome(clientPlayerEntity.getBlockPos());
                        if (biome.getCategory() == Biome.Category.SWAMP) {
                            s += 0.005F;
                        }
                    }

                    fogDensity = s * (1 - animationTicks / 20f);

                    currentTime = System.currentTimeMillis();
                }

                RenderSystem.fogDensity(fogDensity);
            } else { // Lava
                float fogEnd = -0.25f;

                if (playingAnimation) {
                    fogEnd = animationTicks * (viewDistance / 20f + 1);

                    currentTime = System.currentTimeMillis();
                }

                RenderSystem.fogStart(0.0f);
                RenderSystem.fogEnd(fogEnd);
            }

            if (currentTime > lastUpdate + 50) {
                animationTicks++;
                if (animationTicks == 20) {
                    playingAnimation = false;
                    animationTicks = 0;
                }

                lastUpdate = System.currentTimeMillis();
            }
        }

        wasWaterEnabled = CaeTweeks.getConfig().isClearWaterEnabled();
        wasLavaEnabled = CaeTweeks.getConfig().isClearLavaEnabled();
    }
}
