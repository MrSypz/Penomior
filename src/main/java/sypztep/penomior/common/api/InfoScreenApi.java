package sypztep.penomior.common.api;

import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import sypztep.penomior.Penomior;

import java.util.HashMap;
import java.util.Map;

public class InfoScreenApi {
    public static final String PLAYER_INFO_KEY = Penomior.MODID + ".gui.player_info.";
    private static final Map<String, MutableText> information = new HashMap<>();
    private static final Map<String, String> keys = new HashMap<>();

    public static void addInformation(String key, MutableText info) {
        synchronized (information) {
            information.put(key, info);
            keys.put(key, info.getString());
        }
    }

    public static void addInformation(String key, int value) {
        synchronized (information) {
            MutableText text = Text.translatable(PLAYER_INFO_KEY + key).append(String.format(": %d", value));
            information.put(key, text);
            keys.put(key, key);
        }
    }

    public static void addInformation(String key, float value) {
        synchronized (information) {
            MutableText text = Text.translatable(PLAYER_INFO_KEY + key).append(String.format(": %.2f%%", value));
            information.put(key, text);
            keys.put(key, key);
        }
    }

    public static Map<String, MutableText> getInformation() {
        synchronized (information) {
            return new HashMap<>(information);
        }
    }

    public static Map<String, String> getKeys() {
        synchronized (keys) {
            return new HashMap<>(keys);
        }
    }

    public static void clearInformation() {
        synchronized (information) {
            information.clear();
            keys.clear();
        }
    }
}
