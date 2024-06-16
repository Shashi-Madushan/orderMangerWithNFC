package com.zer0bugs.controller;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.zer0bugs.model.tm.MonthViewTm;
import com.zer0bugs.repo.CustomViewRepo;
import com.zer0bugs.util.GlobalVariables;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.Setter;

import java.io.File;
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
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        Stage primaryStage = new Stage();
        File file = fileChooser.showSaveDialog(primaryStage);
        if (file != null) {
            if (!file.getPath().endsWith(".pdf")) {
                file = new File(file.getPath() + ".pdf");
            }
            System.out.println("Saving to: " + file.getAbsolutePath());  // Debugging line
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

        String subDescription = "From : " + startDate + "\t\t\t\t To : " + endDate ;

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
