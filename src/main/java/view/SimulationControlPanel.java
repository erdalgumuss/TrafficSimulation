package view;

import controller.TrafficSimulationManager;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import model.Direction;

import java.util.EnumMap;
import java.util.Map;

public class SimulationControlPanel extends VBox {

    private final Map<Direction, TextField> inputFields = new EnumMap<>(Direction.class);
    private final MainScene mainScene;
    private final TrafficObserverPanel observerPanel;

    private final Slider maxRandomSlider = new Slider(1, 50, 10); // was 20 → now 50
    private final Label maxLabel = new Label("Max: 50");

    public SimulationControlPanel(MainScene mainScene, TrafficObserverPanel observerPanel) {
        this.mainScene = mainScene;
        this.observerPanel = observerPanel;

        setSpacing(10);
        setPadding(new Insets(10));
        setStyle("-fx-background-color: #f0f0f0;");

        Label title = new Label("Vehicle Density Input");

        GridPane grid = new GridPane();
        grid.setVgap(5);
        grid.setHgap(10);

        int row = 0;
        for (Direction dir : Direction.values()) {
            Label label = new Label(dir.name() + ":");
            TextField field = new TextField("0");
            inputFields.put(dir, field);
            grid.add(label, 0, row);
            grid.add(field, 1, row++);
        }

        // Slider ve label
        maxRandomSlider.setShowTickLabels(true);
        maxRandomSlider.setShowTickMarks(true);
        maxRandomSlider.setMajorTickUnit(10);
        maxRandomSlider.setMinorTickCount(4);
        maxRandomSlider.setSnapToTicks(true);
        maxRandomSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            int max = newVal.intValue();
            maxLabel.setText("Max: " + max);
        });

        HBox sliderBox = new HBox(10, new Label("Random Max:"), maxRandomSlider, maxLabel);

        // Butonlar
        Button startBtn = new Button("Start");
        Button pauseBtn = new Button("Pause");
        Button resetBtn = new Button("Reset");
        Button randomBtn = new Button("Randomize");

        startBtn.setOnAction(e -> handleStart());

        pauseBtn.setOnAction(e -> {
            mainScene.togglePause();
            pauseBtn.setText(mainScene.isPaused() ? "Resume" : "Pause");
        });

        resetBtn.setOnAction(e -> {
            mainScene.resetSimulation();
            System.out.println("Simülasyon sıfırlandı.");
        });

        randomBtn.setOnAction(e -> {
            int max = (int) maxRandomSlider.getValue();
            for (Direction dir : Direction.values()) {
                int value = (int) (Math.random() * (max + 1)); // 0–max
                inputFields.get(dir).setText(String.valueOf(value));
            }
        });

        HBox buttons = new HBox(10, startBtn, pauseBtn, resetBtn, randomBtn);
        buttons.setPadding(new Insets(10, 0, 0, 0));

        this.getChildren().addAll(title, grid, sliderBox, buttons);
    }

    private void handleStart() {
        Map<Direction, Integer> counts = new EnumMap<>(Direction.class);
        for (Direction dir : Direction.values()) {
            try {
                int count = Integer.parseInt(inputFields.get(dir).getText());
                counts.put(dir, Math.max(0, count));
            } catch (NumberFormatException ex) {
                counts.put(dir, 0);
            }
        }

        System.out.println("Kullanıcıdan gelen girişler: " + counts);

        TrafficSimulationManager manager = new TrafficSimulationManager(counts);
        mainScene.startSimulation(manager);

        observerPanel.bindToManager(manager);
        mainScene.setObserverPanel(observerPanel);
    }
}
