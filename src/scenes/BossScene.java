// A Class that is the scene for boss fight

package scenes;

import entities.player.Player;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import static threads.BotLogicThread.setCurrentWaveNumber;
import static threads.BotLogicThread.setMaxWaveNumber;
import static utils.AnimationStatus.PIXEL_PER_TILE;
import static utils.GameConstants.*;
import static utils.HelperMethods.*;
import static utils.LevelData.BossSceneData;

public class BossScene extends BaseScene{
    private static BossScene instance;

    public BossScene(Stage stage, Player player) {
        super(stage);
        player.getObjectsInStage().clear();
        setPlayer(player);
        setKey(player.getAnimation());
        setLvlData(BossSceneData);
        setCurrentWaveNumber(0);
        setMaxWaveNumber(getLvlData()[getLvlData().length - 1][0]);

        String backgroundImage = "background/Background" + getLvlData()[getLvlData().length - 1][1] + ".png";
        setBgImg(new Image(
                ClassLoader.getSystemResource(backgroundImage).toString()));
        setBgImgView(new ImageView(getBgImg()));
        getBgImgView().setFitWidth(getLvlData()[0].length * PIXEL_PER_TILE);
        getBgImgView().setFitHeight((getLvlData().length - 1) * PIXEL_PER_TILE);
        getChildren().add(getBgImgView());

        drawBackground();
        getChildren().add(getPlayer().getAnimation());
        spawnPlayer(getPlayer(), getLvlData());
        startAllThreads(this);

        setScenePosition();
        drawUI();
        stopAllBackgroundSongs();
        BOSS_SCENE_SONG.play();
    }

    public static BossScene getBossScene(Stage stage, Player player) {
        if (instance == null) instance = new BossScene(stage, player);
        return instance;
    }

    public static void resetBossScene() {
        instance = null;
    }
}
