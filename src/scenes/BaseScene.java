// A Class that is the base class for most of the scenes

package scenes;

import animating.*;
import entities.bases.BaseBoss;
import entities.bases.BaseMonster;
import entities.player.Player;
import entities.player.classes.Anonymous;
import entities.player.classes.Bow;
import entities.player.classes.BroadSword;
import entities.player.classes.Dagger;
import entities.bases.BasePerk;
import entities.player.perks.DiscountMasterPerk;
import enums.FaceDirection;
import enums.Tier;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import objects.*;
import threads.*;
import utils.CanvasDrawer;

import java.util.*;

import static enums.FaceDirection.LEFT;
import static enums.States.*;
import static enums.Tier.*;
import static javafx.scene.paint.Color.*;
import static scenes.FightScene.*;
import static scenes.MarketScene.getMarketScene;
import static scenes.StartScene.getStartScene;
import static threads.BotLogicThread.getCurrentWaveNumber;
import static threads.BotLogicThread.getMaxWaveNumber;
import static utils.AnimationStatus.*;
import static utils.GameConstants.*;
import static utils.HelperMethods.*;
import static utils.PerkStatus.PRICE_UPGRADE_PERCENT;

public abstract class BaseScene extends Pane {
    private CanvasDrawer bgDrawer = null;
    private CanvasDrawer minimapDrawer = null;
    private CanvasDrawer entitiesInMiniMap = null;
    private Image bgImg;
    private ImageView bgImgView;
    private ImageView healthUI;
    private Rectangle healthBar;
    private Text healthText;
    private ImageView bossHealthBarFrame;
    private Rectangle bossHealthBar;
    private ImageView bossHealthBarBg;
    private ImageView coinUI;
    private Text coinText;
    private ImageView remainMonsterUI;
    private Text remainMonsterText;
    private Text waveText;
    private ImageView dashIcon = new ImageView();
    private ImageView firstIconFrame;
    private ImageView secondIconFrame;
    private Text dashRemaining = new Text();
    private Rectangle dashCoolDown = new Rectangle();
    private ImageView specialAttackIcon = new ImageView();
    private Rectangle specialAttackCoolDown = new Rectangle();
    private ImageView miniMapBgUI;
    private Group playerUI = new Group();
    private Group inventoryUI = new Group();
    private Group upgradeUI = new Group();
    private Group choiceUI = new Group();
    private Group perkChestUI;
    private int[][] lvlData;
    private Player player;
    private Set<KeyCode> pressedKeys = new HashSet<>();
    private Stage stage;
    protected PlayerAnimationThread playerAnimationThread;
    protected MovingThread movingThread;
    protected MonsterAnimationThread monsterAnimationThread;
    protected BotLogicThread botLogicThread;
    protected StatThread statThread;
    private ArrayList<Door> doors = new ArrayList<>();
    private boolean isPause = false;
    private ArrayList<Node> toRemovePerkIcons = new ArrayList<>();
    public BaseScene(Stage stage) {
        this.stage = stage;
        stage.setFullScreenExitHint("");
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        stage.setFullScreen(true);
    }

    public void drawUI() {
        createHpGroup();
        createSkillIcon();
        createMiniMap();
        createStageDetails();
        createBossHealthBar();

        addElementsToPlayerUI();

        playerUI.setTranslateX(-getTranslateX() + uiOffset);
        playerUI.setTranslateY(-getTranslateY() + gameHeight - healthUIHeight -uiOffset);
        getChildren().add(playerUI);
    }

    private void createHpGroup() {
        int currentHp = getPlayer().getCurrentHp();
        int maxHp = finalValuePercent(getPlayer().getBaseMaxHp(),
                getPlayer().getAddMaxHp());
        Image healthImg = new Image(ClassLoader.getSystemResource(
                getPlayer().getUiString()).toString());
        healthUI = new ImageView(healthImg);
        healthUI.setFitWidth(healthUIWidth);
        healthUI.setFitHeight(healthUIHeight);
        healthBar = new Rectangle(0, 0, 0, healthHeight);
        healthBar.setFill(BLACK);
        healthBar.setTranslateX(healthUI.getTranslateX() + edgeRightOfHealthBar);
        healthBar.setTranslateY(healthUI.getTranslateY() + edgeTopOfHealthBar);
        healthText = createText(currentHp + "/" + maxHp, (int) (40 * SCALE), BLACK);
        healthText.setTranslateX(edgeRightOfHealthBar - healthWidth / 1.6);
        healthText.setTranslateY(edgeTopOfHealthBar + healthHeight / (1.6 * SCALE));
        healthText.setFill(WHITE);
    }

    private void createSkillIcon() {
        firstIconFrame = new ImageView();
        secondIconFrame = new ImageView();
        if (!(player.getPlayerClass() instanceof Anonymous)) {
            Image iconFrameImg = new Image(ClassLoader.getSystemResource("etcUI/IconFrame.png").toString());
            Image dashIconImg = new Image(ClassLoader.getSystemResource("etcUI/" + player.getPlayerClass().getClass().getSimpleName() + "Dash_Icon.png").toString());
            Image specialAttackIconImg = new Image(ClassLoader.getSystemResource("etcUI/" + player.getPlayerClass().getClass().getSimpleName() + "SpecialAttack_Icon.png").toString());
            createFirstIcon(iconFrameImg);
            createSecondIcon(iconFrameImg);
            createDashIcon(dashIconImg);
            createSpecialAttackIcon(specialAttackIconImg);
        }
    }

    private void createFirstIcon(Image iconFrameImg) {
        firstIconFrame.setImage(iconFrameImg);
        firstIconFrame.setFitWidth(iconFrameImg.getWidth() * SCALE);
        firstIconFrame.setFitHeight(iconFrameImg.getHeight() * SCALE);
        firstIconFrame.setTranslateX(edgeRightOfHealthBar - 0.965 * healthWidth);
        firstIconFrame.setTranslateY(edgeTopOfHealthBar - firstIconFrame.getFitHeight() - uiOffset);
    }

    private void createSecondIcon(Image iconFrameImg) {
        secondIconFrame.setImage(iconFrameImg);
        secondIconFrame.setFitWidth(firstIconFrame.getFitWidth());
        secondIconFrame.setFitHeight(firstIconFrame.getFitHeight());
        secondIconFrame.setTranslateX(firstIconFrame.getTranslateX() + secondIconFrame.getFitWidth() + uiOffset);
        secondIconFrame.setTranslateY(firstIconFrame.getTranslateY());
    }

    private void createDashIcon(Image dashIconImg) {
        dashIcon.setImage(dashIconImg);
        dashIcon.setFitWidth(dashIconImg.getWidth() * SCALE);
        dashIcon.setFitHeight(dashIconImg.getHeight() * SCALE);
        dashIcon.setTranslateX(firstIconFrame.getTranslateX() + 3 * SCALE);
        dashIcon.setTranslateY(firstIconFrame.getTranslateY() + 3 * SCALE);
        dashRemaining = createTextForInventory(String.valueOf(player.getRemainingDash()), (int) (30 * SCALE), WHITE,
                dashIcon.getTranslateX() + 0.8 * dashIcon.getFitWidth(), dashIcon.getTranslateY() + 0.95 * dashIcon.getFitHeight());
        dashCoolDown = new Rectangle(0, 0, dashIcon.getFitWidth(), dashIcon.getFitHeight());
        dashCoolDown.setFill(BLACK);
        dashCoolDown.setOpacity(0);
        dashCoolDown.setTranslateX(dashIcon.getTranslateX());
        dashCoolDown.setTranslateY(dashIcon.getTranslateY());
    }

