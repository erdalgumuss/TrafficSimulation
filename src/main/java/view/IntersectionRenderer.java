package view;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class IntersectionRenderer {

    public static void render(GraphicsContext gc) {
        gc.setFill(Color.DARKGRAY);
        gc.fillRect(300, 0, 200, 600); // dikey yol
        gc.fillRect(0, 250, 800, 100); // yatay yol
    }
}
