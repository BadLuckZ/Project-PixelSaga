// A Class that is a base class for all objects

package objects;

import animating.Animation;
import animating.HitBox;
import entities.bases.BaseEntity;
import entities.bases.BaseMonster;
import entities.bases.BasePerk;
import entities.monsters.IceThrowerGiGee;
import entities.monsters.KleeGiGee;
import entities.player.Player;
import entities.player.perks.DiscountMasterPerk;
import enums.FaceDirection;
import interfaces.Moveable;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import scenes.BaseScene;
import scenes.MarketScene;
import utils.SoundLoader;

import java.util.ArrayList;
import java.util.Arrays;

import static enums.FaceDirection.*;
import static enums.States.MONSTER_DEAD;
import static enums.Tier.TIER1;
import static enums.States.*;
import static javafx.scene.paint.Color.*;
import static utils.AnimationStatus.*;
import static utils.GameConstants.*;
import static utils.HelperMethods.*;
import static utils.ObjectStatus.*;

public abstract class BaseObject implements Moveable {
    private Animation animation;
    private HitBox hitBox;
    private float speed;
    private double posX;
    private double posY;
    private FaceDirection faceDirection;
    private String imgString;
    private int height;
    private int width;
    private static final ArrayList<String> FROM_PLAYER = new ArrayList<>(Arrays.asList("NormalArrow", "ChargeArrow"));
    private boolean willRemove = false, canIgnoreTile, canFall, isDamageDealt, canInteract;
    private double distance;
    private BaseEntity owner;
    private static final ArrayList<String> GIF_CLASS = new ArrayList<>(Arrays.asList(
            "HPDropSmall", "HPDropLarge", "BowSymbol", "DaggerSymbol", "CoinReward", "HPReward"));
    private Group popUp = new Group();
    private ArrayList<SoundLoader> soundList;

    public BaseObject(BaseEntity owner, double posX, double posY, FaceDirection faceDirection) {
        setPosX(posX);
        setPosY(posY);
        setFaceDirection(faceDirection);
        setSpeed(NOT_MOVE);
        setCanIgnoreTile(false);
        setCanFall(false);
        setDamageDealt(false);
        setCanInteract(false);
        if (GIF_CLASS.contains(getClass().getSimpleName())) {
            setImgString("objects/" + getClass().getSimpleName() + "_Sprite.gif");
        } else {
            setImgString("objects/" + getClass().getSimpleName() + "_Sprite.png");
        }
        setOwner(owner);
        setSoundList(loadObjectsSound(this));
    }

    @Override
    public void move(BaseScene scene) {
        float moveSpeed = MOVE_PER_FRAME * getSpeed();
        distance += moveSpeed;
        if (getDistance() > getOwner().getAttackRange() * PIXEL_PER_TILE && !(this instanceof ThrowerBomb)) {
            setWillRemove(true);
        } else {
            if (!canIgnoreTile) {
                checkHitTile(moveSpeed, scene);
            } else {
                moveObject(moveSpeed);
            }
            if (FROM_PLAYER.contains(getClass().getSimpleName()) ||
                    (this instanceof StompEffect && getOwner() instanceof Player)) {
                checkHitMonster(scene);
            } else {
                checkHitPlayer(scene);
            }
            if (canFall) {
                if (!getAnimation().isJump()) checkFall(scene);
                if (getAnimation().isJump()) jump(scene);
            }
        }
    }

    private void checkHitTile(double moveSpeed, BaseScene scene) {
        double newMoveSpeed = (getFaceDirection() == RIGHT) ? moveSpeed : -moveSpeed;
        if (canMoveHereForObject(getHitBox().getTranslateX() + newMoveSpeed, getHitBox().getTranslateY(),
                getHitBox().getFitWidth(), getHitBox().getFitHeight(), scene.getLvlData())) {
            Platform.runLater(() -> {
                getHitBox().setTranslateX(getHitBox().getTranslateX() + newMoveSpeed);
                getAnimation().setTranslateX(getAnimation().getTranslateX() + newMoveSpeed);
            });
        } else {
            if (this instanceof ThrowerBomb) {
                Platform.runLater(() -> {
                    if (!((ThrowerBomb) this).isActivate()) {
                        ((ThrowerBomb) this).activateBomb(scene);
                    }
                });
            } else {
                setWillRemove(true);
            }
        }
    }

