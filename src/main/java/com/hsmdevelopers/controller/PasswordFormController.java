package com.hsmdevelopers.controller;

import com.hsmdevelopers.model.User;
import com.hsmdevelopers.repo.UserRepo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lombok.Setter;


public class PasswordFormController {

    @FXML
    private ImageView imageView;

    @FXML
    private Label labelName;

    @FXML
    private AnchorPane passwordForm;

    @FXML
    private PasswordField txtPassword;

    @Setter
    private User user;

    @Setter
    private SettingsViewController settingsViewController;

    public void initialize(){
        labelName.setText(UserRepo.user.getUserName());
    }

    @FXML
    void txtPasswordAction(ActionEvent event) {
        if (txtPassword.getText().equals(UserRepo.user.getPassword())){
            settingsViewController.onPasswordFormSuccess(user);
            Stage stage = (Stage) this.passwordForm.getScene().getWindow();
            stage.close();
        } else {
            new Alert(Alert.AlertType.WARNING,"Incorrect Password !!").show();
        }
    }

    @FXML
    void btnCancelOnAction(ActionEvent event) {
        Stage stage = (Stage) this.passwordForm.getScene().getWindow();
        stage.close();
    }
}
