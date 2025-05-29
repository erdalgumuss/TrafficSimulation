package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;

/**
 * CustomTitleBar:
 * Modern ve sade özel pencere başlık çubuğu.
 * - Sol tarafta uygulama simgesi ve başlık.
 * - Sağ tarafta kapatma butonu.
 * - Tüm pencereyi drag ile taşıyabilme özelliği.
 */
public class CustomTitleBar extends HBox {

    private double xOffset = 0;
    private double yOffset = 0;

    public CustomTitleBar(Stage stage) {
        setAlignment(Pos.CENTER_LEFT);
        setPrefHeight(32);
        setSpacing(10);
        setPadding(new Insets(0, 10, 0, 10));
        getStyleClass().add("custom-titlebar");

        // 🔻 Sol taraf: ikon + başlık
        Image icon = new Image(getClass().getResourceAsStream("/png/sedan_Car.png"));
        ImageView iconView = new ImageView(icon);
        iconView.setFitWidth(16);
        iconView.setFitHeight(16);

        Label titleLabel = new Label("🚦 TrafficSimulation - Akıllı Kavşak Sistemi");
        titleLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px;");

        HBox leftBox = new HBox(6, iconView, titleLabel);
        leftBox.setAlignment(Pos.CENTER_LEFT);

        // 🔻 Sağ taraf: kapatma butonu
        Button closeBtn = new Button("✕");
        closeBtn.setOnAction(e -> stage.close());
        closeBtn.getStyleClass().add("titlebar-button");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox rightBox = new HBox(closeBtn);
        rightBox.setAlignment(Pos.CENTER_RIGHT);

        // 🧩 Bar birleşimi: sol - spacer - sağ
        getChildren().addAll(leftBox, spacer, rightBox);

        // 🖱️ Sürükleme özelliği
        setOnMousePressed((MouseEvent event) -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        setOnMouseDragged((MouseEvent event) -> {
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });
    }
}
