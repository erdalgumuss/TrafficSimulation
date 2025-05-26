package model;

public class TrafficLight {
    private final Direction direction;
    private LightState state;
    private int greenDuration;   // seconds
    private int phaseOffset; // saniye cinsinden kavşak döngüsündeki başlangıç zamanı

    public static final int YELLOW_DURATION = 3;
    public static final int CYCLE_DURATION = 120;

    public TrafficLight(Direction direction, int greenDuration, int phaseOffset) {
        this.direction = direction;
        this.greenDuration = greenDuration;
        this.phaseOffset = phaseOffset;
        this.state = LightState.RED;
    }public long getRemainingTime(long now, long startTime) {
        long elapsedSec = (now - startTime) / 1_000_000_000;
        long cyclePos = elapsedSec % CYCLE_DURATION;

        long greenStart = 0;
        long yellowStart = greenStart + greenDuration;
        long redStart = yellowStart + YELLOW_DURATION;

        if (cyclePos < greenDuration) {
            return greenDuration - cyclePos;
        } else if (cyclePos < yellowStart + YELLOW_DURATION) {
            return yellowStart + YELLOW_DURATION - cyclePos;
        } else {
            return CYCLE_DURATION - cyclePos;
        }
    }


    public void update(long now, long startTime) {
        long elapsedSec = (now - startTime) / 1_000_000_000;
        long cyclePos = (elapsedSec - phaseOffset + CYCLE_DURATION) % CYCLE_DURATION;

        if (cyclePos < greenDuration) {
            state = LightState.GREEN;
        } else if (cyclePos < greenDuration + YELLOW_DURATION) {
            state = LightState.YELLOW;
        } else {
            state = LightState.RED;
        }
    }

    public LightState getState() {
        return state;
    }

    public Direction getDirection() {
        return direction;
    }

    public int getGreenDuration() {
        return greenDuration;
    }

    public void setGreenDuration(int duration) {
        this.greenDuration = duration;
    }

    public void setPhaseOffset(int offset) {
        this.phaseOffset = offset;
    }
}
