package main;

import functions.*;
import app.*;
import javafx.application.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;

public class Main extends Application{
    public static TabulatedFunctionDoc tabFDoc;

    public static void main(String[] args) throws InappropriateFunctionPointException, IndexOutOfBoundsException, IOException, ClassNotFoundException, CloneNotSupportedException {
        launch(args);
    }

    @Override
    public void init() throws Exception {
        tabFDoc = new TabulatedFunctionDoc();
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXMLMainForm.fxml"));
        Parent root = loader.load();
        FXMLMainFormController ctrl = loader.getController();
        ctrl.setStage(stage);
        tabFDoc.registerRedrawFunctionController(ctrl);
        Scene scene = new Scene(root);
        stage.setTitle("Tabulated functions app");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }
}
