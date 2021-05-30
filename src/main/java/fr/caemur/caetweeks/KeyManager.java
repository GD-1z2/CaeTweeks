package fr.caemur.caetweeks;

import fr.caemur.caetweeks.gui.ConfigScreen;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.util.Locale;

public class KeyManager {
    private static final String KEY_CATEGORY = "category.caetweeks.core";

    private static KeyBinding configMenuKeyBinding, itemFrameKeyBinding, clearWaterKeyBinding, clearLavaKeyBinding;

    public static void init() {
        configMenuKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.caetweeks.configmenu",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_R,
                KEY_CATEGORY
        ));

        itemFrameKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.caetweeks.itemframehelper",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_UNKNOWN,
                KEY_CATEGORY
        ));

        clearWaterKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.caetweeks.clearwater",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_UNKNOWN,
                KEY_CATEGORY
        ));

        clearLavaKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.caetweeks.clearlava",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_UNKNOWN,
                KEY_CATEGORY
        ));
    }

    public static void registerKeys() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (configMenuKeyBinding.wasPressed()) {
                client.openScreen(new ConfigScreen(Text.of("CaeTweeks Config")));
            }

            while (itemFrameKeyBinding.wasPressed()) {
                CaeTweeks.getConfig().toggleItemFrameHelper();
            }

            while (clearWaterKeyBinding.wasPressed()) {
                CaeTweeks.getConfig().toggleClearWater();
            }

            while (clearLavaKeyBinding.wasPressed()) {
                CaeTweeks.getConfig().toggleClearLava();
            }
        });
    }

    public static KeyBinding getKeyBinding(String name) {
        switch (name.toLowerCase(Locale.ROOT)) {
            case "clearwater":
                return clearWaterKeyBinding;
            case "clearlava":
                return clearLavaKeyBinding;
            case "itemframehelper":
                return itemFrameKeyBinding;
            default:
                return configMenuKeyBinding;
        }
    }
}
