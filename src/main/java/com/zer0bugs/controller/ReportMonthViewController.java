package com.zer0bugs.controller;

import com.jfoenix.controls.JFXButton;

import com.zer0bugs.model.YearMonthPicker;
import com.zer0bugs.model.tm.MonthViewTm;
import com.zer0bugs.repo.MonthViewRepo;
import com.zer0bugs.util.GlobalVariables;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.scene.input.MouseEvent;
import lombok.Setter;


import java.sql.SQLException;
import java.time.LocalDate;

import java.time.YearMonth;


public class ReportMonthViewController {

    String from= "month";

    @Setter
    DashboardController dashboardController;

    @FXML
    private TableColumn<?, ?> colDate;

    @FXML
    private TableColumn<?, ?> colOrder;

    @FXML
    private TableColumn<?, ?> colTakenOrders;

    @FXML
    private JFXButton monthSelectBtm;

    @FXML
    private TableView<MonthViewTm> monthViewTabel;

    @FXML
    private Label takenOrderCountLbl;

    @FXML
    private Label totalOrderCountLbl;


    private YearMonth yearMonth;

    public void initialize() {
        setCellValueFactory();
        loadData(LocalDate.now().getYear(),LocalDate.now().getMonthValue());
        setTotalOrderAndTakenCount();
    }



    private void setTotalOrderAndTakenCount() {
        ObservableList<MonthViewTm> list = monthViewTabel.getItems();
        int totalOrders = 0;
        int takenOrders = 0;
        for (MonthViewTm monthViewTm : list) {
            totalOrders += monthViewTm.getTotalOrder();
            takenOrders += monthViewTm.getTotalTaken();
        }
        totalOrderCountLbl.setText(String.valueOf(totalOrders));
        takenOrderCountLbl.setText(String.valueOf(takenOrders));
    }

    private void loadData(int year, int monthValue) {
        try {
            ObservableList<MonthViewTm> list = MonthViewRepo.getData(year, monthValue);
            ObservableList<MonthViewTm> finalList = sumArray(list);
            monthViewTabel.setItems(finalList);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
    }

    private ObservableList<MonthViewTm> sumArray(ObservableList<MonthViewTm> list) {
        ObservableList<MonthViewTm> finalList = FXCollections.observableArrayList();

        for (int i = 0; i < list.size() -1; i++) {
            if (list.get(i).getData().equals(list.get(i+1).getData())) {
                finalList.add(new MonthViewTm(list.get(i).getData(),list.get(i).getTotalOrder() + list.get(i + 1).getTotalOrder(),list.get(i).getTotalTaken() + list.get(i + 1).getTotalTaken()));
                i++;
                continue;
            }
            finalList.add(new MonthViewTm(list.get(i).getData(),list.get(i).getTotalOrder(),list.get(i).getTotalTaken()));
        }
        return finalList;
    }

    private void setCellValueFactory() {
        colDate.setCellValueFactory(new PropertyValueFactory<>("data"));
        colOrder.setCellValueFactory(new PropertyValueFactory<>("totalOrder"));
        colTakenOrders.setCellValueFactory(new PropertyValueFactory<>("totalTaken"));
    }

    
    @FXML
    void tableRowOnClick(MouseEvent event) {
        MonthViewTm selectedItem = monthViewTabel.getSelectionModel().getSelectedItem();
        GlobalVariables.yearMonthMonthView = yearMonth;
        GlobalVariables.from = from;
        dashboardController.loadDayView(selectedItem.getData());
        
    }

    public void seachBtnOnClick(javafx.event.ActionEvent event) {
      seacrchBtnLoadData();
    }

    public void seacrchBtnLoadData(){
        if(yearMonth == null){
            yearMonth = YearMonth.now();
        }

        int year = yearMonth.getYear();
        int month = yearMonth.getMonthValue();
        loadData(year, month);
        setTotalOrderAndTakenCount();
    }
    public void monthSelectBtmOnClick(javafx.event.ActionEvent event) {
        yearMonth = YearMonthPicker.chooseYearMonth();
        monthSelectBtm.setText(String.valueOf(yearMonth));
    }

    public void setYearMonth(YearMonth yearMonth) {
        this.yearMonth = yearMonth;
    }


}
