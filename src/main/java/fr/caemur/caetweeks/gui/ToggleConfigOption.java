package fr.caemur.caetweeks.gui;

import fr.caemur.caetweeks.CaeTweeks;
import fr.caemur.caetweeks.config.ConfigFile;
import fr.caemur.caetweeks.utils.Constants;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ToggleConfigOption extends ConfigOption {
    private static final Identifier SWITCH_TEXTURE = new Identifier(Constants.MOD_ID, "textures/gui/switch.png");

    private final MinecraftClient client;

    private final String configField;
    private boolean value;
    private final boolean defaultValue;

    private int animationDirection = 0, animationTicks = 0;
    private long animationStart;

    public ToggleConfigOption(int x, int y, int width, int height, Text title, String description, String configField, MinecraftClient client, boolean defaultValue) {
        super(x, y, width, height, title, description);
        this.configField = configField;
        this.client = client;
        this.defaultValue = defaultValue;

        value = CaeTweeks.getConfig().getFieldB(configField);
    }

    @Override
    public void onPress() {
        super.onPress();

        value = !value;
        CaeTweeks.getConfig().setFieldB(configField, value);

        // reverse ticks if an animation is already playing
        if (animationDirection != 0) animationTicks = 8 - animationTicks;

        // 2 = to right, 1 = to left
        animationDirection = value ? 2 : 1;

        animationStart = System.currentTimeMillis();

        ConfigFile.save(CaeTweeks.getConfig());
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        super.render(matrices, mouseX, mouseY, delta);

        if (hovered) {
            fill(matrices, x, y, x + width, y + height, 0x0CFFFFFF);
        }

        renderSwitch(matrices, mouseX, mouseY);

        client.textRenderer.draw(matrices, title, x + 36 + client.textRenderer.fontHeight / 2, y + client.textRenderer.fontHeight / 2, 16777215);
    }

    private void renderSwitch(MatrixStack matrices, int mouseX, int mouseY) {
        client.getTextureManager().bindTexture(SWITCH_TEXTURE);
        drawTexture(matrices, x + 5, y + 5, 0, 0, 26, 8);

        // hover
        final int u = mouseX >= x && mouseY >= y && mouseX < x + 34 && mouseY < y + 16 ? 42 : 26;

        int pos = 1;
        if (animationDirection == 1) { // left
            pos += 18 * ((8 - animationTicks) / 8f);
        } else if (animationDirection == 2) { // right
            pos += 18 * (animationTicks / 8f);
        } else if (value) {
            pos = 19;
        }

        drawTexture(matrices, x + pos, y + 1, u, 0, 16, 16);

        if (animationDirection != 0) {
            animationTicks = (int) ((System.currentTimeMillis() - animationStart) / 12.5f);

            if (animationTicks >= 8) {
                animationDirection = 0;
                animationTicks = 0;
                playDownSound(MinecraftClient.getInstance().getSoundManager());
            }
        }
    }

    @Override
    public void reset() {
        if (value != defaultValue) onPress();
    }
}