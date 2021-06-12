package fr.caemur.caetweeks.mixin;

import com.mojang.blaze3d.platform.GlStateManager;
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
    private static float animationProgress = 0;
    private static long animationStart = 0;

    @Inject(at = @At("HEAD"), method = "applyFog", cancellable = true)
    private static void applyFog(Camera camera, BackgroundRenderer.FogType fogType, float viewDistance, boolean thickFog, CallbackInfo info) throws Exception {
        FluidState fluidState = camera.getSubmergedFluidState();
        if ((CaeTweeks.getConfig().isClearWaterEnabled() && fluidState.isIn(FluidTags.WATER))
                || (CaeTweeks.getConfig().isClearLavaEnabled() && fluidState.isIn(FluidTags.LAVA))) {

            if ((fluidState.isIn(FluidTags.WATER) && !wasWaterEnabled)
                    || (fluidState.isIn(FluidTags.LAVA) && !wasLavaEnabled)) {
                playingAnimation = true;
                animationProgress = 0;
                animationStart = System.currentTimeMillis();
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

                    fogDensity = s * (1 - animationProgress);
                }

                RenderSystem.fogDensity(fogDensity);
                RenderSystem.fogMode(GlStateManager.FogMode.EXP2);
            } else { // Lava
                float fogEnd = (float) (Math.sqrt(Math.pow(viewDistance, 2) * 2) + 20);

                if (playingAnimation)
                    fogEnd *= animationProgress;

                RenderSystem.fogStart(0.0f);
                RenderSystem.fogEnd(fogEnd);
                RenderSystem.fogMode(GlStateManager.FogMode.LINEAR);
                RenderSystem.setupNvFogDistance();
            }

            if (playingAnimation) {
                animationProgress = (System.currentTimeMillis() - animationStart) / 1000f;
                if (animationProgress > 1) {
                    playingAnimation = false;
                    animationProgress = 0;
                }
            }

            info.cancel();
        }

        wasWaterEnabled = CaeTweeks.getConfig().isClearWaterEnabled();
        wasLavaEnabled = CaeTweeks.getConfig().isClearLavaEnabled();
    }
}
