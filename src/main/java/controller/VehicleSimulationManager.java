package controller;

import model.Direction;
import model.Vehicle;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * VehicleSimulationManager sınıfı, yön bazlı araç üretimini ve güncellemelerini yöneten bir simülasyon katmanıdır.
 * Kullanıcının belirlediği araç sayılarını dikkate alarak her yön için araç oluşturur.
 * Araçlar ışık kurallarına uygun şekilde VehicleController tarafından yönetilir.
 */
public class VehicleSimulationManager {

    // Gerçek zamanlı araç hareketlerini ve kontrolünü yöneten sınıf
    private final VehicleController vehicleController;

    // Tüm yönler (NORTH, SOUTH, EAST, WEST)
    private final Direction[] directions = Direction.values();

    // Kullanıcının belirlediği hedef araç sayıları
    private final Map<Direction, Integer> targetCounts;

    // Şu ana kadar üretilmiş araç sayıları
    private final Map<Direction, Integer> producedCounts;

    // Son araç üretim zaman damgası (nano saniye cinsinden)
    private long lastSpawnTime = 0;

    // Hangi yönde sıranın olduğunu belirleyen index
    private int spawnIndex = 0;

    /**
     * Yapıcı metot: Kullanıcının verdiği araç yoğunluk bilgileriyle ve ışık kontrolcüsüyle birlikte başlatılır.
     *
     * @param initialCounts     Kullanıcının her yön için belirlediği araç sayısı
     * @param lightController   Araçların ışıklara uyumlu şekilde durmasını sağlayacak kontrolcü
     */
    public VehicleSimulationManager(Map<Direction, Integer> initialCounts, TrafficLightController lightController) {
        this.vehicleController = new VehicleController(lightController);

        // Hedef araç sayılarını kullanıcının girdisine göre sakla
        this.targetCounts = new EnumMap<>(initialCounts);

        // Başlangıçta hiçbir araç üretilmemiştir
        this.producedCounts = new EnumMap<>(Direction.class);
        for (Direction dir : directions) {
            producedCounts.put(dir, 0);
        }
    }

    /**
     * Her frame'de çağrılır. Araç hareketlerini ve gerekirse yeni araç üretimini yönetir.
     *
     * @param now Güncel zaman (System.nanoTime())
     */
    public void update(long now) {
        // Araçları ışık ve mesafe kurallarına göre güncelle
        vehicleController.updateVehicles(now);

        // Her saniyede bir yön için üretim yapılır
        if (now - lastSpawnTime >= 1_000_000_000) {
            Direction dir = directions[spawnIndex % directions.length];

            if (shouldSpawn(dir)) {
                // Hedef araç sayısına ulaşılmamışsa yeni araç oluştur
                vehicleController.spawnVehicle(dir);
                producedCounts.put(dir, producedCounts.get(dir) + 1);
            }

            // Bir sonraki yön için sıra
            spawnIndex++;
            lastSpawnTime = now;
        }
    }

    /**
     * Bir yön için daha fazla araç üretilip üretilemeyeceğini kontrol eder.
     *
     * @param dir Yön bilgisi
     * @return true ise üretilebilir; false ise hedefe ulaşıldı demektir.
     */
    private boolean shouldSpawn(Direction dir) {
        int produced = producedCounts.getOrDefault(dir, 0);
        int target = targetCounts.getOrDefault(dir, 0);
        return produced < target;
    }

    /**
     * Belirli bir yönün tüm araçlarını döndürür (görselleştirme için).
     *
     * @param direction İlgili yön
     * @return Araç listesi
     */
    public List<Vehicle> getVehicles(Direction direction) {
        return vehicleController.getVehicleMap().get(direction);
    }

    /**
     * Mevcut sistemdeki yön dizisini döndürür.
     *
     * @return Tüm yönler
     */
    public Direction[] getDirections() {
        return directions;
    }
}
