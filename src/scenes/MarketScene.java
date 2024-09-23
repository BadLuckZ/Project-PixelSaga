// A Class that is the scene for purchasing things

package scenes;

import entities.bases.BasePerk;
import entities.player.Player;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import objects.BaseObject;
import objects.BlackSmith;
import objects.Merchant;

import java.util.ArrayList;
import java.util.Arrays;

import static enums.FaceDirection.RIGHT;
import static utils.AnimationStatus.SCALE;
import static utils.GameConstants.*;
import static utils.HelperMethods.*;
import static utils.LevelData.marketSceneData;
import static utils.ObjectStatus.*;

public class MarketScene extends BaseScene{
    private static MarketScene instance;
    public MarketScene(Stage stage, Player player) {
        super(stage);
        setBgImg(new Image("background/MarketScene.png"));
        setBgImgView(new ImageView(getBgImg()));
        getBgImgView().setFitWidth(getBgImg().getWidth() * SCALE);
        getBgImgView().setFitHeight(getBgImg().getHeight() * SCALE);
        getChildren().add(getBgImgView());
        setLvlData(marketSceneData);

        setPlayer(player);
        spawnPlayer(getPlayer(), getLvlData());

        setKey(getPlayer().getAnimation());
        getChildren().add(getPlayer().getAnimation());
        startAllThreads(this);

        createAllPerkSymbols(getPlayer());
        createMerchant();
        createBlackSmith();

        setScenePosition();
        drawUI();
        stopAllBackgroundSongs();
        MARKET_SCENE_SONG.play();
    }

    private void createAllPerkSymbols(Player player) {
        float[] positions = {0.325f, 0.375f, 0.425f, 0.475f};
        ArrayList<BasePerk> perks = createPerkChoices(player, player.getNotObtainPerks(), positions.length);
        for (int i = 0; i < perks.size(); i++) {
            createSymbol(this, perks.get(i).toString(), positions[i], 1.45, perks.get(i));
        }
    }

    private void createMerchant() {
        BaseObject merchant = new Merchant(getPlayer(), getBgImg().getWidth() * 0.22 * SCALE,
                (getBgImg().getHeight() * 0.8 - MERCHANT_HEIGHT) * SCALE, RIGHT);
        getChildren().add(merchant.getAnimation());
        getPlayer().getObjectsInStage().add(merchant);
    }

    private void createBlackSmith() {
        BaseObject blackSmith1 = new BlackSmith(getPlayer(), getBgImg().getWidth() * 0.37 * SCALE,
                (getBgImg().getHeight() * 0.4 - BLACK_SMITH_HEIGHT.get(0)) * SCALE, RIGHT, "Armadillos");
        BaseObject blackSmith2 = new BlackSmith(getPlayer(), getBgImg().getWidth() * 0.7 * SCALE,
                (getBgImg().getHeight() * 0.4 - BLACK_SMITH_HEIGHT.get(1)) * SCALE, RIGHT, "Rabbit");
        getChildren().addAll(blackSmith1.getAnimation(), blackSmith2.getAnimation());
        getPlayer().getObjectsInStage().addAll(Arrays.asList(blackSmith1, blackSmith2));
    }

    public static MarketScene getMarketScene(Stage stage, Player player) {
        if (instance == null) instance = new MarketScene(stage, player);
        return instance;
    }

    public static void resetMarketScene() {
        instance = null;
    }
}