    private void createSpecialAttackIcon(Image specialAttackIconImg) {
        specialAttackIcon.setImage(specialAttackIconImg);
        specialAttackIcon.setFitWidth(specialAttackIconImg.getWidth() * SCALE);
        specialAttackIcon.setFitHeight(specialAttackIconImg.getHeight() * SCALE);
        specialAttackIcon.setTranslateX(secondIconFrame.getTranslateX() + 3 * SCALE);
        specialAttackIcon.setTranslateY(secondIconFrame.getTranslateY() + 3 * SCALE);
        specialAttackCoolDown = new Rectangle(0, 0, specialAttackIcon.getFitWidth(), specialAttackIcon.getFitHeight());
        specialAttackCoolDown.setFill(BLACK);
        specialAttackCoolDown.setOpacity(0);
        specialAttackCoolDown.setTranslateX(specialAttackIcon.getTranslateX());
        specialAttackCoolDown.setTranslateY(specialAttackIcon.getTranslateY());
    }

    private void createMiniMap() {
        Image miniMapBgImg = new Image(
                ClassLoader.getSystemResource("etcUI/MiniMap_UI.png").toString());
        miniMapBgUI = new ImageView(miniMapBgImg);
        miniMapBgUI.setFitWidth(1.0245 * getBgImgView().getFitWidth() * SCALE / 10);
        miniMapBgUI.setFitHeight(1.05 * getBgImgView().getFitHeight() * SCALE / 10);
        miniMapBgUI.setTranslateX(gameWidth - miniMapBgUI.getFitWidth() - 2 * uiOffset);
        miniMapBgUI.setTranslateY(healthUIHeight - miniMapBgUI.getFitHeight());
    }

    private void createStageDetails() {
        String currentCoin = String.valueOf(player.getCoins());
        String remainMonster = String.valueOf(player.getMonstersInStage().size());
        createCoinGroup(currentCoin);
        createMonsterRemainingGroup(remainMonster);
        createWaveNumberGroup(remainMonster);
    }

    private void createBossHealthBar() {
        Image bossHealthBarFrameImg = new Image(ClassLoader.getSystemResource("etcUI/BossHealthBarFrame.png").toString());
        bossHealthBarFrame = new ImageView(bossHealthBarFrameImg);
        bossHealthBarFrame.setFitWidth(bossHealthBarFrameImg.getWidth() * 0.7 * scaleForUI);
        bossHealthBarFrame.setFitHeight(bossHealthBarFrameImg.getHeight() * 0.7 * scaleForUI);
        bossHealthBarFrame.setTranslateX((gameWidth - bossHealthBarFrame.getFitWidth())/2);
        bossHealthBarFrame.setTranslateY(- 0.85 * gameHeight);
        Image bossHealthBarBgImg = new Image(ClassLoader.getSystemResource("etcUI/BossHealthBar.png").toString());
        bossHealthBarBg = new ImageView(bossHealthBarBgImg);
        bossHealthBarBg.setFitWidth(bossHealthBarFrame.getFitWidth());
        bossHealthBarBg.setFitHeight(bossHealthBarFrame.getFitHeight());
        bossHealthBarBg.setTranslateX(bossHealthBarFrame.getTranslateX());
        bossHealthBarBg.setTranslateY(bossHealthBarFrame.getTranslateY());
        bossHealthBar = new Rectangle(0, 0, 0, bossHealthBarBg.getFitHeight() * 4 / 15);
        bossHealthBar.setFill(BLACK);
        bossHealthBar.setTranslateX(bossHealthBarBg.getTranslateX() + 0.948 * bossHealthBarBg.getFitWidth());
        bossHealthBar.setTranslateY(bossHealthBarBg.getTranslateY() + (double) 5 / 12 * bossHealthBarBg.getFitHeight());
    }

    private void createCoinGroup(String currentCoin) {
        coinUI = loadCoinUI();
        coinText = createText(currentCoin, 30, WHITE);
        if (this instanceof FightScene || this instanceof BossScene) {
            adjustCoinGroup(coinUI.getImage(), 2.0/3.0, 2.0/3.0, 0, -3, 2.5, 2);
        } else if (this instanceof MarketScene) {
            coinText = createText(currentCoin, 80, GOLD);
            adjustCoinGroup(coinUI.getImage(), 1.5, 1.5, -currentCoin.length(), -85, 10 - currentCoin.length(), 4.5);
        }
    }

    private void adjustCoinGroup(Image coinImg, double scaleX, double scaleY, double translateX, double translateY, double textX, double textY) {
        coinUI.setFitWidth(coinImg.getWidth() * scaleX * scaleForUI);
        coinUI.setFitHeight(coinImg.getHeight() * scaleY * scaleForUI);
        coinUI.setTranslateX(miniMapBgUI.getTranslateX() + translateX * uiOffset);
        coinUI.setTranslateY(miniMapBgUI.getTranslateY() + translateY * uiOffset);
        coinText.setTranslateX(coinUI.getTranslateX() + textX * uiOffset);
        coinText.setTranslateY(coinUI.getTranslateY() + textY * uiOffset);
    }

    private void createMonsterRemainingGroup(String currentCoin) {
        Image remainMonsterImg = new Image(ClassLoader.getSystemResource("etcUI/Skull_UI.png").toString());
        remainMonsterUI = new ImageView(remainMonsterImg);
        remainMonsterUI.setFitWidth(remainMonsterImg.getWidth() * 2.0/3.0 * scaleForUI);
        remainMonsterUI.setFitHeight(remainMonsterImg.getHeight() * 2.0/3.0 * scaleForUI);
        remainMonsterUI.setTranslateX(coinText.getTranslateX() + (currentCoin.length() + 7.5) * scaleForUI * uiOffset);
        remainMonsterUI.setTranslateY(coinUI.getTranslateY());
        String remainMonster = String.valueOf(player.getMonstersInStage().size());
        remainMonsterText = createText(remainMonster, 30, WHITE);
        remainMonsterText.setTranslateX(remainMonsterUI.getTranslateX() + 4 * scaleForUI * uiOffset);
        remainMonsterText.setTranslateY(coinText.getTranslateY());
    }

    private void createWaveNumberGroup(String remainMonster) {
        waveText = createText("Wave:" + String.valueOf(getCurrentWaveNumber()) + "/" + String.valueOf(getMaxWaveNumber()), 30, WHITE);
        waveText.setTranslateX(remainMonsterText.getTranslateX() + (remainMonster.length() + 3) * SCALE * uiOffset);
        waveText.setTranslateY(remainMonsterText.getTranslateY());
    }