    private void moveObject(double moveSpeed) {
        double newMoveSpeed = (getFaceDirection() == RIGHT) ? moveSpeed : -moveSpeed;
        Platform.runLater(() -> {
            getHitBox().setTranslateX(getHitBox().getTranslateX() + newMoveSpeed);
            getAnimation().setTranslateX(getAnimation().getTranslateX() + newMoveSpeed);
        });
    }

    private void checkHitMonster(BaseScene scene) {
        ArrayList<BaseMonster> allMonsters = new ArrayList<>(scene.getPlayer().getMonstersInStage());
        for (BaseMonster monster : allMonsters) {
            if (scene.getPlayer().getMonstersInStage().contains(monster)) {
                if (isHitEntity(monster)) {
                    if (this instanceof NormalArrow) {
                        if (monster.canTakeDamage()) {
                            monster.isAttacked(scene.getPlayer(), scene, 0);
                            setWillRemove(true);
                        }
                    } else if (this instanceof ChargeArrow) {
                        if (monster.canTakeDamage()) {
                            monster.isAttacked(scene.getPlayer(), scene, ((ChargeArrow) this).getDamageIncreasing());
                        }
                    } else if (this instanceof StompEffect) {
                        if (getOwner() instanceof Player) {
                            if (monster.canTakeDamage()) {
                                monster.isAttacked(scene.getPlayer(), scene, BROADSWORD_SPECIAL_ATTACK_DAMAGE);
                            }
                        }
                    }
                }
            }
        }
    }

    private void checkHitPlayer(BaseScene scene) {
        if (isHitEntity(scene.getPlayer())) {
            Player player = scene.getPlayer();
            if (player.canTakeDamage()) {
                makeDamage(player, scene);
            }
            if ((this instanceof HPDropSmall) || this instanceof HPDropLarge) {
                heal(player);
            } else if (this instanceof Shroom) {
                activateSuperJump(player, scene);
            } else if (canInteract()) {
                moveInsideInteractObject(scene);
            }
        } else {
            if (canInteract()) {
                moveOutInteractObject(scene);
            }
        }
    }

    private void makeDamage(Player player, BaseScene scene) {
        if (this instanceof Missile) {
            player.isAttacked(getOwner(), scene, 0);
            setWillRemove(true);
        } else if (!this.isDamageDealt()) {
            if (this instanceof Bomb) {
                player.isAttacked(getOwner(), scene, (getOwner() instanceof KleeGiGee) ? 0 : 150);
            } else if (this instanceof StompEffect && getOwner() instanceof BaseMonster) {
                player.isAttacked(getOwner(), scene, 0);
            } else if (this instanceof ShockWave) {
                player.isAttacked(getOwner(), scene, SHOCK_WAVE_EXTRA_DAMAGE);
            } else if (this instanceof Fire) {
                player.isAttacked(getOwner(), scene, FIRE_EXTRA_DAMAGE);
            } else if (this instanceof ThrowerBomb) {
                makeThrowerBombDamage(player, scene);
            }
            if (!(this instanceof Fire || this instanceof ThrowerBomb)) setDamageDealt(true);
        }
    }
    private void makeThrowerBombDamage(Player player, BaseScene scene) {
        if (((ThrowerBomb) this).isActivate()) {
            player.isAttacked(getOwner(), scene, 0);
            this.setDamageDealt(true);
            if (getOwner() instanceof IceThrowerGiGee) {
                applyIceThrowerSlow(player);
            }
        } else {
            Platform.runLater(() -> {
                if (!((ThrowerBomb) this).isActivate()) ((ThrowerBomb) this).activateBomb(scene);
            });
        }
    }

