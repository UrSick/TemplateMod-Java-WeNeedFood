package ru.joutak.templatemod.client;

import net.fabricmc.api.ClientModInitializer;
import ru.joutak.templatemod.TemplateMod;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.network.chat.Component;

public class TemplateModClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        TemplateMod.LOGGER.info("Mod runned", TemplateMod.MOD_ID);
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (this.StartRecording.consumeClick()) {
                if (client.player != null) {
                    client.player.sendSystemMessage(Component.literal("Запись начата!"));
                }
            }
        });
    }

    KeyMapping.Category Pathtrack = KeyMapping.Category.register(
            Identifier.fromNamespaceAndPath(TemplateMod.MOD_ID, "controls")
    );
    KeyMapping StartRecording = KeyMappingHelper.registerKeyMapping(
            new KeyMapping(
                    "key.PathtrackMod.start-recording",
                    InputConstants.Type.KEYSYM,
                    InputConstants.KEY_C,
                    this.Pathtrack
            ));
}
