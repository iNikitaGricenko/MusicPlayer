package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Sound player");
        primaryStage.setScene(new Scene(root, 469, 239));
        primaryStage.initStyle(StageStyle.UNIFIED);
        primaryStage.setResizable(false);
        Controller.start(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}