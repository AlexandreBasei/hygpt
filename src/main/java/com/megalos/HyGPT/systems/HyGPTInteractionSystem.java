package com.megalos.HyGPT.systems;

import com.hypixel.hytale.component.AddReason;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.RemoveReason;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.RefSystem;
import com.hypixel.hytale.protocol.InteractionType;
import com.hypixel.hytale.server.core.entity.UUIDComponent;
import com.hypixel.hytale.server.core.modules.entity.component.Interactable;
import com.hypixel.hytale.server.core.modules.interaction.Interactions;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.npc.entities.NPCEntity;
import com.megalos.HyGPT.HyGPTPlugin;

import javax.annotation.Nonnull;
import java.util.logging.Level;

public class HyGPTInteractionSystem extends RefSystem<EntityStore> {
    private final HyGPTPlugin plugin;

    public HyGPTInteractionSystem(HyGPTPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public Query<EntityStore> getQuery() {
        // Query specifically for NPCEntity components only
        return NPCEntity.getComponentType();
    }

    @Override
    public void onEntityAdded(
            @Nonnull Ref<EntityStore> ref,
            @Nonnull AddReason reason,
            @Nonnull Store<EntityStore> store,
            @Nonnull CommandBuffer<EntityStore> commandBuffer
    ) {
        // Get the NPC entity component
        NPCEntity npcEntity = commandBuffer.getComponent(ref, NPCEntity.getComponentType());
        if (npcEntity == null) {
            return; // Should not happen since we query for NPCEntity
        }

        // Check if it's our HyGPT_Kweebec NPC
        String roleName = npcEntity.getRoleName();
        if (!"HyGPT_Kweebec".equals(roleName)) {
            return; // Not our NPC
        }

        plugin.getLogger().at(Level.INFO).log("Adding HyGPT interaction to NPC: %s", roleName);

        try {
            // Ensure the NPC is interactable
            if (commandBuffer.getComponent(ref, Interactable.getComponentType()) == null) {
                commandBuffer.putComponent(ref, Interactable.getComponentType(), Interactable.INSTANCE);
            }

            // Get or create Interactions component
            Interactions interactionsComponent = commandBuffer.getComponent(ref, Interactions.getComponentType());
            if (interactionsComponent == null) {
                interactionsComponent = new Interactions();
                commandBuffer.putComponent(ref, Interactions.getComponentType(), interactionsComponent);
            }

            // Set the interaction to use our HyGPT root interaction
            interactionsComponent.setInteractionId(InteractionType.Use, "*HyGPT");
            interactionsComponent.setInteractionHint("Talk");

            plugin.getLogger().at(Level.INFO).log("Successfully added HyGPT interaction to NPC: %s", roleName);
        } catch (Exception e) {
            plugin.getLogger().at(Level.WARNING).log("Failed to add HyGPT interaction: %s", e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void onEntityRemove(
            @Nonnull Ref<EntityStore> ref,
            @Nonnull RemoveReason reason,
            @Nonnull Store<EntityStore> store,
            @Nonnull CommandBuffer<EntityStore> commandBuffer
    ) {
        // Nothing to clean up when entity is removed
    }
}
