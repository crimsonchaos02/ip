package edith.gui;

import java.io.IOException;

import edith.Edith;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * A GUI for Edith using FXML.
 */
public class Main extends Application {

    private Edith edith = new Edith("output.txt");

    @Override
    public void start(Stage stage) {
        try {
            Font.loadFont(getClass().getResourceAsStream("/fonts/Orbitron-VariableFont.ttf"), 14);
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap);
            stage.setScene(scene);
            fxmlLoader.<MainWindow>getController().setEdith(edith);
            stage.setTitle("EDITH");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
