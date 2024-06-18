package com.hsmdevelopers.controller;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.jfoenix.controls.JFXButton;

import com.hsmdevelopers.model.YearMonthPicker;
import com.hsmdevelopers.model.tm.MonthViewTm;
import com.hsmdevelopers.repo.MonthViewRepo;
import com.hsmdevelopers.util.GlobalVariables;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.Setter;


import java.io.File;
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


    @Setter
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

    @FXML
    void genaratePdfBtnOnAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        Stage primaryStage = new Stage();
        File file = fileChooser.showSaveDialog(primaryStage);
        if (file != null) {
            if (!file.getPath().endsWith(".pdf")) {
                file = new File(file.getPath() + ".pdf");
            }
            //System.out.println("Saving to: " + file.getAbsolutePath());  // Debugging line
            try {
                createPDF(file.getAbsolutePath(), monthViewTabel);
                new Alert(Alert.AlertType.CONFIRMATION,"PDF saved successfully").show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Save operation cancelled.");  // Debugging line
        }
    }

    private void createPDF(String dest, TableView<MonthViewTm> tableView) throws Exception {
        PdfWriter writer = new PdfWriter(dest);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);

        String title = "Report";

        Paragraph pdfTitle = new Paragraph(title)
                .setFontSize(18)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER); // Center align the title
        document.add(pdfTitle);

        // Add some space between the title and the table
        document.add(new Paragraph("\n"));

        String subDescription = "From : " + "-" + yearMonth.atDay(1) + "\t\t\t\t To : " + "-" + yearMonth.atEndOfMonth()
                ;

        Paragraph pdfSubDescription = new Paragraph(subDescription)
                .setFontSize(12)
                .setItalic();
        document.add(pdfSubDescription);

        // Add some space between the sub-description and the table
        document.add(new Paragraph("\n"));

        float availableWidth = document.getPdfDocument().getDefaultPageSize().getWidth()
                - document.getLeftMargin() - document.getRightMargin();

        // Create a table with the same number of columns as the TableView
        Table pdfTable = new Table(UnitValue.createPercentArray(new float[]{1, 1, 1}))
                .useAllAvailableWidth();

        // Add table headers
        for (TableColumn<MonthViewTm, ?> column : tableView.getColumns()) {
            pdfTable.addHeaderCell(new Paragraph(column.getText()));
        }

        // Add table rows
        ObservableList<MonthViewTm> data = tableView.getItems();
        for (MonthViewTm viewTm : data) {
            pdfTable.addCell(new Paragraph(String.valueOf(viewTm.getData())));
            pdfTable.addCell(new Paragraph(String.valueOf(viewTm.getTotalOrder())));
            pdfTable.addCell(new Paragraph(String.valueOf(viewTm.getTotalTaken())));
        }

        document.add(pdfTable);
        document.close();
    }
}