    private void applyIceThrowerSlow(Player player) {
        int movementSlowAmount;
        int dashSlowAmount;
        if (player.getAddMovementSpeed() - ICETHROWERGIGEE_SLOW_AMOUNT > -ICETHROWERGIGEE_MAX_SLOW_AMOUNT) {
            movementSlowAmount = ICETHROWERGIGEE_SLOW_AMOUNT;
        } else {
            movementSlowAmount = (player.getAddMovementSpeed() + ICETHROWERGIGEE_MAX_SLOW_AMOUNT);
        }
        if (player.getAddDashDistance() - ICETHROWERGIGEE_SLOW_AMOUNT > -ICETHROWERGIGEE_MAX_SLOW_AMOUNT) {
            dashSlowAmount = ICETHROWERGIGEE_SLOW_AMOUNT;
        } else {
            dashSlowAmount = (player.getAddDashDistance() + ICETHROWERGIGEE_MAX_SLOW_AMOUNT);
        }
        player.setAddMovementSpeed(player.getAddMovementSpeed() - movementSlowAmount);
        player.setAddDashDistance(player.getAddDashDistance() - dashSlowAmount);
        setSlowTimer(player, movementSlowAmount, dashSlowAmount);
    }

    private void setSlowTimer(Player player, int movementSlowAmount, int dashSlowAmount) {
        AnimationTimer slowTimer = new AnimationTimer() {
            long lastTime = System.nanoTime();

            @Override
            public void handle(long now) {
                long elapsedTime = now - lastTime;
                double secondsElapsed = elapsedTime / 1_000_000_000.0;
                if (secondsElapsed > ICETHROWERGIGEE_SLOW_DURATION) {
                    player.setAddMovementSpeed(player.getAddMovementSpeed() + movementSlowAmount);
                    player.setAddDashDistance(player.getAddDashDistance() + dashSlowAmount);
                    stop();
                }
            }
        };
        slowTimer.start();
    }

    private void heal(Player player) {
        if (player.getCurrentHp() < finalValuePercent(player.getBaseMaxHp(), player.getAddMaxHp())) {
            player.setCurrentHp((int) (player.getCurrentHp() +
                    ((this instanceof HPDropSmall) ? HP_DROP_SMALL_HEAL : HP_DROP_LARGE_HEAL) / 100 * finalValuePercent(player.getBaseMaxHp(), player.getAddMaxHp())));
            setWillRemove(true);
            getSoundFromSoundList(getSoundList(), "GetHeal").play();
        }
    }

    private void activateSuperJump(Player player, BaseScene scene) {
        if (player.getAnimation().getState() == PLAYER_JUMP_DOWN) {
            player.getAnimation().setyVelocity(-40 * SCALE);
            player.getAnimation().setCanJump(true);
            runOneTimeAnimation(SHROOM_FRAMES, SHROOM_FRAMES_PER_SECOND, scene, false);
        }
    }

    private void moveInsideInteractObject(BaseScene scene) {
        if (scene.getPlayer().getObjectThatInteractWith() != this) {
            scene.getPlayer().setCanInteract(true);
            scene.getPlayer().setObjectThatInteractWith(this);
            if (this instanceof Merchant merchant) {
                if (!merchant.isSold()) {
                    createMerchantPopUp(merchant, scene);
                }
            } else if (this instanceof Symbol symbol) {
                if (symbol.getPerk() != null) {
                    addPerkSymbolPopUp(symbol.getPerk(), scene);
                } else {
                    addClassSymbolPopUp(symbol, scene);
                }
            }
        }
    }

    private void moveOutInteractObject(BaseScene scene) {
        if (scene.getPlayer().getObjectThatInteractWith() == this) {
            scene.getPlayer().setCanInteract(false);
            scene.getPlayer().setObjectThatInteractWith(null);
        }
        if (this instanceof Merchant || this instanceof Symbol) {
            removePopUp(scene);
        }
    }

