package com.megalos.HyGPT;

import com.hypixel.hytale.server.core.modules.interaction.interaction.config.server.OpenCustomUIInteraction;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.megalos.HyGPT.commands.HyGPTCommand;
import com.megalos.HyGPT.pages.HyGPTPage;
import com.megalos.HyGPT.pages.HyGPTPageSupplier;
import com.megalos.HyGPT.systems.HyGPTInteractionSystem;

import javax.annotation.Nonnull;

public class HyGPTPlugin extends JavaPlugin {

    public HyGPTPlugin(@Nonnull JavaPluginInit init) {
        super(init);
    }

    @Override
    protected void setup() {
        // Enregistre la commande /HyGPT
        getCommandRegistry().registerCommand(new HyGPTCommand());

        // Enregistre la page personnalisée pour l'interaction avec le Kweebec
        OpenCustomUIInteraction.registerCustomPageSupplier(
            this,
            HyGPTPage.class,
            "HyGPT",
            new HyGPTPageSupplier()
        );

        // DÉSACTIVÉ POUR DEbuG
        // getEntityStoreRegistry().registerSystem(new HyGPTInteractionSystem(this));
    }
}
