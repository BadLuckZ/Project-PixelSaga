// A Class that is the scene for adjusting sound

package scenes;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import utils.SoundLoader;

import static javafx.geometry.Pos.CENTER;
import static javafx.scene.paint.Color.BLACK;
import static javafx.scene.paint.Color.WHITE;
import static scenes.StartScene.getStartScene;
import static utils.GameConstants.*;
import static utils.HelperMethods.*;

public class OptionScene extends GridPane {
    private static OptionScene instances;
    private static Slider soundSlider = new Slider(0, 1, 0.5);
    public OptionScene(Stage stage) {
        setBackground(Background.fill(BLACK));
        setPadding(new Insets(10));
        setAlignment(CENTER);

        createColumns(this, new int[]{5, 25, 40, 25, 5});
        createRows(this, new int[]{30, 5, 35, 5, 20, 5});

        Text optionText = createText("Options", 200, WHITE);

        soundSlider.setStyle("--fx-background-color: #ffffff; " +
                "-fx-padding: 10px; " +
                "-fx-background-radius: 10px; " +
                "-fx-border-color: #808080; " +
                "-fx-border-width: 2px; " +
                "-fx-border-radius: 10px; ");
        soundSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            double value = newValue.doubleValue();
            soundVolume = value / 10;
            soundSlider.setValue(value);
            for (SoundLoader sound: getAllSounds()) {
                sound.getMediaPlayer().setVolume(soundVolume);
            }
        });

        Text soundText = createText("Sound", 70, WHITE);

        Button backButton = createButton("Back");
        backButton.setTextFill(WHITE);
        backButton.setOnAction(actionEvent -> stage.getScene().setRoot(getStartScene(stage)));

        add(optionText, 2, 0);
        add(soundText, 1, 2);
        add(soundSlider, 2, 2, 2, 1);
        add(backButton, 2, 4);
    }

    public static OptionScene getOptionScene(Stage stage) {
        if (instances == null) instances = new OptionScene(stage);
        return instances;
    }
}
