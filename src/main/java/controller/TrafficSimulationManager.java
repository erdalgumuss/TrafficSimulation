package controller;

import model.Direction;
import model.LightState;
import model.Vehicle;
import util.TrafficSignalCalculator;

import java.util.List;
import java.util.Map;

public class TrafficSimulationManager {

    private final VehicleSimulationManager vehicleManager;
    private final TrafficLightController lightController;

    private final Map<Direction, Integer> vehicleCounts;
    private final long simulationStartTime;

    public TrafficSimulationManager(Map<Direction, Integer> initialVehicleCounts) {
        this.vehicleCounts = initialVehicleCounts;
        this.simulationStartTime = System.nanoTime();

        // 1. Işık sürelerini hesapla
        Map<Direction, Integer> greenDurations =
                TrafficSignalCalculator.calculateGreenDurations(vehicleCounts);

        // 2. Işık kontrolcüsünü oluştur
        this.lightController = new TrafficLightController(greenDurations, simulationStartTime);

        // 3. Araç yöneticisini ışık kontrolcüsüyle birlikte başlat
        this.vehicleManager = new VehicleSimulationManager(vehicleCounts, this.lightController);
    }

    public void update(long now) {
        lightController.update(now);
        vehicleManager.update(now);
    }

    public LightState getLightState(Direction direction) {
        return lightController.getState(direction);
    }

    public List<Vehicle> getVehicles(Direction direction) {
        return vehicleManager.getVehicles(direction);
    }

    public Direction[] getDirections() {
        return vehicleManager.getDirections();
    }

    public TrafficLightController getLightController() {
        return lightController;
    }

    public Map<Direction, Integer> getGreenDurations() {
        return lightController.getAllLights().entrySet().stream()
                .collect(java.util.stream.Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue().getGreenDuration()
                ));
    }
}
