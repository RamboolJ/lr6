package app;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import main.Main;

public class FXMLDialogWindow {

    private Stage stage;

    public void setStage(Stage stage){
        this.stage = stage;
    }

    @FXML
    private Spinner<Integer> spinner;

    @FXML
    private TextField leftBorder;

    @FXML
    private TextField rightBorder;

    private BUTTON LastBtn;

    public void setLastBtn(BUTTON lastBtn) {
        LastBtn = lastBtn;
    }

    public BUTTON getLastBtn(){
        return LastBtn;
    }

    enum BUTTON {OK, CANCEL};

    public void initialize(){
        SpinnerValueFactory<Integer> spinnerValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(2, 20, 2);
        spinner.setValueFactory(spinnerValueFactory);
    }

    @FXML
    private void btnOKClick(ActionEvent av){
        try {
            Main.tabFDoc.newFunction(getLeftDomainBorder(), getRightDomainBorder(), getPointsCount());
            this.stage.hide();
            LastBtn = BUTTON.OK;
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Incorrect input!");
            alert.showAndWait();
        }
    }

    public double getLeftDomainBorder(){
        return Double.parseDouble(leftBorder.getText());
    }

    public double getRightDomainBorder(){
        return Double.parseDouble(rightBorder.getText());
    }

    public int getPointsCount(){
        return Integer.parseInt(spinner.getEditor().getText());
    }

    @FXML
    private void btnCancelClick(ActionEvent av){
        stage.hide();
        LastBtn = BUTTON.CANCEL;
    }
}