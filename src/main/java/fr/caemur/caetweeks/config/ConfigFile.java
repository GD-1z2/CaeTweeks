package fr.caemur.caetweeks.config;

import net.minecraft.client.MinecraftClient;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ConfigFile {
    public static final File FILE = new File(MinecraftClient.getInstance().runDirectory, "caetweeks.txt");

    public static void init() {
        try {
            if (FILE.createNewFile()) {
                save(ModConfig.getDefault());
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public static ModConfig load() {
        final ModConfig config = ModConfig.getDefault();
        final List<String> lines = new ArrayList<>();

        try {
            Scanner scanner = new Scanner(FILE);
            while (scanner.hasNextLine()) {
                lines.add(scanner.nextLine());
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error when loading caetweeks config");
            e.printStackTrace();
            return config;
        }

        final String[] fileContent = String.join("", lines).split(";");

        for (String field : fileContent) {
            final String[] fieldInfo = field.split("=");
            if (fieldInfo.length != 2) continue;

            if (fieldInfo[1].equals("true") || fieldInfo[1].equals("false")) { // boolean
                config.setFieldB(fieldInfo[0], Boolean.parseBoolean(fieldInfo[1]));
            }
        }

        return config;
    }

    public static void save(ModConfig config) {
        try {
            FileWriter writer = new FileWriter(FILE);
            writer.write(config.toString());
            writer.close();
        } catch (IOException e) {
            System.out.println("Error when saving caetweeks config");
            e.printStackTrace();
        }
    }
}
