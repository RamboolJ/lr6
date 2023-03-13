package app;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import main.Main;

public class ActionsDialogWindow {

    private Stage stage;

    private FXMLDialogWindow curCtrl;

    public void setStage(Stage stage){
        this.stage = stage;
    }

    public FXMLDialogWindow.BUTTON showDialog(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/app/FXMLDialogWindow.fxml"));
        Parent root = loader.load();
        FXMLDialogWindow ctrl = loader.getController();
        Scene scene = new Scene(root);
        Stage stg = new Stage();
        stg.setTitle("Tabulated functions app");
        stg.setScene(scene);
        stg.setResizable(false);
        stg.initModality(Modality.APPLICATION_MODAL);
        stg.initOwner(primaryStage);
        ctrl.setStage(stg);

        stg.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                ctrl.setLastBtn(FXMLDialogWindow.BUTTON.CANCEL);
            }
        });

        stg.showAndWait();

        return ctrl.getLastBtn();
    }
}
