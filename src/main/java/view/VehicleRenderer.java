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

/**
 * VehicleRenderer:
 * Simülasyondaki tüm araçların sahneye çizilmesini sağlar.
 * Araçların yönlerine göre döndürülmesi ve modellerine göre görsellerle temsil edilmesi desteklenir.
 */
public class VehicleRenderer {

    // Araç modeli → Görsel eşlemesi
    private static final Map<CarModel, Image> imageMap = new HashMap<>();

    // Tüm araç modelleri için görselleri yükle
    static {
        for (CarModel model : CarModel.values()) {
            String path = "/png/" + model.getImageFile(); // model'e ait görsel dosyası
            try {
                imageMap.put(model, new Image(VehicleRenderer.class.getResourceAsStream(path)));
            } catch (Exception e) {
                System.err.println("Görsel yüklenemedi: " + path);
                imageMap.put(model, null); // Görsel bulunamazsa null koy
            }
        }
    }

    /**
     * Araçları sahneye çizer.
     * @param gc JavaFX GraphicsContext
     * @param vehicles Yönü ve modeli belli olan araç listesi
     */
    public static void renderVehicles(GraphicsContext gc, List<Vehicle> vehicles) {
        for (Vehicle v : vehicles) {
            CarModel model = v.getModel();
            Image img = imageMap.get(model);

            // Görsel yoksa varsayılan bir gri dikdörtgen çiz
            if (img == null) {
                gc.setFill(Color.GRAY);
                gc.fillRect(v.getX(), v.getY(), v.getWidth(), v.getHeight());
                continue;
            }

            // Modelin kendi açısı ve yönüne göre toplam dönüş
            double totalRotation = model.getBaseRotation() + directionToRotation(v.getDirection());

            gc.save(); // Canvas durumunu kaydet

            // Rotasyon için merkez noktası
            double centerX = v.getX() + v.getWidth() / 2;
            double centerY = v.getY() + v.getHeight() / 2;

            gc.translate(centerX, centerY); // Merkez noktaya taşı
            gc.rotate(totalRotation);       // Aracı döndür
            gc.drawImage(img, -v.getWidth() / 2, -v.getHeight() / 2, v.getWidth(), v.getHeight());

            gc.restore(); // Önceki canvas durumuna geri dön
        }
    }

    /**
     * Her yön için uygun rotasyon açısını döner (derece cinsinden).
     * EAST → 0°, NORTH → -90°, WEST → 180°, SOUTH → 90°
     */
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
