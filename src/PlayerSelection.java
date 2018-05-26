import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class PlayerSelection extends VBox {

    private static final int WIDTH = 600, HEIGHT = 400;
    private static final int SPINNER_ROW_HEIGHT = 67;
    private static final int ROW_HEIGHT = 66;
    private static final Font TEXT_FONT = new Font(15);
    private HBox[] subRows = new HBox[10];

    public PlayerSelection() {
        super();
        this.setPrefSize(WIDTH, HEIGHT);
        //this.setAlignment(Pos.TOP_CENTER);
        this.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        populate();
    }

    private void populate() {
        Spinner spinner = new Spinner(5, 10, 10);
        spinner.valueProperty().addListener((obs, oldValue, newValue) -> handleSpinnerChange((int) oldValue, (int) newValue));


        Label spinnerLabel = new Label("Number Of Players:");
        spinnerLabel.setFont(TEXT_FONT);
        spinnerLabel.setPadding(new Insets(5));

        HBox spinnerRow = new HBox(spinnerLabel, spinner);
        spinnerRow.setPrefHeight(SPINNER_ROW_HEIGHT);
        spinnerRow.setAlignment(Pos.CENTER);

        ObservableList<Node> children = this.getChildren();

        children.add(spinnerRow);
        for (int i = 1; i <= 5; i++) children.add(createRow(i, i + 5));
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
