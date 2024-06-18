package com.zer0bugs.controller;

import com.jfoenix.controls.JFXButton;
import com.zer0bugs.util.GlobalVariables;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Date;

import java.io.IOException;
import java.time.LocalDate;


public class DashboardController {

    @FXML
    private ImageView custonOrdersImageView;

    @FXML
    private JFXButton customOrdersBtn;
    
    @FXML
    private JFXButton customRangeBtn;

    @FXML
    private ImageView customeBtnImageView;

    @FXML
    private JFXButton employeeBtn;

    @FXML
    private ImageView employeeImageView;

    @FXML
    private JFXButton homeBtn;

    @FXML
    private ImageView homeImageView;

    @FXML
    private AnchorPane innerPane;

    @FXML
    private JFXButton monthBtn;

    @FXML
    private ImageView monthlyBtnImageView;

    @FXML
    private JFXButton reportBtn;

    @FXML
    private ImageView reportImageView;

    @FXML
    private VBox sidePane;

    @FXML
    private JFXButton todayBtn;

    @FXML
    private ImageView todayBtnImageView;

    @FXML
    private AnchorPane rootAncherPane;


    private Date todaysDate;


    public void initialize(){
        todaysDate = Date.valueOf(LocalDate.now());
        defaultStyle();
        setReportBtnHidden();
        changeStyle(homeBtn,homeImageView);
        loadDefaultView();
    }

    private void setReportBtnHidden() {
        todayBtn.setVisible(false);
        monthBtn.setVisible(false);
        customRangeBtn.setVisible(false);
    }
    private void setReportBtnVisible() {
        todayBtn.setVisible(true);
        monthBtn.setVisible(true);
        customRangeBtn.setVisible(true);
    }


    private void loadDefaultView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/home.fxml"));
            AnchorPane homeAnchorPane = loader.load();
            homeAnchorPane.setPrefSize(600, 400);
            homeAnchorPane.setMinSize(600,400);

            // Set AnchorPane constraints to make it resizable
            AnchorPane.setTopAnchor(homeAnchorPane, 3.0);
            AnchorPane.setBottomAnchor(homeAnchorPane, 3.0);
            AnchorPane.setLeftAnchor(homeAnchorPane, 3.0);
            AnchorPane.setRightAnchor(homeAnchorPane, 3.0);

