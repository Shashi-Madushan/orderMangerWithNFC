package com.zer0bugs.controller;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.jfoenix.controls.JFXButton;
import com.zer0bugs.model.TimePicker;
import com.zer0bugs.model.tm.MonthViewTm;
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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.Setter;

import java.io.File;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
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

    public void genaratePdfBtnOnAction(ActionEvent event) {
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
                createPDF(file.getAbsolutePath(), tblOrder);
                new Alert(Alert.AlertType.CONFIRMATION,"PDF saved successfully").show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Save operation cancelled.");  // Debugging line
        }
    }

    private void createPDF(String dest, TableView<OrderTm> tableView) throws Exception {
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

        String subDescription = "";
        if (startTime != null && endTime != null) {
            subDescription = "Date : " + LocalDate.now() + "\t\t\t From : " + startTime + "\t To : " + endTime;
        } else if (endTime == null) {
            subDescription = "Date : " + LocalDate.now() + "\t\t\t From : " + startTime;
        } else if (startTime == null){
            subDescription = "Date : " + LocalDate.now();
        }

        Paragraph pdfSubDescription = new Paragraph(subDescription)
                .setFontSize(12)
                .setItalic();
        document.add(pdfSubDescription);

        // Add some space between the sub-description and the table
        document.add(new Paragraph("\n"));

        float availableWidth = document.getPdfDocument().getDefaultPageSize().getWidth()
                - document.getLeftMargin() - document.getRightMargin();

        // Create a table with the same number of columns as the TableView
        Table pdfTable = new Table(UnitValue.createPercentArray(new float[]{1, 1, 1, 1, 1, 1}))
                .useAllAvailableWidth();

        // Add table headers
        for (TableColumn<OrderTm, ?> column : tableView.getColumns()) {
            pdfTable.addHeaderCell(new Paragraph(column.getText()));
        }

        // Add table rows
        ObservableList<OrderTm> data = tableView.getItems();
        for (OrderTm viewTm : data) {
            pdfTable.addCell(new Paragraph(String.valueOf(viewTm.getName())));
            pdfTable.addCell(new Paragraph(String.valueOf(viewTm.getDepartment())));
            pdfTable.addCell(new Paragraph(String.valueOf(viewTm.getDate())));
            pdfTable.addCell(new Paragraph(String.valueOf(viewTm.getPlaceTime())));
            pdfTable.addCell(new Paragraph(String.valueOf(viewTm.getTaken())));
            if (viewTm.getTakenTime() != null) {
                pdfTable.addCell(new Paragraph(String.valueOf(viewTm.getTakenTime())));
            } else {
                pdfTable.addCell(new Paragraph(""));
            }

        }

        document.add(pdfTable);
        document.close();
    }
}
