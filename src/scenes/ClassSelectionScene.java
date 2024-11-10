// A Class that is the scene for choosing player's class

package scenes;

import entities.player.Player;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import objects.BaseObject;
import objects.VendingMachine;

import static entities.player.Player.resetPlayer;
import static enums.FaceDirection.RIGHT;
import static utils.AnimationStatus.*;
import static utils.GameConstants.*;
import static utils.HelperMethods.createSymbol;
import static utils.HelperMethods.spawnPlayer;
import static utils.LevelData.classSelectionSceneData;
import static utils.ObjectStatus.VENDING_MACHINE_HEIGHT;

public class ClassSelectionScene extends BaseScene {
    private static ClassSelectionScene instance;

    public ClassSelectionScene(Stage stage) {
        super(stage);
        setBgImg(new Image("background/ClassSelectionScene.png"));
        setBgImgView(new ImageView(getBgImg()));
        getBgImgView().setFitWidth(getBgImg().getWidth() * SCALE);
        getBgImgView().setFitHeight(getBgImg().getHeight() * SCALE);
        getChildren().add(getBgImgView());
        setLvlData(classSelectionSceneData);

        resetPlayer();
        setPlayer(new Player());
        spawnPlayer(getPlayer(), getLvlData());

        setKey(getPlayer().getAnimation());
        getChildren().add(getPlayer().getAnimation());
        startAllThreads(this);

        createAllClassSymbols();
        createVendingMachine();

        setScenePosition();
        drawUI();
        stopAllBackgroundSongs();
        CLASS_SELECTION_SCENE_SONG.play();
    }

    private void createAllClassSymbols() {
        createSymbol(this, "BroadSword", 0.52, 1.5, null);
        createSymbol(this, "Dagger", 0.575, 1.5, null);
        createSymbol(this, "Bow", 0.63, 1.5, null);
    }

    private void createVendingMachine() {
        BaseObject vendingMachine = new VendingMachine(getPlayer(), getBgImg().getWidth() * 0.74 * SCALE,
                (getBgImg().getHeight() * 0.8 - VENDING_MACHINE_HEIGHT) * SCALE, RIGHT);
        getChildren().add(vendingMachine.getAnimation());
        getPlayer().getObjectsInStage().add(vendingMachine);
    }

    public static ClassSelectionScene getClassSelectionScene(Stage stage) {
        if (instance == null) instance = new ClassSelectionScene(stage);
        return instance;
    }

    public static void resetClassSelectionScene() {
        instance = null;
    }
}
