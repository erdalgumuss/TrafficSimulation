package view;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import model.Direction;
import model.LightState;
import controller.TrafficLightController;
import util.SimConstants;

/**
 * TrafficLightRenderer:
 * Kavşakta her yön için trafik lambalarını grafiksel olarak çizer.
 * Her lamba, 3 daireden (Kırmızı, Sarı, Yeşil) oluşan bir ışık kutusu ile temsil edilir.
 */
public class TrafficLightRenderer {

    /**
     * Tüm yönlerdeki trafik lambalarını çizer.
     * @param gc JavaFX GraphicsContext
     * @param controller Işıkların durumunu sağlayan kontrolcü
     */
    public static void render(GraphicsContext gc, TrafficLightController controller) {
        for (Direction dir : Direction.values()) {
            LightState state = controller.getState(dir);
            double[] pos = SimConstants.LIGHT_POSITIONS.get(dir); // Bu yön için lamba konumu

            drawThreeLight(gc, pos[0], pos[1], state); // Işık kutusunu çiz
        }
    }

    /**
     * Üç ampulden (daire) oluşan bir trafik ışığını çizer.
     */
    private static void drawThreeLight(GraphicsContext gc, double x, double y, LightState state) {
        // Arka plan (ışık kutusu) – dekoratif
        gc.setFill(Color.DARKGRAY);
        gc.fillRect(x, y, 20, 60); // 3 daire yüksekliğinde dikdörtgen

        // Kırmızı → Sarı → Yeşil sıralı şekilde daireleri yerleştir
        drawCircle(gc, x + 3, y + 3, getColor(state, LightState.RED));       // Üst: Kırmızı
        drawCircle(gc, x + 3, y + 23, getColor(state, LightState.YELLOW));   // Orta: Sarı
        drawCircle(gc, x + 3, y + 43, getColor(state, LightState.GREEN));    // Alt: Yeşil
    }

    /**
     * Tek bir ışık dairesi çizer.
     */
    private static void drawCircle(GraphicsContext gc, double x, double y, Color color) {
        gc.setFill(color);
        gc.fillOval(x, y, 14, 14); // Dolu daire

        gc.setStroke(Color.BLACK); // Kenarlık
        gc.setLineWidth(1);
        gc.strokeOval(x, y, 14, 14);
    }

    /**
     * Belirli bir hedef ışığın aktif olup olmadığını kontrol eder.
     * Aktif değilse soluk (gri) renk verir.
     */
    private static Color getColor(LightState current, LightState target) {
        return current == target ? getActiveColor(target) : Color.LIGHTGRAY;
    }

    /**
     * Her ışık durumu için aktif renk tanımı
     */
    private static Color getActiveColor(LightState state) {
        switch (state) {
            case RED: return Color.RED;
            case YELLOW: return Color.GOLD;
            case GREEN: return Color.LIMEGREEN;
            default: return Color.TRANSPARENT;
        }
    }
}
