package com.zer0bugs.controller;

import com.zer0bugs.model.User;
import com.zer0bugs.repo.UserRepo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import com.jfoenix.controls.JFXButton;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

public class SettingsViewController {

    @FXML
    private TextField currentPwTextFiled;

    @FXML
    private Label currentUnameLbel;

    @FXML
    private TextField newPwTextField;

    @FXML
    private TextField newPwrReenterTextField;

    @FXML
    private TextField newUnameTExtField;

    @FXML
    private JFXButton changeBtn;

    @FXML
    private AnchorPane addUserAnchorpane;

    @FXML
    private TableColumn<?, ?> colDeleteBtn;

    @FXML
    private TableColumn<?, ?> colPassword;

    @FXML
    private TableColumn<?, ?> colUserName;

    @FXML
    private TableView<?> tblUserDetails;

    @FXML
    private TextField txtPassword;

    @FXML
    private TextField txtUserName;

    public void initialize() {
        currentUnameLbel.setText(" " + UserRepo.user.getUserName());
        visible(false);
        setCellValueFactory();
        isShowAddUser();
    }

    private void isShowAddUser() {
        if (UserRepo.user.getId() == 1){

        }
    }

    private void setCellValueFactory() {
        colUserName.setCellValueFactory(new PropertyValueFactory<>("username"));
        colPassword.setCellValueFactory(new PropertyValueFactory<>("password"));
        colDeleteBtn.setCellValueFactory(new PropertyValueFactory<>("deleteButton"));
    }

    private void visible(boolean isVisible) {
        newUnameTExtField.setVisible(isVisible);
        newPwTextField.setVisible(isVisible);
        newPwrReenterTextField.setVisible(isVisible);
        changeBtn.setVisible(isVisible);
    }

    @FXML
    void changeBtnOnAction(ActionEvent event) {
        User user;
        if (newPwTextField.getText().equals(newPwrReenterTextField.getText())) {
            if (!newUnameTExtField.getText().isEmpty() && !newPwTextField.getText().isEmpty()) {
                user = new User(newUnameTExtField.getText(), newPwTextField.getText());
            } else if (newUnameTExtField.getText().isEmpty() && !newPwTextField.getText().isEmpty()){
                user = new User(UserRepo.user.getUserName(), newPwTextField.getText());
            } else if (!newUnameTExtField.getText().isEmpty() && newPwTextField.getText().isEmpty()){
                user = new User(newUnameTExtField.getText(), UserRepo.user.getPassword());
            } else {
                return;
            }
            try {
                if (UserRepo.changeUserDetails(user)){
                    new Alert(Alert.AlertType.CONFIRMATION,"User has been changed successfully").show();
                    UserRepo.user.setUserName(user.getUserName());
                    UserRepo.user.setPassword(user.getPassword());
                    visible(false);
                    setText();
                    currentUnameLbel.setVisible(true);
                    currentPwTextFiled.setVisible(true);
                }
            } catch (Exception e){
                new Alert(Alert.AlertType.ERROR, "Something went wrong").show();
            }
        } else {
            new Alert(Alert.AlertType.ERROR, "Passwords do not match").show();
        }
    }

    private void setText() {
        currentUnameLbel.setText(" " + UserRepo.user.getUserName());
        currentPwTextFiled.setText("");
        newPwTextField.setText("");
        newPwrReenterTextField.setText("");
        newUnameTExtField.setText("");
    }

    @FXML
    void currentPwTextFiledOnAction(ActionEvent event) {
        if (!currentPwTextFiled.getText().isEmpty()) {
            if (currentPwTextFiled.getText().equals(UserRepo.user.getPassword())) {
                visible(true);
                currentUnameLbel.setVisible(false);
                currentPwTextFiled.setVisible(false);
                newUnameTExtField.requestFocus();
            } else {
                new Alert(Alert.AlertType.ERROR,"Invalid Password").show();
            }
        }
    }

    @FXML
    void newPwTextFieldOnAction(ActionEvent event) {
        newPwrReenterTextField.requestFocus();
    }

    @FXML
    void newPwrReenterTextFieldOnAction(ActionEvent event) {
        changeBtnOnAction(event);
    }

    @FXML
    void newUnameTExtFieldOnAction(ActionEvent event) {
        newPwTextField.requestFocus();
    }

    @FXML
    void txtPasswordOnAction(ActionEvent event) {
        addBtnOnAction(event);
    }

    @FXML
    void txtUserNameOnAction(ActionEvent event) {
        txtPassword.requestFocus();
    }

    @FXML
    void addBtnOnAction(ActionEvent event) {

    }

}
