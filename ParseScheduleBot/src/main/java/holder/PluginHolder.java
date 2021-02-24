package holder;

import plugin.Plugin;

import java.util.ArrayList;

public class PluginHolder {
    public static ArrayList<Plugin> plugins = new ArrayList<>();

    public static void setPlugins(ArrayList<Plugin> plugins) {
        PluginHolder.plugins = plugins;
    }

    public static ArrayList<Plugin> getPlugins() {
        return plugins;
    }
}
