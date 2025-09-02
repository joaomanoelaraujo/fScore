package me.joaomanoel.d4rkk.dev.fScore.listeners.player;

import me.joaomanoel.d4rkk.dev.fScore.hook.SimpleScoreboard;
import me.joaomanoel.d4rkk.dev.player.Profile;
import me.joaomanoel.d4rkk.dev.player.hotbar.Hotbar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
  
  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent evt) {
    evt.setJoinMessage(null);
    Player player = evt.getPlayer();
    Profile profile = Profile.getProfile(player.getName());
    SimpleScoreboard.setup(profile.getPlayer());
    profile.setHotbar(Hotbar.getHotbarById("lobby"));
    profile.refresh();
  }
}
