package controller;

import model.Direction;
import model.LightState;
import model.Vehicle;
import util.TrafficSignalCalculator;

import java.util.List;
import java.util.Map;

/**
 * TrafficSimulationManager:
 * Trafik simülasyonunun ana yöneticisidir.
 * - Araç üretimi ve hareketlerini kontrol eder.
 * - Trafik ışığı döngülerini yönetir.
 * - Işık sürelerini başlangıçta araç yoğunluğuna göre hesaplar.
 */
public class TrafficSimulationManager {

    private final VehicleSimulationManager vehicleManager;   // Araç yönetimi (üretim, konum, hareket)
    private final TrafficLightController lightController;    // Işık yönetimi (süre, döngü, durum)

    private final Map<Direction, Integer> vehicleCounts;     // Kullanıcıdan alınan araç sayıları
    private final long simulationStartTime;                  // Simülasyonun başlama zamanı (nanoTime cinsinden)

    /**
     * Yapıcı metot: Başlangıç araç sayılarına göre simülasyonu başlatır.
     *
     * @param initialVehicleCounts Kullanıcı tarafından girilen her yön için araç sayısı
     */
    public TrafficSimulationManager(Map<Direction, Integer> initialVehicleCounts) {
        this.vehicleCounts = initialVehicleCounts;
        this.simulationStartTime = System.nanoTime();

        // 1️⃣ Araç yoğunluğuna göre yeşil sürelerini hesapla
        Map<Direction, Integer> greenDurations =
                TrafficSignalCalculator.calculateGreenDurations(vehicleCounts);

        // 2️⃣ Trafik ışığı yöneticisini başlat
        this.lightController = new TrafficLightController(greenDurations, simulationStartTime);

        // 3️⃣ Araç yöneticisini başlat (ışık kontrol bilgisi ile birlikte)
        this.vehicleManager = new VehicleSimulationManager(vehicleCounts, this.lightController);
    }

    /**
     * Simülasyonu her kare (frame) için günceller.
     * - Işıkların durumunu günceller.
     * - Araçları hareket ettirir veya durdurur.
     */
    public void update(long now) {
        lightController.update(now);
        vehicleManager.update(now);
    }

    /**
     * Belirli bir yön için ışığın mevcut durumunu döner.
     *
     * @param direction Yön (NORTH, SOUTH, ...)
     * @return LightState (RED, YELLOW, GREEN)
     */
    public LightState getLightState(Direction direction) {
        return lightController.getState(direction);
    }

    /**
     * Belirli bir yönde bulunan araç listesini verir.
     *
     * @param direction Yön
     * @return Araç listesi
     */
    public List<Vehicle> getVehicles(Direction direction) {
        return vehicleManager.getVehicles(direction);
    }

    /**
     * Tüm yönleri döner (Enum.values() şeklinde).
     */
    public Direction[] getDirections() {
        return vehicleManager.getDirections();
    }

    /**
     * Işık kontrolcüsüne dışarıdan erişim sağlar (ileri seviye kontroller için).
     */
    public TrafficLightController getLightController() {
        return lightController;
    }

    /**
     * Şu anki simülasyonda her yön için hesaplanmış yeşil süreleri döner.
     *
     * @return Her yön için yeşil ışık süresi
     */
    public Map<Direction, Integer> getGreenDurations() {
        return lightController.getAllLights().entrySet().stream()
                .collect(java.util.stream.Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue().getGreenDuration()
                ));
    }
}
