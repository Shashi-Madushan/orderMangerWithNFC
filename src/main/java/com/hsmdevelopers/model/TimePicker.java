package com.hsmdevelopers.model;

import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

import java.time.LocalTime;
import java.util.stream.IntStream;

public class TimePicker {
    public static LocalTime chooseTimeAndAmPm(ActionEvent actionEvent) {
        Dialog<LocalTime> dialog = new Dialog<>();
        dialog.setTitle("Select Time");
        dialog.setHeaderText("Choose a time:");

        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        ComboBox<Integer> hourBox = new ComboBox<>();
        IntStream.rangeClosed(1, 12).forEach(hourBox.getItems()::add);

        ComboBox<Integer> minuteBox = new ComboBox<>();
        IntStream.rangeClosed(0, 59).forEach(minuteBox.getItems()::add);

        ComboBox<String> amPmBox = new ComboBox<>();
        amPmBox.getItems().addAll("AM", "PM");

        HBox content = new HBox(10);
        content.getChildren().addAll(hourBox, minuteBox, amPmBox);
        dialogPane.setContent(content);

        dialog.setResultConverter((ButtonType button) -> {
            if (button == ButtonType.OK) {
                if (hourBox.getValue() == null || minuteBox.getValue() == null || amPmBox.getValue() == null) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Warning");
                    alert.setHeaderText("Incomplete Time Selection");
                    alert.setContentText("Please select hour, minute, and AM/PM.");
                    alert.showAndWait();
                    return null; // Return null to prevent dialog from closing
                }
                int hour = amPmBox.getValue().equals("PM") ? hourBox.getValue() + 12 : hourBox.getValue();
                return LocalTime.of(hour, minuteBox.getValue());
            }
            return null;
        });

        try {
            return dialog.showAndWait().orElse(null);
        } catch (IllegalStateException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Incomplete Time Selection");
            alert.setContentText("Please select hour, minute, and AM/PM.");
            alert.showAndWait();
            return null;
        }
    }
}