package fr.caemur.caetweeks;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.LiteralText;
import org.lwjgl.glfw.GLFW;

public class KeyManager {
    private static final String KEY_CATEGORY = "category.caetweeks.core";

    private static KeyBinding itemFrameKeyBinding;

    public static void init() {
        itemFrameKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.caetweeks.itemframehelper",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_U,
                KEY_CATEGORY
        ));
    }

    public static void registerKeys() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (itemFrameKeyBinding.wasPressed()) {
                ItemFrameHelper.toggle();
            }
        });
    }
}
