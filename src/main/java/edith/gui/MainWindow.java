package edith.gui;

import edith.Edith;
import edith.util.EdithException;
import javafx.fxml.FXML;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * Controller for the main GUI.
 */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private Edith edith;

    private Image userImage = new Image(this.getClass().getResourceAsStream("/images/1.png"));
    private Image edithImage = new Image(this.getClass().getResourceAsStream("/images/2.png"));

    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /**
     * Helper to crop the profile pictures into circles. ChatGPT-assisted generation.
     * @param input Original profile.
     * @return Circular profile picture Image.
     */
    public Image makeCircular(Image input) {
        // Wrap in ImageView
        ImageView iv = new ImageView(input);
        double size = Math.min(input.getWidth(), input.getHeight());
        iv.setFitWidth(size);
        iv.setFitHeight(size);

        // Apply circular clip
        Circle clip = new Circle(size / 2, size / 2, size / 2);
        iv.setClip(clip);

        // Snapshot parameters: make background transparent
        SnapshotParameters params = new SnapshotParameters();
        params.setFill(Color.TRANSPARENT);

        // Take snapshot -> new Image
        return iv.snapshot(params, null);
    }

    /** Injects the Edith instance */
    public void setEdith(Edith e) {
        edith = e;

        String greeting = "hello! this is edith :D\nwhat do we need today?";

        dialogContainer.getChildren().add(
                DialogBox.getEdithDialog(greeting, makeCircular(edithImage))
        );
    }

    /**
     * Creates two dialog boxes, one echoing user input and the other containing Duke's reply and then appends them to
     * the dialog container. Clears the user input after processing.
     */
    @FXML
    private void handleUserInput() throws EdithException {
        String input = userInput.getText();
        String response;
        try {
            response = edith.getResponse(input);
        } catch (EdithException e) {
            response = e.getMessage();
        }
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, makeCircular(userImage)),
                DialogBox.getEdithDialog(response, makeCircular(edithImage))
        );
        userInput.clear();
    }
}

