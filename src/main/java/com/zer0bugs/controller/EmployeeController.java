package com.zer0bugs.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.zer0bugs.model.Employee;
import com.zer0bugs.repo.EmployeeRepo;
import com.zer0bugs.util.ExcelReader;
import com.zer0bugs.util.Regex;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.stage.FileChooser;
import lombok.Setter;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class EmployeeController {

    @FXML
    private Label addEmployeeLabel;

    @FXML
    private Label viewEmployeeLabel;

    @Setter
    DashboardController dashboardController;

    @FXML
    private TableView<String[]> bulckEmployeeShowTabeel;

    @FXML
    private TableColumn<?, ?> depCol;

    @FXML
    private TableColumn<?, ?> idCol;

    @FXML
    private TableColumn<?, ?> nameCol;

    @FXML
    private Pane addEmployeePane;

    @FXML
    private AnchorPane changePane;

    @FXML
    private JFXComboBox<String> comboBoxDepartment;

    @FXML
    private Line lineAddEmployee;

    @FXML
    private Line lineViewEmployee;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtName;

    @FXML
    private Pane viewEmployeePane;



    public void initialize() {
        txtId.requestFocus();
        defaultStyle();
        changeStyle(lineAddEmployee, addEmployeeLabel);
        setDepartment();
    }

    private void setDepartment() {
        ObservableList<String> list = FXCollections.observableArrayList("Dye House", "Finishing", "Stores", "Engineering", "Vet Lab", "Office", "Cleaning Services", "Security");
        comboBoxDepartment.setItems(list);
    }

    @FXML
    void btnSaveAction(ActionEvent event) {
        if (isValid()) {
            Employee employee = new Employee(txtId.getText(), txtName.getText(), comboBoxDepartment.getValue());
            try {
                if (EmployeeRepo.save(employee)) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Employee Saved !!").show();
                    clear();
                } else {
                    new Alert(Alert.AlertType.WARNING, "Employee Unsaved !!").show();
                }
            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            }
        }
    }

    private void clear() {
        txtId.clear();
        txtName.clear();
    }

    @FXML
    void txtIdAction(ActionEvent event) {
        txtName.requestFocus();
    }

    @FXML
    void txtNameAction(ActionEvent event) {
        comboBoxDepartment.requestFocus();
    }

    @FXML
    void addEmployeeAction(MouseEvent event) {
        dashboardController.loadEmployeeView();
    }

    @FXML
    void viewEmployeeAction(MouseEvent event) {
        defaultStyle();
        changeStyle(lineViewEmployee, viewEmployeeLabel);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/viewEmployeeForm.fxml"));
            AnchorPane viewEmployeeForm = loader.load();
            ViewEmployeeFormController controller = loader.getController();
            AnchorPane.setTopAnchor(viewEmployeeForm, 3.0);
            AnchorPane.setBottomAnchor(viewEmployeeForm, 3.0);
            AnchorPane.setLeftAnchor(viewEmployeeForm, 3.0);
            AnchorPane.setRightAnchor(viewEmployeeForm, 3.0);
            changePane.getChildren().clear();
            changePane.getChildren().setAll(viewEmployeeForm);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void changeStyle(Line line, Label label) {
        line.setStyle("-fx-stroke: #1e90ff;");
        label.setStyle("-fx-text-fill: #1e90ff;");
    }

    private void defaultStyle() {
        String style = "-fx-stroke: white;";
        lineAddEmployee.setStyle(style);
        lineViewEmployee.setStyle(style);
        addEmployeeLabel.setStyle("-fx-text-fill: black;");
        viewEmployeeLabel.setStyle("-fx-text-fill: black;");
    }

    private boolean isValid() {
        if (!Regex.setTextColor(com.zer0bugs.util.TextField.NAME, txtName)) return false;
        return true;
    }

    @FXML
    void txtIdKeyReleaseAction(KeyEvent event) {
        //Regex.setTextColor(dev.hsm.util.TextField.ID,txtId);
    }

    @FXML
    void txtNameKeyReleaseAction(KeyEvent event) {
        Regex.setTextColor(com.zer0bugs.util.TextField.NAME, txtName);
    }

    private ObservableList<String[]> data;
    @FXML
    void loadFromeExcelBtnOnAction(ActionEvent event) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));
        File file = fileChooser.showOpenDialog(null); // Change showOpenDialog() to showSaveDialog()

        if (file != null) {
            try {
                ExcelReader excelReader = new ExcelReader();
                List<String[]> excelData = excelReader.readExcel(file.getPath());

                if (!excelData.isEmpty()) {
                    String[] headers = excelData.get(0);
                    bulckEmployeeShowTabeel.getColumns().clear();

                    for (int i = 0; i < headers.length; i++) {
                        TableColumn<String[], String> column = new TableColumn<>(headers[i]);
                        final int colIndex = i;
                        column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[colIndex]));
                        column.setCellFactory(TextFieldTableCell.forTableColumn());
                        bulckEmployeeShowTabeel.getColumns().add(column);
                    }

                    data = FXCollections.observableArrayList(excelData.subList(1, excelData.size()));
                    bulckEmployeeShowTabeel.setItems(data);
                }
            } catch (Exception e) {
                e.printStackTrace(); // Handle the exception appropriately, e.g., show an error message
            }
        }
    }
}