    private void addElementsToPlayerUI() {
        playerUI.getChildren().addAll(healthUI, healthBar, healthText,
                firstIconFrame,secondIconFrame, dashIcon, dashCoolDown, dashRemaining, specialAttackIcon, specialAttackCoolDown);
        if (this instanceof MarketScene) {
            playerUI.getChildren().addAll(coinUI, coinText);
        } else {
            try {
                playerUI.getChildren().addAll(miniMapBgUI, minimapDrawer, entitiesInMiniMap,
                        coinUI, coinText, remainMonsterUI, remainMonsterText, waveText);
            } catch (Exception ignored) {}
        }
        if (this instanceof BossScene) {
            playerUI.getChildren().addAll(bossHealthBarBg, bossHealthBar, bossHealthBarFrame);
        }
    }

    public void updateUI() {
        if (healthText != null) {
            Platform.runLater(() -> {
                updateHpGroup();
                updateStageDetails();
                updateMiniMap();
                updateBossHealthBar();
                if (!isPause) {
                    player.getAnimation().toFront();
                    playerUI.toFront();
                }
            });
        }
    }

    private void updateHpGroup() {
        int currentHp = getPlayer().getCurrentHp();
        int maxHp = finalValuePercent(getPlayer().getBaseMaxHp(), getPlayer().getAddMaxHp());
        double percentHp = 1 - ((double) currentHp / (double) maxHp);
        healthText.setText(currentHp + "/" + maxHp);
        healthBar.setWidth(percentHp * healthWidth);
        healthBar.setTranslateX(edgeRightOfHealthBar - percentHp * healthWidth);
    }

    private void updateBossHealthBar() {
        if (!getPlayer().getMonstersInStage().isEmpty()) {
            if (getPlayer().getMonstersInStage().get(0) instanceof BaseBoss) {
                BaseBoss boss = (BaseBoss) getPlayer().getMonstersInStage().get(0);
                int currentHp = boss.getCurrentHp();
                int maxHp = finalValuePercent(boss.getBaseMaxHp(), boss.getAddMaxHp());
                double percentHp = 1 - ((double) currentHp / (double) maxHp);
                bossHealthBar.setWidth(percentHp * (0.9 * bossHealthBarBg.getFitWidth()));
                bossHealthBar.setTranslateX(bossHealthBarBg.getTranslateX() + 0.945 * bossHealthBarBg.getFitWidth() - percentHp * (0.9 * bossHealthBarBg.getFitWidth()));
            }
        }
    }

    private void updateStageDetails() {
        String currentCoin = String.valueOf(player.getCoins());
        ArrayList<BaseMonster> allMonsters = new ArrayList<>(player.getMonstersInStage());
        String remainMonster = String.valueOf((int) allMonsters.stream().filter(baseMonster -> baseMonster.getAnimation().getState() != MONSTER_DEAD).count());
        updateCoin(currentCoin);
        updateMonsterRemaining(currentCoin);
        updateDash();
        updateWaveNumber(remainMonster);
    }

    private void updateCoin(String currentCoin) {
        if (this instanceof MarketScene) {
            adjustCoinGroup(coinUI.getImage(), 1.5, 1.5, -currentCoin.length(), -85, 10 - currentCoin.length(), 4.5);
        }
        coinText.setText(currentCoin);
    }

    private void updateMonsterRemaining(String currentCoin) {
        remainMonsterUI.setTranslateX(coinText.getTranslateX() +
                (currentCoin.length() + 7.5) * scaleForUI * uiOffset);
        ArrayList<BaseMonster> allMonsters = new ArrayList<>(player.getMonstersInStage());
        String remainMonster = String.valueOf((int) allMonsters.stream().filter(baseMonster -> baseMonster.getAnimation().getState() != MONSTER_DEAD).count());
        remainMonsterText.setText(remainMonster);
        remainMonsterText.setTranslateX(remainMonsterUI.getTranslateX() + 4 * scaleForUI * uiOffset);
    }

    private void updateDash() {
        dashRemaining.setText(String.valueOf(player.getRemainingDash()));
    }

    private void updateWaveNumber(String remainMonster) {
        if (getCurrentWaveNumber() > getMaxWaveNumber()) {
            waveText.setText("Complete!");
        } else {
            waveText.setText("Wave:" +
                    String.valueOf(getCurrentWaveNumber()) + "/" +
                    String.valueOf(getMaxWaveNumber()));
            waveText.setTranslateX(remainMonsterText.getTranslateX() + (remainMonster.length() + 3) * SCALE * uiOffset);
        }
    }

    public void updateMiniMap() {
        if (minimapDrawer != null) {
            entitiesInMiniMap.drawEntities(entitiesInMiniMap.getGraphicsContext2D());
        }
    }

    public void drawBackground() {
        bgDrawer = new CanvasDrawer(this,
                getBgImgView().getFitWidth(), getBgImgView().getFitHeight());
        GraphicsContext bgGc = bgDrawer.getGraphicsContext2D();
        bgDrawer.drawBackground(bgGc);
        getChildren().add(bgDrawer);
        bgDrawer.toBack();
        bgImgView.toBack();

        minimapDrawer = new CanvasDrawer(this,
                getBgImgView().getFitWidth()/10 * SCALE, getBgImgView().getFitHeight()/10 * SCALE);
        GraphicsContext minimapGc = minimapDrawer.getGraphicsContext2D();
        minimapDrawer.drawMinimap(minimapGc);
        minimapDrawer.setTranslateX(gameWidth - minimapDrawer.getWidth() - 2.5 * uiOffset);
        minimapDrawer.setTranslateY(healthUIHeight - (1.025 * getBgImgView().getFitHeight() * SCALE / 10));

        entitiesInMiniMap = new CanvasDrawer(this, minimapDrawer.getWidth(), minimapDrawer.getHeight());
        GraphicsContext entitiesGc = entitiesInMiniMap.getGraphicsContext2D();
        entitiesInMiniMap.drawEntities(entitiesGc);
        entitiesInMiniMap.setTranslateX(minimapDrawer.getTranslateX());
        entitiesInMiniMap.setTranslateY(minimapDrawer.getTranslateY());
    }

    public void createInventoryUI() {
        INVENTORY.stop();
        INVENTORY.play();
        inventoryUI.getChildren().clear();
        ImageView inventoryBg = new ImageView(new Image(ClassLoader.getSystemResource(
                getPlayer().getInventoryUIString()).toString()));
        inventoryBg.setFitWidth(0.8 * gameWidth);
        inventoryBg.setFitHeight(0.8 * gameHeight);
        inventoryBg.setOnMouseClicked(mouseEvent -> pressInventory());
        Group menuButton = createMenuButton();
        addAllStats(scaleForUI, inventoryBg, menuButton);
        inventoryUI.toFront();
        float[] posX = {(float) (0.075 * gameWidth), (float) (0.144 * gameWidth), (float) (0.212 * gameWidth)};
        float[] posY = {(float) (0.463 * gameHeight), (float) (0.577 * gameHeight)};
        drawAllPerks(inventoryUI, posX, posY);
        inventoryUI.setTranslateX(Math.abs(getTranslateX()) + 0.1 * gameWidth);
        inventoryUI.setTranslateY(Math.abs(getTranslateY()) + 0.1 * gameHeight);
    }

