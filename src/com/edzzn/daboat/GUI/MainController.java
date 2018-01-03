package com.edzzn.daboat.GUI;

import com.edzzn.daboat.LOGIC.Helper;
import com.edzzn.daboat.LOGIC.ObjetoTabla;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.paint.Color;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.LocalDateTimeStringConverter;

import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.ResourceBundle;

//@SuppressWarnings("unchecked")
public class MainController implements Initializable {

    @FXML
    private TextField valorCondicionTextoSeleccion;

    @FXML
    private TextArea consultaTextSeleccion;

    @FXML
    private TableView tablaSeleccion;

    @FXML
    private RadioButton ascRadioButtonSeleccion;

    @FXML
    private RadioButton descRadioButtonSeleccion;

    @FXML
    private ListView disponibleListViewSeleccion;

    @FXML
    private ListView seleccionadoListViewSeleccion;

    @FXML
    private ListView condicionesListViewSeleccion;

    @FXML
    private ComboBox tablaComboBoxSeleccion;

    @FXML
    private ComboBox comparacionComboBoxSeleccion;

    @FXML
    private ComboBox colCondicionComboBoxSeleccion;

    @FXML
    private ComboBox atributosOrdenarComboBoxSeleccion;

    @FXML
    private Label alertLabelSeleccion;

    private final ObservableList<String> candidatesListSeleccionar = FXCollections.observableArrayList();

    private final ObservableList<String> selectedListSeleccionar = FXCollections.observableArrayList();

    private final ObservableList<String> condiciones = FXCollections.observableArrayList();

    private final ArrayList<TableColumn> tableColumnsSeleccion = new ArrayList<>();

    /* Agregar Tab */

    @FXML
    private ComboBox tablaComboBoxAgregar;

    @FXML
    private TableView tablaAgregar;

    @FXML
    private Label alertLabelAgregar;

    @FXML
    private TextArea consultaTextAgregar;

    private final ArrayList<TableColumn> tableColumnsAgregar = new ArrayList<>();

    private ObjetoTabla objetoTabla;

    /* Editar Tab variables */

    @FXML
    private ComboBox tablaComboBoxEditar;

    @FXML
    private TableView ListObjectsTableViewEditar;

    @FXML
    private TableView ListEditableObjectTableViewEditar;

    @FXML
    private Label alertLabelEditar;

    @FXML
    private TextArea textConsutaTextAreaEditar;

    private final ArrayList<TableColumn> tableColumnsEditar = new ArrayList<>();


    /* Editar Tab variables */

    @FXML
    private ComboBox tablaComboBoxEliminar;

    @FXML
    private TableView ListObjectsTableViewEliminar;

    @FXML
    private TableView ListEditableObjectTableViewEliminar;

    @FXML
    private Label alertLabelEliminar;

    @FXML
    private TextArea textConsutaTextAreaEliminar;

    private final ArrayList<TableColumn> tableColumnsEliminar = new ArrayList<>();


    /* Editar Tab variables */

    @FXML
    private ComboBox userComboBoxAdmin;

    @FXML
    private ListView disponibleListViewAdmin;

    @FXML
    private ListView noDisponibleListViewAdmin;

    @FXML
    private Label alertLabelTabAdmin;

    @FXML
    private TextArea consultaTextAdmin;

    private final ObservableList<String> candidatesListAdmin = FXCollections.observableArrayList();

