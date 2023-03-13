package app;

import functions.FunctionPoint;
import functions.InappropriateFunctionPointException;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.Main;
import functions.FunctionPointT;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.cell.PropertyValueFactory;

import java.awt.*;

public class FXMLMainFormController {
    private Stage stage;

    public void setStage(Stage stage){
        this.stage = stage;
    }

    @FXML
    private TextField edX;

    @FXML
    private TextField edY;

    @FXML
    private Label numberPointLabel;

    @FXML
    private TableView<FunctionPointT> table;

    @FXML
    private TableColumn<FunctionPointT, Double> columnX;

    @FXML
    private TableColumn<FunctionPointT, Double> columnY;

    @FXML
    private void initialize(){
        columnX = new TableColumn<FunctionPointT, Double>("X value");
        columnY = new TableColumn<FunctionPointT, Double>("Y value");

        columnX.setCellValueFactory(new PropertyValueFactory<FunctionPointT, Double>("x"));
        columnY.setCellValueFactory(new PropertyValueFactory<FunctionPointT, Double>("y"));

        table.getColumns().add(columnX);
        table.getColumns().add(columnY);

        redraw();
    }

    @FXML
    public void redraw(){
        ObservableList<FunctionPointT> data = table.getItems();
        data.clear();
        for(int i = 0; i < Main.tabFDoc.getPointsCount(); ++i) {
            data.add(new FunctionPointT(Main.tabFDoc.getPointX(i), Main.tabFDoc.getPointY(i)));
        }
        table.setItems(data);
        updatePointCount();
    }

    @FXML
    private void btnClickNewPoint(ActionEvent av) throws InappropriateFunctionPointException {
        try {
            Main.tabFDoc.addPoint(new FunctionPoint(Double.parseDouble(edX.getText()), Double.parseDouble(edY.getText())));
        }catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Unable to add this point!");
            alert.showAndWait();
        }
    }

    @FXML
    private void btnClickDeletePoint(ActionEvent av) {
        try {
            Main.tabFDoc.deletePoint(table.getSelectionModel().getSelectedIndex());
        }catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Unable to delete this point!");
            alert.showAndWait();
        }
    }

    @FXML
    private void mouseReleasedOnTable(){
        updatePointCount();
    }

    @FXML
    private void keyReleasedOnTable(){
        updatePointCount();
    }

    @FXML
    private void  btnNewFile() throws Exception {
        ActionsDialogWindow wind = new ActionsDialogWindow();
        FXMLDialogWindow.BUTTON lstBtn = wind.showDialog(stage);
    }

    private void updatePointCount() {
        numberPointLabel.setText("Point " + Integer.toString(table.getSelectionModel().getSelectedIndex() + 1) + " of " + Integer.toString(Main.tabFDoc.getPointsCount()));
    }
}
