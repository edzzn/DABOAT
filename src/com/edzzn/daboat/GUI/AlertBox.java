package com.edzzn.daboat.GUI;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AlertBox {
    static boolean response;

    public static boolean display(String title, String message) {
        Stage window = new Stage();

        // Block user interaction with other windows
        window.initModality(Modality.APPLICATION_MODAL);

        window.setTitle(title);
        window.setMinWidth(500);
        window.setMinHeight(200);

        Label label = new Label();
        label.setText(message);
        Button yesButton = new Button("Aceptar");
        yesButton.setOnAction(e -> {
            response = true;
            window.close();
        });

        Button noButton = new Button("Cancelar");
        noButton.setOnAction(e -> {
            response = false;
            window.close();
        });


        HBox layout2 = new HBox(10);
        layout2.getChildren().addAll(yesButton, noButton);
        layout2.setAlignment(Pos.CENTER);

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, layout2);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        window.setScene(scene);

        window.showAndWait();
        return response;
    }
}
