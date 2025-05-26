package view;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import model.Direction;
import model.LightState;
import controller.TrafficLightController;
import util.SimConstants;

public class TrafficLightRenderer {

    public static void render(GraphicsContext gc, TrafficLightController controller) {
        for (Direction dir : Direction.values()) {
            LightState state = controller.getState(dir);
            double[] pos = SimConstants.LIGHT_POSITIONS.get(dir);

            drawThreeLight(gc, pos[0], pos[1], state);
        }
    }

    private static void drawThreeLight(GraphicsContext gc, double x, double y, LightState state) {
        // Işık kutusu çizimi (isteğe bağlı)
        gc.setFill(Color.DARKGRAY);
        gc.fillRect(x, y, 20, 60); // 3 lambaya uygun dikdörtgen

        // Her ampulün konumu (3 daire)
        drawCircle(gc, x + 3, y + 3, getColor(state, LightState.RED));
        drawCircle(gc, x + 3, y + 23, getColor(state, LightState.YELLOW));
        drawCircle(gc, x + 3, y + 43, getColor(state, LightState.GREEN));
    }

    private static void drawCircle(GraphicsContext gc, double x, double y, Color color) {
        gc.setFill(color);
        gc.fillOval(x, y, 14, 14);

        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);
        gc.strokeOval(x, y, 14, 14);
    }

    private static Color getColor(LightState current, LightState target) {
        return current == target ? getActiveColor(target) : Color.LIGHTGRAY;
    }

    private static Color getActiveColor(LightState state) {
        switch (state) {
            case RED: return Color.RED;
            case YELLOW: return Color.GOLD;
            case GREEN: return Color.LIMEGREEN;
            default: return Color.TRANSPARENT;
        }
    }
}