    private final ObservableList<String> selectedListAdmin = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        populateDataSeleccionar();
        populateDataAgregar();
        populateEditarTab();
        populateEliminarTab();
        populateAdmin();
    }


    private void populateDataSeleccionar() {
        groupRadioButton();

        tablaComboBoxSeleccion.getItems().addAll(Helper.getTables());
        tablaComboBoxSeleccion.getSelectionModel().selectFirst();

        String selectedTable = tablaComboBoxSeleccion.getSelectionModel().getSelectedItem().toString();
        colCondicionComboBoxSeleccion.getItems().addAll(Helper.getColumns(selectedTable));
        colCondicionComboBoxSeleccion.getSelectionModel().selectFirst();

        atributosOrdenarComboBoxSeleccion.getItems().addAll(Helper.getColumns(selectedTable));
        atributosOrdenarComboBoxSeleccion.getSelectionModel().selectFirst();

        disponibleListViewSeleccion.setItems(candidatesListSeleccionar);
        seleccionadoListViewSeleccion.setItems(selectedListSeleccionar);

        condicionesListViewSeleccion.setItems(condiciones);

        //  Populate the ListView
        Collections.addAll(candidatesListSeleccionar, Helper.getColumns(selectedTable));

        comparacionComboBoxSeleccion.getItems().addAll(">", "<", "=", "!=");
        comparacionComboBoxSeleccion.getSelectionModel().selectFirst();

        // Populate the TableView
        populateTable(selectedTable, getTableDataSeleccion(selectedTable), tablaSeleccion, tableColumnsSeleccion);
    }

    private ObservableList<ObjetoTabla> getTableDataSeleccion(String selectedTable) {
        ObservableList<ObjetoTabla> tableData = FXCollections.observableArrayList();
        ArrayList<ObjetoTabla> data = Helper.returnAllObject(selectedTable);
        tableData.addAll(data);
        return tableData;
    }

    private ObservableList<ObjetoTabla> getTableDataSeleccion(String selectedTable, String consulta) {
        ObservableList<ObjetoTabla> tableData = FXCollections.observableArrayList();
        ArrayList<ObjetoTabla> data = Helper.returnAllObject(selectedTable, consulta);
        tableData.addAll(data);
        return tableData;
    }

    private void groupRadioButton() {
        final ToggleGroup group = new ToggleGroup();

        ascRadioButtonSeleccion.setToggleGroup(group);
        ascRadioButtonSeleccion.setSelected(true);
        descRadioButtonSeleccion.setToggleGroup(group);
    }

    public void onSeleccionarListViewClicked() {
        String str = "";
        try {
            str = disponibleListViewSeleccion.getSelectionModel().getSelectedItem().toString();
        } catch (Exception ignored) {
        }

        if (str != null && !Objects.equals(str, "")) {
            disponibleListViewSeleccion.getSelectionModel().clearSelection();
            candidatesListSeleccionar.remove(str);
            selectedListSeleccionar.add(str);
        }
    }

    public void onDeseleccionarListViewButtonClicked() {
        String str = "";
        try {
            str = seleccionadoListViewSeleccion.getSelectionModel().getSelectedItem().toString();
        } catch (Exception ignored) {
        }

        if (str != null && !Objects.equals(str, "")) {
            seleccionadoListViewSeleccion.getSelectionModel().clearSelection();
            selectedListSeleccionar.remove(str);
            candidatesListSeleccionar.add(str);
        }
    }

    public void onAgregarCondicionButtonClicked() {
        String condicion = comparacionComboBoxSeleccion.getSelectionModel().getSelectedItem().toString();
        String columCondicion = colCondicionComboBoxSeleccion.getSelectionModel().getSelectedItem().toString();
        String valorCompararar = valorCondicionTextoSeleccion.getText();
        condiciones.add(columCondicion + " " + condicion + " '" + valorCompararar + "'");
    }

    public void onResetButtonClicked() {
        String selectedTable = tablaComboBoxSeleccion.getSelectionModel().getSelectedItem().toString();

        // Populate comboBoxed
        colCondicionComboBoxSeleccion.getItems().clear();
        colCondicionComboBoxSeleccion.getItems().addAll(Helper.getColumns(selectedTable));
        colCondicionComboBoxSeleccion.getSelectionModel().selectFirst();

        atributosOrdenarComboBoxSeleccion.getItems().clear();
        atributosOrdenarComboBoxSeleccion.getItems().addAll(Helper.getColumns(selectedTable));
        atributosOrdenarComboBoxSeleccion.getSelectionModel().selectFirst();

        // Populate the ListView
        candidatesListSeleccionar.clear();
        selectedListSeleccionar.clear();
        Collections.addAll(candidatesListSeleccionar, Helper.getColumns(selectedTable));
        condiciones.clear();
        consultaTextSeleccion.clear();
    }

    public void updatedSelectedTableSeleccion() {
        onResetButtonClicked();
    }

    public void onConsultarButtonClicked() {
        if (validDataSeleccion()) {

            StringBuilder consulta = new StringBuilder();
            StringBuilder consultaReal = new StringBuilder();

            // SELECT
            consultaReal.append("SELECT ");
            for (int i = 0; i < selectedListSeleccionar.size(); i++) {
                if (i == 0) {
                    consultaReal.append(" ").append(selectedListSeleccionar.get(i)).append(" ");
                } else {
                    consultaReal.append(", ").append(selectedListSeleccionar.get(i)).append(" ");
                }
            }

            String selectedTable = tablaComboBoxSeleccion.getSelectionModel().getSelectedItem().toString();
            consultaReal.append("\nFROM ").append(selectedTable);

            // Condiciones
            for (int i = 0; i < condiciones.size(); i++) {
                if (i == 0) {
                    consulta.append("\nWHERE ").append(condiciones.get(i));
                } else {
                    consulta.append("\nAND ").append(condiciones.get(i));
                }
            }

            // Ordenar
            consulta.append("\nORDER BY ").append(atributosOrdenarComboBoxSeleccion.getSelectionModel().getSelectedItem().toString());
            if (ascRadioButtonSeleccion.isSelected()) {
                consulta.append(" ASC");
            } else {
                consulta.append(" DESC");
            }

            consultaTextSeleccion.setText(consultaReal + consulta.toString());
            alertLabelSeleccion.setText("");

            populateTable(selectedTable, getTableDataSeleccion(selectedTable, "SELECT * FROM " + selectedTable + consulta), tablaSeleccion, tableColumnsSeleccion);

            // remove extra Table Columns
            for (String candidate : candidatesListSeleccionar) {
                for (TableColumn aTableColumnsSeleccion : tableColumnsSeleccion) {
                    if (candidate.equalsIgnoreCase(aTableColumnsSeleccion.getText())) {
                        aTableColumnsSeleccion.setVisible(false);
                    }
                }
            }

        } else {
            alertLabelSeleccion.setText("Selecciona todos los campos antes de continuar.");
        }
    }

    private boolean validDataSeleccion() {
        return selectedListSeleccionar.size() != 0;
    }


    /* Functions to manage the Agregar TAB*/

    private void populateDataAgregar() {
        tablaComboBoxAgregar.getItems().addAll(Helper.getTables());
        tablaComboBoxAgregar.getSelectionModel().selectFirst();
        String tableName = tablaComboBoxAgregar.getSelectionModel().getSelectedItem().toString();

        objetoTabla = ObjetoTabla.loadGenericObject(tableName);
        populateEditableTableHeader(tableName, tablaAgregar, tableColumnsAgregar, objetoTabla, true);
    }

    private void populateTable(String selectedTable, ObservableList<ObjetoTabla> data, TableView tabla, ArrayList<TableColumn> tablaColumnas) {
        // Populate the TableView Agregar
        tablaColumnas.clear();
        tabla.getColumns().clear();
        tabla.getItems().clear();
        String[] columnas = Helper.getColumns(selectedTable);
        for (String columna : columnas) {
            TableColumn<ObjetoTabla, Integer> column = new TableColumn<>(columna);
            column.setMinWidth(100);
            column.setCellValueFactory(new PropertyValueFactory<>(columna.toLowerCase()));

            tablaColumnas.add(column);
        }

        tabla.setItems(data);

        for (TableColumn column : tablaColumnas) {
            tabla.getColumns().addAll(column);
        }
    }

    private void populateEditableTableHeader(String selectedTable, TableView tabla, ArrayList<TableColumn> tablaColumnas, ObjetoTabla objetoTabla, boolean editable) {
        // Populate the TableView Agregar
        tablaColumnas.clear();
        tabla.getColumns().clear();
        tabla.getItems().clear();
        String[] columnas = Helper.getColumns(selectedTable);

        if (editable) {
            tabla.setEditable(true);
        } else {
            tabla.setEditable(false);
        }

        for (String columna : columnas) {
            TableColumn tableColumn;
            Class<?> parametroType = objetoTabla.getParameterClass(columna);
            if (parametroType.equals(String.class)) {
                tableColumn = new TableColumn<ObjetoTabla, String>(columna);
                tableColumn.setCellValueFactory(new PropertyValueFactory<ObjetoTabla, String>(columna.toLowerCase()));
                tableColumn.setCellFactory(TextFieldTableCell.<ObjetoTabla>forTableColumn());
                tableColumn.setOnEditCommit((EventHandler<TableColumn.CellEditEvent<ObjetoTabla, String>>) t -> objetoTabla.getClass().cast(t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                ).genericSetter(
                        columna.toLowerCase(), t.getNewValue()));

            } else if (parametroType.equals(Integer.class)) {
                tableColumn = new TableColumn<ObjetoTabla, Integer>(columna);
                tableColumn.setCellValueFactory(new PropertyValueFactory<ObjetoTabla, Integer>(columna.toLowerCase()));
                tableColumn.setCellFactory(TextFieldTableCell.<ObjetoTabla, Integer>forTableColumn(new IntegerStringConverter()));
                tableColumn.setOnEditCommit((EventHandler<TableColumn.CellEditEvent<ObjetoTabla, Integer>>) t -> objetoTabla.getClass().cast(t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                ).genericSetter(
                        columna.toLowerCase(), t.getNewValue()));

            } else if (parametroType.equals(Double.class)) {
                tableColumn = new TableColumn<ObjetoTabla, Double>(columna);
                tableColumn.setCellValueFactory(new PropertyValueFactory<ObjetoTabla, Double>(columna.toLowerCase()));
                tableColumn.setCellFactory(TextFieldTableCell.<ObjetoTabla, Double>forTableColumn(new DoubleStringConverter()));
                tableColumn.setOnEditCommit((EventHandler<TableColumn.CellEditEvent<ObjetoTabla, Double>>) t -> objetoTabla.getClass().cast(t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                ).genericSetter(
                        columna.toLowerCase(), t.getNewValue()));

            } else if (parametroType.equals(LocalDateTime.class)) {
                tableColumn = new TableColumn<ObjetoTabla, LocalDateTime>(columna);
                tableColumn.setCellValueFactory(new PropertyValueFactory<ObjetoTabla, Timestamp>(columna.toLowerCase()));
                tableColumn.setCellFactory(TextFieldTableCell.<ObjetoTabla, LocalDateTime>forTableColumn(new LocalDateTimeStringConverter()));
                tableColumn.setOnEditCommit((EventHandler<TableColumn.CellEditEvent<ObjetoTabla, LocalDateTime>>) t -> objetoTabla.getClass().cast(t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                ).genericSetter(
                        columna.toLowerCase(), t.getNewValue()));
            } else {
                throw new IllegalArgumentException("Unknown Class: ");
            }

            tableColumn.setMinWidth(100);
            /* Agregamos la Columna a la Lista */
            tablaColumnas.add(tableColumn);
        }

        // Agregamos las columnas a la tabla
        tabla.getItems().addAll(objetoTabla);

        for (TableColumn column : tablaColumnas) {
            tabla.getColumns().addAll(column);
        }
    }

    public void updatedSelectedTableAgregar() {
        // Actulizar tablas cuando se escoge una nueva Tabla en el combobox
        String tableName = tablaComboBoxAgregar.getSelectionModel().getSelectedItem().toString();

        objetoTabla = ObjetoTabla.loadGenericObject(tableName);
        populateEditableTableHeader(tableName, tablaAgregar, tableColumnsAgregar, objetoTabla, true);
    }


    public void onResetButtonAgregarClicked() {
        updatedSelectedTableAgregar();
    }

    public void onSolicitarButtonAgregarClicked() {
        String sqlQuery = ObjetoTabla.class.cast(tablaAgregar.getItems().get(0)).saveObject();
        if (sqlQuery.contains("java.")) {
            // Display error
            alertLabelAgregar.setTextFill(Color.valueOf("ff0000"));
            alertLabelAgregar.setText(sqlQuery);
        } else {
            consultaTextAgregar.setText(sqlQuery);
            alertLabelAgregar.setTextFill(Color.valueOf("0042ff"));
            alertLabelAgregar.setText("Objeto Agregado a la Base de datos");
        }
    }

    /* Editar Tab Controllers */

    private void populateEditarTab() {
        tablaComboBoxEditar.getItems().addAll(Helper.getTables());
        tablaComboBoxEditar.getSelectionModel().selectFirst();
        String tableName = tablaComboBoxEditar.getSelectionModel().getSelectedItem().toString();
        ObservableList<ObjetoTabla> dataTablaAgregar = getTableDataSeleccion(tableName);
        populateTable(tableName, dataTablaAgregar, ListObjectsTableViewEditar, tableColumnsEditar);
    }

    public void onSolicitarButtonEditarClicked() {
        objetoTabla = (ObjetoTabla) ListObjectsTableViewEditar.getFocusModel().getFocusedItem();
        String sqlQuery = objetoTabla.updateObject();
        if (sqlQuery.contains("java.")) {
            // Display error
            alertLabelEditar.setTextFill(Color.valueOf("ff0000"));
            alertLabelEditar.setText(sqlQuery);
        } else {
            textConsutaTextAreaEditar.setText(sqlQuery);
            alertLabelEditar.setTextFill(Color.valueOf("0042ff"));
            alertLabelEditar.setText("Objeto Editado a la Base de datos");

            // ahora actualizamos la tabla Superior.
            String tableName = tablaComboBoxEditar.getSelectionModel().getSelectedItem().toString();
            ObservableList<ObjetoTabla> dataTablaAgregar = getTableDataSeleccion(tableName);
            populateTable(tableName, dataTablaAgregar, ListObjectsTableViewEditar, tableColumnsEditar);
        }
    }

    public void onResetButtonEditarClicked() {
        tableColumnsEditar.clear();
        alertLabelEditar.setText("");
        textConsutaTextAreaEditar.clear();
    }

    public void onSeleccionarObjectButtonEditarClicked() {
        String tableName = tablaComboBoxEditar.getSelectionModel().getSelectedItem().toString();
        objetoTabla = (ObjetoTabla) ListObjectsTableViewEditar.getFocusModel().getFocusedItem();
        populateEditableTableHeader(tableName, ListEditableObjectTableViewEditar, tableColumnsEditar, objetoTabla, true);
    }

    public void updatedSelectedTableEditar() {
        String tableName = tablaComboBoxEditar.getSelectionModel().getSelectedItem().toString();
        ObservableList<ObjetoTabla> dataTablaAgregar = getTableDataSeleccion(tableName);
        populateTable(tableName, dataTablaAgregar, ListObjectsTableViewEditar, tableColumnsEditar);

        // Limpiamos los otros campos
        onResetButtonEditarClicked();
    }


    /* Eliminar Tab Controllers */

    private void populateEliminarTab() {
        tablaComboBoxEliminar.getItems().addAll(Helper.getTables());
        tablaComboBoxEliminar.getSelectionModel().selectFirst();
        String tableName = tablaComboBoxEliminar.getSelectionModel().getSelectedItem().toString();
        ObservableList<ObjetoTabla> dataTablaAgregar = getTableDataSeleccion(tableName);
        populateTable(tableName, dataTablaAgregar, ListObjectsTableViewEliminar, tableColumnsEliminar);
    }

    public void onSolicitarButtonEliminarClicked() {
        objetoTabla = (ObjetoTabla) ListObjectsTableViewEliminar.getFocusModel().getFocusedItem();
        String sqlQuery = objetoTabla.deleteObject();
        if (sqlQuery.contains("java.")) {
            // Display error
            alertLabelEliminar.setTextFill(Color.valueOf("ff0000"));
            alertLabelEliminar.setText(sqlQuery);
        } else {
            textConsutaTextAreaEliminar.setText(sqlQuery);
            alertLabelEliminar.setTextFill(Color.valueOf("0042ff"));
            alertLabelEliminar.setText("Objeto elimnado de Base de datos");

            // ahora actualizamos la tabla Superior.
            String tableName = tablaComboBoxEliminar.getSelectionModel().getSelectedItem().toString();
            ObservableList<ObjetoTabla> dataTablaAgregar = getTableDataSeleccion(tableName);
            populateTable(tableName, dataTablaAgregar, ListObjectsTableViewEliminar, tableColumnsEliminar);

            // limpiamos la tabla inferior
            ListEditableObjectTableViewEliminar.getItems().clear();
        }
    }

    public void onResetButtonEliminarClicked() {
        tableColumnsEliminar.clear();
        alertLabelEliminar.setText("");
        textConsutaTextAreaEliminar.clear();
    }

    public void onSeleccionarObjectButtonEliminarClicked() {
        String tableName = tablaComboBoxEliminar.getSelectionModel().getSelectedItem().toString();
        objetoTabla = (ObjetoTabla) ListObjectsTableViewEliminar.getFocusModel().getFocusedItem();
        populateEditableTableHeader(tableName, ListEditableObjectTableViewEliminar, tableColumnsEliminar, objetoTabla, false);
    }

    public void updatedSelectedTableEliminar() {
        String tableName = tablaComboBoxEliminar.getSelectionModel().getSelectedItem().toString();
        ObservableList<ObjetoTabla> dataTablaAgregar = getTableDataSeleccion(tableName);
        populateTable(tableName, dataTablaAgregar, ListObjectsTableViewEliminar, tableColumnsEliminar);

        // Limpiamos los otros campos
        onResetButtonEliminarClicked();
    }


    /* Admin Tab Controllers */

    private void populateAdmin() {
        userComboBoxAdmin.getItems().addAll(Helper.getUsers());
        userComboBoxAdmin.getSelectionModel().selectFirst();
        updateAdministracionTab();

        disponibleListViewAdmin.setItems(candidatesListAdmin);
        noDisponibleListViewAdmin.setItems(selectedListAdmin);
    }

    public void updateAdministracionTab() {
        String selectedUser = userComboBoxAdmin.getSelectionModel().getSelectedItem().toString();

        // Populate the ListView
        candidatesListAdmin.clear();
        selectedListAdmin.clear();

        Collections.addAll(candidatesListAdmin, Helper.getPrivileges());

        for (String priv : Helper.getUsersPrivileges(selectedUser)) {
            selectedListAdmin.add(priv);
            // Se elimina los priv que sí pertencen al usuario.
            candidatesListAdmin.remove(priv);
        }
        FXCollections.sort(candidatesListAdmin);
        FXCollections.sort(selectedListAdmin);
    }

    public void onMoveLeftButtonAdmin() {
        String str = "";
        try {
            str = noDisponibleListViewAdmin.getSelectionModel().getSelectedItem().toString();
        } catch (Exception ignored) {
        }

        if (str != null && !Objects.equals(str, "")) {
            noDisponibleListViewAdmin.getSelectionModel().clearSelection();
            selectedListAdmin.remove(str);
            candidatesListAdmin.add(str);
        }
    }

    public void onMoveRightButtonAdmin() {
        String str = "";
        try {
            str = disponibleListViewAdmin.getSelectionModel().getSelectedItem().toString();
        } catch (Exception ignored) {
        }

        if (str != null && !Objects.equals(str, "")) {
            disponibleListViewAdmin.getSelectionModel().clearSelection();
            candidatesListAdmin.remove(str);
            selectedListAdmin.add(str);
        }
    }

    public void onConsultarButtonAdminClicked() {
        String selectedUser = userComboBoxAdmin.getSelectionModel().getSelectedItem().toString();
        ArrayList<String> permisosCandidatosOriginales = new ArrayList<>();
        ArrayList<String> permisosAgredos = new ArrayList<>(selectedListAdmin);
        ArrayList<String> permisosEliminados = new ArrayList<>(candidatesListAdmin);

        Collections.addAll(permisosCandidatosOriginales, Helper.getPrivileges());

        for (String priv : Helper.getUsersPrivileges(selectedUser)) {
            // Eliminamos los permisos originales del usuario
            permisosAgredos.remove(priv);
            permisosCandidatosOriginales.remove(priv);
        }

        for (String priv : permisosCandidatosOriginales) {
            permisosEliminados.remove(priv);
        }

        if (permisosEliminados.size() == 0 && permisosAgredos.size() == 0) {
            alertLabelTabAdmin.setText("No se realizaron cambios en los permisos");
        } else {
            if (AlertBox.display("ALERTA", "¿Esta seguro?\nModificar privilegios de usuarios es una operación peligrosa.")) {
                String finalConsuta = "";
                if (permisosAgredos.size() != 0) {
                    String SQLConsultaAdd = "GRANT ";
                    for (int i = 0; i < permisosAgredos.size(); i++) {
                        if (i == permisosAgredos.size() - 1) {
                            SQLConsultaAdd += permisosAgredos.get(i);
                        } else {
                            SQLConsultaAdd += permisosAgredos.get(i) + ", ";
                            consultaTextAdmin.setText(SQLConsultaAdd);
                        }
                    }
                    SQLConsultaAdd += " TO " + selectedUser;
                    finalConsuta += SQLConsultaAdd + "\n";
                }

                if (permisosEliminados.size() != 0) {
                    StringBuilder SQLConsultaRemove = new StringBuilder("REVOKE ");
                    for (int i = 0; i < permisosEliminados.size(); i++) {
                        if (i == permisosEliminados.size() - 1) {
                            SQLConsultaRemove.append(permisosEliminados.get(i));
                        } else {
                            SQLConsultaRemove.append(permisosEliminados.get(i)).append(", ");
                        }
                    }
                    SQLConsultaRemove.append(" FROM ").append(selectedUser);
                    finalConsuta += SQLConsultaRemove;
                }
                alertLabelTabAdmin.setText("Cambios a los privilegios realizados");
                Helper.ejecutarMultiples(finalConsuta.split("\n"));
                consultaTextAdmin.setText(finalConsuta);
            }
        }
    }

    public void onResetButtonAdminClicked() {
        updateAdministracionTab();
    }

}
