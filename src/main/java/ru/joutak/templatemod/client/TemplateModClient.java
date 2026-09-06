package ru.joutak.templatemod.client;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import ru.joutak.templatemod.TemplateMod;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.network.chat.Component;
import ru.joutak.templatemod.client.RoutRecorder.RecordingState;

public class TemplateModClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        TemplateMod.LOGGER.info("Mod runned", TemplateMod.MOD_ID);
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null || client.isPaused()) {
                return;
            }
            while (this.StartRecording.consumeClick()) {
                if (client.player != null) {
                    if (recorder.getState() == RecordingState.STOPPED ) {
                        client.player.sendSystemMessage(Component.literal("Запись начата!"));
                    }
                    else if (recorder.getState() == RecordingState.RECORDING) {
                        client.player.sendSystemMessage(Component.literal("Запись завершена"));
                    }
                }
                recorder.toggleRecording(client.player.position());
            }
            while (this.ShowList.consumeClick()) {
                if (recorder.getPoints().isEmpty()){
                    client.player.sendSystemMessage(Component.literal("Список пуст"));
                }
                else {
                    for (Vec3 pos : recorder.getPoints()){
                        client.player.sendSystemMessage(Component.literal(pos.toString()));
                    }
                }
            }
            recorder.tick(client.player.position());
        });
    }
    private final RoutRecorder recorder = new RoutRecorder();

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
    KeyMapping ShowList = KeyMappingHelper.registerKeyMapping(
            new KeyMapping("key.templatemod.show_list",
            InputConstants.Type.KEYSYM,
            InputConstants.KEY_P,
            this.Pathtrack
    ));
}
