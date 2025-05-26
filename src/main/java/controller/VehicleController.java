package controller;

import model.Direction;
import model.LightState;
import model.Vehicle;

import java.util.*;

public class VehicleController {
    private final Map<Direction, List<Vehicle>> vehicleMap;
    private final TrafficLightController lightController;

    public VehicleController(TrafficLightController lightController) {
        this.lightController = lightController;
        vehicleMap = new EnumMap<>(Direction.class);
        for (Direction dir : Direction.values()) {
            vehicleMap.put(dir, new ArrayList<>());
        }
    }


    public void updateVehicles(long now) {
        for (Direction dir : Direction.values()) {
            List<Vehicle> list = vehicleMap.get(dir);
            for (int i = 0; i < list.size(); i++) {
                Vehicle current = list.get(i);
                boolean canMove = true;

                if (i > 0) {
                    Vehicle front = list.get(i - 1);
                    canMove = current.distanceTo(front) > 35;
                }

                LightState lightState = lightController.getState(dir);

                if (current.approachingIntersection() && lightState != LightState.GREEN) {
                    canMove = false;
                }

                current.updatePosition(canMove, now);
            }
            list.removeIf(v -> !v.isActive());
        }
    }

    public void spawnVehicle(Direction direction) {
        Vehicle newVehicle = VehicleFactory.generateSingleVehicle(direction);
        vehicleMap.get(direction).add(newVehicle);
    }

    public Map<Direction, List<Vehicle>> getVehicleMap() {
        return vehicleMap;
    }
}
