package com.zer0bugs;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Launcher extends Application {
        @Override
        public void start(Stage primaryStage) {
            try {
                // Load the FXML file
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/login.fxml"));

                Scene scene = new Scene(loader.load());

                // Set the stage properties
                primaryStage.setScene(scene);
                primaryStage.setResizable(false);

                primaryStage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public static void main(String[] args) {
            launch(args);
        }


    }