    private void checkFall(BaseScene scene) {
        if (canMoveHereForObject(getHitBox().getTranslateX(), getHitBox().getTranslateY() + 1,
                getHitBox().getFitWidth(), getHitBox().getFitHeight(), scene.getLvlData())) {
            getAnimation().setJump(true);
            getAnimation().setyVelocity(0);
        }
    }

    private void jump(BaseScene scene) {
        getAnimation().setyVelocity(getAnimation().getyVelocity() + GRAVITY_FOR_OBJECT);
        if (canIgnoreTile) {
            Platform.runLater(() -> {
                getHitBox().setTranslateY(getHitBox().getTranslateY() + getAnimation().getyVelocity());
                getAnimation().setTranslateY(getAnimation().getTranslateY() + getAnimation().getyVelocity());
            });
        } else {
            if (canMoveHereForObject(getHitBox().getTranslateX(), getHitBox().getTranslateY() + getAnimation().getyVelocity(),
                    getHitBox().getFitWidth(), getHitBox().getFitHeight(), scene.getLvlData())) {
                Platform.runLater(() -> {
                    getHitBox().setTranslateY(getHitBox().getTranslateY() + getAnimation().getyVelocity());
                    getAnimation().setTranslateY(getAnimation().getTranslateY() + getAnimation().getyVelocity());
                });
            } else {
                if (this instanceof ThrowerBomb) {
                    Platform.runLater(() -> {
                        if (!((ThrowerBomb) this).isActivate()) ((ThrowerBomb) this).activateBomb(scene);
                    });
                }
                getAnimation().setyVelocity(0);
                getAnimation().setJump(false);
            }
        }
    }

    private void createMerchantPopUp(Merchant merchant, BaseScene scene) {
        Platform.runLater(() -> {
            createPotionPrice(merchant);
            scene.getChildren().add(getPopUp());
        });
    }

    private void removePopUp(BaseScene scene) {
        Platform.runLater(() -> {
            try {
                scene.getChildren().remove(getPopUp());
            } catch (Exception e) {}
        });
    }

    private void addClassSymbolPopUp(Symbol symbol, BaseScene scene) {
        Platform.runLater(() -> {
            createClassDetail(symbol);
            try {
                scene.getChildren().add(getPopUp());
            } catch (Exception e){}
        });
    }

    private void createClassDetail(Symbol symbol) {
        Image image = new Image(ClassLoader.getSystemResource(symbol.getPopUpString()).toString());
        ImageView imgView = new ImageView(image);
        imgView.setFitWidth(image.getWidth() * 0.8 * scaleForUI);
        imgView.setFitHeight(image.getHeight() * 0.8 * scaleForUI);
        imgView.setTranslateX(symbol.getAnimation().getTranslateX() + 0.05 * gameWidth);
        imgView.setTranslateY(symbol.getAnimation().getTranslateY() - 0.45 * gameHeight);
        try {
            popUp.getChildren().add(imgView);
        } catch (Exception e) {}
    }

    private void addPerkSymbolPopUp(BasePerk perk, BaseScene scene) {
        Platform.runLater(() -> {
            createPerkDetail(perk);
            if (scene instanceof MarketScene) createPerkPrice(perk);
            scene.getChildren().add(getPopUp());
        });
    }

