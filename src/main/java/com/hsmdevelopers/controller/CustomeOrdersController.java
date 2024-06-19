package com.hsmdevelopers.controller;

import com.hsmdevelopers.db.DbConnection;
import com.hsmdevelopers.model.CustomOrder;
import com.hsmdevelopers.model.CustomOrderItem;
import com.hsmdevelopers.model.tm.CustomOrderItemTm;
import com.hsmdevelopers.model.tm.EmployeeTm;
import com.hsmdevelopers.repo.CustomOrderItemRepo;
import com.hsmdevelopers.repo.CustomOrderRepo;
import com.hsmdevelopers.repo.EmployeeRepo;
import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Optional;

public class CustomeOrdersController {

    @FXML
    private JFXButton addItemBtn;

    @FXML
    private TableColumn<?, ?> amountCol;

    @FXML
    private TableColumn<?, ?> btnCol;

    @FXML
    private TableColumn<?, ?> descriptionCol;

    @FXML
    private TextField descriptionTextField;

    @FXML
    private TextField numberOfPeopleTextField;

    @FXML
    private TableView<CustomOrderItemTm> orderItemTabel;

    @FXML
    private TextField orderNameTextField;

    @FXML
    private JFXButton placeOrderBtn;

    @FXML
    private TextField qtyTextField;

    public void initialize() {
        setCellValueFactory();
    }

    private void setCellValueFactory() {
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        amountCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        btnCol.setCellValueFactory(new PropertyValueFactory<>("deleteButton"));
    }

    @FXML
    void addItemBtnOnAction(ActionEvent event) {
        if (!descriptionTextField.getText().isEmpty() && !qtyTextField.getText().isEmpty()) {
            orderItemTabel.getItems().add(new CustomOrderItemTm(descriptionTextField.getText(),Integer.parseInt(qtyTextField.getText()),createDeleteButton()));
        }
        descriptionTextField.clear();
        qtyTextField.clear();
        descriptionTextField.requestFocus();
    }

    private Button createDeleteButton() {
        Button button = new Button("Delete");
        button.setStyle("-fx-background-color: red;");

        button.setOnAction((e) -> {
            ButtonType yes = new ButtonType("yes", ButtonBar.ButtonData.OK_DONE);
            ButtonType no = new ButtonType("no", ButtonBar.ButtonData.CANCEL_CLOSE);

            Optional<ButtonType> type = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure to remove?", yes, no).showAndWait();

            if (type.orElse(no) == yes) {
                CustomOrderItemTm selectedItem = orderItemTabel.getSelectionModel().getSelectedItem();

                if (selectedItem != null) {
                    // Get all items
                    ObservableList<CustomOrderItemTm> items = FXCollections.observableArrayList(orderItemTabel.getItems());
                    orderItemTabel.getItems().clear();

                    for (CustomOrderItemTm item : items) {
                        if (selectedItem.getDescription().equals(item.getDescription())) {
                            continue;
                        }
                        orderItemTabel.getItems().add(item);
                    }
                } else {
                    new Alert(Alert.AlertType.INFORMATION,"Select Column And Remove !!").show();
                }
            }
        });

        return button;
    }

    @FXML
    void descriptionTextFieldOnAction(ActionEvent event) {
        qtyTextField.requestFocus();
    }

    @FXML
    void numberOfPeopleTextFieldOnAction(ActionEvent event) {
        descriptionTextField.requestFocus();
    }

    @FXML
    void orderNameTextFieldOnAction(ActionEvent event) {
        numberOfPeopleTextField.requestFocus();
    }

    @FXML
    void qtyTextFieldOnAction(ActionEvent event) {
        addItemBtnOnAction(event);
    }

    @FXML
    void placeOrderBtnOnAction(ActionEvent event) {
        ObservableList<CustomOrderItemTm> items = FXCollections.observableArrayList(orderItemTabel.getItems());
        if (!orderNameTextField.getText().isEmpty() && !items.isEmpty()) {
            int customerOrderCount = CustomOrderRepo.getOrderCount();
            CustomOrder customOrder = new CustomOrder(customerOrderCount + 1 ,orderNameTextField.getText(),Integer.parseInt(numberOfPeopleTextField.getText()), Date.valueOf(LocalDate.now()), Time.valueOf(LocalTime.now()));

            ArrayList<CustomOrderItem> orderItems = new ArrayList<>();
            for (CustomOrderItemTm item : items) {
                orderItems.add(new CustomOrderItem(item.getDescription(),item.getQuantity(),customOrder.getCustomOrderId()));
            }
            try {
                if (placeOrder(customOrder,orderItems)){
                    new Alert(Alert.AlertType.CONFIRMATION,"Order Placed !!").show();
                } else {
                    new Alert(Alert.AlertType.ERROR,"Order Not Placed !!").show();
                }
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
            }
        }
    }

    private boolean placeOrder(CustomOrder customOrder, ArrayList<CustomOrderItem> orderItems) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        connection.setAutoCommit(false);

        try {
            if (CustomOrderRepo.save(customOrder)){
                if (CustomOrderItemRepo.save(orderItems)){
                    connection.commit();
                    return true;
                }
            }
            connection.rollback();
            return false;
        } catch (Exception e){
            connection.rollback();
            return false;
        } finally {
            connection.setAutoCommit(true);
        }
    }

}
