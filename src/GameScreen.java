import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class GameScreen extends HBox {

    private static final int WIDTH = 1280, HEIGHT = 720;

    private Deck deck;
    private LBoard lBoard;
    private FBoard fBoard;
    private ElectionTracker electionTracker;
    private PlayerTracker playerTracker;

    private Card card1;
    private Card card2;
    private Card card3;

    private Button submitButton;
    private Button vetoButton;

    public GameScreen() {
        super();
        this.setPrefSize(WIDTH, HEIGHT);
        this.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        populate();
    }

    private void populate() {
        this.deck = new Deck();

        VBox left = new VBox(this.deck);

        this.lBoard = new LBoard();
        this.fBoard = new FBoard();
        //VBox boards = new VBox(lBoard, fBoard);

        //this.setCenter(boards);
        //this.setBottom(electionTracker = new ElectionTracker());
        //this.setTop(playerTracker = new PlayerTracker());

        VBox center = new VBox(playerTracker = new PlayerTracker(), this.lBoard,
                this.fBoard, electionTracker = new ElectionTracker());

        VBox right = new VBox(this.card1 = new Card(CardType.PLACEHOLDER),
                this.card2 = new Card(CardType.PLACEHOLDER), this.card3 = new Card(CardType.PLACEHOLDER),
                this.submitButton = new Button("Submit"), this.vetoButton = new Button("Veto"));

        this.getChildren().addAll(left, center, right);

    }
}
