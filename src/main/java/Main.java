import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import util.SimConstants;
import view.MainScene;
import view.SimulationControlPanel;
import view.TrafficObserverPanel;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Ana sahne (trafik simülasyonu canvas)
        MainScene mainScene = new MainScene();

        // Gözlem paneli (trafik verileri) ve kontrol paneli (start/pause/reset + yoğunluk girişi)
        TrafficObserverPanel observerPanel = new TrafficObserverPanel();
        SimulationControlPanel controlPanel = new SimulationControlPanel(mainScene, observerPanel);

        // Sol dikey panel: kontrol paneli + gözlem paneli üst üste
        VBox leftPanel = new VBox(10); // dikey boşluk
        leftPanel.getChildren().addAll(controlPanel, observerPanel);
        leftPanel.setPrefWidth(SimConstants.PANEL_WIDTH);

        // Ana layout: solda panel, sağda canvas (MainScene)
        HBox root = new HBox(30); // yatay boşluk
        root.getChildren().addAll(leftPanel, mainScene);

        // Sahne boyutunu dinamik olarak hesapla
        double totalWidth = SimConstants.SCENE_WIDTH + SimConstants.PANEL_WIDTH + 40; // + spacing
        double totalHeight = SimConstants.SCENE_HEIGHT;

        Scene scene = new Scene(root, totalWidth, totalHeight);
        scene.getStylesheets().add(getClass().getResource("/app.css").toExternalForm());

        primaryStage.setTitle("Traffic Simulation");
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        primaryStage.centerOnScreen(); // ekran ortasına al

        // Alternatif: pencereyi tam ekran yapmak istersen aşağıdaki satırı aç:
        //primaryStage.setMaximized(true);

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
