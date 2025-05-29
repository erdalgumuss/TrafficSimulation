package controller;

import model.Direction;
import model.LightState;
import model.TrafficLight;

import java.util.EnumMap;
import java.util.Map;

/**
 * TrafficLightController sınıfı, dört yönlü trafik ışıklarının kontrolünü sağlar.
 * Simülasyon süresi boyunca her ışığın süresini, sırasını ve durumunu yönetir.
 */
public class TrafficLightController {

    // Yön başına trafik ışığı nesneleri
    private final Map<Direction, TrafficLight> lights;

    // Simülasyonun başladığı zaman (nanoTime olarak)
    private final long simulationStartTime;

    // Işıkların zaman hesaplamasında referans alınacak zaman (gelişmiş hesaplama için)
    private final long startTime;

    /**
     * Yapıcı metot. Tüm yönler için trafik ışığı oluşturur.
     * Her ışığın sırasıyla (offset) başlamasını sağlar.
     *
     * @param greenDurations        Her yön için yeşil ışık süresi (saniye)
     * @param simulationStartTime   Simülasyonun başlatıldığı zaman (nanoTime)
     */
    public TrafficLightController(Map<Direction, Integer> greenDurations, long simulationStartTime) {
        this.simulationStartTime = simulationStartTime;
        this.startTime = simulationStartTime;
        this.lights = new EnumMap<>(Direction.class);

        // Işık sırasını takip etmek için offset
        int offset = 0;

        // Her yön için bir TrafficLight nesnesi oluştur
        for (Direction dir : Direction.values()) {
            int green = greenDurations.getOrDefault(dir, 30); // default: 30 saniye
            lights.put(dir, new TrafficLight(dir, green, offset));
            offset += green + TrafficLight.YELLOW_DURATION;
        }
    }

    /**
     * Her frame çağrılır. Işık durumlarını günceller.
     *
     * @param now Şu anki zaman (System.nanoTime())
     */
    public void update(long now) {
        for (TrafficLight light : lights.values()) {
            light.update(now, simulationStartTime);
        }
    }

    /**
     * Belirtilen yönün şu anki ışık durumunu döner.
     *
     * @param direction Yön
     * @return RED / YELLOW / GREEN
     */
    public LightState getState(Direction direction) {
        return lights.get(direction).getState();
    }

    /**
     * Gelişmiş kullanım veya özel işlemler için tam ışık nesnesini döner.
     *
     * @param direction Yön
     * @return TrafficLight nesnesi
     */
    public TrafficLight getLight(Direction direction) {
        return lights.get(direction);
    }

    /**
     * Tüm ışık nesnelerini döner.
     *
     * @return Map<Direction, TrafficLight>
     */
    public Map<Direction, TrafficLight> getAllLights() {
        return lights;
    }

    /**
     * Bir ışığın yeşil süresini günceller.
     * UYARI: offset’ler yeniden hesaplanmaz! recomputeOffsets() çağrılmalı.
     *
     * @param direction    Güncellenecek yön
     * @param newDuration  Yeni yeşil süresi (saniye)
     */
    public void setGreenDuration(Direction direction, int newDuration) {
        lights.get(direction).setGreenDuration(newDuration);
        // ⚠️ recomputeOffsets() çağrısı unutulmamalı!
    }

    /**
     * (Gelişmiş kullanım) Tüm ışıkların başlangıç sırasını yeniden hesaplar.
     * Eğer yeşil süreler dinamik olarak değiştiyse bu metod çağrılmalıdır.
     */
    public void recomputeOffsets() {
        int offset = 0;
        for (Direction dir : Direction.values()) {
            TrafficLight light = lights.get(dir);
            light.setPhaseOffset(offset); // TrafficLight sınıfında bu setter tanımlı olmalı
            offset += light.getGreenDuration() + TrafficLight.YELLOW_DURATION;
        }
    }

    /**
     * Belirli bir ışığın mevcut döngüde ne kadar süresi kaldığını verir.
     *
     * @param dir İlgili yön
     * @param now Şu anki zaman (nanoTime)
     * @return Kalan süre (saniye cinsinden)
     */
    public long getRemainingTime(Direction dir, long now) {
        TrafficLight light = lights.get(dir);
        return light.getRemainingTime(now, startTime);
    }
}
