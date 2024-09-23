// A Class that is the scene for GiGee fight

package scenes;

import entities.player.Player;
import entities.player.perks.TreasureHunterPerk;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import utils.SoundLoader;

import static entities.player.perks.TreasureHunterPerk.setCanRandomTreasure;
import static enums.Tier.TIER1;

import static threads.BotLogicThread.*;
import static utils.AnimationStatus.PIXEL_PER_TILE;
import static utils.GameConstants.*;
import static utils.HelperMethods.*;
import static utils.LevelData.getAllFightScene;

public class FightScene extends BaseScene {
    private static FightScene instance;
    private static int numberOfFightScene = 0;
    private static SoundLoader fightSceneSong;
    private String roomType;
    public FightScene(Stage stage, Player player, String roomType) {
        super(stage);
        numberOfFightScene++;
        player.getObjectsInStage().clear();
        setPlayer(player);
        if (hasPerk(player, new TreasureHunterPerk(player, TIER1), false)) {;
            setCanRandomTreasure(true);
        }
        setRoomType(roomType);
        setKey(player.getAnimation());
        setLvlData(getAllFightScene().get(randomInteger(0, getAllFightScene().size()-1)));
        setCurrentWaveNumber(0);
        setMaxWaveNumber(getLvlData()[getLvlData().length - 1][0]);
        String backgroundImage = "background/Background" + getLvlData()[getLvlData().length - 1][1] + ".png";
        setBgImg(new Image(
                ClassLoader.getSystemResource(backgroundImage).toString()));
        setBgImgView(new ImageView(getBgImg()));
        getBgImgView().setFitWidth(getLvlData()[0].length * PIXEL_PER_TILE);
        getBgImgView().setFitHeight((getLvlData().length - 1) * PIXEL_PER_TILE);
        getChildren().add(getBgImgView());

        addAndDeleteStageStatus(this,
                createText("Stage " + String.valueOf(numberOfFightScene), 200, Color.WHITE));

        drawBackground();
        getChildren().add(getPlayer().getAnimation());
        spawnPlayer(getPlayer(), getLvlData());
        startAllThreads(this);

        setScenePosition();
        drawUI();
        stopAllBackgroundSongs();
        getRandomFightSceneSong().play();
    }

    private SoundLoader getRandomFightSceneSong() {
        SoundLoader[] songs = {FIGHT_SCENE_SONG1, FIGHT_SCENE_SONG2, FIGHT_SCENE_SONG3, FIGHT_SCENE_SONG4, FIGHT_SCENE_SONG5, FIGHT_SCENE_SONG6};
        SoundLoader song = songs[randomInteger(0, songs.length - 1)];
        setFightSceneSong(song);
        return song;
    }

    public static FightScene getFightScene(Stage stage, Player player, String roomType) {
        if (instance == null) instance = new FightScene(stage, player, roomType);
        return instance;
    }

    public static void resetFightScene() {
        instance = null;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public static int getNumberOfFightScene() {
        return numberOfFightScene;
    }

    public static void setNumberOfFightScene(int numberOfFightScene) {
        FightScene.numberOfFightScene = numberOfFightScene;
    }
    public static void setFightSceneSong(SoundLoader fightSceneSong) {
        FightScene.fightSceneSong = fightSceneSong;
    }
}

