// A Class that is the scene when player dies

package scenes;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import static javafx.geometry.Pos.CENTER;
import static javafx.scene.paint.Color.*;
import static scenes.ClassSelectionScene.getClassSelectionScene;
import static utils.HelperMethods.*;
import static utils.HelperMethods.createButton;

public class DeathScene extends GridPane {
    private static DeathScene instance;
    public DeathScene(Stage stage) {
        setBackground(Background.fill(BLACK));
        setPadding(new Insets(10));
        setAlignment(CENTER);

        createColumns(this, new int[]{20, 60, 20});
        createRows(this, new int[]{35, 5, 25, 5, 25, 5});

        Text deathText = createText("Noob!", 150, RED);

        Button restartButton = createButton("RESTART", null, WHITE);
        restartButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                resetAllScenes();
                stage.getScene().setRoot(getClassSelectionScene(stage));
            }
        });

        Button exitButton = createButton("QUIT", null, WHITE);
        exitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                System.exit(0);
            }
        });

        add(deathText, 1, 0);
        add(restartButton, 1, 2);
        add(exitButton, 1, 4);
    }

    public static DeathScene getDeathScene(Stage stage) {
        if (instance == null) instance = new DeathScene(stage);
        return instance;
    }
}

