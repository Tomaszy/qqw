package sample;


import Controller_File.Student_Controller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static Stage parentWindow;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/FXML_File/Login_panel.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        Student_Controller student_controller;
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        Student_Controller.getInstance().zamknijPolaczenie();
    }

    @Override
    public void init() throws Exception {

    }

    public static void main(String[] args) {
        launch(args);
    }
}
