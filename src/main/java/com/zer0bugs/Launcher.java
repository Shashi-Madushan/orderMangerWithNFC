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
                primaryStage.setTitle("Organizer");
                primaryStage.setScene(scene);
                primaryStage.setMinWidth(1050);
                primaryStage.setMinHeight(650);

                // Show the stage
                primaryStage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public static void main(String[] args) {
            launch(args);
        }


    }


