package controller;

import model.Direction;
import model.LightState;
import model.Vehicle;

import java.util.*;

/**
 * VehicleController sınıfı, simülasyon boyunca araçların yön bazlı yönetimini ve güncellenmesini sağlar.
 * Her yön için ayrı bir araç listesi tutar. Işık durumuna ve önündeki araca göre araçların hareketini belirler.
 */
public class VehicleController {

    // Yönlere göre araç listesi
    private final Map<Direction, List<Vehicle>> vehicleMap;

    // Işık kontrolcüsü referansı (araçların durup durmayacağını belirlemek için)
    private final TrafficLightController lightController;

    /**
     * Controller başlatıldığında her yön için boş araç listesi oluşturulur.
     *
     * @param lightController Işıkların durumunu kontrol etmek için gerekli olan kontrolcü
     */
    public VehicleController(TrafficLightController lightController) {
        this.lightController = lightController;
        vehicleMap = new EnumMap<>(Direction.class);

        for (Direction dir : Direction.values()) {
            vehicleMap.put(dir, new ArrayList<>()); // Başlangıçta her yön için boş liste
        }
    }

    /**
     * Her frame’de tüm araçların durumunu günceller.
     * Önündeki araca çarpmasın diye araca mesafe kontrolü uygulanır.
     * Kavşağa yaklaşan araç, ışık kırmızı veya sarıysa durur.
     *
     * @param now Sistem zamanı (nano saniye)
     */
    public void updateVehicles(long now) {
        for (Direction dir : Direction.values()) {
            List<Vehicle> list = vehicleMap.get(dir);

            for (int i = 0; i < list.size(); i++) {
                Vehicle current = list.get(i);
                boolean canMove = true;

                // Önündeki araçla mesafe kontrolü
                if (i > 0) {
                    Vehicle front = list.get(i - 1);
                    canMove = current.distanceTo(front) > 35;
                }

                // Işık durumu kontrolü
                LightState lightState = lightController.getState(dir);

                // Eğer araç kavşağa yaklaşıyorsa ve ışık yeşil değilse dur
                if (current.approachingIntersection() && lightState != LightState.GREEN) {
                    canMove = false;
                }

                // Aracı hareket ettir veya ettirme
                current.updatePosition(canMove, now);
            }

            // Simülasyondan çıkan (sahne dışı) araçları listeden sil
            list.removeIf(v -> !v.isActive());
        }
    }

    /**
     * Belirli bir yön için sahneye yeni araç ekler.
     * Araç, giriş noktası ve rastgele bir model ile oluşturulur.
     *
     * @param direction Yön bilgisi
     */
    public void spawnVehicle(Direction direction) {
        Vehicle newVehicle = VehicleFactory.generateSingleVehicle(direction);
        vehicleMap.get(direction).add(newVehicle);
    }

    /**
     * Tüm yönlerin araç listelerini döner.
     * UI tarafından çağrılarak çizim yapılabilir.
     *
     * @return Yön -> Araç listesi haritası
     */
    public Map<Direction, List<Vehicle>> getVehicleMap() {
        return vehicleMap;
    }
}
