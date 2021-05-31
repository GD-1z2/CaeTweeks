package fr.caemur.caetweeks;

import fr.caemur.caetweeks.config.ConfigFile;
import fr.caemur.caetweeks.config.ModConfig;
import net.fabricmc.api.ModInitializer;

public class CaeTweeks implements ModInitializer {
    private static ModConfig config;

    @Override
    public void onInitialize() {
        ConfigFile.init();
        config = ConfigFile.load();

        KeyManager.init();
        KeyManager.registerKeys();
    }

    public static ModConfig getConfig() {
        return config;
    }
}