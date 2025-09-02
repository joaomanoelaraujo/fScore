package me.joaomanoel.d4rkk.dev.fScore;

import me.joaomanoel.d4rkk.dev.fScore.listeners.Listeners;
import me.joaomanoel.d4rkk.dev.plugin.KPlugin;

public final class Main extends KPlugin {

    private static Main instance;
    public static Main getInstance() {
        return instance;
    }

    @Override
    public void start() {}

    @Override
    public void load() {}

    @Override
    public void enable() {
        instance = this;

        saveDefaultConfig();

        this.getLogger().info("Iniciando plugin...");
        Listeners.setupListeners();
        Language.setupLanguage();
        this.getLogger().info("Plugin iniciado com sucesso!");
    }

    @Override
    public void disable() {
        instance = null;
        this.getLogger().severe("Plugin desabilitado com sucesso!");
    }
}
