// A Class that is the beginning scene

package scenes;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;

import static javafx.geometry.Pos.CENTER;
import static javafx.scene.paint.Color.BLACK;
import static javafx.scene.paint.Color.WHITE;
import static scenes.ClassSelectionScene.getClassSelectionScene;
import static scenes.OptionScene.getOptionScene;
import static utils.GameConstants.*;
import static utils.HelperMethods.*;

public class StartScene extends GridPane {
    private static StartScene instance;
    public StartScene(Stage stage) {
        setBackground(Background.fill(WHITE));
        setPadding(new Insets(10));
        setAlignment(CENTER);

        createColumns(this, new int[]{25, 50, 25});
        createRows(this, new int[]{30, 5, 20, 20, 20, 5});

        Text nameLabel = createText("Pixel Saga", 200, BLACK);

        Button startButton = createButton("PLAY");
        startButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                stage.getScene().setRoot(getClassSelectionScene(stage));
                resetStartScene();
            }
        });

        Button optionButton = createButton("OPTIONS");
        optionButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                stage.getScene().setRoot(getOptionScene(stage));
            }
        });

        Button exitButton = createButton("QUIT");
        exitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                System.exit(0);
            }
        });

        add(nameLabel, 1, 0);
        add(startButton, 1, 2);
        add(optionButton, 1, 3);
        add(exitButton, 1, 4);

        Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        setGameWidth((int) screenBounds.getWidth());
        setGameHeight((int) screenBounds.getHeight());

        stage.setFullScreenExitHint("");
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        stage.setFullScreen(true);
        stopAllBackgroundSongs();
        START_SCENE_SONG.play();
    }
    public static StartScene getStartScene(Stage stage) {
        if (instance == null) instance = new StartScene(stage);
        return instance;
    }

    public static void resetStartScene() {
        instance = null;
    }
}