    private void drawAllPerks(Group group, float[] posX, float[] posY) {
        int x = 0, y = 0;
        for (BasePerk perk: player.getObtainPerks()) {
            if (x >= posX.length) {
                x = 0;
                y++;
                if (y >= posY.length) {
                    break;
                }
            }
            drawPerk(perk, posX[x], posY[y], group);
            x++;
        }
    }

    private void drawPerk(BasePerk perk, float posX, float posY, Group group) {
        createPerkIcon(perk, posX, posY, group);
        createPerkText(perk, posX, posY, group);
    }

    private void createPerkText(BasePerk perk, float posX, float posY, Group group) {
        String tier = perk.getTier() == TIER1 ? String.valueOf(1) : String.valueOf(2);
        Text perkTier;
        float deltaX, deltaY;
        if (group == inventoryUI) {
            perkTier = createText(tier, (int) (40 * scaleForUI), GREEN_FOR_UI);
            deltaX = (float) (0.03 * gameWidth); deltaY = (float) (0.069 * gameHeight);
        } else {
            perkTier = createText(tier, (int) (64 * scaleForUI), GREEN_FOR_UI);
            deltaX = (float) (0.046 * gameWidth); deltaY = (float) (0.1 * gameHeight);
            toRemovePerkIcons.add(perkTier);
        }
        perkTier.setTranslateX((posX + deltaX));
        perkTier.setTranslateY((posY + deltaY));
        group.getChildren().add(perkTier);
        perkTier.toFront();
    }

    private void createPerkIcon(BasePerk perk, float posX, float posY, Group group) {
        ImageView perkImageView = new ImageView(new Image(ClassLoader.getSystemResource(perk.getIconString()).toString()));
        double scale = (group == inventoryUI) ? 0.8 : 1.28;
        perkImageView.setFitWidth(perkImageView.getImage().getWidth() * scale * scaleForUI);
        perkImageView.setFitHeight(perkImageView.getImage().getHeight() * scale * scaleForUI);
        perkImageView.setTranslateX(posX);
        perkImageView.setTranslateY(posY);
        if (group == upgradeUI) {
            toRemovePerkIcons.add(perkImageView);
            perkImageView.setOnMouseClicked(mouseEvent -> {
                choiceUI.getChildren().clear();
                if (perk.getTier() == TIER1) {
                    pickPerkToUpgrade(perk);
                } else {
                    getSoundFromSoundList(player.getSoundList(), "PlayerBruh").play();
                }
            });
        } else if (group == inventoryUI){
            final ImageView[] imgView = new ImageView[1];
            perkImageView.setOnMouseEntered(mouseEvent -> imgView[0] = showMiniPopUp(perk, perkImageView.getTranslateX(), perkImageView.getTranslateY()));
            perkImageView.setOnMouseExited(mouseEvent -> {
                try {
                    inventoryUI.getChildren().remove(imgView[0]);
                } catch (Exception e) {}
            });
        }
        group.getChildren().add(perkImageView);
        perkImageView.toFront();
    }

    private ImageView showMiniPopUp(BasePerk perk, double posX, double posY) {
        ImageView popupImgView = perk.getImageAtRowColumn(perk.getTier(), 0);
        popupImgView.setTranslateX(posX);
        popupImgView.setTranslateY(posY - 0.35 * gameHeight);
        inventoryUI.getChildren().add(popupImgView);
        return popupImgView;
    }

    private void pickPerkToUpgrade(BasePerk perk) {
        upgradeUI.getChildren().remove(choiceUI);
        Tier[] target = {TIER2_1, TIER2_2};
        double[] posX = {0.27 * gameWidth, 0.52 * gameWidth};
        for (int i = 0; i < target.length; i++) {
            createPerkChoiceToUpgrade(perk, target[i], posX[i], 0.18 * gameHeight);
        }
        upgradeUI.getChildren().add(choiceUI);
    }

    private void createPerkChoiceToUpgrade(BasePerk perk, Tier tier, double posX, double posY) {
        ImageView choice = perk.getImageAtRowColumn(tier, 2);
        setUpChoice(choice, posX, posY, perk, tier);

        Group priceGroup = new Group();
        setUpPriceGroup(choice, perk, priceGroup);

        choiceUI.getChildren().addAll(choice, priceGroup);
    }

    private void setUpChoice(ImageView choice, double posX, double posY, BasePerk perk, Tier tier) {
        choice.setTranslateX(posX);
        choice.setTranslateY(posY);
        BaseScene scene = this;

        choice.setOnMouseClicked(mouseEvent -> {
            if (perk.canUpgrade(player)) {
                perk.upgrade(player, tier);
                if (scene instanceof FightScene) {
                    if (getChildren().contains(upgradeUI)) {
                        startAllThreads(scene);
                        setDefaultBrightness();
                        setPause(false);
                        getChildren().remove(upgradeUI);
                        ArrayList<BaseObject> allObjects = new ArrayList<>(player.getObjectsInStage());
                        for (BaseObject object : allObjects) {
                            if (object instanceof BlackSmith blackSmith) {
                                blackSmith.disappear(scene);
                                break;
                            }
                        }
                    }
                } else {
                    String currentCoin = String.valueOf(player.getCoins());
                    updateCoin(currentCoin);
                    upgradeUI.getChildren().removeAll(toRemovePerkIcons);
                    toRemovePerkIcons.clear();
                    float[] posX1 = {(float) (0.073 * gameWidth), (float) (0.174 * gameWidth)};
                    float[] posY1 = {(float) (0.215 * gameHeight), (float) (0.3735 * gameHeight), (float) (0.533 * gameHeight)};
                    drawAllPerks(upgradeUI, posX1, posY1);
                }
            } else {
                getSoundFromSoundList(player.getSoundList(), "BuyingFail").play();
            }
            choiceUI.getChildren().clear();
        });
    }

    private void setUpPriceGroup(ImageView choice, BasePerk perk, Group priceGroup) {
        ImageView coinUI = loadCoinUI();
        coinUI.setFitHeight(coinUI.getImage().getHeight() * 2.0 / 3.0 * scaleForUI);
        coinUI.setFitWidth(coinUI.getImage().getWidth() * 2.0 / 3.0 * scaleForUI);
        coinUI.setTranslateX(choice.getTranslateX() + scaleForUI * 105);
        coinUI.setTranslateY(choice.getTranslateY() + choice.getFitHeight() - scaleForUI * 100);

        String originalPrice = String.valueOf((int) (PRICE_UPGRADE_PERCENT * perk.getOriginalPrice()));
        Text normalCoinText = createText(originalPrice, 35, BLACK);
        normalCoinText.setTranslateX(coinUI.getTranslateX() + coinUI.getFitWidth() + 10 * scaleForUI);
        normalCoinText.setTranslateY(coinUI.getTranslateY() + coinUI.getFitHeight() - 3 * scaleForUI);
        Text discountCoinText = null;
        if (hasPerk(player, new DiscountMasterPerk(player, TIER1), false)) {
            perk.setNewPrice(perk.getOriginalPrice());
            String newPrice = String.valueOf((int) (PRICE_UPGRADE_PERCENT * perk.getNewPrice()));
            discountCoinText = createText("/".repeat(originalPrice.length() + 1) + " --> " + newPrice, 40, RED);
            discountCoinText.setTranslateX(normalCoinText.getTranslateX() + 10 * scaleForUI);
            discountCoinText.setTranslateY(normalCoinText.getTranslateY());
        }

        try {
            if (hasPerk(player, new DiscountMasterPerk(player, TIER1), false)) {
                priceGroup.getChildren().add(discountCoinText);
            }
            priceGroup.getChildren().addAll(coinUI, normalCoinText);
            discountCoinText.toFront();
        } catch (Exception ignored) {}
    }

