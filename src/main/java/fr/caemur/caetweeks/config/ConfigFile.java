package fr.caemur.caetweeks.config;

import net.minecraft.client.MinecraftClient;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ConfigFile {
    public static final File FILE = new File(MinecraftClient.getInstance().runDirectory, "caetweeks.txt");

    public static void init(File gameFolder) {
        try {
            if (FILE.createNewFile()) {
                save(ModConfig.DEFAULT);
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public static void load() {

    }

    public static void save(ModConfig config) {
        try {
            FileWriter writer = new FileWriter(FILE);
            writer.write("clearWater:" + config.isClearWaterEnabled() + ";"
                    + "clearWater:" + config.isClearWaterEnabled() + ";"
                    + "itemFrameHelper:" + config.isClearWaterEnabled() + ";");
            writer.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
