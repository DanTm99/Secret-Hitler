import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class StartMenu extends HBox {

    private static final int WIDTH = 600, HEIGHT = 400;
    private static final int SINGLE_MACHINE_BUTTON_WIDTH = 200, SINGLE_MACHINE_BUTTON_HEIGHT = 50;
    private static final int MULTI_MACHINE_BUTTON_WIDTH = 100, MULTI_MACHINE_BUTTON_HEIGHT = 30;
    private static final int MULTI_MACHINE_TITLE_SIZE = 20;

    private StartScreen startScreen;

    public StartMenu(StartScreen startScreen) {
        super(50);
        this.startScreen = startScreen;
        this.setPrefSize(WIDTH, HEIGHT);
        this.setAlignment(Pos.TOP_CENTER);
        this.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        populate();
    }

    private void populate() {
        Button singleMachineButton = new Button("Single Machine");
        singleMachineButton.setPrefSize(SINGLE_MACHINE_BUTTON_WIDTH, SINGLE_MACHINE_BUTTON_HEIGHT);
        setMargin(singleMachineButton, new Insets(50, 0, 0, 0));
        singleMachineButton.setOnAction(event -> handleSingleMachine());

        HBox multiMachineButtons = new HBox(20);

        Button localButton = createMultiMachineButton("Local");
        Button onlineButton = createMultiMachineButton("Online");
        multiMachineButtons.getChildren().addAll(localButton, onlineButton);

        Label multiMachineTitle = new Label("Multi Machine");
        multiMachineTitle.setFont(new Font(MULTI_MACHINE_TITLE_SIZE));

        VBox multiMachine = new VBox(multiMachineTitle, multiMachineButtons);
        multiMachine.setAlignment(Pos.TOP_CENTER);

        this.getChildren().addAll(singleMachineButton, multiMachine);
    }

    private void handleSingleMachine() {
        startScreen.switchToPlayerSelection();
    }

    private Button createMultiMachineButton(String text) {
        Button button = new Button(text);
        button.setPrefSize(MULTI_MACHINE_BUTTON_WIDTH, MULTI_MACHINE_BUTTON_HEIGHT);
        setMargin(button, new Insets(30, 0, 0, 0));
        button.setDisable(true);
        return button;
    }
}