            innerPane.getChildren().setAll(homeAnchorPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void employeeBtnOnClick(ActionEvent event) {
        defaultStyle();
        changeStyle(employeeBtn,employeeImageView);
        loadEmployeeView();
    }

    public void loadEmployeeView() {
        reSet();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/employee.fxml"));
            AnchorPane EmployeeAnchorPane = loader.load();
            EmployeeAnchorPane.setPrefSize(600, 400);
            EmployeeAnchorPane.setMaxSize(600,400);
            EmployeeController controller = loader.getController();
            controller.setDashboardController(this);


            AnchorPane.setTopAnchor(EmployeeAnchorPane, 3.0);
            AnchorPane.setBottomAnchor(EmployeeAnchorPane, 3.0);
            AnchorPane.setLeftAnchor(EmployeeAnchorPane, 3.0);
            AnchorPane.setRightAnchor(EmployeeAnchorPane, 3.0);

            innerPane.getChildren().setAll(EmployeeAnchorPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void customOrdersBtnOnAction(ActionEvent event) {
        defaultStyle();
        changeStyle(customOrdersBtn,custonOrdersImageView);
        loadCustomOrdersView();
        reSet();
    }

    private void loadCustomOrdersView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/customeOrdersView.fxml"));
            AnchorPane customOrdersAncherPAne = loader.load();
            customOrdersAncherPAne.setPrefSize(600, 400);
            customOrdersAncherPAne.setMinSize(600,400);


            AnchorPane.setTopAnchor(customOrdersAncherPAne, 3.0);
            AnchorPane.setBottomAnchor(customOrdersAncherPAne, 3.0);
            AnchorPane.setLeftAnchor(customOrdersAncherPAne, 3.0);
            AnchorPane.setRightAnchor(customOrdersAncherPAne, 3.0);

            innerPane.getChildren().setAll(customOrdersAncherPAne);
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception accordingly
        }
    }

    @FXML
    void homeBtnOnClick(ActionEvent event) {
        defaultStyle();
        changeStyle(homeBtn,homeImageView);
        loadDefaultView();
        reSet();

    }

    @FXML
    void reportBtnOnClick(ActionEvent event) {
        defaultStyle();
        reportBtnIconChange();


    }

    private void reportBtnIconChange() {
        Image closeImage = new Image("/assets/cancel.png");
        if (!reportImageView.getImage().getUrl().equals(closeImage.getUrl())) {
            set(closeImage);
            return;
        }

        if (reportImageView.getImage().getUrl().equals(closeImage.getUrl())) {
            reSet();
        }

    }
    private void set(Image closeImage){
        reportImageView.setImage(closeImage);
        reportBtn.setText("");
        setReportBtnVisible();
    }

    private void reSet(){
        reportImageView.setImage(new Image("/assets/reportsgray.png"));
        reportBtn.setText("   Reports      ");
        setReportBtnHidden();
    }

    @FXML
    void customRangeBtnOnClick(ActionEvent event) {
        defaultStyle();
        changeStyle(customRangeBtn,customeBtnImageView);
        loadCustomRangeView();
    }
public void  loadCustomRangeView(){
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/reportsCustomView.fxml"));
        AnchorPane reportsCustomViewAncherpane = loader.load();
        ReportCustomViewController controller = loader.getController();
        controller.setDashboardController(this);
        controller.searchBtnLoadDate();

        reportsCustomViewAncherpane.setPrefSize(600, 400);
        reportsCustomViewAncherpane.setMinSize(600,400);

        // Set AnchorPane constraints to make it resizable
        AnchorPane.setTopAnchor(reportsCustomViewAncherpane, 3.0);
        AnchorPane.setBottomAnchor(reportsCustomViewAncherpane, 3.0);
        AnchorPane.setLeftAnchor(reportsCustomViewAncherpane, 3.0);
        AnchorPane.setRightAnchor(reportsCustomViewAncherpane, 3.0);

        innerPane.getChildren().setAll(reportsCustomViewAncherpane);
    } catch (IOException e) {
        e.printStackTrace(); // Handle the exception accordingly
    }
}

