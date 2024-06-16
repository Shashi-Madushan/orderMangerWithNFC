package com.zer0bugs.controller;

import com.zer0bugs.model.tm.MonthViewTm;
import com.zer0bugs.repo.CustomViewRepo;
import com.zer0bugs.repo.MonthViewRepo;
import com.zer0bugs.util.GlobalVariables;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import lombok.Setter;

import java.sql.Date;
import java.sql.SQLException;

public class ReportCustomViewController {
    Date endDate;
    Date startDate;
    String from= "custom";
    @Setter
    private DashboardController dashboardController;

    @FXML
    private DatePicker ReportEndDatePicker;

    @FXML
    private DatePicker ReportStartDatePicker;

    @FXML
    private TableColumn<?, ?> colDate;

    @FXML
    private TableColumn<?, ?> colOrder;

    @FXML
    private TableColumn<?, ?> colTakenOrders;

    @FXML
    private TableView<MonthViewTm> monthViewTabel;

    @FXML
    private Label takenOrderCountLbl;

    @FXML
    private Label totalOrderCountLbl;

    public void initialize() {
        setCellValueFactory();
    }

    private void setCellValueFactory() {
        colDate.setCellValueFactory(new PropertyValueFactory<>("data"));
        colOrder.setCellValueFactory(new PropertyValueFactory<>("totalOrder"));
        colTakenOrders.setCellValueFactory(new PropertyValueFactory<>("totalTaken"));
    }

    @FXML
    void searchBtnOnAction(ActionEvent event) {
       searchBtnLoadDate();
    }
    public void searchBtnLoadDate() {
        if (ReportStartDatePicker.getValue() == null || ReportEndDatePicker.getValue() == null) {
            startDate= GlobalVariables.startDate;
            endDate = GlobalVariables.endDate;

        }else {
            startDate = Date.valueOf(ReportStartDatePicker.getValue());
            endDate = Date.valueOf(ReportEndDatePicker.getValue());
        }

        loadData(startDate, endDate);
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

    private void loadData(Date startDate, Date endDate) {
        try {
            ObservableList<MonthViewTm> list = CustomViewRepo.getData(startDate, endDate);
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

    @FXML
    void tableRowOnClick(MouseEvent event) {
        GlobalVariables.from = this.from;
        GlobalVariables.startDate = startDate;
        GlobalVariables.endDate = endDate;
        MonthViewTm selectedItem = monthViewTabel.getSelectionModel().getSelectedItem();
        dashboardController.loadDayView(selectedItem.getData());
    }


    @FXML
    void genaratePdfBtnOnAction(ActionEvent event) {

    }

}
