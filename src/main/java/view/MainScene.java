package view;

import controller.TrafficSimulationManager;
import model.Direction;
import model.Vehicle;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.animation.AnimationTimer;

import java.util.List;

public class MainScene extends Pane {
    private boolean isPaused = false;
    private TrafficObserverPanel observerPanel;

    private final Canvas canvas;
    private final GraphicsContext gc;
    private TrafficSimulationManager simulationManager;

    private AnimationTimer timer;

    public MainScene() {
        canvas = new Canvas(800, 600);
        gc = canvas.getGraphicsContext2D();
        this.getChildren().add(canvas);
    }

    public void startSimulation(TrafficSimulationManager manager) {
        this.simulationManager = manager;

        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (!isPaused && simulationManager != null) {
                    simulationManager.update(now);
                    draw();
                    if (observerPanel != null) observerPanel.update();
                }
            }
        };
        timer.start();
        isPaused = false;
    }

    public void togglePause() {
        isPaused = !isPaused;
    }

    public boolean isPaused() {
        return isPaused;
    }

    public void resetSimulation() {
        if (timer != null) {
            timer.stop();
        }
        this.simulationManager = null;
        isPaused = false;
        drawInitialScene();
    }

    private void draw() {
        gc.setFill(javafx.scene.paint.Color.LIGHTGRAY);
        gc.fillRect(0, 0, 800, 600);

        if (simulationManager == null) return;

        IntersectionRenderer.render(gc);

        for (Direction dir : simulationManager.getDirections()) {
            List<Vehicle> vehicles = simulationManager.getVehicles(dir);
            VehicleRenderer.renderVehicles(gc, vehicles);
        }
        TrafficLightRenderer.render(gc, simulationManager.getLightController());

        // Gelecekte:
        // LightRenderer.render(gc, simulationManager.getLights());
    }
    private void drawInitialScene() {
        gc.setFill(javafx.scene.paint.Color.LIGHTGRAY);
        gc.fillRect(0, 0, 800, 600);
        IntersectionRenderer.render(gc);
    }


    public void setObserverPanel(TrafficObserverPanel panel) {
        this.observerPanel = panel;
    }

}
