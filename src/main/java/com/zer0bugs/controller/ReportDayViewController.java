package com.zer0bugs.controller;

import com.jfoenix.controls.JFXButton;
import com.zer0bugs.model.TimePicker;
import com.zer0bugs.model.tm.OrderTm;
import com.zer0bugs.repo.DayViewRepo;
import com.zer0bugs.util.GlobalVariables;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import lombok.Setter;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalTime;

public class ReportDayViewController {

    @Setter
    DashboardController dashboardController ;

    @FXML
    private TableColumn<?, ?> colDate;

    @FXML
    private TableColumn<?, ?> colDep;

    @FXML
    private TableColumn<?, ?> colName;

    @FXML
    private TableColumn<?, ?> colPlaceTime;

    @FXML
    private TableColumn<?, ?> colTaken;

    @FXML
    private TableColumn<?, ?> colTakenTime;

    @FXML
    private JFXButton fromTimeBtn;

    @FXML
    private Label takenOrdersLbl;

    @FXML
    private TableView<OrderTm> tblOrder;

    @FXML
    private JFXButton toTimeBTn;

    @FXML
    private Label totalOrdersLbl;

    private LocalTime endTime;
    private LocalTime startTime;
    private Date passedDate;


    public void initialize(Date date) {
        setCellValueFactory();
        loadData(date);
        passedDate = date;
    }
    private void loadData(Date date) {
        try {
            ObservableList<OrderTm> list = DayViewRepo.getData(date);
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

        try {
            if (startTime != null && endTime != null) {
                list = DayViewRepo.generateReport(startTime,endTime,passedDate);
            } else if (startTime != null && endTime == null) {
                list = DayViewRepo.generateReport(startTime,passedDate);
            } else if (startTime == null && endTime == null) {
                new Alert(Alert.AlertType.WARNING,"Select a Time Range !!").show();
            } else if (startTime == null && endTime != null) {
                new Alert(Alert.AlertType.WARNING,"Start Time can't be empty !!").show();
            }
            tblOrder.setItems(list);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }

        clearAll();

    }

    private void clearAll() {
        fromTimeBtn.setText("From");
        toTimeBTn.setText("To");
        startTime = null;
        endTime = null;
    }

    @FXML
    void backOnClick(MouseEvent event) {
        String from = GlobalVariables.from;
        if(from.equals("month")){
            dashboardController.loadMonthView();
        } else if (from.equals("custom")) {
            dashboardController.loadCustomRangeView();
        }
    }

}