    private Group createMenuButton() {
        Group menu = new Group();
        Rectangle rect = new Rectangle(0.1 * gameWidth, 0.1 * gameHeight, GREEN_FOR_UI);
        Text menuText = createText("MENU", (int) (112 * scaleForUI), WHITE);
        menuText.setTranslateX(0.08 * rect.getWidth());
        menuText.setTranslateY(0.7 * rect.getHeight());
        menuText.setOnMouseClicked(mouseEvent -> {
            stage.getScene().setRoot(getStartScene(stage));
            resetAllScenes();
        });
        menu.getChildren().addAll(rect, menuText);
        menu.setTranslateX(0.75 * gameWidth);
        menu.setTranslateY(0.75 * gameHeight);
        return menu;
    }

    private void addAllStats(double scaleForInventory, ImageView inventoryBg, Group button) {
        Text health = createTextForInventory(String.valueOf(finalValuePercent(player.getBaseMaxHp(),player.getAddMaxHp())),
                (int) (65 * scaleForInventory), GREEN_FOR_UI, 0.7 * inventoryBg.getFitWidth(), 0.355 * inventoryBg.getFitHeight());
        Text atk = createTextForInventory(String.valueOf(finalValuePercent(player.getBaseAtk(),player.getAddAtk())),
                (int) (65 * scaleForInventory), GREEN_FOR_UI, 0.7 * inventoryBg.getFitWidth(), 0.4175 * inventoryBg.getFitHeight());
        Text def = createTextForInventory(String.valueOf(finalValuePercent(player.getBaseDef(),player.getAddDef())),
                (int) (65 * scaleForInventory), GREEN_FOR_UI, 0.7 * inventoryBg.getFitWidth(), 0.48 * inventoryBg.getFitHeight());
        Text atkSpeed = createTextForInventory(100 + player.getAddAttackSpeed() + " %",
                (int) (65 * scaleForInventory), GREEN_FOR_UI, 0.7 * inventoryBg.getFitWidth(), 0.5425 * inventoryBg.getFitHeight());
        Text movementSpeed = createTextForInventory(100 + player.getAddMovementSpeed() + " %",
                (int) (65 * scaleForInventory), GREEN_FOR_UI, 0.7 * inventoryBg.getFitWidth(), 0.605 * inventoryBg.getFitHeight());
        Text evadeRate = createTextForInventory(player.getEvadeRate() + " %",
                (int) (65 * scaleForInventory), GREEN_FOR_UI, 0.7 * inventoryBg.getFitWidth(), 0.6675 * inventoryBg.getFitHeight());
        Text critRate = createTextForInventory(player.getCritRate() + " %",
                (int) (65 * scaleForInventory), GREEN_FOR_UI, 0.7 * inventoryBg.getFitWidth(), 0.73 * inventoryBg.getFitHeight());
        Text critDmg = createTextForInventory("x" + String.format("%.2f", 1 + ((double) player.getCritDamage() / 100)),
                (int) (65 * scaleForInventory), GREEN_FOR_UI, 0.7 * inventoryBg.getFitWidth(), 0.7925 * inventoryBg.getFitHeight());
        inventoryUI.getChildren().addAll(inventoryBg, health, atk, def, atkSpeed, movementSpeed, evadeRate, critRate, critDmg, button);
        getChildren().add(inventoryUI);
    }

    public void createUpgradeUI() {
        if (!isPause) {
            stopAllThreads();
            setLowBrightness();
            setPause(true);
            upgradeUI.getChildren().clear();
            ImageView upgradeBg = new ImageView(new Image(String.valueOf(ClassLoader.getSystemResource(
                    "etcUI/Upgrade_UI.png"))));
            upgradeBg.setFitWidth(0.8 * gameWidth);
            upgradeBg.setFitHeight(0.8 * gameHeight);
            Group exitBack = createBackButton();
            getChildren().add(upgradeUI);
            upgradeUI.getChildren().addAll(upgradeBg, exitBack, choiceUI);
            float[] posX = {(float) (0.073 * gameWidth), (float) (0.174 * gameWidth)};
            float[] posY = {(float) (0.215 * gameHeight), (float) (0.3735 * gameHeight), (float) (0.533 * gameHeight)};
            drawAllPerks(upgradeUI, posX, posY);
            upgradeUI.toFront();
            upgradeUI.setTranslateX(Math.abs(getTranslateX()) + 0.1 * gameWidth);
            upgradeUI.setTranslateY(Math.abs(getTranslateY()) + 0.1 * gameHeight);
        }
    }

    private Group createBackButton() {
        Group back = new Group();
        Rectangle rect = new Rectangle(0.1 * gameWidth, 0.1 * gameHeight, GREEN_FOR_UI);
        Text backText = createText("BACK", (int) (112 * scaleForUI), WHITE);
        backText.setTranslateX(0.08 * rect.getWidth());
        backText.setTranslateY(0.7 * rect.getHeight());
        backText.setOnMouseClicked(mouseEvent -> {
            if (getChildren().contains(upgradeUI)) {
                startAllThreads(this);
                setDefaultBrightness();
                setPause(false);
                getChildren().remove(upgradeUI);
            }
        });
        back.getChildren().addAll(rect, backText);
        back.setTranslateX(0.75 * gameWidth);
        back.setTranslateY(0.75 * gameHeight);
        return back;
    }

    public void createPerkChestUI() {
        BaseScene scene = this;
        perkChestUI = new Group();
        ImageView perkChestBg = new ImageView(new Image(String.valueOf(ClassLoader.getSystemResource("etcUI/PerkChest_UI.png"))));

        setPerkChestBackground(perkChestBg);

        ArrayList<BasePerk> perks = createPerkChoices(player, player.getNotObtainPerks(), 3);
        addSelectedPerksForChest(perks, perkChestBg, scene);

        perkChestUI.setTranslateX(Math.abs(getTranslateX()) + 0.1 * gameWidth);
        perkChestUI.setTranslateY(Math.abs(getTranslateY()) + 0.1 * gameHeight);
        getChildren().add(perkChestUI);
        perkChestUI.toFront();
    }

    private void setPerkChestBackground(ImageView perkChestBg) {
        perkChestBg.setFitWidth(0.8 * gameWidth);
        perkChestBg.setFitHeight(0.8 * gameHeight);
        perkChestUI.getChildren().add(perkChestBg);
    }

    private void addSelectedPerksForChest(ArrayList<BasePerk> perks, ImageView perkChestBg, BaseScene scene) {
        for (int i = 0; i < perks.size(); i++) {
            BasePerk perk = perks.get(i);
            ImageView perkImgView = perk.getImageAtRowColumn(perk.getTier(), 1);
            perkChestUI.getChildren().add(perkImgView);
            perkImgView.setTranslateX(perkChestUI.getTranslateX() +
                    82 * SCALE + perkChestBg.getFitWidth() * i/3.27);
            perkImgView.setTranslateY(perkChestUI.getTranslateY() + 0.189 * gameHeight);
            perkImgView.setOnMouseClicked(mouseEvent -> {
                selectPerkToObtain(perk, scene);
            });
        }
    }

