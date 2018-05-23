import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GUI extends Application {
    public static Stage stage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        GUI.stage = stage;
        Parent root = FXMLLoader.load(getClass().getResource("startScreen.fxml"));
        stage.setTitle("Secret Spies");
        stage.setScene(new Scene(root, 800, 500));
        stage.show();
    }
}
