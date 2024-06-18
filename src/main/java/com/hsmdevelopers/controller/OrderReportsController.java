package com.hsmdevelopers.controller;

import com.jfoenix.controls.JFXButton;

import com.hsmdevelopers.model.TimePicker;
import com.hsmdevelopers.model.tm.OrderTm;
import com.hsmdevelopers.repo.OrdersRepo;
import com.hsmdevelopers.repo.FilterOrdersRepo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

public class OrderReportsController {

    @FXML
    private DatePicker ReportEndDatePicker;

    @FXML
    private DatePicker ReportStartDatePicker;


    @FXML
    private JFXButton fromTimeBtn;

    @FXML
    private JFXButton toTimeBTn;



    @FXML
    private TableColumn<?, ?> colDate;

    @FXML
    private TableColumn<?, ?> colDep;

    @FXML
    private TableColumn<?, ?> colPlaceTime;

    @FXML
    private TableColumn<?, ?> colTaken;

    @FXML
    private TableColumn<?, ?> colTakenTime;

    @FXML
    private TableColumn<?, ?> colName;

    @FXML
    private TableView<OrderTm> tblOrder;

    private LocalTime endTime;
    private LocalTime startTime;

    public void initialize() {
        setCellValueFactory();
        loadData();

    }

    private void setResizable() {
        colDate.prefWidthProperty().bind(tblOrder.widthProperty().multiply(0.15));
        colName.prefWidthProperty().bind(tblOrder.widthProperty().multiply(0.2));
        colDep.prefWidthProperty().bind(tblOrder.widthProperty().multiply(0.15));
        colPlaceTime.prefWidthProperty().bind(tblOrder.widthProperty().multiply(0.15));
        colTaken.prefWidthProperty().bind(tblOrder.widthProperty().multiply(0.2));
        colTakenTime.prefWidthProperty().bind(tblOrder.widthProperty().multiply(0.15));

    }
    private void loadData() {
        try {
            ObservableList<OrderTm> list = OrdersRepo.getData();
            tblOrder.setItems(list);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
    }

    private void setCellValueFactory() {
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colDep.setCellValueFactory(new PropertyValueFactory<>("department"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colPlaceTime.setCellValueFactory(new PropertyValueFactory<>("placeTime"));
        colTaken.setCellValueFactory(new PropertyValueFactory<>("taken"));
        colTakenTime.setCellValueFactory(new PropertyValueFactory<>("takenTime"));
    }



    @FXML
    void fromTimeBtnOnAction(ActionEvent event) {
        LocalTime selectedTime = TimePicker.chooseTimeAndAmPm(event);
        if (selectedTime != null) {
            startTime = selectedTime;
            fromTimeBtn.setText(startTime.toString());
        }
    }

    @FXML
    void toTimeBtnOnAction(ActionEvent event) {
        LocalTime selectedTime = TimePicker.chooseTimeAndAmPm(event);
        if (selectedTime != null) {
            endTime = selectedTime;
            toTimeBTn.setText(endTime.toString());
        }
    }

    @FXML
    void searchBtnOnAction(ActionEvent event) {
        ObservableList<OrderTm> list = FXCollections.observableArrayList();

        LocalDate startDate = ReportStartDatePicker.getValue();
        LocalDate endDate = ReportEndDatePicker.getValue();
    try {
        if (startDate != null && endDate != null && startTime != null && endTime != null) {
            list = FilterOrdersRepo.generateReport(startTime, endTime);
        }else if (startDate != null && endDate == null && startTime == null && endTime == null) {
            list = FilterOrdersRepo.generateReport(startDate);
        }else if (startDate != null && endDate != null && startTime == null && endTime == null) {
            list = FilterOrdersRepo.generateReport(startDate, endDate);
        }else if (startDate != null && endDate == null && startTime != null && endTime != null) {
            list = FilterOrdersRepo.generateReport(startDate,startTime,endTime);
        }
        tblOrder.setItems(list);
    } catch (Exception e){
        System.out.println(e.getMessage());
    }

        clearAll();

    }

    private void clearAll() {
        ReportStartDatePicker.setValue(null);
        ReportEndDatePicker.setValue(null);
        fromTimeBtn.setText("From");
        toTimeBTn.setText("To");
        endTime = null;
        startTime = null;
    }


    @FXML
    void genaratePdfBtnOnAction(ActionEvent event) {

    }



}
