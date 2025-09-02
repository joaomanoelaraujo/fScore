package me.joaomanoel.d4rkk.dev.fScore;

import me.joaomanoel.d4rkk.dev.plugin.config.KConfig;
import me.joaomanoel.d4rkk.dev.plugin.config.KWriter;
import me.joaomanoel.d4rkk.dev.plugin.logger.KLogger;
import me.joaomanoel.d4rkk.dev.utils.StringUtils;
import org.bukkit.Bukkit;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

@SuppressWarnings("rawtypes")
public class Language {
  
  public static final KLogger LOGGER = ((KLogger) Main.getInstance().getLogger())
      .getModule("LANGUAGE");
  private static final KConfig CONFIG = Main.getInstance().getConfig("language");

  public static long scoreboards$scroller$every_tick = 1;
  public static List<String> scoreboards$scroller$titles = Arrays
          .asList("§e§lÁREA DE LOGIN");

  public static List<String> scoreboards$lobby = Arrays
          .asList(" §7Seja bem-vindo(a) a FireMC!",
                  "",
                  " §6§lLOGIN",
                  " §7para logar utilize",
                  " §f/login <senha>",
                  "",
                  " §6§lREGISTRAR",
                  " §7para registrar-se utilize",
                  " §f/registrar <senha> <senha>",
                  "",
                  " Status: {nlogin_auth}",
                  " Ping: {ping}",
                  "",
                  " §6www.firemc.com.br");

  
  public static void setupLanguage() {
    boolean save = false;
    KWriter writer = Main.getInstance().getWriter(CONFIG.getFile(),
        "aScoreboard - Created by D4RKK\nConfiguration version: " + Main.getInstance()
            .getDescription().getVersion());
    for (Field field : Language.class.getDeclaredFields()) {
      if (field.getName().contains("$") && !Modifier.isFinal(field.getModifiers())) {
        String nativeName = field.getName().replace("$", ".").replace("_", "-");
        
        try {
          Object value;
          KWriter.YamlEntryInfo entryInfo = field.getAnnotation(KWriter.YamlEntryInfo.class);
          
          if (CONFIG.contains(nativeName)) {
            value = CONFIG.get(nativeName);
            if (value instanceof String) {
              value = StringUtils.formatColors((String) value).replace("\\n", "\n");
            } else if (value instanceof List) {
              List l = (List) value;
              List<Object> list = new ArrayList<>(l.size());
              for (Object v : l) {
                if (v instanceof String) {
                  list.add(StringUtils.formatColors((String) v).replace("\\n", "\n"));
                } else {
                  list.add(v);
                }
              }
              
              value = list;
            }
            
            field.set(null, value);
            writer.set(nativeName, new KWriter.YamlEntry(
                new Object[]{entryInfo == null ? "" : entryInfo.annotation(),
                    CONFIG.get(nativeName)}));
          } else {
            value = field.get(null);
            if (value instanceof String) {
              value = StringUtils.deformatColors((String) value).replace("\n", "\\n");
            } else if (value instanceof List) {
              List l = (List) value;
              List<Object> list = new ArrayList<>(l.size());
              for (Object v : l) {
                if (v instanceof String) {
                  list.add(StringUtils.deformatColors((String) v).replace("\n", "\\n"));
                } else {
                  list.add(v);
                }
              }
              
              value = list;
            }
            
            save = true;
            writer.set(nativeName, new KWriter.YamlEntry(new Object[]{entryInfo == null ? "" : entryInfo.annotation(), value}));
          }
        } catch (ReflectiveOperationException e) {
          LOGGER.log(Level.WARNING, "Unexpected error on settings file: ", e);
        }
      }
    }
    
    if (save) {
      writer.write();
      CONFIG.reload();
      Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(),
          () -> LOGGER.info("A config §6language.yml §afoi modificada ou criada."));
    }
  }
}
