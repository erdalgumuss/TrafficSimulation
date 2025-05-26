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

            drawLight(gc, pos[0], pos[1], state);
        }
    }

    private static void drawLight(GraphicsContext gc, double x, double y, LightState state) {
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);
        gc.strokeRect(x, y, 20, 20); // dış kutu

        switch (state) {
            case RED:
                gc.setFill(Color.RED); break;
            case YELLOW:
                gc.setFill(Color.YELLOW); break;
            case GREEN:
                gc.setFill(Color.LIMEGREEN); break;
        }

        gc.fillOval(x + 3, y + 3, 14, 14); // ışığın kendisi
    }
}
