package fr.caemur.caetweeks.mixin;

import fr.caemur.caetweeks.ItemFrameHelper;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.ItemFrameEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemFrameEntityRenderer.class)
public abstract class ItemFrameMixin extends EntityRenderer<Entity> {
    protected ItemFrameMixin(EntityRenderDispatcher dispatcher) {
        super(dispatcher);
    }

    @Inject(at = @At("HEAD"), method = "render")
    private void render(ItemFrameEntity itemFrameEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo info) {
        if (ItemFrameHelper.isEnabled()) {
            final int rotation = itemFrameEntity.getRotation();
            final Text text = Text.of((rotation == 0 ? Formatting.GREEN : Formatting.GOLD) + String.valueOf(rotation == 0 ? 0 : 8 - rotation));
            renderLabelIfPresent(itemFrameEntity, text, matrixStack, vertexConsumerProvider, i);
        }
    }

    /**
     * @author Caemur
     */
    @Overwrite
    public void renderLabelIfPresent(ItemFrameEntity itemFrameEntity, Text text, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        super.renderLabelIfPresent(itemFrameEntity, text, matrixStack, vertexConsumerProvider, i);
    }
}
