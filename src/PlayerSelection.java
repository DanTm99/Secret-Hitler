import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.List;

public class PlayerSelection extends VBox {

    private static final int WIDTH = 600, HEIGHT = 400;
    private static final int SPINNER_ROW_HEIGHT = 67;
    private static final int ROW_HEIGHT = 66;
    private static final Font TEXT_FONT = new Font(15);
    private static final int MIN_NO_OF_PLAYERS = 5, MAX_NO_OF_PLAYERS = 10;

    private final StartScreen startScreen;
    private final HBox[] subRows = new HBox[MAX_NO_OF_PLAYERS];
    private Spinner spinner;

    public PlayerSelection(StartScreen startScreen) {
        super();
        this.startScreen = startScreen;
        this.setPrefSize(WIDTH, HEIGHT);
        this.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        populate();
    }

    private void populate() {
        this.spinner = new Spinner(MIN_NO_OF_PLAYERS, MAX_NO_OF_PLAYERS, MAX_NO_OF_PLAYERS);
        setMargin(this.spinner, new Insets(5));
        this.spinner.valueProperty().addListener((obs, oldValue, newValue) -> handleSpinnerChange((int) oldValue, (int) newValue));

        Label spinnerLabel = new Label("Number Of Players:");
        spinnerLabel.setFont(TEXT_FONT);
        spinnerLabel.setPadding(new Insets(5));

        Button confirmationButton = new Button("Confirm");
        confirmationButton.setOnAction(event -> handleConfirmationButton());

        HBox spinnerRow = new HBox(spinnerLabel, spinner, confirmationButton);
        spinnerRow.setPrefHeight(SPINNER_ROW_HEIGHT);
        spinnerRow.setAlignment(Pos.CENTER);

        ObservableList<Node> children = this.getChildren();

        children.add(spinnerRow);
        for (int i = 1; i <= 5; i++) children.add(createRow(i, i + 5));
    }

    private void handleConfirmationButton() {

        List<String> playerNames = new ArrayList<>();
        for (int i = 0; i < (int) this.spinner.getValue(); i++) {
            String playerName = ((TextField) subRows[i].getChildren().get(1)).getCharacters().toString();
            if (playerName.equals("") || playerNames.contains(playerName)) { // Check if a name is blank or repeated
                displayConfirmationError();
                return;
            }
            playerNames.add(playerName);
        }
        startScreen.beginGame(playerNames);
    }

    private void displayConfirmationError() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Invalid Names");
        alert.setHeaderText("Invalid Names");
        alert.setContentText("Player names must be unique and not blank.");

        alert.showAndWait();
    }

    private void handleSpinnerChange(int oldValue, int newValue) {
        if (newValue > oldValue) subRows[newValue - 1].setVisible(true);
        else subRows[oldValue - 1].setVisible(false);
    }

    private HBox createRow(int subRow1, int subRow2) {
        HBox returnValue = new HBox(createSubRow(subRow1), createSubRow(subRow2));
        returnValue.setPrefHeight(ROW_HEIGHT);
        returnValue.setAlignment(Pos.CENTER);
        return returnValue;
    }

    private HBox createSubRow(int subRowNumber) {
        Label label = new Label("Player " + subRowNumber + "'s name:");
        label.setFont(TEXT_FONT);
        label.setPadding(new Insets(5));

        TextField textField = new TextField();

        HBox subRow = new HBox(label, textField);
        subRow.setPrefWidth(300);
        subRow.setAlignment(Pos.CENTER);

        subRows[subRowNumber - 1] = subRow;

        return subRow;
    }
}
