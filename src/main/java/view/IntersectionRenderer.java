package view;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * IntersectionRenderer:
 * Kavşak (intersection) sahnesinin arka planını çizen yardımcı sınıftır.
 * Genelde ilk çizilen öğedir, üzerine diğer grafik bileşenler (araçlar, ışıklar vb.) eklenir.
 */
public class IntersectionRenderer {

    // Kavşak arka plan görseli (proje içinden mutlak dosya yolu ile alınır)
    private static final Image backgroundImage = new Image("file:src/main/java/view/assets/intersection.jpg");

    /**
     * Arka plan görüntüsünü canvas'a çizer.
     * @param gc JavaFX GraphicsContext nesnesi (çizim arayüzü)
     */
    public static void render(GraphicsContext gc) {
        gc.drawImage(backgroundImage, 0, 0, 800, 600); // Resmi sahne boyutuna göre yerleştir

        // (Opsiyonel) Eğer resim yerine dinamik yol çizimi:
        // gc.setFill(Color.DARKGRAY);
        // gc.fillRect(300, 0, 200, 600);  // Dikey yol alanı
        // gc.fillRect(0, 250, 800, 100);  // Yatay yol alanı
    }
}
