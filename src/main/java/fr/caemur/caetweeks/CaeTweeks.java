package fr.caemur.caetweeks;

import fr.caemur.caetweeks.config.ModConfig;
import net.fabricmc.api.ModInitializer;

public class CaeTweeks implements ModInitializer {
    private static ModConfig config;

    @Override
    public void onInitialize() {
        config = ModConfig.DEFAULT;

        KeyManager.init();
        KeyManager.registerKeys();
    }

    public static ModConfig getConfig() {
        return config;
    }
}