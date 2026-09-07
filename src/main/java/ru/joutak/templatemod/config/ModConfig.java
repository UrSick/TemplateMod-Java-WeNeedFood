package ru.joutak.templatemod.config;

public class ModConfig {
    private int recordingIntervalTicks = 40;

    public void setRecordingIntervalTicks(int value) {
        if (value < 1) {
            throw new IllegalArgumentException(">=1");
        }

        this.recordingIntervalTicks = value;
    }
    public int getRecordingIntervalTicks() {
        return recordingIntervalTicks;
    }
}