    private void createPotionPrice(Merchant merchant) {
        ImageView coinImgView = loadCoinUI();
        coinImgView.setFitHeight(coinImgView.getImage().getHeight() * 2.0 / 3.0 * scaleForUI);
        coinImgView.setFitWidth(coinImgView.getImage().getWidth() * 2.0 / 3.0 * scaleForUI);
        coinImgView.setTranslateX(getAnimation().getTranslateX() - 50 * scaleForUI);
        coinImgView.setTranslateY(getAnimation().getTranslateY() - 50 * scaleForUI);

        String originalPrice = String.valueOf(merchant.getPrice());
        Text normalCoinText = createText(originalPrice, 56 , WHITE);
        normalCoinText.setTranslateX(coinImgView.getTranslateX() + 4.5 * scaleForUI * uiOffset);
        normalCoinText.setTranslateY(coinImgView.getTranslateY() + 3 * scaleForUI * uiOffset);
        Text discountCoinText = null;
        if (hasPerk((Player) owner, new DiscountMasterPerk((Player) owner, TIER1), false)) {
            merchant.setPrice(merchant.calculateDiscountPrice((Player) owner));
            String newPrice = String.valueOf(merchant.getPrice());
            discountCoinText = createText("/".repeat(originalPrice.length() + 1) + " --> " + newPrice, 40, RED);
            discountCoinText.setTranslateX(coinImgView.getTranslateX() + 5 * scaleForUI * uiOffset);
            discountCoinText.setTranslateY(coinImgView.getTranslateY() + 3.1 * scaleForUI * uiOffset);
        }

        try {
            if (hasPerk((Player) owner, new DiscountMasterPerk((Player) owner, TIER1), false)) {
                popUp.getChildren().add(discountCoinText);
            }
            popUp.getChildren().addAll(coinImgView, normalCoinText);
            discountCoinText.toFront();
        } catch (Exception ignored) {}
    }

    private void createPerkPrice(BasePerk perk) {
        ImageView coinImgView = loadCoinUI();
        coinImgView.setFitHeight(coinImgView.getImage().getHeight() * 2.0 / 3.0 * scaleForUI);
        coinImgView.setFitWidth(coinImgView.getImage().getWidth() * 2.0 / 3.0 * scaleForUI);
        coinImgView.setTranslateX(getAnimation().getTranslateX() - 120 * scaleForUI);
        coinImgView.setTranslateY(getAnimation().getTranslateY() - 505 * scaleForUI);

        String originalPrice = String.valueOf(perk.getOriginalPrice());
        Text normalCoinText = createText(originalPrice, 30, WHITE);
        normalCoinText.setTranslateX(coinImgView.getTranslateX() + 4.5 * scaleForUI * uiOffset);
        normalCoinText.setTranslateY(coinImgView.getTranslateY() + 3 * scaleForUI * uiOffset);
        Text discountCoinText = null;
        if (hasPerk((Player) owner, new DiscountMasterPerk((Player) owner, TIER1), false)) {
            perk.setNewPrice(perk.getOriginalPrice());
            String newPrice = String.valueOf(perk.getNewPrice());
            discountCoinText = createText("/".repeat(originalPrice.length() + 1) + " --> " + newPrice, 35, RED);
            discountCoinText.setTranslateX(coinImgView.getTranslateX() + 5 * scaleForUI * uiOffset);
            discountCoinText.setTranslateY(coinImgView.getTranslateY() + 3.1 * scaleForUI * uiOffset);
        }

        try {
            if (hasPerk((Player) owner, new DiscountMasterPerk((Player) owner, TIER1), false)) {
                popUp.getChildren().add(discountCoinText);
            }
            popUp.getChildren().addAll(coinImgView, normalCoinText);
            discountCoinText.toFront();
        } catch (Exception ignored) {}
    }

    private void createPerkDetail(BasePerk perk) {
        ImageView perkImageView = perk.getImageAtRowColumn(perk.getTier(), 0);
        perkImageView.setFitHeight(700 * scaleForUI);
        perkImageView.setFitWidth(700 * scaleForUI);
        perkImageView.setTranslateX(getAnimation().getTranslateX() - 500 * scaleForUI);
        perkImageView.setTranslateY(getAnimation().getTranslateY() - 500 * scaleForUI);
        popUp.getChildren().add(perkImageView);
    }

