package ru.joutak.templatemod.client;
import net.minecraft.world.phys.Vec3;
import java.util.ArrayList;

public class RoutRecorder {
    enum RecordingState {
        STOPPED,
        RECORDING,
        PAUSED
    }
    public void start(Vec3 position) {
        points.clear();
        points.add(position);
        this.state = RecordingState.RECORDING;
        this.ticksSinceLastPoint = 0;
    }
    private RecordingState state = RecordingState.STOPPED;
    private int ticksSinceLastPoint = 0;
    private ArrayList<Vec3> points = new ArrayList<>();

}
