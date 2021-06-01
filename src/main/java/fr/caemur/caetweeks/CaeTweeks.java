package fr.caemur.caetweeks;

import fr.caemur.caetweeks.config.ConfigFile;
import fr.caemur.caetweeks.config.ModConfig;
import fr.caemur.caetweeks.utils.KeyManager;
import net.fabricmc.api.ModInitializer;

public class CaeTweeks implements ModInitializer {
    private static ModConfig config;

    @Override
    public void onInitialize() {
        ConfigFile.init();
        config = ConfigFile.load();

        KeyManager.registerKeys();
        KeyManager.registerEvent();
    }

    public static ModConfig getConfig() {
        return config;
    }
}