package fr.caemur.caetweeks.mixin;

import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatScreen.class)
public class ChatScreenMixin extends Screen {
    private static final Identifier ARROW_BUTTONS_TEXTURE = new Identifier("textures/gui/server_selection.png");
    private static final Identifier CLEAR_BUTTON_TEXTURE = new Identifier("textures/item/barrier.png");

    private ButtonWidget scrollUpButton, scrollDownButton, clearButton;

    protected ChatScreenMixin(Text title) {
        super(title);
    }

    @Inject(at = @At("HEAD"), method = "init")
    private void init(CallbackInfo info) {
        initButtons();
    }

    private void initButtons() {
        buttons.clear();

        final double lineHeight = 9 * (client.options.chatLineSpacing + 1)
                * client.inGameHud.getChatHud().getChatScale();
        final int visibleMessages = ((ChatHudAccessor) client.inGameHud.getChatHud()).getVisibleMessages().size();

        final int buttonX = client.inGameHud.getChatHud().getWidth() + 7;

        int buttonY = height - 40 - (int) (lineHeight * Math.min(
                client.inGameHud.getChatHud().getVisibleLineCount(), visibleMessages));
        buttonY = Math.min(buttonY, height - 78);

        scrollUpButton = new ButtonWidget(buttonX, buttonY, 20, 20, Text.of(""),
                button -> {
                    client.inGameHud.getChatHud().scroll(visibleMessages);
                }
        );

        scrollDownButton = new ButtonWidget(buttonX, buttonY + 21, 20, 20, Text.of(""),
                button -> {
                    client.inGameHud.getChatHud().scroll(-visibleMessages);
                }
        );

        clearButton = new ButtonWidget(buttonX, buttonY + 42, 20, 20, Text.of(""),
                button -> {
                    client.inGameHud.getChatHud().clear(false);
                    initButtons();
                }
        );

        addButton(scrollUpButton);
        addButton(scrollDownButton);
        addButton(clearButton);
    }

    @Inject(at = @At("RETURN"), method = "render")
    private void render(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo info) {
        client.getTextureManager().bindTexture(ARROW_BUTTONS_TEXTURE);
        drawTexture(matrices, scrollUpButton.x + 5, scrollUpButton.y + 6, 99, (scrollUpButton.isHovered() ? 37 : 5), 11, 7);
        drawTexture(matrices, scrollDownButton.x + 5, scrollDownButton.y + 7, 67, (scrollDownButton.isHovered() ? 52 : 20), 11, 7);

        client.getTextureManager().bindTexture(CLEAR_BUTTON_TEXTURE);
        drawTexture(matrices, clearButton.x + 2, clearButton.y + 2, 0, 0, 16, 16, 16, 16);
    }
}
