package view;

import controller.TrafficSimulationManager;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import model.Direction;
import model.LightState;

import java.util.EnumMap;
import java.util.Map;

public class TrafficObserverPanel extends VBox {

    private final Map<Direction, Label> stateLabels = new EnumMap<>(Direction.class);
    private final Map<Direction, Label> durationLabels = new EnumMap<>(Direction.class);
    private final Map<Direction, Label> remainingLabels = new EnumMap<>(Direction.class);

    private TrafficSimulationManager manager;

    public TrafficObserverPanel() {
        setSpacing(10);
        setPadding(new Insets(10));
        setStyle("-fx-background-color: #e8e8e8; -fx-border-color: #ccc; -fx-border-radius: 5;");

        for (Direction dir : Direction.values()) {
            VBox box = createDirectionBox(dir);
            getChildren().add(box);
        }
    }

    private VBox createDirectionBox(Direction dir) {
        Label title = new Label(dir.name());
        title.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        Label stateLabel = new Label("Durum: N/A");
        Label durationLabel = new Label("Yeşil Süre: 0s");
        Label remainingLabel = new Label("Kalan: 0s");

        stateLabels.put(dir, stateLabel);
        durationLabels.put(dir, durationLabel);
        remainingLabels.put(dir, remainingLabel);

        VBox box = new VBox(5, title, stateLabel, durationLabel, remainingLabel);
        box.setPadding(new Insets(8));
        box.setStyle("-fx-background-color: #fefefe; -fx-border-color: #999; -fx-border-radius: 4;");
        return box;
    }

    public void bindToManager(TrafficSimulationManager manager) {
        this.manager = manager;
    }

    public void update() {
        if (manager == null) return;

        long now = System.nanoTime();

        for (Direction dir : Direction.values()) {
            LightState state = manager.getLightState(dir);
            int greenDuration = manager.getGreenDurations().getOrDefault(dir, 0);
            long remaining = manager.getLightController().getRemainingTime(dir, now);

            String stateText = "Durum: " + state.name();
            String durationText = "Yeşil Süre: " + greenDuration + "s";
            String remainingText = "Kalan: " + remaining + "s";

            stateLabels.get(dir).setText(stateText);
            durationLabels.get(dir).setText(durationText);
            remainingLabels.get(dir).setText(remainingText);

            // Renk stilini ayarlamak istersen buradan yapılabilir
            applyColorToState(stateLabels.get(dir), state);
        }
    }

    private void applyColorToState(Label label, LightState state) {
        String color;
        switch (state) {
            case GREEN:  color = "#4CAF50"; break;
            case YELLOW: color = "#FFC107"; break;
            case RED:    color = "#F44336"; break;
            default:     color = "#CCCCCC";
        }
        label.setStyle("-fx-font-weight: bold; -fx-text-fill: " + color + ";");
    }
}
