package com.zer0bugs.controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class CustomeOrdersController {


    @FXML
    private TextField numberOfPeapolTextField;

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
    private TableView<?> orderItemTabel;

    @FXML
    private TextField orderNameTextField;

    @FXML
    private JFXButton placeOrderBtn;

    @FXML
    private TextField qtyTextField;

    @FXML
    void addItemBtnOnAction(ActionEvent event) {

    }

    @FXML
    void placeOrderBtnOnAction(ActionEvent event) {

    }

}

