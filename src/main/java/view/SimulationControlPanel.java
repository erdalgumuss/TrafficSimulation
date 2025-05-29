package view;

import controller.TrafficSimulationManager;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import model.Direction;

import java.util.EnumMap;
import java.util.Map;

/**
 * SimulationControlPanel:
 * Kullanıcının araç sayılarını girmesini sağlayan ve simülasyonu kontrol eden paneldir.
 * - Start, Pause, Reset ve Randomize işlevleri içerir.
 * - Ayrıca maksimum rastgele üretim için slider içerir.
 */
public class SimulationControlPanel extends VBox {

    private final Map<Direction, TextField> inputFields = new EnumMap<>(Direction.class); // Her yön için giriş kutusu
    private final MainScene mainScene;                   // Ana sahne referansı
    private final TrafficObserverPanel observerPanel;    // Bilgilendirme paneli

    private final Slider maxRandomSlider = new Slider(1, 50, 10); // Rastgele üretim için maksimum değer
    private final Label maxLabel = new Label("Max: 50");

    /**
     * Kontrol paneli kurucu metodu
     */
    public SimulationControlPanel(MainScene mainScene, TrafficObserverPanel observerPanel) {
        this.mainScene = mainScene;
        this.observerPanel = observerPanel;

        // Panel için stil sınıfı atanıyor
        this.getStyleClass().add("simulation-control-panel");

        // Başlık etiketi
        Label title = new Label("🚦 Araç Yoğunluğu Girişi");
        title.getStyleClass().add("panel-title");

        // Araç sayısı girişleri için grid yapı
        GridPane grid = new GridPane();
        grid.setVgap(5);
        grid.setHgap(10);

        int row = 0;
        for (Direction dir : Direction.values()) {
            Label label = new Label(dir.name() + ":");
            label.getStyleClass().add("input-label");

            TextField tf = new TextField("0");
            tf.getStyleClass().add("input-field");

            inputFields.put(dir, tf);
            grid.add(label, 0, row);
            grid.add(tf, 1, row++);
        }

        // Slider etiketleri ve görünümü
        Label sliderTitle = new Label("Rastgele Maks:");
        sliderTitle.getStyleClass().add("input-label");
        maxLabel.getStyleClass().add("input-label");

        maxRandomSlider.setShowTickLabels(true);
        maxRandomSlider.setShowTickMarks(true);
        maxRandomSlider.setMajorTickUnit(10);
        maxRandomSlider.setMinorTickCount(4);
        maxRandomSlider.setSnapToTicks(true);
        maxRandomSlider.getStyleClass().add("custom-slider");

        maxRandomSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            int max = newVal.intValue();
            maxLabel.setText("Max: " + max);
        });

        HBox sliderBox = new HBox(10, sliderTitle, maxRandomSlider, maxLabel);

        // Kontrol butonları
        Button startBtn = new Button(" ▶ ");
        Button pauseBtn = new Button(" ⏸ ");
        Button resetBtn = new Button(" ⟳ ");
        Button randomBtn = new Button(" 🎲 ");

        for (Button btn : new Button[]{startBtn, pauseBtn, resetBtn, randomBtn}) {
            btn.getStyleClass().add("control-button");
        }

        // Başlat butonu → simülasyonu kullanıcı verileriyle başlat
        startBtn.setOnAction(e -> handleStart());

        // Pause → toggle pause/resume
        pauseBtn.setOnAction(e -> {
            mainScene.togglePause();
            pauseBtn.setText(mainScene.isPaused() ? "▶ Resume" : "⏸ Pause");
        });

        // Reset → simülasyonu sıfırla
        resetBtn.setOnAction(e -> {
            mainScene.resetSimulation();
            System.out.println("Simülasyon sıfırlandı.");
        });

        // Randomize → her yöne rastgele sayı ata (slider'a göre)
        randomBtn.setOnAction(e -> {
            int max = (int) maxRandomSlider.getValue();
            for (Direction dir : Direction.values()) {
                int value = (int) (Math.random() * (max + 1)); // 0–max
                inputFields.get(dir).setText(String.valueOf(value));
            }
        });

        // Butonlar yatay kutusu
        HBox buttons = new HBox(10, startBtn, pauseBtn, resetBtn, randomBtn);
        buttons.setPadding(new Insets(10, 0, 0, 0));

        // Bileşenleri panele ekle
        this.getChildren().addAll(title, grid, sliderBox, buttons);
    }

    /**
     * Başlat butonuna basıldığında çağrılır.
     * - TextField'lardan verileri toplar
     * - Yeni bir TrafficSimulationManager oluşturur
     * - Simülasyonu başlatır
     * - Observer panelini bağlar
     */
    private void handleStart() {
        Map<Direction, Integer> counts = new EnumMap<>(Direction.class);

        for (Direction dir : Direction.values()) {
            try {
                int count = Integer.parseInt(inputFields.get(dir).getText());
                counts.put(dir, Math.max(0, count));
            } catch (NumberFormatException ex) {
                counts.put(dir, 0); // Hatalı giriş için varsayılan 0
            }
        }

        System.out.println("Kullanıcıdan gelen girişler: " + counts);

        TrafficSimulationManager manager = new TrafficSimulationManager(counts);
        mainScene.startSimulation(manager);

        observerPanel.bindToManager(manager);
        mainScene.setObserverPanel(observerPanel);
    }
}
