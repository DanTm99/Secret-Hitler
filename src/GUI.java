import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class GUI extends Application {

    private static final int WINDOW_WIDTH = 1280, WINDOW_HEIGHT = 720;
    private static final String SHADE_COLOR = "#000000D0";

    private Stage stage;
    private Pane mainPane = null;
    private StartScreen startScreen = null;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        this.stage = stage;
        this.stage.setTitle("Secret Spies");
        this.stage.setWidth(WINDOW_WIDTH);
        this.stage.setHeight(WINDOW_HEIGHT);
        this.stage.setResizable(false);

        initialise();
        stage.show();
    }

    private void initialise() {
        this.mainPane = new StackPane();
        this.startScreen = new StartScreen();
        this.startScreen.setContent(new StartMenu(this.startScreen));

        mainPane.getChildren().addAll(new Rectangle(WINDOW_WIDTH, WINDOW_HEIGHT, Paint.valueOf(SHADE_COLOR)), startScreen);
        this.stage.setScene(new Scene(this.mainPane));
    }
}
