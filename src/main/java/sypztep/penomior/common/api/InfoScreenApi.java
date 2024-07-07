package sypztep.penomior.common.api;

import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import sypztep.penomior.Penomior;

import java.util.ArrayList;
import java.util.List;

/**
 * API for managing player information display.
 */
public class InfoScreenApi {
    public static final String PLAYER_INFO_KEY = Penomior.MODID + ".gui.player_info.";
    private static final List<MutableText> information = new ArrayList<>();
    private static final List<String> keys = new ArrayList<>();

    /**
     * Adds a custom piece of information.
     *
     * @param info The information to add.
     */
    public static void addInformation(MutableText info) {
        synchronized (information) {
            information.add(info);
            keys.add(info.getString());
        }
    }

    /**
     * Adds an integer value with a specific key.
     *
     * @param key   The key for the information.
     * @param value The integer value to display.
     */
    public static void addInformation(String key, int value) {
        synchronized (information) {
            information.add(Text.translatable(PLAYER_INFO_KEY + key).append(String.format(": %d", value)));
            keys.add(key);
        }
    }

    /**
     * Adds a float value with a specific key.
     *
     * @param key   The key for the information.
     * @param value The float value to display.
     */
    public static void addInformation(String key, float value) {
        synchronized (information) {
            information.add(Text.translatable(PLAYER_INFO_KEY + key).append(String.format(": %.2f%%", value)));
            keys.add(key);
        }
    }

    /**
     * Retrieves the current list of information.
     *
     * @return A copy of the current information list.
     */
    public static List<MutableText> getInformation() {
        synchronized (information) {
            return new ArrayList<>(information);
        }
    }

    /**
     * Retrieves the current list of keys.
     *
     * @return A copy of the current keys list.
     */
    public static List<String> getKeys() {
        synchronized (keys) {
            return new ArrayList<>(keys);
        }
    }

    /**
     * Clears all the stored information and keys.
     */
    public static void clearInformation() {
        synchronized (information) {
            information.clear();
            keys.clear();
        }
    }
}