package com.megalos.HyGPT;

import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.megalos.HyGPT.commands.HyGPTCommand;

import javax.annotation.Nonnull;

public class HyGPTPlugin extends JavaPlugin {

    public HyGPTPlugin(@Nonnull JavaPluginInit init) {
        super(init);
    }

    @Override
    protected void setup() {
        getCommandRegistry().registerCommand(new HyGPTCommand());
    }
}
