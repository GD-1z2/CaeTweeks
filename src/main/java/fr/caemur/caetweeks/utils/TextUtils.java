package fr.caemur.caetweeks.utils;

import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class TextUtils {
    public static List<Text> stringToTexts(String text) {
        String[] lines = text.split("\\|");
        List<Text> result = new ArrayList<>();
        for (String line : lines) {
            result.add(Text.of(line));
        }
        return result;
    }
}
