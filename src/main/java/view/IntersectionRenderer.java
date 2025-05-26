package view;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class IntersectionRenderer {

    private static final Image backgroundImage = new Image("file:src/main/java/view/assets/intersection.jpg");

    public static void render(GraphicsContext gc) {
        gc.drawImage(backgroundImage, 0, 0, 800, 600); // Arka plan resmi

        // (İstersen aşağıdaki yolları da üstüne çizebilirsin)
        // gc.setFill(Color.DARKGRAY);
        // gc.fillRect(300, 0, 200, 600); // dikey yol
        // gc.fillRect(0, 250, 800, 100); // yatay yol
    }
}