    @FXML
    void monthViewOnClick(ActionEvent event) {
        defaultStyle();
        changeStyle(monthBtn,monthlyBtnImageView);
        loadMonthView();

    }
    public void loadMonthView(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/reportMonthView.fxml"));
            AnchorPane reportMonthView = loader.load();
            ReportMonthViewController controller = loader.getController();
            controller.setDashboardController(this);
            controller.setYearMonth(GlobalVariables.yearMonthMonthView);
            controller.seacrchBtnLoadData();
            reportMonthView.setPrefSize(600, 400);
            reportMonthView.setMinSize(600,400);

            // Set AnchorPane constraints to make it resizable
            AnchorPane.setTopAnchor(reportMonthView, 3.0);
            AnchorPane.setBottomAnchor(reportMonthView, 3.0);
            AnchorPane.setLeftAnchor(reportMonthView, 3.0);
            AnchorPane.setRightAnchor(reportMonthView, 3.0);

            innerPane.getChildren().setAll(reportMonthView);
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception accordingly
        }
    }

    @FXML
    void todayBtnOnClick(ActionEvent event) {
        loadDayView(todaysDate);
        defaultStyle();
        changeStyle(todayBtn,todayBtnImageView);
    }

    public void loadDayView(Date date){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/reportsDayView.fxml"));
            AnchorPane reportsDayViewAncherpane = loader.load();
            ReportDayViewController controller = loader.getController();
            controller.initialize(date);
            controller.setDashboardController(this);
            reportsDayViewAncherpane.setPrefSize(600, 400);
            reportsDayViewAncherpane.setMinSize(600,400);

            // Set AnchorPane constraints to make it resizable
            AnchorPane.setTopAnchor(reportsDayViewAncherpane, 3.0);
            AnchorPane.setBottomAnchor(reportsDayViewAncherpane, 3.0);
            AnchorPane.setLeftAnchor(reportsDayViewAncherpane, 3.0);
            AnchorPane.setRightAnchor(reportsDayViewAncherpane, 3.0);

            innerPane.getChildren().setAll(reportsDayViewAncherpane);
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception accordingly
        }

    }


    private void loadReportsView(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/orderReports.fxml"));
            AnchorPane orderReportsAncherpane = loader.load();
            orderReportsAncherpane.setPrefSize(600, 400);
            orderReportsAncherpane.setMinSize(600,400);

            // Set AnchorPane constraints to make it resizable
            AnchorPane.setTopAnchor(orderReportsAncherpane, 3.0);
            AnchorPane.setBottomAnchor(orderReportsAncherpane, 3.0);
            AnchorPane.setLeftAnchor(orderReportsAncherpane, 3.0);
            AnchorPane.setRightAnchor(orderReportsAncherpane, 3.0);

            innerPane.getChildren().setAll(orderReportsAncherpane);
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception accordingly
        }
    }



    private void changeStyle(Button button, ImageView view){
        button.setStyle("-fx-text-fill: white;-fx-background-color: DODGERBLUE;");
        Image image = null;

        if (view.equals(employeeImageView)) {
            image = new Image("/assets/employeewhite.png");
        } else if (view.equals(homeImageView)) {
            image = new Image("/assets/homewhite.png");
        } else if (view.equals(monthlyBtnImageView)) {
            image = new Image("/assets/monthlyViewWhite.png");
        } else if (view.equals(todayBtnImageView)) {
            image = new Image("/assets/dailyViewWhite.png");
        } else if (view.equals(customeBtnImageView)) {
            image = new Image("/assets/customeWhite.png");
        }else if(view.equals(custonOrdersImageView)){
            image = new Image("/assets/ordersWhite.png");

        }
        view.setImage(image);
    }

    private void defaultStyle() {
        String style = "-fx-text-fill: #756868;-fx-background-color: white;";
        employeeBtn.setStyle(style);
        homeBtn.setStyle(style);
        monthBtn.setStyle(style);
        todayBtn.setStyle(style);
        customRangeBtn.setStyle(style);
        customOrdersBtn.setStyle(style);

        //reportBtn.setStyle(style);

        Image image ;

        image = new Image("/assets/employeegray.png");
        employeeImageView.setImage(image);
        image = new Image("/assets/homegray.png");
        homeImageView.setImage(image);
        image = new Image("/assets/monthlyView.png");
        monthlyBtnImageView.setImage(image);
        image = new Image("/assets/dailyView.png");
        todayBtnImageView.setImage(image);
        image = new Image("/assets/customeGray.png");
        customeBtnImageView.setImage(image);
        image = new Image("/assets/ordersGray.png");
        custonOrdersImageView.setImage(image);

    }

    public void settingBtnOnAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/settings.fxml"));
            AnchorPane settingsAncherPAne = loader.load();
            settingsAncherPAne.setPrefSize(600, 400);
            settingsAncherPAne.setMinSize(600,400);

            // Set AnchorPane constraints to make it resizable
            AnchorPane.setTopAnchor(settingsAncherPAne, 3.0);
            AnchorPane.setBottomAnchor(settingsAncherPAne, 3.0);
            AnchorPane.setLeftAnchor(settingsAncherPAne, 3.0);
            AnchorPane.setRightAnchor(settingsAncherPAne, 3.0);

            innerPane.getChildren().setAll(settingsAncherPAne);
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception accordingly
        }
    }
    @FXML
    void logOutBtnOnAction(ActionEvent event) {
        defaultStyle();
        Stage stage = (Stage) innerPane.getScene().getWindow();
        stage.close();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/login.fxml"));
            Parent root = loader.load();
            Stage loginStage = new Stage();
            loginStage.setScene(new Scene(root));
            loginStage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception accordingly
        }
    }
}
