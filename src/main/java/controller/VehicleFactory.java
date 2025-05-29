package controller;

import model.CarModel;
import model.Direction;
import model.Vehicle;
import util.SimConstants;

/**
 * VehicleFactory sınıfı, simülasyonda yönlere göre araç üretimini yönetir.
 * Araçların rastgele hız, konum ve modele sahip olarak sahneye girişini sağlar.
 */
public class VehicleFactory {

    // Mevcut araç modelleri (enum) önceden tanımlanmış olmalı
    private static final CarModel[] MODELS = CarModel.values();

    /**
     * Belirli bir yönden sahneye yeni bir araç oluşturur.
     * - Giriş pozisyonu SimConstants.ENTRY_POINTS ile belirlenir (sabit).
     * - Şeritler arasında konumlanma SimConstants.LANE_CENTER ile sağlanır.
     * - Araç modeli ve hızı rastgele seçilir.
     *
     * @param direction Hangi yönden araç giriş yapacak
     * @return Oluşturulan yeni Vehicle nesnesi
     */
    public static Vehicle generateSingleVehicle(Direction direction) {
        double[] entry = SimConstants.ENTRY_POINTS.get(direction);     // Sabit giriş noktası (x, y)
        double[] laneRange = SimConstants.LANE_CENTER.get(direction); // Şerit aralığı (min, max)

        double x, y;

        // Yatay yönlerde sabit x, rastgele y (şerit)
        // Dikey yönlerde sabit y, rastgele x (şerit)
        if (direction == Direction.EAST || direction == Direction.WEST) {
            x = entry[0]; // x sabit
            y = randomInRange(laneRange[0], laneRange[1]); // şerit y
        } else {
            x = randomInRange(laneRange[0], laneRange[1]); // şerit x
            y = entry[1]; // y sabit
        }

        CarModel model = randomModel();               // Rastgele araç modeli (boyut/sürat)
        double speed = model.getRandomSpeed();        // Modele göre uygun rastgele hız
        return new Vehicle(direction, speed, x, y, model);
    }

    /**
     * MODELS dizisinden rastgele bir araç modeli seçer.
     * Sedan, SUV, F1, vs. olabilir.
     */
    private static CarModel randomModel() {
        int index = (int) (Math.random() * MODELS.length);
        return MODELS[index];
    }

    /**
     * Belirtilen aralıkta rastgele bir değer üretir.
     *
     * @param min Minimum değer
     * @param max Maksimum değer
     * @return min ve max arasında rastgele double değer
     */
    private static double randomInRange(double min, double max) {
        return min + Math.random() * (max - min);
    }
}
