package com.hsmdevelopers.controller;


import com.hsmdevelopers.model.Employee;
import com.hsmdevelopers.model.tm.EmployeeTm;
import com.hsmdevelopers.repo.EmployeeRepo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.sql.SQLException;
import java.util.Optional;

public class ViewEmployeeFormController {

    @FXML
    private TableColumn<?, ?> colDelete;

    @FXML
    private TableColumn<?, ?> colDep;

    @FXML
    private TableColumn<?, ?> colId;

    @FXML
    private TableColumn<?, ?> colName;

    @FXML
    private TableView<EmployeeTm> tblEmployee;

    @FXML
    private TextField txtIdSearch;


    @FXML
    private Button searchBtn;

    private ObservableList<EmployeeTm> employees = FXCollections.observableArrayList();

    public void initialize() {
        setResizeable();
        setCellValueFactory();
        loadData();
    }

    private void setResizeable() {
        txtIdSearch.setMinWidth(309);
        txtIdSearch.setMaxWidth(Double.MAX_VALUE);
        colId.prefWidthProperty().bind(tblEmployee.widthProperty().multiply(0.2));
        colName.prefWidthProperty().bind(tblEmployee.widthProperty().multiply(0.3));
        colDep.prefWidthProperty().bind(tblEmployee.widthProperty().multiply(0.3));
        colDelete.prefWidthProperty().bind(tblEmployee.widthProperty().multiply(0.2));
    }

    private void loadData() {
        try {
            ObservableList<Employee> list = EmployeeRepo.getEmployees();
            for (int i = 0; i < list.size(); i++) {
                employees.add(getTableData(list.get(i)));
            }
            tblEmployee.setItems(employees);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
    }

    private Button createDeleteButton() {
        Button button = new Button("Delete");
        button.setStyle("-fx-background-color: red;-fx-text-fill: white;");

        button.setOnAction((e) -> {
            ButtonType yes = new ButtonType("yes", ButtonBar.ButtonData.OK_DONE);
            ButtonType no = new ButtonType("no", ButtonBar.ButtonData.CANCEL_CLOSE);

            Optional<ButtonType> type = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure to remove?", yes, no).showAndWait();

            if (type.orElse(no) == yes) {
                EmployeeTm selectedIndex = tblEmployee.getSelectionModel().getSelectedItem();
                try{
                    if (EmployeeRepo.delete(selectedIndex)){
                        employees.clear();
                        loadData();
                        new Alert(Alert.AlertType.CONFIRMATION,"Delete Successfully !!").show();
                    }
                } catch (Exception exception){
                    new Alert(Alert.AlertType.INFORMATION,"Select Column And Remove !!").show();
                }
            }
        });

        return button;
    }

    private EmployeeTm getTableData(Employee employee) {
        return new EmployeeTm(employee.getId(), employee.getName(), employee.getDepartment(), createDeleteButton());
    }

    private void setCellValueFactory() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colDep.setCellValueFactory(new PropertyValueFactory<>("department"));
        colDelete.setCellValueFactory(new PropertyValueFactory<>("button"));
    }

    @FXML
    void btnSearchAction(ActionEvent event) {
        try {
            Employee employee = EmployeeRepo.search(txtIdSearch.getText());
            if (employee != null){
                ObservableList<EmployeeTm> emList = FXCollections.observableArrayList();
                emList.add(getTableData(employee));
                tblEmployee.setItems(emList);
            } else {
                new Alert(Alert.AlertType.INFORMATION,"Can't Find Employee !!").show();
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
    }

    @FXML
    void tblEmployeeAction(MouseEvent event) {

    }

    @FXML
    void txtIdSearchAction(ActionEvent event) {
        btnSearchAction(event);
    }


}
