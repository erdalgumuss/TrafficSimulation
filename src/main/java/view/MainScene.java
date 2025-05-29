package view;

import controller.TrafficSimulationManager;
import model.Direction;
import model.Vehicle;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.animation.AnimationTimer;
import javafx.scene.paint.Color;

import java.util.List;

/**
 * MainScene:
 * Trafik simülasyonunun görsel arayüzünü çizen ve yöneten ana sahne sınıfıdır.
 * - Araçların ve ışıkların çizimi burada gerçekleşir.
 * - Simülasyonu başlatır, duraklatır ve sıfırlar.
 */
public class MainScene extends Pane {

    private boolean isPaused = false;                      // Simülasyon duraklatıldı mı?
    private TrafficObserverPanel observerPanel;            // Bilgi paneli (opsiyonel)

    private final Canvas canvas;                           // Çizim alanı
    private final GraphicsContext gc;                      // Çizim için grafik konteksti
    private TrafficSimulationManager simulationManager;    // Simülasyon yöneticisi

    private AnimationTimer timer;                          // Frame-by-frame animasyon zamanlayıcısı

    /**
     * Ana sahne oluşturulur, canvas başlatılır ve sahneye eklenir.
     */
    public MainScene() {
        canvas = new Canvas(800, 600); // Sabit boyutlu sahne
        gc = canvas.getGraphicsContext2D();
        this.getChildren().add(canvas);
        IntersectionRenderer.render(gc); // Kavşak yapısı

        // Pane arka plan rengi modern dark ton
        this.setStyle("-fx-background-color: #1E1E2F;");
    }

    /**
     * Simülasyonu başlatır ve zamanlayıcıyı devreye sokar.
     *
     * @param manager Trafik simülasyon yöneticisi
     */
    public void startSimulation(TrafficSimulationManager manager) {
        this.simulationManager = manager;

        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (!isPaused && simulationManager != null) {
                    simulationManager.update(now); // her karede güncelle
                    draw();                         // çizimleri yap
                    if (observerPanel != null) observerPanel.update(); // paneli güncelle
                }
            }
        };
        timer.start();
        isPaused = false;
    }

    /**
     * Simülasyonu geçici olarak duraklatır veya devam ettirir (toggle).
     */
    public void togglePause() {
        isPaused = !isPaused;
    }

    /**
     * Simülasyonun şu an duraklatılmış olup olmadığını döner.
     */
    public boolean isPaused() {
        return isPaused;
    }

    /**
     * Simülasyonu sıfırlar:
     * - Animasyonu durdurur
     * - Simülasyon yöneticisini temizler
     * - Başlangıç sahnesini çizer
     */
    public void resetSimulation() {
        if (timer != null) {
            timer.stop();
        }
        this.simulationManager = null;
        isPaused = false;
        drawInitialScene();
    }

    /**
     * Simülasyondaki tüm varlıkları (araç, ışık, kavşak) çizer.
     */
    private void draw() {
        gc.setFill(Color.web("#1E1E2F")); // Dark arka plan
        gc.fillRect(0, 0, 800, 600);

        if (simulationManager == null) return;

        IntersectionRenderer.render(gc); // Kavşak yapısı

        // Her yöndeki araçları çiz
        for (Direction dir : simulationManager.getDirections()) {
            List<Vehicle> vehicles = simulationManager.getVehicles(dir);
            VehicleRenderer.renderVehicles(gc, vehicles);
        }

        // Trafik ışıklarını çiz
        TrafficLightRenderer.render(gc, simulationManager.getLightController());
    }

    /**
     * Simülasyon henüz başlamamışken sadece kavşağı gösteren ilk ekranı çizer.
     */
    private void drawInitialScene() {
        gc.setFill(Color.web("#1E1E2F"));
        gc.fillRect(0, 0, 800, 600);
        IntersectionRenderer.render(gc); // Sadece yollar ve şeritler
    }

    /**
     * Durum panelini (gözlemci) sahneye bağlar.
     *
     * @param panel Gözlem paneli
     */
    public void setObserverPanel(TrafficObserverPanel panel) {
        this.observerPanel = panel;
    }
}
