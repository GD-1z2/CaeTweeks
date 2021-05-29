package fr.caemur.caetweeks;

import net.fabricmc.api.ModInitializer;

public class CaeTweeks implements ModInitializer {
    @Override
    public void onInitialize() {
        KeyManager.init();
        KeyManager.registerKeys();
    }
}
