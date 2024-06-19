package com.hsmdevelopers.controller;

import com.hsmdevelopers.model.User;
import com.hsmdevelopers.model.tm.UserTm;
import com.hsmdevelopers.repo.UserRepo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import com.jfoenix.controls.JFXButton;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

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
    public TableView<UserTm> tblUserDetails;

    @FXML
    private TextField txtPassword;

    @FXML
    private TextField txtUserName;

    @FXML
    private Pane changeDetailsPane;

    ObservableList<UserTm> users = FXCollections.observableArrayList();

    public void initialize() {
        currentUnameLbel.setText(" " + UserRepo.user.getUserName());
        visible(false);
        setCellValueFactory();
        isShowAddUser();
        tblUserDetails.setStyle("-fx-border-width: 1px;");
    }

    private void isShowAddUser() {
        if (UserRepo.user.getId() == 1){
            addUserAnchorpane.setVisible(true);
            loadAllData();
        } else {
            addUserAnchorpane.setVisible(false);
        }
    }

    private void loadAllData() {
        users.clear();
        try {
            ObservableList<User> list = UserRepo.getAllData();
            for (int i = 0; i < list.size(); i++) {
                users.add(getTableData(list.get(i)));
            }
            tblUserDetails.setItems(users);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
    }

    public UserTm getTableData(User user) {
        return new UserTm(user.getUserName(), user.getPassword(), createDeleteButton());
    }

    public Button createDeleteButton() {
        Button button = new Button("Delete");
        button.setStyle("-fx-background-color: red;");

        button.setOnAction((e) -> {
            ButtonType yes = new ButtonType("yes", ButtonBar.ButtonData.OK_DONE);
            ButtonType no = new ButtonType("no", ButtonBar.ButtonData.CANCEL_CLOSE);

            Optional<ButtonType> type = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure to remove?", yes, no).showAndWait();

            if (type.orElse(no) == yes) {
                UserTm selectedIndex = tblUserDetails.getSelectionModel().getSelectedItem();
                try{
                    if (UserRepo.delete(selectedIndex)){
                        users.clear();
                        loadAllData();
                        new Alert(Alert.AlertType.CONFIRMATION,"Delete Successfully !!").show();
                    }
                } catch (Exception exception){
                    new Alert(Alert.AlertType.INFORMATION,"Select Column And Remove !!").show();
                }
            }
        });

        return button;
    }

    private void setCellValueFactory() {
        colUserName.setCellValueFactory(new PropertyValueFactory<>("username"));
        colPassword.setCellValueFactory(new PropertyValueFactory<>("password"));
        colDeleteBtn.setCellValueFactory(new PropertyValueFactory<>("deleteButton"));
    }

    private void visible(boolean isVisible) {
        changeDetailsPane.setVisible(isVisible);
//        newUnameTExtField.setVisible(isVisible);
//        newPwTextField.setVisible(isVisible);
//        newPwrReenterTextField.setVisible(isVisible);
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
        currentPwTextFiled.clear();
        newPwTextField.clear();
        newPwrReenterTextField.clear();
        newUnameTExtField.clear();
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
        if (!txtUserName.getText().isEmpty() && !txtPassword.getText().isEmpty()) {
            User user = new User(txtUserName.getText(), txtPassword.getText());
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/password_form.fxml"));
                Parent root = loader.load();

                // Get the controller of the password form
                PasswordFormController passwordFormController = loader.getController();
                passwordFormController.setUser(user); // Pass the user object to the password form
                passwordFormController.setSettingsViewController(this); // Pass the main controller to the password form

                Stage loginStage = new Stage();
                loginStage.setScene(new Scene(root));
                loginStage.initStyle(StageStyle.UNDECORATED);
                loginStage.centerOnScreen();
                loginStage.show();
            } catch (IOException e) {
                e.printStackTrace(); // Handle the exception accordingly
            }
        }
    }

    // Method to be called from PasswordFormController
    public void onPasswordFormSuccess(User user) {
        try {
            if (UserRepo.save(user)) {
                new Alert(Alert.AlertType.INFORMATION, "User added successfully").show();
                tblUserDetails.getItems().add(new UserTm(user.getUserName(), user.getPassword(), createDeleteButton()));
                clear();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    private void clear() {
        txtUserName.setText("");
        txtPassword.setText("");
    }


}
