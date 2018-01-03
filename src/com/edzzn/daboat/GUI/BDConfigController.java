package com.edzzn.daboat.GUI;

import com.edzzn.daboat.DBD.Session;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class BDConfigController implements Initializable {

    @FXML
    private TextField userTextField;

    @FXML
    private TextField urlTextField;

    @FXML
    private PasswordField passTextField;

    @FXML
    private Button guardarButton;

    @FXML
    private Label alertLabelConfig;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        populateData();
    }

    private void populateData() {
        Session session = Session.getInstance();
        userTextField.setText(session.getDb_SysUsername());
        passTextField.setText(session.getDb_SysPassword());
        urlTextField.setText(session.getDb_url());
    }

    public void onGuardarButtonClicked() {

        String usuario = userTextField.getText();
        String pass = passTextField.getText();
        String url = urlTextField.getText();

        Session session = Session.getInstance();

        try {
            if (session.validCredentials(url, usuario, pass)) {
                alertLabelConfig.setText("Inicio de sesión exitoso");
                session.setDb_url(url);
                session.setDb_SysUsername(usuario);
                session.setDb_SysPassword(pass);
                onCancelarButtonClicked();
            } else {
                alertLabelConfig.setText("Credenciales incorrectos");

            }
        } catch (SQLException e) {
            e.printStackTrace();
            alertLabelConfig.setText("Credenciales incorrectos");
        }
    }

    public void onCancelarButtonClicked() {
        // Cerramos la ventana
        Stage stageLogin = (Stage) guardarButton.getScene().getWindow();
        stageLogin.close();
    }
}