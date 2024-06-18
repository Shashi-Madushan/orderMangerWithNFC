package com.zer0bugs.controller;

import com.zer0bugs.model.User;
import com.zer0bugs.model.tm.UserTm;
import com.zer0bugs.repo.UserRepo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lombok.Setter;

import java.io.IOException;
import java.sql.SQLException;


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

}
