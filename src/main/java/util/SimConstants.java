package util;

import model.Direction;

import java.util.EnumMap;
import java.util.Map;

public class SimConstants {

    // 🧭 Kavşağa girişte bekleme koordinatları (hayali ışık noktaları)
    public static final Map<Direction, Double> LIGHT_BOUNDARIES = new EnumMap<>(Direction.class);
    static {
        LIGHT_BOUNDARIES.put(Direction.EAST, 250.0);   // x koordinatı
        LIGHT_BOUNDARIES.put(Direction.WEST, 550.0);
        LIGHT_BOUNDARIES.put(Direction.NORTH, 400+.0);  // y koordinatı
        LIGHT_BOUNDARIES.put(Direction.SOUTH, 180.0);
    }

    // ⏱️ Araç sabitleri
    public static final double MIN_DISTANCE_BETWEEN_VEHICLES = 35.0;
    public static final long WAIT_DURATION_MS = 3000;

    // 🚗 Araç sabitleri
    public static final double VEHICLE_WIDTH = 30;
    public static final double VEHICLE_HEIGHT = 15;
    public static final double VEHICLE_GAP = 40;

    // 🖼️ Sahne boyutu
    public static final double SCENE_WIDTH = 800;
    public static final double SCENE_HEIGHT = 750;

    public static final double PANEL_WIDTH = 320; // Sol panel genişliği

    // 🚥 Işık süresi (ileride override edilebilir)
    public static final int DEFAULT_GREEN_DURATION = 30;
    public static final int DEFAULT_YELLOW_DURATION = 3;

    // ➖ Şerit merkezleri (araç yerleşimi için)
    public static final Map<Direction, double[]> LANE_CENTER = new EnumMap<>(Direction.class);
    static {
        LANE_CENTER.put(Direction.EAST, new double[]{295, 310});   // y aralığı
        LANE_CENTER.put(Direction.WEST, new double[]{225, 240});
        LANE_CENTER.put(Direction.NORTH, new double[]{410, 430});  // x aralığı
        LANE_CENTER.put(Direction.SOUTH, new double[]{340, 360});
    }
    // Şerit girişleri araç doğması için
    public static final Map<Direction, double[]> ENTRY_POINTS = new EnumMap<>(Direction.class);
    static {
        ENTRY_POINTS.put(Direction.EAST, new double[]{-50, 320});        // x, y
        ENTRY_POINTS.put(Direction.WEST, new double[]{850, 270});
        ENTRY_POINTS.put(Direction.NORTH, new double[]{420, 650});
        ENTRY_POINTS.put(Direction.SOUTH, new double[]{360, -50});
    }
    public static final Map<Direction, double[]> LIGHT_POSITIONS = new EnumMap<>(Direction.class);
    static {
        LIGHT_POSITIONS.put(Direction.NORTH, new double[]{550, 400}); // x, y
        LIGHT_POSITIONS.put(Direction.SOUTH, new double[]{235, 130});
        LIGHT_POSITIONS.put(Direction.EAST,  new double[]{235, 400});
        LIGHT_POSITIONS.put(Direction.WEST,  new double[]{550, 130});
    }

}
