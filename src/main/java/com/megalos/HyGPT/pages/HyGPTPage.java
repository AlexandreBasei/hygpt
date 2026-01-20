package com.megalos.HyGPT.pages;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.packets.interface_.CustomPageLifetime;
import com.hypixel.hytale.protocol.packets.interface_.CustomUIEventBindingType;
import com.hypixel.hytale.server.core.entity.entities.player.pages.InteractiveCustomUIPage;
import com.hypixel.hytale.server.core.ui.builder.EventData;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.ui.builder.UIEventBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.megalos.HyGPT.service.MistralAIService;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class HyGPTPage extends InteractiveCustomUIPage<HyGPTPage.ChatBoxEventData> {
    private final MistralAIService chatGPTService = new MistralAIService();
    private final List<String> chatLines = new ArrayList<>();

    /**
     * EventData class - holds the data we receive when the button is clicked.
     */
    public static class ChatBoxEventData {
        private String playerPrompt = "";

        public static final BuilderCodec<ChatBoxEventData> CODEC =
                BuilderCodec.builder(ChatBoxEventData.class, ChatBoxEventData::new)
                        .append(
                                new KeyedCodec<>("@PlayerPrompt", Codec.STRING),
                                (ChatBoxEventData obj, String val) -> obj.playerPrompt = val,
                                (ChatBoxEventData obj) -> obj.playerPrompt
                        )
                        .add()
                        .build();
    }

    public HyGPTPage(PlayerRef playerRef) {
        super(playerRef, CustomPageLifetime.CanDismissOrCloseThroughInteraction, ChatBoxEventData.CODEC);
        // Message de bienvenue
        chatLines.add("[Kweebec] Kweek kweek ! Bienvenue aventurier !");
    }

    @Override
    public void build(
            @Nonnull Ref<EntityStore> ref,
            @Nonnull UICommandBuilder cmd,
            @Nonnull UIEventBuilder evt,
            @Nonnull Store<EntityStore> store
    ) {
        cmd.append("Pages/HyGPT.ui");

        evt.addEventBinding(
                CustomUIEventBindingType.Activating,
                "#SendButton",
                new EventData().append("@PlayerPrompt", "#PromptInput.Value")
        );

        // Affiche l'historique
        cmd.set("#ChatText.Text", buildChatText());
    }

    private String buildChatText() {
        // Garde les 10 derniers messages pour éviter le débordement
        int start = Math.max(0, chatLines.size() - 10);
        StringBuilder sb = new StringBuilder();
        for (int i = start; i < chatLines.size(); i++) {
            if (sb.length() > 0) sb.append("\n");
            sb.append(chatLines.get(i));
        }
        return sb.toString();
    }

    @Override
    public void handleDataEvent(
            @Nonnull Ref<EntityStore> ref,
            @Nonnull Store<EntityStore> store,
            @Nonnull ChatBoxEventData data
    ) {
        if (data.playerPrompt == null || data.playerPrompt.isEmpty()) {
            return;
        }

        // Ajoute le message du joueur
        chatLines.add("[You] " + data.playerPrompt);
        chatLines.add("[Kweebec] ...");
        refreshPage();

        // Lance la requête
        chatGPTService.ask(data.playerPrompt)
                .thenAccept(response -> {
                    // Remplace "..." par la vraie réponse
                    chatLines.set(chatLines.size() - 1, "[Kweebec] " + response);
                    refreshPage();
                })
                .exceptionally(error -> {
                    chatLines.set(chatLines.size() - 1, "[Kweebec] Erreur: " + error.getMessage());
                    refreshPage();
                    return null;
                });
    }

    private void refreshPage() {
        UICommandBuilder cmd = new UICommandBuilder();
        UIEventBuilder evt = new UIEventBuilder();

        cmd.set("#ChatText.Text", buildChatText());
        cmd.set("#PromptInput.Value", "");

        sendUpdate(cmd, evt, false);
    }
}
