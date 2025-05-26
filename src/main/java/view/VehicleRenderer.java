package view;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import model.CarModel;
import model.Direction;
import model.Vehicle;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VehicleRenderer {

    private static final Map<CarModel, Image> imageMap = new HashMap<>();

    static {
        for (CarModel model : CarModel.values()) {
            String path = "/png/" + model.getImageFile();
            try {
                imageMap.put(model, new Image(VehicleRenderer.class.getResourceAsStream(path)));
            } catch (Exception e) {
                System.err.println("Görsel yüklenemedi: " + path);
                imageMap.put(model, null);
            }
        }
    }

    public static void renderVehicles(GraphicsContext gc, List<Vehicle> vehicles) {
        for (Vehicle v : vehicles) {
            CarModel model = v.getModel();
            Image img = imageMap.get(model);

            if (img == null) {
                gc.setFill(Color.GRAY);
                gc.fillRect(v.getX(), v.getY(), v.getWidth(), v.getHeight());
                continue;
            }

            double totalRotation = model.getBaseRotation() + directionToRotation(v.getDirection());

            gc.save();

            double centerX = v.getX() + v.getWidth() / 2;
            double centerY = v.getY() + v.getHeight() / 2;

            gc.translate(centerX, centerY);
            gc.rotate(totalRotation);
            gc.drawImage(img, -v.getWidth() / 2, -v.getHeight() / 2, v.getWidth(), v.getHeight());

            gc.restore();
        }
    }

    private static double directionToRotation(Direction dir) {
        switch (dir) {
            case EAST:  return 0;
            case NORTH: return -90;
            case WEST:  return 180;
            case SOUTH: return 90;
            default:    return 0;
        }
    }
}
