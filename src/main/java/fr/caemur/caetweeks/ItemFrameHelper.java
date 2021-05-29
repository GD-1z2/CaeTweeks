package fr.caemur.caetweeks;

public class ItemFrameHelper {
    private static boolean enabled = false;

    public static void toggle() {
        enabled = !enabled;
    }

    public static boolean isEnabled() {
        return enabled;
    }
}
