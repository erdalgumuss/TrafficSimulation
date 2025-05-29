package view;

import controller.TrafficSimulationManager;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
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
        setSpacing(12);
        setPadding(new Insets(10));
        getStyleClass().add("observer-panel"); // CSS sınıfı

        for (Direction dir : Direction.values()) {
            VBox card = createDirectionCard(dir);
            getChildren().add(card);
        }
    }

    private VBox createDirectionCard(Direction dir) {
        Label title = new Label(dir.name());
        title.getStyleClass().add("observer-title");

        Label stateLabel = new Label("Durum: N/A");
        Label durationLabel = new Label("Yeşil Süre: 0s");
        Label remainingLabel = new Label("Kalan: 0s");

        stateLabels.put(dir, stateLabel);
        durationLabels.put(dir, durationLabel);
        remainingLabels.put(dir, remainingLabel);

        for (Label label : new Label[]{stateLabel, durationLabel, remainingLabel}) {
            label.getStyleClass().add("observer-label");
        }

        VBox card = new VBox(5, title, stateLabel, durationLabel, remainingLabel);
        card.getStyleClass().add("traffic-card");
        return card;
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

            stateLabels.get(dir).setText("Durum: " + state.name());
            durationLabels.get(dir).setText("Yeşil Süre: " + greenDuration + "s");
            remainingLabels.get(dir).setText("Kalan: " + remaining + "s");

            applyColorToState(stateLabels.get(dir), state);
        }
    }

    private void applyColorToState(Label label, LightState state) {
        label.getStyleClass().removeIf(c -> c.startsWith("light-"));
        switch (state) {
            case GREEN -> label.getStyleClass().add("light-green");
            case YELLOW -> label.getStyleClass().add("light-yellow");
            case RED -> label.getStyleClass().add("light-red");
        }
    }
}
