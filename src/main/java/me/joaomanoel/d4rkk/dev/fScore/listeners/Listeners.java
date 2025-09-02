package me.joaomanoel.d4rkk.dev.fScore.listeners;


import me.joaomanoel.d4rkk.dev.fScore.Main;
import me.joaomanoel.d4rkk.dev.fScore.listeners.player.PlayerJoinListener;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

public class Listeners {
  
  public static void setupListeners() {
    try {
      PluginManager pm = Bukkit.getPluginManager();
      
      pm.getClass().getDeclaredMethod("registerEvents", Listener.class, Plugin.class).invoke(pm, new PlayerJoinListener(), Main.getInstance());

    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
}
