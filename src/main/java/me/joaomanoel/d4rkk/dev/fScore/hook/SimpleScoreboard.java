package me.joaomanoel.d4rkk.dev.fScore.hook;

import com.nickuc.login.api.nLoginAPI;
import com.nickuc.login.api.types.Identity;
import me.joaomanoel.d4rkk.dev.fScore.Language;
import me.joaomanoel.d4rkk.dev.fScore.Main;
import me.joaomanoel.d4rkk.dev.player.Profile;
import me.joaomanoel.d4rkk.dev.player.role.Role;
import me.joaomanoel.d4rkk.dev.player.scoreboard.KScoreboard;
import me.joaomanoel.d4rkk.dev.player.scoreboard.scroller.ScoreboardScroller;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class SimpleScoreboard {

  public static void setup(Player player) {
    Profile profile = Profile.getProfile(player.getName());

    if (profile == null) {
      return;
    }

    profile.setScoreboard(new KScoreboard() {
      @Override
      public void update() {
        nLoginAPI api = nLoginAPI.getApi();

        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
          String status;
          long lastAttempt = -1;

          try {
            if (api.isAuthenticated(player.getName())) {
              status = "§aAutenticado!";
            } else {
              Identity identity = Identity.ofKnownName(player.getName());
              lastAttempt = api.getRemainingSeconds(identity);
              if (lastAttempt < 0) lastAttempt = 0;
              status = "§eAguardando (§e" + lastAttempt + "s§e)";
            }
          } catch (Exception e) {
            e.printStackTrace();
            status = "§cErro";
          }

          long finalLastAttempt = lastAttempt;
          String finalStatus = status;

          Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
            List<String> lines = Language.scoreboards$lobby;
            for (int i = 0; i < Math.min(lines.size(), 15); i++) {
              String line = lines.get(i);

              line = line.replace("{player}", Role.getColored(player.getName()))
                      .replace("{nlogin_auth}", finalStatus)
                      .replace("{online}", String.valueOf(Bukkit.getOnlinePlayers().size()))
                      .replace("{ping}", getPlayerPing(player) >= 100 ? "§c" + getPlayerPing(player) + "ms" : "§a" + getPlayerPing(player) + "ms");

              this.add(15 - i, line);
            }
          });
        });
      }
    }.scroller(new ScoreboardScroller(Language.scoreboards$scroller$titles))
            .to(player)
            .build());

    profile.update();
    profile.getScoreboard().scroll();
    Bukkit.getScheduler().runTaskTimer(Main.getInstance(), () -> {
      if (player.isOnline() && profile.getScoreboard() != null) {
        profile.update();
      }
    }, 20L, 20L);
  }



  public static void startAutoUpdate() {
    new BukkitRunnable() {
      @Override
      public void run() {
        for (Profile profile : Profile.listProfiles()) {
          if (profile.getScoreboard() != null) {
            profile.update();
          }
        }
      }
    }.runTaskTimerAsynchronously(Main.getInstance(), 0, 20); // a cada 1 segundo
  }

  public static int getPlayerPing(Player player) {
    try {
      CraftPlayer craftPlayer = (CraftPlayer) player;
      EntityPlayer entityPlayer = craftPlayer.getHandle();
      return entityPlayer.ping;
    } catch (Exception e) {
      e.printStackTrace();
      return -1;
    }
  }
}
