package fr.caemur.caetweeks.gui;

import fr.caemur.caetweeks.config.ModConfig;
import fr.caemur.caetweeks.utils.Constants;
import fr.caemur.caetweeks.utils.KeyManager;
import fr.caemur.caetweeks.utils.TextUtils;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.gui.screen.ConfirmChatLinkScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.options.ControlsOptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Util;

import java.util.ArrayList;
import java.util.List;

public class ConfigScreen extends Screen {
    private int titleX, titleY, startY, optionWidth, optionHeight, secColumnX, marginX, marginY;
    private final List<ConfigOption> configOptions;

    public ConfigScreen(Text title) {
        super(title);
        configOptions = new ArrayList<>();
    }

    @Override
    protected void init() {
        super.init();
        titleX = (int) ((width - (textRenderer.getWidth(title) * 2)) / 4f);
        titleY = (int) (height / 20f);
        marginX = (int) (width / 10f);
        marginY = (int) (height / 10f);
        startY = (int) (titleY + (textRenderer.fontHeight * 2) + marginX);
        optionWidth = (int) ((width - 3 * marginX) / 2f);
        optionHeight = textRenderer.fontHeight * 2;
        secColumnX = (int) (2 * marginX + optionWidth);

        configOptions.clear();

        configOptions.add(new ToggleConfigOption(
                marginX, startY, optionWidth, optionHeight,
                new TranslatableText("config.caetweeks.clearwater.name"),
                new TranslatableText("config.caetweeks.clearwater.description",
                        KeyBindingHelper.getBoundKeyOf(KeyManager.getKeyBinding("clearwater")).getLocalizedText()).getString(),
                "clearWater", client, ModConfig.getDefault().isClearWaterEnabled()
        ));
        configOptions.add(new ToggleConfigOption(
                secColumnX, startY, optionWidth, optionHeight,
                new TranslatableText("config.caetweeks.clearlava.name"),
                new TranslatableText("config.caetweeks.clearlava.description",
                        KeyBindingHelper.getBoundKeyOf(KeyManager.getKeyBinding("clearlava")).getLocalizedText()).getString(),
                "clearLava", client, ModConfig.getDefault().isClearLavaEnabled()
        ));
        configOptions.add(new ToggleConfigOption(
                marginX, startY + (2 * optionHeight), optionWidth, optionHeight,
                new TranslatableText("config.caetweeks.itemframehelper.name"),
                new TranslatableText("config.caetweeks.itemframehelper.description",
                        KeyBindingHelper.getBoundKeyOf(KeyManager.getKeyBinding("itemframehelper")).getLocalizedText()).getString(),
                "itemFrameHelper", client, ModConfig.getDefault().isItemFrameHelperEnabled()
        ));
        configOptions.add(new ToggleConfigOption(
                secColumnX, startY + (2 * optionHeight), optionWidth, optionHeight,
                new TranslatableText("config.caetweeks.antibreak.name"),
                new TranslatableText("config.caetweeks.antibreak.description",
                        KeyBindingHelper.getBoundKeyOf(KeyManager.getKeyBinding("antibreak")).getLocalizedText()).getString(),
                "antiBreak", client, ModConfig.getDefault().isAntiBreakEnabled()
        ));


        final int githubButtonWidth = textRenderer.getWidth("GitHub") + 24,
                keysButtonWidth = textRenderer.getWidth(new TranslatableText("button.caetweeks.changekeys")) + 24,
                resetButtonWidth = textRenderer.getWidth(new TranslatableText("button.caetweeks.reset")) + 24,
                startX = (width - (githubButtonWidth + keysButtonWidth + resetButtonWidth + 20)) / 2,
                buttonY = height - marginY - 20;

        // buttons
        ButtonWidget githubButton = new ButtonWidget(startX, buttonY,
                githubButtonWidth, 20, Text.of("Github"), button -> {

            this.client.openScreen(new ConfirmChatLinkScreen((bl) -> {
                if (bl) {
                    Util.getOperatingSystem().open(Constants.GITHUB);
                }
                this.client.openScreen(this);
            }, Constants.GITHUB, true));
        });

        ButtonWidget keysButton = new ButtonWidget(startX + githubButtonWidth + 10, buttonY,
                keysButtonWidth, 20, new TranslatableText("button.caetweeks.changekeys"), button -> {

            client.openScreen(new ControlsOptionsScreen(this, client.options));
        });

        ButtonWidget resetButton = new ButtonWidget(startX + githubButtonWidth + keysButtonWidth + 20, buttonY,
                resetButtonWidth, 20, new TranslatableText("button.caetweeks.reset"), button -> {

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

        String tooltip = null;
        for (ConfigOption configOption : configOptions) {
            configOption.render(matrices, mouseX, mouseY, delta);

            if (configOption.isHovered()) tooltip = configOption.description;
        }

        if (tooltip != null) renderTooltip(matrices, TextUtils.stringToTexts(tooltip), mouseX, mouseY);
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