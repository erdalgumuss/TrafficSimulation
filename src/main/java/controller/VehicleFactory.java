package controller;

import model.CarModel;
import model.Direction;
import model.Vehicle;
import util.SimConstants;

public class VehicleFactory {

    private static final CarModel[] MODELS = CarModel.values();

    /**
     * Zamanlı olarak tek bir araç üretir.
     * Sabit giriş ekseninden gelir, diğer eksende şerit aralığına göre random pozisyon alır.
     */
    public static Vehicle generateSingleVehicle(Direction direction) {
        double[] entry = SimConstants.ENTRY_POINTS.get(direction);
        double[] laneRange = SimConstants.LANE_CENTER.get(direction);

        double x, y;

        if (direction == Direction.EAST || direction == Direction.WEST) {
            x = entry[0]; // sabit x
            y = randomInRange(laneRange[0], laneRange[1]); // rastgele şerit (y)
        } else {
            x = randomInRange(laneRange[0], laneRange[1]); // rastgele şerit (x)
            y = entry[1]; // sabit y
        }

        CarModel model = randomModel();
        double speed = model.getRandomSpeed();
        return new Vehicle(direction, speed, x, y, model);
    }

    private static CarModel randomModel() {
        int index = (int) (Math.random() * MODELS.length);
        return MODELS[index];
    }

    private static double randomInRange(double min, double max) {
        return min + Math.random() * (max - min);
    }
}
