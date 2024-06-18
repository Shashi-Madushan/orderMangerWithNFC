package com.hsmdevelopers.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

import java.time.Month;
import java.time.YearMonth;

public class YearMonthPicker {

    public static YearMonth chooseYearMonth() {
        Dialog<YearMonth> dialog = new Dialog<>();
        dialog.setTitle("Select Year and Month");
        dialog.setHeaderText("Choose a year and month:");

        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        TextField yearField = new TextField();
        yearField.setPromptText("Year");

        ComboBox<String> monthBox = new ComboBox<>();
        ObservableList<String> months = FXCollections.observableArrayList();
        months.addAll("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December");
        monthBox.setItems(months);
        monthBox.setPromptText("Month");

        HBox content = new HBox(10);
        content.getChildren().addAll(yearField, monthBox);
        dialogPane.setContent(content);

        dialog.setResultConverter((ButtonType button) -> {
            if (button == ButtonType.OK) {
                if (yearField.getText().isEmpty() || monthBox.getValue() == null) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Warning");
                    alert.setHeaderText("Incomplete Year and Month Selection");
                    alert.setContentText("Please enter a year and select a month.");
                    alert.showAndWait();
                    return null; // Return null to prevent dialog from closing
                }
                int year = Integer.parseInt(yearField.getText());
                int month = Month.valueOf(monthBox.getValue().toUpperCase()).getValue();

                return YearMonth.of(year, month);
            }
            return null;
        });

        try {
            return dialog.showAndWait().orElse(null);
        } catch (IllegalStateException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Incomplete Year and Month Selection");
            alert.setContentText("Please enter a year and select a month.");
            alert.showAndWait();
            return null;
        }
    }
}