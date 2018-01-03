package com.edzzn.daboat.GUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Este proyecto es una implementación de la abstracción de objetos
 * y su manejo en un base de datos SQL. El proyecto es parte del curso
 * de Base de Datos de la Universidad de Cuenca.
 *
 * @author  Edisson Reinozo edzzn.com
 * @version 1.0
 * @since   2018-01-04
 */
public class App extends Application {

    public static void main(String[] args) {
        launch(args);
    }
    @Override

    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        primaryStage.setTitle("DABOAT - DATABASE OBJECT ABSTRACTION TOOL");

        Image applicationIcon = new Image(getClass().getResourceAsStream("..\\Resources\\imgs\\icon.png"));
        primaryStage.getIcons().add(applicationIcon);
        primaryStage.setScene(new Scene(root, 800, 500));
        primaryStage.setResizable(false);

        primaryStage.show();
    }
}
