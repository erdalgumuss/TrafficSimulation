package view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import util.SimConstants;

public class Main extends Application {

    private double xOffset = 0;
    private double yOffset = 0;

    @Override
    public void start(Stage primaryStage) {
        // 🚗 Ana simülasyon sahnesi
        MainScene mainScene = new MainScene();

        // 🎛️ Kontrol ve gözlem paneli
        TrafficObserverPanel observerPanel = new TrafficObserverPanel();
        SimulationControlPanel controlPanel = new SimulationControlPanel(mainScene, observerPanel);

        VBox leftPanel = new VBox(10);
        leftPanel.getChildren().addAll(controlPanel, observerPanel);
        leftPanel.setPrefWidth(SimConstants.PANEL_WIDTH);
        leftPanel.getStyleClass().add("panel");

        HBox content = new HBox(30, leftPanel, mainScene);
        content.setStyle("-fx-background-color: transparent;");
        content.setPrefSize(SimConstants.SCENE_WIDTH + SimConstants.PANEL_WIDTH + 40, SimConstants.SCENE_HEIGHT);

        // 🪟 Özel başlık çubuğu (drag + close buton)
        CustomTitleBar titleBar = new CustomTitleBar(primaryStage);

        // 🔲 Tüm pencere yapısı: üstte başlık, altta içerik
        VBox root = new VBox();
        root.getChildren().addAll(titleBar, content);
        root.getStyleClass().add("root");

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT); // arka plan cam gibi olsun istiyorsak
        scene.getStylesheets().add(getClass().getResource("/app.css").toExternalForm());

        // ✅ Uygulama ikonu (resources > assets > app_icon.png içinde olmalı)
        Image icon = new Image(getClass().getResourceAsStream("/png/sedan_Car.png"));
        primaryStage.setTitle("🚦 TrafficSimulation - Akıllı Kavşak Sistemi");

        primaryStage.getIcons().add(icon);

        primaryStage.getIcons().add(icon);

        // Sistem çerçevesini kaldır, modern görünüm için
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }
}
