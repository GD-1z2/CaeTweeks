package fr.caemur.caetweeks.utils;

import fr.caemur.caetweeks.CaeTweeks;
import fr.caemur.caetweeks.config.ConfigFile;
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

    private static KeyBinding configMenuKeyBinding, itemFrameKeyBinding, clearWaterKeyBinding, clearLavaKeyBinding, antiBreakKeyBinding;

    public static void registerKeys() {
        configMenuKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.caetweeks.configmenu",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_R,
                KEY_CATEGORY
        ));

        clearWaterKeyBinding = registerKeyBinding("key.caetweeks.clearwater");
        clearLavaKeyBinding = registerKeyBinding("key.caetweeks.clearlava");
        itemFrameKeyBinding = registerKeyBinding("key.caetweeks.itemframehelper");
        antiBreakKeyBinding = registerKeyBinding("key.caetweeks.antibreak");
    }

    public static void registerEvent() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (configMenuKeyBinding.wasPressed()) {
                client.openScreen(new ConfigScreen(Text.of(
                        client.player.getMainHandStack().getName().getString().equalsIgnoreCase("jeb_")
                                ? "§4C§ca§6e§eT§2w§ae§be§3k§1s§9 §dC§5o§4n§cf§6i§eg" // probably not an easter egg
                                : "CaeTweeks Config")));
            }

            while (itemFrameKeyBinding.wasPressed()) {
                CaeTweeks.getConfig().toggleItemFrameHelper();
                ConfigFile.save(CaeTweeks.getConfig());
            }

            while (clearWaterKeyBinding.wasPressed()) {
                CaeTweeks.getConfig().toggleClearWater();
                ConfigFile.save(CaeTweeks.getConfig());
            }

            while (clearLavaKeyBinding.wasPressed()) {
                CaeTweeks.getConfig().toggleClearLava();
                ConfigFile.save(CaeTweeks.getConfig());
            }

            while (antiBreakKeyBinding.wasPressed()) {
                CaeTweeks.getConfig().toggleAntiBreak();
                ConfigFile.save(CaeTweeks.getConfig());
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
            case "antibreak":
                return antiBreakKeyBinding;
            default:
                return configMenuKeyBinding;
        }
    }

    private static KeyBinding registerKeyBinding(String name) {
        return KeyBindingHelper.registerKeyBinding(
                new KeyBinding(name, InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, KEY_CATEGORY));
    }
}
