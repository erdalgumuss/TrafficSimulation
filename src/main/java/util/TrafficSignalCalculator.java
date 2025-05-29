package util;

import model.Direction;

import java.util.EnumMap;
import java.util.Map;

/**
 * TrafficSignalCalculator:
 * Her bir yön için trafik yoğunluğuna bağlı olarak yeşil ışık süresi hesaplar.
 * Toplam döngü süresi sabittir (örneğin 120 saniye).
 */
public class TrafficSignalCalculator {

    public static final int TOTAL_CYCLE = 120;     // Tüm ışık döngüsünün süresi (saniye)
    public static final int YELLOW_DURATION = 3;   // Her yön için sarı ışık süresi
    public static final int GREEN_MIN = 10;        // Yeşil ışığın minimum süresi
    public static final int GREEN_MAX = 60;        // Yeşil ışığın maksimum süresi

    /**
     * Araç sayısına göre yeşil süreleri hesaplar.
     *
     * @param vehicleCounts Her yön için araç sayısı
     * @return Her yön için yeşil ışık süresi
     */
    public static Map<Direction, Integer> calculateGreenDurations(Map<Direction, Integer> vehicleCounts) {
        Map<Direction, Integer> greenDurations = new EnumMap<>(Direction.class);
        int directionCount = Direction.values().length;

        int totalVehicles = vehicleCounts.values().stream().mapToInt(Integer::intValue).sum();
        int totalGreenTime = TOTAL_CYCLE - (directionCount * YELLOW_DURATION); // toplam yeşil süre

        if (totalVehicles == 0) {
            // Hiç araç yoksa tüm yönlere eşit yeşil süre ver
            int equalGreen = totalGreenTime / directionCount;
            for (Direction dir : Direction.values()) {
                greenDurations.put(dir, equalGreen);
            }
            return greenDurations;
        }

        // 1️⃣ Araç yoğunluğuna göre orantısal süreler hesapla
        Map<Direction, Integer> rawDurations = new EnumMap<>(Direction.class);
        for (Direction dir : Direction.values()) {
            int count = vehicleCounts.getOrDefault(dir, 0);
            double ratio = (double) count / totalVehicles;
            int raw = (int) Math.round(ratio * totalGreenTime);
            rawDurations.put(dir, raw);
        }

        // 2️⃣ Min ve Max sınırlarına uydur (clamp)
        Map<Direction, Integer> clampedDurations = new EnumMap<>(Direction.class);
        int clampedSum = 0;
        for (Direction dir : Direction.values()) {
            int clamped = clamp(rawDurations.get(dir));
            clampedDurations.put(dir, clamped);
            clampedSum += clamped;
        }

        // 3️⃣ Clamp sonrası oluşabilecek farkı (süre açığı ya da fazlası) düzelt
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
     * Bir değeri minimum ve maksimum limitler arasında sabitler.
     *
     * @param value Süre değeri
     * @return Clamp edilmiş süre
     */
    private static int clamp(int value) {
        return Math.max(GREEN_MIN, Math.min(GREEN_MAX, value));
    }
}
