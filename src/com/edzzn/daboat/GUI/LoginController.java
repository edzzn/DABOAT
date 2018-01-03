package com.edzzn.daboat.GUI;

import com.edzzn.daboat.DBD.Session;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoginController implements Initializable {

    @FXML
    private Label alertLabel;

    @FXML
    private TextField username;

    @FXML
    private PasswordField password;

    @FXML
    private Button loginButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {}

    public void loginButtonClicked() {

        String usuario = username.getText();
        String pass = password.getText();
        Session session = Session.getInstance();

        if (session.connect(usuario, pass)) {
            alertLabel.setText("Inicio de sesión exitoso");
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("main.fxml"));
                Scene scene = new Scene(fxmlLoader.load(), 1000, 820);
                Stage stage = new Stage();
                stage.setResizable(false);
                stage.setTitle("DABOAT - DATABASE OBJECT ABSTRACTION TOOL");

                // fijar icono de la aplicación
                Image applicationIcon = new Image(getClass().getResourceAsStream("..\\Resources\\imgs\\icon.png"));
                stage.getIcons().add(applicationIcon);

                stage.setScene(scene);
                stage.show();

                // Cerrar login Window
                Stage stageLogin = (Stage) loginButton.getScene().getWindow();
                stageLogin.close();


            } catch (IOException e) {
                Logger logger = Logger.getLogger(getClass().getName());
                logger.log(Level.SEVERE, "Failed to create new Window.", e);
            }


        } else {
            alertLabel.setText("Usuario y/o contraseña incorrectos");
        }

    }

    public void onConfConButton() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("dbconfig.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        Stage stage = new Stage();
        stage.setTitle("DABOAT - DATABASE OBJECT ABSTRACTION TOOL");

        Image applicationIcon = new Image(getClass().getResourceAsStream("..\\Resources\\imgs\\icon.png"));
        stage.getIcons().add(applicationIcon);

        stage.setScene(scene);

        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);

        stage.show();

    }


}