    private void selectPerkToObtain(BasePerk perk, BaseScene scene) {
        perk.collect(player);
        getChildren().remove(perkChestUI);
        if (player.canGetDoubleReward()) {
            player.setTreasureHunterActivate(false);
            createPerkChestUI();
        } else {
            startAllThreads(scene);
            setDefaultBrightness();
            setPause(false);
        }
    }

    public void setKey(Animation animation) {
        stage.getScene().setOnKeyPressed(e -> {
            pressedKeys.add(e.getCode());
            handleKeyPressed(animation);
        });
        stage.getScene().setOnKeyReleased(e -> {
            pressedKeys.remove(e.getCode());
            handleKeyReleased(e.getCode(), animation);
        });
        stage.getScene().setOnMouseClicked(e -> handleMouseClicked(e.getButton(),animation));
        stage.getScene().setOnMousePressed(e -> handleMousePressed(e.getButton(), animation));
        stage.getScene().setOnMouseReleased(e -> handleMouseReleased(e.getButton(),animation));
    }

    private void handleKeyPressed(Animation animation) {
        if (!animation.isAnimationPlaying()) {
            if (pressedKeys.contains(KeyCode.TAB) && (!(player.getPlayerClass() instanceof Anonymous))) {
                pressInventory();
            }
            if (!isPause()) {
                if (pressedKeys.contains(KeyCode.A)) {
                    pressMoveLeft(animation);
                }
                if (pressedKeys.contains(KeyCode.D)) {
                    pressMoveRight(animation);
                }
                if (pressedKeys.contains(KeyCode.E)) {
                    interact();
                }
                if (pressedKeys.contains(KeyCode.SHIFT)) {
                    if (!(player.getPlayerClass() instanceof Anonymous)) {
                        activateDash(animation);
                    }
                }
                if (pressedKeys.contains(KeyCode.SPACE) && (!(player.getPlayerClass() instanceof Anonymous))) {
                    if (!pressedKeys.contains(KeyCode.S)) {
                        activateJump(animation);
                    } else {
                        activatePassingTheFloor();
                    }
                }
            }
        }
    }

    private void interact() {
        if (player.canInteract()) {
            if (this instanceof ClassSelectionScene) {
                interactInClassSelectionScene();
            } else if (this instanceof FightScene) {
                interactInFightScene();
            } else if (this instanceof MarketScene) {
                interactInMarketScene();
            }
        }
    }

    private void interactInClassSelectionScene() {
        if (player.getObjectThatInteractWith() instanceof Symbol symbol) {
            interactSymbol(symbol);
        } else if (player.getObjectThatInteractWith() instanceof VendingMachine vendingMachine) {
            vendingMachine.setCanInteract(false);
            player.setObjectThatInteractWith(null);
            player.setCanInteract(false);
            BasePerk perk = createPerkChoices(player, player.getNotObtainPerks(), 1).get(0);
            createSymbol(this, perk.toString(), 0.76, 1.4, perk);
        }
    }

    private void interactInFightScene() {
        if (player.getObjectThatInteractWith() instanceof BaseReward) {
            obtainBaseReward();
        } else if (player.getObjectThatInteractWith() instanceof PerkChestReward) {
            obtainPerkChestReward();
        } else if (player.getObjectThatInteractWith() instanceof Door) {
            nextStage();
        } else if (player.getObjectThatInteractWith() instanceof BlackSmith) {
            createOpenDoor();
            createUpgradeUI();
        }
    }

    private void interactInMarketScene() {
        if (player.getObjectThatInteractWith() instanceof Symbol perkSymbol) {
            pickPerkToBuy(perkSymbol);
        } else if (player.getObjectThatInteractWith() instanceof Merchant merchant) {
            if (merchant.canSell(player)) {
                merchant.sell(player, this);
            } else {
                getSoundFromSoundList(player.getSoundList(), "BuyingFail").play();
            }
        } else if (player.getObjectThatInteractWith() instanceof BlackSmith) {
            createUpgradeUI();
        }
    }

    private void pickPerkToBuy(Symbol perkSymbol) {
        BasePerk perk = perkSymbol.getPerk();
        if (perk.canSell(player)) {
            perk.sell(player, this);
            removePerk(perkSymbol);
        } else {
            getSoundFromSoundList(player.getSoundList(), "BuyingFail").play();
        }
    }

    private void removePerk(Symbol perkSymbol) {
        player.setCanInteract(false);
        player.setObjectThatInteractWith(null);
        getChildren().remove(perkSymbol.getAnimation());
        getChildren().remove(perkSymbol.getHitBox());
        getChildren().remove(perkSymbol.getPopUp());
        player.getObjectsInStage().remove(perkSymbol);
    }

    private void activatePassingTheFloor() {
        if (passableTile.contains(getTypeOfTile(player.getHitBox().getTranslateX(),
                player.getHitBox().getTranslateY() + player.getHitBox().getFitHeight() + 2, getLvlData())) ||
                passableTile.contains(getTypeOfTile(player.getHitBox().getTranslateX() + player.getHitBox().getFitWidth(),
                        player.getHitBox().getTranslateY() + player.getHitBox().getFitHeight() + 2, getLvlData()))) {
            player.getAnimation().setTranslateY(player.getAnimation().getTranslateY() + 2);
            player.getHitBox().setTranslateY(player.getHitBox().getTranslateY() + 2);
        }
    }

    private void activateJump(Animation animation) {
        if (animation.canJump()) {
            getSoundFromSoundList(player.getSoundList(), "PlayerJump1").play();
            getSoundFromSoundList(player.getSoundList(), "PlayerWalk").stop();
            if (animation.isJump()) {
                getSoundFromSoundList(player.getSoundList(), "PlayerJump2").play();
                getSoundFromSoundList(player.getSoundList(), "PlayerJump1").stop();
                animation.setCanJump(false);
            } else {
                animation.setJump(true);
            }
            animation.setyVelocity(JUMP_VELOCITY);
        }
    }

    private void activateDash(Animation animation) {
        if (!animation.isAnimationPlaying()) {
            if (player.canDash()) {
                getSoundFromSoundList(player.getSoundList(), "PlayerDash").play();
                animation.setDisableMovement();
                animation.setAnimationPlaying(true);
                animation.setState(PLAYER_DASH);
                player.setDash(true);
                player.setCanDash(false);
            }
        }
    }

    private void obtainBaseReward() {
        BaseReward baseReward = (BaseReward) player.getObjectThatInteractWith();
        getSoundFromSoundList(baseReward.getSoundList(), "BaseRewardGet").play();
        baseReward.collect(player);
        if (player.canGetDoubleReward()) {
            baseReward.collect(player);
        }
        player.setTreasureHunterActivate(false);
        player.getObjectsInStage().remove(player.getObjectThatInteractWith());
        getChildren().remove(player.getObjectThatInteractWith().getAnimation());
        player.setCanInteract(false);
        player.setObjectThatInteractWith(null);
        createOpenDoor();
    }

