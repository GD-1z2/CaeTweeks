package fr.caemur.caetweeks.gui;

import net.minecraft.client.gui.widget.AbstractPressableButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class ConfigOption extends AbstractPressableButtonWidget {
    protected Text title;
    protected String[] description;

    public ConfigOption(int x, int y, int width, int height, Text title, String[] description) {
        super(x, y, width, height, title);
        this.title = title;
        this.description = description;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
    }

    @Override
    public void onPress() {
    }

    public void tick() {
    }

    public void reset() {}
}
