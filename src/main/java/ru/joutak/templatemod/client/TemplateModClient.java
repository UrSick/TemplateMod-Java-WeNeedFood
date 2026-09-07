package ru.joutak.templatemod.client;
import ru.joutak.templatemod.config.ConfigLoader;
import ru.joutak.templatemod.config.ModConfig;
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
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
public class TemplateModClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ModConfig config = ConfigLoader.load();

        this.recorder = new RoutRecorder(
                config.getRecordingIntervalTicks()
        );
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
            while (this.pauseRecording.consumeClick()) {
                recorder.togglePause();
            }
            recorder.tick(client.player.position());
        });
        LevelRenderEvents.END_EXTRACTION.register(context -> {
            RouteMarkers.extract(recorder.getPoints());
        });
        LevelRenderEvents.COLLECT_SUBMITS.register(RouteMarkers::draw);
        HudElementRegistry.attachElementBefore(
                VanillaHudElements.CHAT,
                Identifier.fromNamespaceAndPath(TemplateMod.MOD_ID, "recording_hud"),
                (graphics, deltaTracker) ->
                        RecordingHud.draw(graphics, recorder.getState())
        );
    }
    private RoutRecorder recorder;

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
    KeyMapping pauseRecording = KeyMappingHelper.registerKeyMapping(
            new KeyMapping(
                    "key.templatemod.pause_recording",
                    InputConstants.Type.KEYSYM,
                    InputConstants.KEY_V,
                    this.Pathtrack
            )
    );
    KeyMapping ShowList = KeyMappingHelper.registerKeyMapping(
            new KeyMapping("key.templatemod.show_list",
            InputConstants.Type.KEYSYM,
            InputConstants.KEY_P,
            this.Pathtrack
    ));
}