    private void obtainPerkChestReward() {
        PerkChestReward chest = (PerkChestReward) player.getObjectThatInteractWith();
        if (!chest.isOpen()) {
            chest.openChest();
            player.getObjectsInStage().remove(player.getObjectThatInteractWith());
            player.setCanInteract(false);
            player.setObjectThatInteractWith(null);
            createOpenDoor();
            stopAllThreads();
            setLowBrightness();
            setPause(true);
            createPerkChestUI();
        }
    }

    private void pressInventory() {
        if (!isPause) {
            stopAllThreads();
            setLowBrightness();
            setPause(true);
            createInventoryUI();
        } else {
            if (getChildren().contains(inventoryUI)) {
                startAllThreads(this);
                setDefaultBrightness();
                setPause(false);
                getChildren().remove(inventoryUI);
            }
        }
    }

    private void nextStage() {
        Door door = (Door) player.getObjectThatInteractWith();
        if (door.isOpen()) {
            stopAllBackgroundSongs();
            stopAllThreads();
            resetFightScene();
            getPlayer().getObjectsInStage().clear();
            getPlayer().setCanInteract(false);
            getPlayer().setObjectThatInteractWith(null);
            getSoundFromSoundList(door.getSoundList(), "DoorOpen").play();
            if (door.getDoorType().equals("Coin")) {
                getScene().setRoot(getFightScene(stage, player, "Coin"));
            } else if (door.getDoorType().equals("HP")) {
                getScene().setRoot(getFightScene(stage, player, "HP"));
            } else if (door.getDoorType().equals("PerkChest")) {
                getScene().setRoot(getFightScene(stage, player, "PerkChest"));
            } else if (door.getDoorType().equals("Market")) {
                getScene().setRoot(getMarketScene(stage, player));
            } else if (door.getDoorType().equals("Upgrade")) {
                getScene().setRoot(getFightScene(stage, player, "Upgrade"));
            }
        }
    }

    private void interactSymbol(Symbol symbol) {
        if (symbol.getPerk() == null) {
            if (symbol.getSymbolType().equals("Dagger")) {
                selectClass("Dagger");
            } else if (symbol.getSymbolType().equals("Bow")) {
                selectClass("Bow");
            } else if (symbol.getSymbolType().equals("BroadSword")) {
                selectClass("BroadSword");
            }
            ArrayList<BaseObject> allObjects = new ArrayList<>(player.getObjectsInStage());
            for (BaseObject vendingMachine : allObjects) {
                if (vendingMachine instanceof VendingMachine) {
                    vendingMachine.setCanInteract(true);
                    break;
                }
            }
            for (BaseObject perkSymbol : allObjects) {
                if (perkSymbol instanceof Symbol) {
                    if (((Symbol) perkSymbol).getPerk() != null) {
                        removePerk((Symbol) perkSymbol);
                        break;
                    }
                }
            }
        } else {
            symbol.getPerk().collect(player);
            removePerk(symbol);
        }
    }

    private void pressMoveLeft(Animation animation) {
        if (animation.isMoveRight()) {
            animation.setWillMoveLeft(true);
        } else {
            getSoundFromSoundList(player.getSoundList(), "PlayerWalk").play();
            animation.setMoveLeft(true);
            animation.setScaleX(-1);
            animation.setState(PLAYER_WALK);
        }
    }

    private void pressMoveRight(Animation animation) {
        if (animation.isMoveLeft()) {
            animation.setWillMoveRight(true);
        } else {
            getSoundFromSoundList(player.getSoundList(), "PlayerWalk").play();
            animation.setMoveRight(true);
            animation.setScaleX(1);
            animation.setState(PLAYER_WALK);
        }
    }

    private void createOpenDoor() {
        for (Door door : getDoors()) {
            getChildren().remove(door.getAnimation());
            door.openDoor();
            getChildren().add(door.getAnimation());
        }
    }

    private void selectClass(String className) {
        setPlayerPosition(className);
        FaceDirection previousFaceDirection = player.getFaceDirection();
        ArrayList<BaseObject> objects = player.getObjectsInStage();
        playerUI.getChildren().clear();
        getChildren().remove(player.getAnimation());
        getChildren().remove(playerUI);
        stopAllThreads();
        setPlayerClass(className);
        player.getObjectsInStage().addAll(objects);
        player.setFaceDirection(previousFaceDirection);
        setFaceDirection(previousFaceDirection);
        setKey(player.getAnimation());
        getChildren().add(player.getAnimation());
        drawUI();
        startAllThreads(this);
    }

    private void setPlayerClass(String className) {
        if (className == "Dagger") {
            setPlayer(new Player(new Dagger()));
        } else if (className == "Bow") {
            setPlayer(new Player(new Bow()));
        } else if (className == "BroadSword") {
            setPlayer(new Player(new BroadSword()));
        }
    }

    private void setPlayerPosition(String className) {
        if (!className.equals("BroadSword")) {
            SPAWN_X = player.getAnimation().getTranslateX() - (DEFAULT_ANIMATION_WIDTH * SCALE - player.getAnimation().getFitWidth())/2;
            SPAWN_Y = player.getAnimation().getTranslateY() - ((DEFAULT_HITBOX_Y_OFFSET * SCALE + DEFAULT_HITBOX_HEIGHT * SCALE) - (player.getHitBox().getFitHeight() + player.getHitBoxYOffset() * SCALE));
        } else {
            SPAWN_X = player.getAnimation().getTranslateX() - (BROADSWORD_ANIMATION_WIDTH * SCALE - player.getAnimation().getFitWidth())/2;
            SPAWN_Y = player.getAnimation().getTranslateY() - ((BROADSWORD_HITBOX_Y_OFFSET * SCALE + BROADSWORD_HITBOX_HEIGHT * SCALE) - (player.getHitBox().getFitHeight() + player.getHitBoxYOffset() * SCALE));
        }
    }

    private void setFaceDirection(FaceDirection previousFaceDirection) {
        if (previousFaceDirection == LEFT) {
            player.getAnimation().setScaleX(-1);
        } else {
            player.getAnimation().setScaleX(1);
        }
    }

    public void startAllThreads(BaseScene baseScene) {
        playerAnimationThread = new PlayerAnimationThread(baseScene);
        movingThread = new MovingThread(baseScene);
        monsterAnimationThread = new MonsterAnimationThread(baseScene);
        botLogicThread = new BotLogicThread(baseScene);
        statThread = new StatThread(this);
        movingThread.setDaemon(true);
        movingThread.start();
        playerAnimationThread.setDaemon(true);
        playerAnimationThread.start();
        monsterAnimationThread.setDaemon(true);
        monsterAnimationThread.start();
        botLogicThread.setDaemon(true);
        botLogicThread.start();
        statThread.setDaemon(true);
        statThread.start();
    }

    private void handleKeyReleased(KeyCode keyCode, Animation animation) {
        if(!animation.isAnimationPlaying()) {
            if (keyCode == KeyCode.A) {
                releaseMoveLeft(animation);
            } else if (keyCode == KeyCode.D) {
                releaseMoveRight(animation);
            }
        }
    }

