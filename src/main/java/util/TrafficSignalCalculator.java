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
        int directionCount = Direction.values().length;

        int totalVehicles = vehicleCounts.values().stream().mapToInt(Integer::intValue).sum();

        int totalGreenTime = TOTAL_CYCLE - (directionCount * YELLOW_DURATION);

        if (totalVehicles == 0) {
            // Hiç araç yoksa tüm yönlere eşit süre ver
            int equalGreen = totalGreenTime / directionCount;
            for (Direction dir : Direction.values()) {
                greenDurations.put(dir, equalGreen);
            }
            return greenDurations;
        }

        // Adım 1: Orantısal olarak süreleri dağıt (clamp edilmeden)
        Map<Direction, Integer> rawDurations = new EnumMap<>(Direction.class);
        for (Direction dir : Direction.values()) {
            int count = vehicleCounts.getOrDefault(dir, 0);
            double ratio = (double) count / totalVehicles;
            int raw = (int) Math.round(ratio * totalGreenTime);
            rawDurations.put(dir, raw);
        }

        // Adım 2: Clamp ile min-max sınırlamalarını uygula
        Map<Direction, Integer> clampedDurations = new EnumMap<>(Direction.class);
        int clampedSum = 0;
        for (Direction dir : Direction.values()) {
            int clamped = clamp(rawDurations.get(dir));
            clampedDurations.put(dir, clamped);
            clampedSum += clamped;
        }

        // Adım 3: Eğer clamp sonrası toplam süre doğru değilse düzelt
        int diff = totalGreenTime - clampedSum;

        while (diff != 0) {
            for (Direction dir : Direction.values()) {
                int current = clampedDurations.get(dir);
                if (diff > 0 && current < GREEN_MAX) {
                    clampedDurations.put(dir, current + 1);
                    diff--;
                } else if (diff < 0 && current > GREEN_MIN) {
                    clampedDurations.put(dir, current - 1);
                    diff++;
                }
                if (diff == 0) break;
            }
        }

        return clampedDurations;
    }

    /**
     * Süreyi min ve max sınırları içinde sabitler.
     */
    private static int clamp(int value) {
        return Math.max(GREEN_MIN, Math.min(GREEN_MAX, value));
    }
}
