package controller;

import model.Direction;
import model.LightState;
import model.TrafficLight;

import java.util.EnumMap;
import java.util.Map;

public class TrafficLightController {

    private final Map<Direction, TrafficLight> lights;
    private final long simulationStartTime;
    private final long startTime; // ✅ EKLENDİ

    public TrafficLightController(Map<Direction, Integer> greenDurations, long simulationStartTime) {
        this.simulationStartTime = simulationStartTime;
        this.lights = new EnumMap<>(Direction.class);
        this.startTime = simulationStartTime; // ✅ ALINDI

        int offset = 0;

        for (Direction dir : Direction.values()) {
            int green = greenDurations.getOrDefault(dir, 30); // varsayılan yeşil süresi
            lights.put(dir, new TrafficLight(dir, green, offset));
            offset += green + TrafficLight.YELLOW_DURATION;
        }
    }

    /**
     * Tüm ışıkları günceller (her frame çağrılır).
     */
    public void update(long now) {
        for (TrafficLight light : lights.values()) {
            light.update(now, simulationStartTime);
        }
    }

    /**
     * Belirli bir yönün anlık ışık durumunu döner.
     */
    public LightState getState(Direction direction) {
        return lights.get(direction).getState();
    }

    /**
     * Gelişmiş kullanım için tam ışık nesnesini verir.
     */
    public TrafficLight getLight(Direction direction) {
        return lights.get(direction);
    }

    /**
     * Tüm ışık nesnelerine erişim sağlar.
     */
    public Map<Direction, TrafficLight> getAllLights() {
        return lights;
    }

    /**
     * Belirli bir yön için yeşil süresini günceller.
     * NOT: Phase offset güncellenmez. Dinamik destek için recomputeOffsets() eklenmeli.
     */
    public void setGreenDuration(Direction direction, int newDuration) {
        lights.get(direction).setGreenDuration(newDuration);
        // ⚠️ recomputeOffsets() çağrısı gerekebilir!
    }

    /**
     * (İLERİDE) Yeşil süreler değiştiğinde tüm ışıkların offsetlerini yeniden hesaplar.
     */
    public void recomputeOffsets() {
        int offset = 0;
        for (Direction dir : Direction.values()) {
            TrafficLight light = lights.get(dir);
            light.setPhaseOffset(offset); // Bu setter TrafficLight içinde eklenmeli
            offset += light.getGreenDuration() + TrafficLight.YELLOW_DURATION;
        }
    }
    public long getRemainingTime(Direction dir, long now) {
        TrafficLight light = lights.get(dir);
        return light.getRemainingTime(now, startTime);
    }

}
