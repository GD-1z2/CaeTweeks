package fr.caemur.caetweeks.gui;

import fr.caemur.caetweeks.KeyManager;
import fr.caemur.caetweeks.config.ModConfig;
import fr.caemur.caetweeks.utils.Constants;
import fr.caemur.caetweeks.utils.TextUtils;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.gui.screen.ConfirmChatLinkScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.options.ControlsOptionsScreen;
import net.minecraft.client.gui.screen.options.OptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.options.GameOptions;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Util;

import java.util.ArrayList;
import java.util.List;

public class ConfigScreen extends Screen {
    private float titleX, titleY, margin;
    private int startY, optionWidth, optionHeight, secColumnX;
    private final List<ConfigOption> configOptions;

    public ConfigScreen(Text title) {
        super(title);
        configOptions = new ArrayList<>();
    }

    @Override
    protected void init() {
        super.init();
        titleX = (width - (textRenderer.getWidth(title) * 2)) / 4f;
        titleY = height / 20f;
        margin = width / 10f;
        startY = (int) (titleY + (textRenderer.fontHeight * 2) + margin);
        optionWidth = (int) ((width - 3 * margin) / 2f);
        optionHeight = textRenderer.fontHeight * 2;
        secColumnX = (int) (2 * margin + optionWidth);

        configOptions.clear();

        configOptions.add(new ToggleConfigOption(
                (int) margin, startY, optionWidth, optionHeight,
                new TranslatableText("config.caetweeks.clearwater.name"),
                new TranslatableText("config.caetweeks.clearwater.description",
                        KeyBindingHelper.getBoundKeyOf(KeyManager.getKeyBinding("clearwater")).getLocalizedText()).getString().split("\\|"),
                "clearWater", client, ModConfig.DEFAULT.isClearWaterEnabled()
        ));
        configOptions.add(new ToggleConfigOption(
                secColumnX, startY, optionWidth, optionHeight,
                new TranslatableText("config.caetweeks.clearlava.name"),
                new TranslatableText("config.caetweeks.clearlava.description",
                        KeyBindingHelper.getBoundKeyOf(KeyManager.getKeyBinding("clearlava")).getLocalizedText()).getString().split("\\|"),
                "clearLava", client, ModConfig.DEFAULT.isClearLavaEnabled()
        ));
        configOptions.add(new ToggleConfigOption(
                (int) margin, startY + (2 * optionHeight), optionWidth, optionHeight,
                new TranslatableText("config.caetweeks.itemframehelper.name"),
                new TranslatableText("config.caetweeks.itemframehelper.description",
                        KeyBindingHelper.getBoundKeyOf(KeyManager.getKeyBinding("itemframehelper")).getLocalizedText()).getString().split("\\|"),
                "itemFrameHelper", client, ModConfig.DEFAULT.isItemFrameHelperEnabled()
        ));

        // buttons
        ButtonWidget githubButton = new ButtonWidget((int) margin, (int) (height - margin - 20), 80, 20, Text.of("Github"), button -> {
            this.client.openScreen(new ConfirmChatLinkScreen((bl) -> {
                if (bl) {
                    Util.getOperatingSystem().open(Constants.GITHUB);
                }
                this.client.openScreen(this);
            }, Constants.GITHUB, true));
        });

        ButtonWidget keysButton = new ButtonWidget((int) margin + 90, (int) (height - margin - 20), 120, 20, new TranslatableText("button.caetweeks.changekeys"), button -> {
            client.openScreen(new ControlsOptionsScreen(this, client.options));
        });

        ButtonWidget resetButton = new ButtonWidget((int) margin + 220, (int) (height - margin - 20), 140, 20, new TranslatableText("button.caetweeks.reset"), button -> {
            for (ConfigOption configOption : configOptions) {
                configOption.reset();
            }
        });

        buttons.add(githubButton);
        buttons.add(keysButton);
        buttons.add(resetButton);
        children.add(githubButton);
        children.add(keysButton);
        children.add(resetButton);
    }

    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
//        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
//        client.getTextureManager().bindTexture(TEXTURE);
//        int x = (width - 500) / 2;
//        int y = (height - 50) / 2;
//        drawTexture(matrices, x, y, 0, 0, 500, 500);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);

        drawBackground(matrices, 0, mouseX, mouseY);

        super.render(matrices, mouseX, mouseY, delta);

        // title
        matrices.push();
        matrices.scale(2, 2, 1);
        textRenderer.draw(matrices, title, titleX, titleY, 16777215);
        matrices.pop();

        String[] tooltip = null;
        for (ConfigOption configOption : configOptions) {
            configOption.render(matrices, mouseX, mouseY, delta);

            if (configOption.isHovered()) tooltip = configOption.description;
        }

        if (tooltip != null) renderTooltip(matrices, TextUtils.stringsToTexts(tooltip), mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        super.mouseClicked(mouseX, mouseY, button);

        for (ConfigOption configOption : configOptions) {
            if (configOption.isHovered()) configOption.onPress();
        }

        return false;
    }

    @Override
    public void tick() {
        for (ConfigOption configOption : configOptions) {
            configOption.tick();
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}