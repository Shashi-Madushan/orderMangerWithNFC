package com.hsmdevelopers.controller;

import com.hsmdevelopers.model.tm.CustomOrderTm;
import com.hsmdevelopers.repo.CustomOrderRepo;
import com.hsmdevelopers.repo.EmployeeRepo;
import com.hsmdevelopers.repo.OrdersRepo;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Paint;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class HomeViewController {

    @FXML
    private BarChart<String, Number> barChart;

    @FXML
    private TableView<CustomOrderTm> customOrderTabel;

    @FXML
    private TableColumn<?, ?> customOrderTabelCountColl;

    @FXML
    private TableColumn<?, ?> customOrderTabelDesecriptionColl;

    @FXML
    private Label dateLabel;


    @FXML
    private LineChart<String, Number> lineChart;



    @FXML
    private Label takenOrderCountLabel;

    @FXML
    private Label totalEmployeeLabel;

    @FXML
    private Label totalOrderCountLabel;

    public void initialize() {
        dateLabel.setText(String.valueOf(LocalDate.now()));
        setCounts();
        setCellValueFactory();
        loadData();
        setDataOnBarChart();
        setDataOnLineChart();
    }

    private void setDataOnLineChart() {
        lineChart.getData().clear();

        int[] orderCountData = OrdersRepo.getThisMonthOrderCount();
        int[] customOrderCount = CustomOrderRepo.getThisMonthCustomOrderCount();
        int[] totalOrderCounts = sumArrays(orderCountData,customOrderCount);
        String[] daysOfMonth = getDaysOfCurrentMonth();

        XYChart.Series<String, Number> totalOrdersSeries = new XYChart.Series<>();
        totalOrdersSeries.setName("Total Orders");
        for (int i = 0; i < daysOfMonth.length; i++) {
            totalOrdersSeries.getData().add(new XYChart.Data<>(daysOfMonth[i], totalOrderCounts[i]));
        }

        int[] takenOrderCounts = OrdersRepo.getThisMonthTakenOrderCount();
        int[] totalTakenOrderCounts = sumArrays(takenOrderCounts,customOrderCount);
        XYChart.Series<String, Number> customOrdersSeries = new XYChart.Series<>();
        customOrdersSeries.setName("Taken Orders");
        for (int i = 0; i < daysOfMonth.length; i++) {
            customOrdersSeries.getData().add(new XYChart.Data<>(daysOfMonth[i], totalTakenOrderCounts[i]));
        }

        lineChart.getData().addAll(totalOrdersSeries,customOrdersSeries);
    }

    private String[] getDaysOfCurrentMonth() {
        LocalDate now = LocalDate.now();
        int daysInMonth = now.lengthOfMonth();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d");
        List<String> days = new ArrayList<>();
        for (int i = 1; i <= daysInMonth; i++) {
            days.add(formatter.format(LocalDate.of(now.getYear(), now.getMonth(), i)));
        }
        return days.toArray(new String[0]);
    }

    private void setDataOnBarChart() {
        barChart.getData().clear();

        int[] orderCountData = OrdersRepo.getLast5DaysCount();
        int[] customOrderCount = CustomOrderRepo.getLast5DaysCustomOrderCount();
        int[] totalOrderCounts = sumArrays(orderCountData,customOrderCount);
        String[] daysOfWeek = reverseArrays();

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Total Orders");
        for (int i = 0; i < daysOfWeek.length; i++) {
            XYChart.Data<String, Number> data = new XYChart.Data<>(daysOfWeek[i], totalOrderCounts[i]);
            series.getData().add(data);


            // Set the label for each data point
            Label label = new Label(String.valueOf(totalOrderCounts[i]));
            label.setStyle("-fx-font-size: 10px; ");
            label.setAlignment(Pos.TOP_CENTER);
            label.setTextFill(Paint.valueOf("white"));
            data.setNode(label);
        }
        for (XYChart.Data<String, Number> data : series.getData()) {
            data.getNode().setStyle("-fx-bar-fill: DODGERBLUE;");
        }

        int[] takenOrderCounts = OrdersRepo.getLast5DaysTakenOrderCount();
        int[] totalTakenOrderCounts = sumArrays(takenOrderCounts,customOrderCount);
        // Second series: Custom Orders
        XYChart.Series<String, Number> customOrdersSeries = new XYChart.Series<>();
        customOrdersSeries.setName("Taken Orders");
        for (int i = 0; i < daysOfWeek.length; i++) {
            XYChart.Data<String, Number> data = new XYChart.Data<>(daysOfWeek[i], totalTakenOrderCounts[i]);
            customOrdersSeries.getData().add(data);

            // Set the label for each data point
            Label label = new Label(String.valueOf(totalTakenOrderCounts[i]));
            label.setStyle("-fx-font-size: 10px; ");
            label.setAlignment(Pos.TOP_CENTER);
            label.setTextFill(Paint.valueOf("white"));
            data.setNode(label);
        }
        for (XYChart.Data<String, Number> data : customOrdersSeries.getData()) {
            data.getNode().setStyle("-fx-bar-fill: ORANGE;");
        }

        barChart.getData().addAll(series,customOrdersSeries);
    }

    private int[] sumArrays(int[] orderCountData, int[] customOrderCount) {
        int[] totalOrderCounts = new int[customOrderCount.length];

        for (int i = 0; i < customOrderCount.length; i++) {
            totalOrderCounts[i] = customOrderCount[i] + orderCountData[i];
        }
        return totalOrderCounts;
    }

    private String[] getLastDaysArray() {
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        return IntStream.range(0, 5).mapToObj(i -> today.minusDays(i).format(formatter)).toArray(String[]::new);
    }

    private String[] reverseArrays(){
        String[] array = getLastDaysArray();
        String[] reversedArray = new String[array.length];
        for (int i = 0; i < array.length; i++) {
            reversedArray[i] = array[array.length - 1 - i];
        }
        return reversedArray;
    }

    private void loadData() {
        try {
            ObservableList<CustomOrderTm> list = CustomOrderRepo.getData();
            customOrderTabel.setItems(list);
        } catch (SQLException e) {
        }
    }

    private void setCellValueFactory() {
        customOrderTabelDesecriptionColl.setCellValueFactory(new PropertyValueFactory<>("description"));
        customOrderTabelCountColl.setCellValueFactory(new PropertyValueFactory<>("orderCount"));
    }

    private void setCounts() {
        try {
            int employeeCount = EmployeeRepo.getEmployeeCount();
            totalEmployeeLabel.setText(String.valueOf(employeeCount));

            int orderCount = OrdersRepo.getTotalOrder();
            int customOrderCount = CustomOrderRepo.getOrderCount();
            totalOrderCountLabel.setText(String.valueOf(orderCount + customOrderCount));

            int takenOrderCount = OrdersRepo.getTakenOrderCount();
            takenOrderCountLabel.setText(String.valueOf(takenOrderCount + customOrderCount));
        } catch (Exception e){
        }
    }







}
