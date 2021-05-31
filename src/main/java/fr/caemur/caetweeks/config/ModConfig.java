package fr.caemur.caetweeks.config;

public class ModConfig {
    private boolean itemFrameHelperEnabled, clearWaterEnabled, clearLavaEnabled;

    public ModConfig(boolean itemFrameHelperEnabled, boolean clearWaterEnabled, boolean clearLavaEnabled) {
        this.itemFrameHelperEnabled = itemFrameHelperEnabled;
        this.clearWaterEnabled = clearWaterEnabled;
        this.clearLavaEnabled = clearLavaEnabled;
    }

    public boolean getFieldB(String field) {
        switch (field) {
            case "itemFrameHelper":
                return itemFrameHelperEnabled;
            case "clearWater":
                return clearWaterEnabled;
            case "clearLava":
                return clearLavaEnabled;
            default:
                return false;
        }
    }

    public void setFieldB(String field, boolean value) {
        switch (field) {
            case "itemFrameHelper":
                itemFrameHelperEnabled = value;
                break;
            case "clearWater":
                clearWaterEnabled = value;
                break;
            case "clearLava":
                clearLavaEnabled = value;
                break;
        }
    }

    public boolean isItemFrameHelperEnabled() {
        return itemFrameHelperEnabled;
    }

    public void toggleItemFrameHelper() {
        itemFrameHelperEnabled = !itemFrameHelperEnabled;
    }


    public boolean isClearWaterEnabled() {
        return clearWaterEnabled;
    }

    public void toggleClearWater() {
        clearWaterEnabled = !clearWaterEnabled;
    }

    public boolean isClearLavaEnabled() {
        return clearLavaEnabled;
    }

    public void toggleClearLava() {
        clearLavaEnabled = !clearLavaEnabled;
    }

    @Override
    public String toString() {
        return "clearWater=" + clearWaterEnabled +
                ";clearLava=" + clearLavaEnabled +
                ";itemFrameHelper=" + itemFrameHelperEnabled;
    }

    public static ModConfig getDefault() {
        return new ModConfig(false, false, false);
    }
}