    private void releaseMoveRight(Animation animation) {
        getSoundFromSoundList(player.getSoundList(), "PlayerWalk").stop();
        animation.setMoveRight(false);
        animation.setWillMoveRight(false);
        if (!animation.isMoveLeft()) {
            if (animation.willMoveLeft()) {
                animation.setWillMoveLeft(false);
                animation.setScaleX(-1);
                animation.setMoveLeft(true);
            } else {
                animation.setState(PLAYER_STAND_STILL);
            }
        }
    }

    private void releaseMoveLeft(Animation animation) {
        getSoundFromSoundList(player.getSoundList(), "PlayerWalk").stop();
        animation.setMoveLeft(false);
        animation.setWillMoveLeft(false);
        if (!animation.isMoveRight()) {
            if (animation.willMoveRight()) {
                animation.setWillMoveRight(false);
                animation.setScaleX(1);
                animation.setMoveRight(true);
            } else {
                animation.setState(PLAYER_STAND_STILL);
            }
        }
    }

    private void handleMouseClicked(MouseButton button, Animation animation) {
        if (!(player.getPlayerClass() instanceof Anonymous)) {
            if (Objects.requireNonNull(button) == MouseButton.PRIMARY) {
                activateNormalAttack(animation);
            } else if (Objects.requireNonNull(button) == MouseButton.SECONDARY) {
                activateSpecialAttack(animation);
            }
        }
    }

    private void activateNormalAttack(Animation animation) {
        if (!animation.isAnimationPlaying()) {
            animation.setDisableMovement();
            animation.setAnimationPlaying(true);
            animation.setState(PLAYER_NORMAL_ATTACK);
        }
    }

    private void activateSpecialAttack(Animation animation) {
        if (player.getPlayerClass() instanceof Dagger){
            activateDaggerSpecialAttack(animation);
        } else if (player.getPlayerClass() instanceof BroadSword && !player.isSpecialAttackInCooldown()) {
            activateBroadSwordSpecialAttack(animation);
        }
    }

    private void handleMousePressed(MouseButton button, Animation animation) {
        if (!(player.getPlayerClass() instanceof Anonymous)) {
            if (Objects.requireNonNull(button) == MouseButton.SECONDARY) {
                if (player.getPlayerClass() instanceof Bow && !player.isSpecialAttackInCooldown()) {
                    activateBowSpecialAttack(animation);
                }
            }
        }
    }

    private void activateBowSpecialAttack(Animation animation) {
        if (!animation.isAnimationPlaying()) {
            animation.setDisableMovement();
            animation.setHoldRMB(true);
            animation.setAnimationPlaying(true);
            animation.setState(PLAYER_SPECIAL_ATTACK);
        }
    }

    private void activateBroadSwordSpecialAttack(Animation animation) {
        if (!animation.isAnimationPlaying()) {
            animation.setDisableMovement();
            animation.setAnimationPlaying(true);
            animation.setState(PLAYER_SPECIAL_ATTACK);
        }
    }

    private void activateDaggerSpecialAttack(Animation animation) {
        if (player.canWarpToNearestMonster() && !player.isSpecialAttackInCooldown()) {
            if (!animation.isAnimationPlaying()) {
                animation.setDisableMovement();
                animation.setAnimationPlaying(true);
                animation.setState(PLAYER_SPECIAL_ATTACK);
                player.warpToNearestMonster(this);
            }
        }
    }

    private void handleMouseReleased(MouseButton button, Animation animation) {
        if (Objects.requireNonNull(button) == MouseButton.SECONDARY) {
            if (animation.isHoldRMB()) {
                animation.setHoldRMB(false);
            }
        }
    }

    public void blink(ImageView imageView) {
        ColorAdjust colorAdjust = new ColorAdjust();
        AnimationTimer blinkTimer = new AnimationTimer() {
            private long startTime = System.nanoTime();
            @Override
            public void handle(long now) {
                long elapsedTime = now - startTime;
                double secondsElapsed = elapsedTime / 1_000_000_000.0;
                if (secondsElapsed < 0.125) {
                    colorAdjust.setBrightness(0.9);
                } else if (secondsElapsed < 0.175){
                    colorAdjust.setBrightness(0.75);
                } else {
                    colorAdjust.setBrightness(0);
                    imageView.setEffect(colorAdjust);
                    stop();
                }
                imageView.setEffect(colorAdjust);
            }
        };
        blinkTimer.start();
    }
    public void setScenePosition() {
        if (getPlayer().getAnimation().getTranslateY() >= (double) gameHeight / 2) {
            if (getPlayer().getAnimation().getTranslateY() <= getBgImgView().getFitHeight() - (double) gameHeight / 2) {
                setTranslateY(-getPlayer().getAnimation().getTranslateY() + (double) gameHeight / 2);
            } else {
                setTranslateY(-getBgImgView().getFitHeight() + (double) gameHeight);
            }
        } else {
            setTranslateY(0);
        }
        if (getPlayer().getAnimation().getTranslateX() >= (double) gameWidth / 2) {
            if (getPlayer().getAnimation().getTranslateX() <= getBgImgView().getFitWidth() - (double) gameWidth / 2) {
                setTranslateX(-getPlayer().getAnimation().getTranslateX() + (double) gameWidth / 2);
            } else {
                setTranslateX(-getBgImgView().getFitWidth() + (double) gameWidth + 1.5 * PIXEL_PER_TILE);
            }
        } else {
            setTranslateX(0);
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

    public void stopAllThreads() {
        playerAnimationThread.stopThread();
        movingThread.stopThread();
        monsterAnimationThread.stopThread();
        botLogicThread.stopThread();
        statThread.stopThread();
    }

    public void setLowBrightness() {
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(-0.5);
        for (Node node : getChildren()) {
            node.setEffect(colorAdjust);
        }
    }

    public void setDefaultBrightness() {
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(0);
        for (Node node : getChildren()) {
            node.setEffect(colorAdjust);
        }
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Image getBgImg() {
        return bgImg;
    }

    public void setBgImg(Image bgImg) {
        this.bgImg = bgImg;
    }

    public ImageView getBgImgView() {
        return bgImgView;
    }

    public void setBgImgView(ImageView imageView) {
        this.bgImgView = imageView;
    }

    public Set<KeyCode> getPressedKeys() {
        return pressedKeys;
    }

    public int[][] getLvlData() {
        return lvlData;
    }

    public void setLvlData(int[][] lvlData) {
        this.lvlData = lvlData;
    }

    public Stage getStage() {
        return stage;
    }

    public Group getPlayerUI() {
        return playerUI;
    }

    public ArrayList<Door> getDoors() {
        return doors;
    }

    public void setDoors(ArrayList<Door> doors) {
        this.doors = doors;
    }

    public boolean isPause() {
        return isPause;
    }

    public void setPause(boolean pause) {
        isPause = pause;
    }

    public ImageView getDashIcon() {
        return dashIcon;
    }

    public Rectangle getDashCoolDown() {
        return dashCoolDown;
    }

    public ImageView getSpecialAttackIcon() {
        return specialAttackIcon;
    }

    public Rectangle getSpecialAttackCoolDown() {
        return specialAttackCoolDown;
    }
}