    private boolean isHitEntity(BaseEntity baseEntity) {
        if (baseEntity.getAnimation().getState() != MONSTER_DEAD) {
            if (getHitBox().getTranslateY() + getHitBox().getFitHeight() < baseEntity.getHitBox().getTranslateY() ||
                    baseEntity.getHitBox().getTranslateY() + baseEntity.getHitBox().getFitHeight() < getHitBox().getTranslateY()) {
                return false;
            }
            for (int i = 1; i <= 16; i++) {
                if (checkEntityHitbox(getHitBox().getTranslateX() + getHitBox().getFitWidth() * i / 16, baseEntity)) return true;
            }
        }
        return false;
    }

    private boolean checkEntityHitbox(double posX, BaseEntity baseEntity) {
        return posX < baseEntity.getHitBox().getTranslateX() + baseEntity.getHitBox().getFitWidth() &&
                baseEntity.getHitBox().getTranslateX() < posX;
    }

    protected void runOneTimeAnimation(int frames, int framesPerSec, BaseScene scene, boolean willRemove) {
        BaseObject object = this;
        Platform.runLater(() -> {
            AnimationTimer runAnimation = new AnimationTimer() {
                long lastTime = 0;
                int frameCount = 0;

                @Override
                public void handle(long now) {
                    long elapsedTime = now - lastTime;
                    double secondsElapsed = elapsedTime / 1_000_000_000.0;
                    if (frameCount == frames) {
                        if (willRemove) {
                            scene.getChildren().remove(object.getAnimation());
                            scene.getPlayer().getObjectsInStage().remove(object);
                        } else {
                            getAnimation().setViewport(new Rectangle2D(START_X, START_Y, getWidth(), getHeight()));
                        }
                        stop();
                    }
                    if (secondsElapsed > (double) 1 / framesPerSec) {
                        getAnimation().setViewport(new Rectangle2D(START_X, START_Y + frameCount * (getHeight() + GAP_HEIGHT),
                                getWidth(), getHeight()));
                        lastTime = now;
                        frameCount += 1;
                    }
                }
            };
            runAnimation.start();
        });
    }

    public Animation getAnimation() {
        return animation;
    }

    public void setAnimation(Animation animation) {
        this.animation = animation;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public double getPosX() {
        return posX;
    }

    public void setPosX(double posX) {
        this.posX = posX;
    }

    public double getPosY() {
        return posY;
    }

    public void setPosY(double posY) {
        this.posY = posY;
    }

    public FaceDirection getFaceDirection() {
        return faceDirection;
    }

    public void setFaceDirection(FaceDirection faceDirection) {
        this.faceDirection = faceDirection;
    }

    public String getImgString() {
        return imgString;
    }

    public void setImgString(String imgString) {
        this.imgString = imgString;
    }

    public HitBox getHitBox() {
        return hitBox;
    }

    public void setHitBox(HitBox hitBox) {
        this.hitBox = hitBox;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public boolean willRemove() {
        return willRemove;
    }

    public void setWillRemove(boolean willRemove) {
        this.willRemove = willRemove;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public BaseEntity getOwner() {
        return owner;
    }

    public void setOwner(BaseEntity owner) {
        this.owner = owner;
    }

    public Group getPopUp() {
        return popUp;
    }

    public ArrayList<SoundLoader> getSoundList() {
        return soundList;
    }

    public void setSoundList(ArrayList<SoundLoader> soundList) {
        this.soundList = soundList;
    }

    public void setCanIgnoreTile(boolean canIgnoreTile) {
        this.canIgnoreTile = canIgnoreTile;
    }

    public void setCanFall(boolean canFall) {
        this.canFall = canFall;
    }

    public boolean isDamageDealt() {
        return isDamageDealt;
    }

    public void setDamageDealt(boolean damageDealt) {
        isDamageDealt = damageDealt;
    }

    public boolean canInteract() {
        return canInteract;
    }

    public void setCanInteract(boolean canInteract) {
        this.canInteract = canInteract;
    }
}