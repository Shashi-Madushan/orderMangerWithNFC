package com.hsmdevelopers.controller;

import com.hsmdevelopers.model.User;
import com.hsmdevelopers.repo.UserRepo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML
    private AnchorPane loginAnchorPane;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private TextField txtUserName;

    @FXML
    private VBox vBox;

    public void initialize() {
//        Platform.runLater(() -> {
//            // Bind the VBox's layoutX and layoutY properties to center it within the AnchorPane
//            vBox.layoutXProperty().bind(loginAnchorPane.widthProperty().subtract(vBox.widthProperty()).divide(2));
//            vBox.layoutYProperty().bind(loginAnchorPane.heightProperty().subtract(vBox.heightProperty()).divide(2));
//        });
    }

    @FXML
    void loginOnAction(ActionEvent event) {
        String userName = txtUserName.getText();
        String password = txtPassword.getText();

        User user = new User(userName, password);

        try {
            if (UserRepo.checkUser(user)){
                navigateDashboard();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void closeStage() {
        Stage stage = (Stage) loginAnchorPane.getScene().getWindow();
        stage.close();
    }

    private void navigateDashboard() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/dashboard.fxml"));
        Parent root = loader.load();
        DashboardController controller = loader.getController();
        controller.initialize();

        Scene scene = new Scene(root, 1000, 600);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setMinWidth(1050);
        stage.setMinHeight(650);

        // Show the stage
        stage.show();
        closeStage();
    }

    @FXML
    void txtPasswordOnAction(ActionEvent event) {
        loginOnAction(event);
    }

    @FXML
    void txtUserNameOnAction(ActionEvent event) {
        txtPassword.requestFocus();
    }
}
