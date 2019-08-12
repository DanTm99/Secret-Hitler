import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.List;

public class GUI extends Application {

    private static final int WINDOW_WIDTH = 1280, WINDOW_HEIGHT = 720;
    private static final String SHADE_COLOR = "#000000D0";

    private Stage stage;
    private Pane mainPane = null;
    private GameScreen gameScreen;

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
        StartScreen startScreen = new StartScreen(this);
        startScreen.setContent(new StartMenu(startScreen));

        mainPane.getChildren().addAll(gameScreen = new GameScreen(), new Rectangle(WINDOW_WIDTH, WINDOW_HEIGHT, Paint.valueOf(SHADE_COLOR)), startScreen);
        this.stage.setScene(new Scene(this.mainPane));
    }

    public void beginGame(List<String> playerNames) {
        mainPane.getChildren().remove(1, 3);
        //new GUIGame(gameScreen).start(playerNames);
    }
}
