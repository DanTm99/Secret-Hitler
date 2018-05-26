import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class StartScreen extends VBox {

    private static final int TITLE_FONT_SIZE = 40;
    private static final int SCREEN_WIDTH = 800, SCREEN_HEIGHT = 500;
    private final PlayerSelection playerSelection;

    private Label title = null;
    private Pane content;

    public StartScreen() {
        super();
        this.setPrefSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        this.setMaxSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        this.setAlignment(Pos.TOP_CENTER);
        this.playerSelection = new PlayerSelection();

        this.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        populate();
    }

    private void populate() {
        this.getChildren().addAll(createTitle());
    }

    private Label createTitle() {
        title = new Label("Secret Spies");
        title.setFont(new Font(TITLE_FONT_SIZE));
        setMargin(title, new Insets(20));
        return title;
    }

    public void setContent(Pane content) {
        if (content != null) this.getChildren().remove(this.content);
        this.getChildren().add(content);
        this.content = content;
    }

    public void switchToPlayerSelection() {
        setContent(playerSelection);
    }
}
