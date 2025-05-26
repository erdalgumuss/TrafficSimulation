package controller;

import model.Direction;
import model.Vehicle;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class VehicleSimulationManager {

    private final VehicleController vehicleController;
    private final Direction[] directions = Direction.values();

    private final Map<Direction, Integer> targetCounts;    // Kullanıcının istediği toplam araç sayısı
    private final Map<Direction, Integer> producedCounts;  // Gerçekte kaç araç üretildiği

    private long lastSpawnTime = 0;
    private int spawnIndex = 0;

    public VehicleSimulationManager(Map<Direction, Integer> initialCounts, TrafficLightController lightController) {
        this.vehicleController = new VehicleController(lightController);

        this.targetCounts = new EnumMap<>(initialCounts); // Kullanıcının belirlediği hedefler
        this.producedCounts = new EnumMap<>(Direction.class);

        // Her yön için üretilen araç sayısını sıfırla
        for (Direction dir : directions) {
            producedCounts.put(dir, 0);
        }
    }

    public void update(long now) {
        // Mevcut araçları güncelle
        vehicleController.updateVehicles(now);

        // Her 1 saniyede bir yön için üretim denemesi
        if (now - lastSpawnTime >= 1_000_000_000) {
            Direction dir = directions[spawnIndex % directions.length];

            if (shouldSpawn(dir)) {
                vehicleController.spawnVehicle(dir);
                producedCounts.put(dir, producedCounts.get(dir) + 1);
            }

            spawnIndex++;
            lastSpawnTime = now;
        }
    }

    private boolean shouldSpawn(Direction dir) {
        int produced = producedCounts.getOrDefault(dir, 0);
        int target = targetCounts.getOrDefault(dir, 0);
        return produced < target;
    }

    public List<Vehicle> getVehicles(Direction direction) {
        return vehicleController.getVehicleMap().get(direction);
    }

    public Direction[] getDirections() {
        return directions;
    }
}
