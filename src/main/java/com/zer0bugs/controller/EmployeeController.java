package com.zer0bugs.controller;



import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.zer0bugs.model.Employee;
import com.zer0bugs.repo.EmployeeRepo;
import com.zer0bugs.repo.FileRepo;
import com.zer0bugs.util.Regex;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.stage.DirectoryChooser;
import lombok.Setter;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public class EmployeeController {

    @FXML
    private Label addEmployeeLabel;

    @FXML
    private Label viewEmployeeLabel;

    @Setter
    DashboardController dashboardController;


    @FXML
    private JFXButton filePathOpenBtn;

    @FXML
    private Pane addEmployeePane;

    @FXML
    private AnchorPane changePane;

    @FXML
    private JFXComboBox<String> comboBoxDepartment;

    @FXML
    private Line lineAddEmployee;

    @FXML
    private Line lineViewEmployee;

    @FXML
    private ImageView qrImageView;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtName;

    @FXML
    private Pane viewEmployeePane;


    @FXML
    private Label filePathTextLabel;


    public void initialize() {
        qrImageView.setVisible(false);
        txtId.requestFocus();
        defaultStyle();
        changeStyle(lineAddEmployee,addEmployeeLabel);
        setDepartment();
        String filePath = FileRepo.getFilePath();
        filePathTextLabel.setText(filePath);
    }

    private void setDepartment() {
        ObservableList<String> list = FXCollections.observableArrayList();
        list.add("Dye House");
        list.add("Finishing");
        list.add("Stores");
        list.add("Engineering");
        list.add("Vet Lab");
        list.add("Office");
        list.add("Cleaning Services");
        list.add("Security");
        comboBoxDepartment.setItems(list);
    }


    @FXML
    void btnClearAction(ActionEvent event) {
        clear();
    }

    private void clear() {
        txtId.clear();
        txtName.clear();
        qrImageView.setVisible(false);
    }

    @FXML
    void btnSaveAction(ActionEvent event) {
        if (isValid()){
            Employee employee = new Employee(txtId.getText(), txtName.getText(), comboBoxDepartment.getValue());

            try {
                if (EmployeeRepo.save(employee)){
                    new Alert(Alert.AlertType.CONFIRMATION,"Employee Saved !!").show();

                    String saveFilePath = FileRepo.getFilePath() + File.separator + txtId.getText() + ".png";
                    saveQrImage(saveFilePath, txtId.getText());
                    Image image = new Image(new File(saveFilePath).toURI().toString());
                    qrImageView.setImage(image);
                    qrImageView.setVisible(true);

                } else {
                    new Alert(Alert.AlertType.WARNING,"Employee Unsaved !!").show();
                }
            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
            }
        }
    }

    @FXML
    void txtIdAction(ActionEvent event) {
        txtName.requestFocus();
    }

    @FXML
    void txtNameAction(ActionEvent event) {
        comboBoxDepartment.requestFocus();
    }

    @FXML
    void addEmployeeAction(MouseEvent event) {
     dashboardController.loadEmployeeView();

    }

    @FXML
    void viewEmployeeAction(MouseEvent event) {
        defaultStyle();
        changeStyle(lineViewEmployee,viewEmployeeLabel);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/viewEmployeeForm.fxml"));
            AnchorPane viewEployeeForm = loader.load();
            ViewEmployeeFormController controller = loader.getController();

            AnchorPane.setTopAnchor(viewEployeeForm, 3.0);
            AnchorPane.setBottomAnchor(viewEployeeForm, 3.0);
            AnchorPane.setLeftAnchor(viewEployeeForm, 3.0);
            AnchorPane.setRightAnchor(viewEployeeForm, 3.0);
            changePane.getChildren().clear();
            changePane.getChildren().setAll(viewEployeeForm);
        }catch (Exception e){
            e.printStackTrace();
        }

    }



    @FXML
    void filePathOpenBtnOnClick(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Choose Directory Path");

        File selectedDirectory = directoryChooser.showDialog(null);
        if (selectedDirectory != null) {
            String directoryPath = selectedDirectory.getAbsolutePath();
            System.out.println(directoryPath);
            filePathTextLabel.setText(directoryPath);
            if (FileRepo.isFilePathTableEmpty()){
                System.out.println("istabelempty");
                if (FileRepo.addFilePath(directoryPath)){
                    filePathTextLabel.setText(directoryPath);
                }
            }else {
                System.out.println("empty na");
                if (FileRepo.updateFilePath(directoryPath)){
                    filePathTextLabel.setText(directoryPath);
                }
            }


        }
    }

    private void changeStyle(Line line,Label label){
        line.setStyle("-fx-stroke: #1e90ff;");
        label.setStyle("-fx-text-fill: #1e90ff;");
    }

    private void defaultStyle(){
        String style = "-fx-stroke: white;";
        lineAddEmployee.setStyle(style);
        lineViewEmployee.setStyle(style);
        addEmployeeLabel.setStyle("-fx-text-fill: black;");
        viewEmployeeLabel.setStyle("-fx-text-fill: black;");
    }



    private void saveQrImage(String filePath, String detail){
        String QR_CODE_IMAGE_PATH = filePath;

        try {
            String text = detail; // Content for the QR code
            int width = 300;
            int height = 300;
            String format = "png";

            BitMatrix bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, width, height);


            Path path = FileSystems.getDefault().getPath(QR_CODE_IMAGE_PATH);
            MatrixToImageWriter.writeToPath(bitMatrix, format, path);
            // new Alert(Alert.AlertType.CONFIRMATION,"QR Code saved successfully.").show();
        } catch (WriterException | IOException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
    }

    private boolean isValid(){
        if (!Regex.setTextColor(com.zer0bugs.util.TextField.NAME,txtName)) return false;
        return true;
    }

    @FXML
    void txtIdKeyReleaseAction(KeyEvent event) {
        //Regex.setTextColor(dev.hsm.util.TextField.ID,txtId);
    }

    @FXML
    void txtNameKeyReleaseAction(KeyEvent event) {
        Regex.setTextColor(com.zer0bugs.util.TextField.NAME,txtName);
    }
}
