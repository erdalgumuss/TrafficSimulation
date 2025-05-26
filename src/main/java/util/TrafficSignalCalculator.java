package util;

import model.Direction;

import java.util.EnumMap;
import java.util.Map;

public class TrafficSignalCalculator {

    public static final int TOTAL_CYCLE = 120;
    public static final int YELLOW_DURATION = 3;
    public static final int GREEN_MIN = 10;
    public static final int GREEN_MAX = 60;

    /**
     * Verilen araç sayılarına göre yeşil süreleri hesaplar.
     */
    public static Map<Direction, Integer> calculateGreenDurations(Map<Direction, Integer> vehicleCounts) {
        Map<Direction, Integer> greenDurations = new EnumMap<>(Direction.class);

        int totalVehicles = vehicleCounts.values().stream().mapToInt(Integer::intValue).sum();
        if (totalVehicles == 0) {
            // Hiç araç yoksa tüm yönlere eşit süre ver
            int equalGreen = TOTAL_CYCLE / Direction.values().length - YELLOW_DURATION;
            for (Direction dir : Direction.values()) {
                greenDurations.put(dir, clamp(equalGreen));
            }
            return greenDurations;
        }

        int totalGreenTime = TOTAL_CYCLE - (Direction.values().length * YELLOW_DURATION);

        for (Direction dir : Direction.values()) {
            int count = vehicleCounts.getOrDefault(dir, 0);
            double ratio = (double) count / totalVehicles;
            int green = (int) Math.round(ratio * totalGreenTime);
            greenDurations.put(dir, clamp(green));
        }

        return greenDurations;
    }

    /**
     * Süreyi min ve max sınırları içinde sabitler.
     */
    private static int clamp(int value) {
        return Math.max(GREEN_MIN, Math.min(GREEN_MAX, value));
    }
}
