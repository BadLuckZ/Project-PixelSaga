// An Entity for playing

package entities.player;

import animating.Animation;
import animating.HitBox;
import entities.bases.BaseEntity;
import entities.bases.BaseClass;
import entities.bases.BaseMonster;
import entities.player.classes.*;
import entities.bases.BasePerk;
import entities.player.perks.*;
import enums.FaceDirection;
import enums.States;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import objects.BaseObject;
import objects.BlackSmith;
import objects.VendingMachine;
import scenes.BaseScene;
import scenes.BossScene;
import scenes.ClassSelectionScene;
import scenes.MarketScene;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static entities.player.perks.UndeadPerk.*;
import static enums.FaceDirection.LEFT;
import static enums.FaceDirection.RIGHT;
import static enums.States.*;
import static enums.Tier.*;
import static javafx.scene.paint.Color.*;
import static scenes.BossScene.getBossScene;
import static scenes.BossScene.resetBossScene;
import static scenes.DeathScene.getDeathScene;
import static scenes.FightScene.getFightScene;
import static scenes.MarketScene.resetMarketScene;
import static utils.AnimationStatus.*;
import static utils.GameConstants.*;
import static utils.HelperMethods.*;
import static utils.PerkStatus.THORN_2_1_REFLECTCHANCE;
import static utils.PerkStatus.THORN_NORMAL_REFLECTCHANCE;
import static utils.PlayerStatus.*;

public class Player extends BaseEntity {
    private static Player instances;
    private BaseClass playerClass;
    private float baseAttackSpeed;
    private int addAttackSpeed;
    private int critRate;
    private int critDamage;
    private int coins;
    private String uiString;
    private String inventoryUIString;
    private int specialAttackFrames;
    private int dashFrames;
    private ArrayList<BasePerk> obtainPerks;
    private ArrayList<BasePerk> notObtainPerks;
    private ArrayList<BaseMonster> monstersInStage;
    private ArrayList<BaseObject> objectsInStage;
    private ArrayList<Integer> frameMakeDamageSpecialAttack;
    private int attackCombo;
    private float timeSinceLastAttack;
    private float timeCanSecondAttack;
    private boolean isSecondAttack;
    private boolean canInteract = false;
    private BaseObject objectThatInteractWith = null;
    private int remainingDash;
    private int maxDash;
    private float dashDistance;
    private int addDashDistance;
    private int dashTime;
    private boolean canDash = true;
    private float specialAttackCooldown;
    private boolean isSpecialAttackInCooldown = false;
    private boolean isTreasureHunterActivate = false;
    public Player() {
        this(new Anonymous(), SPAWN_X, SPAWN_Y);
    }
    public Player(BaseClass baseClass) {
        this(baseClass, SPAWN_X, SPAWN_Y);
    }
    public Player(BaseClass baseClass, double spawnX, double spawnY) {
        setStatsFromClass(baseClass);
        setFramesFromClass(baseClass);
        setCoins(START_COINS);
        setAnimation(new Animation(this, spawnX, spawnY));
        setHitBox(new HitBox(this));
        setFaceDirection(RIGHT);
        monstersInStage = new ArrayList<>();
        objectsInStage = new ArrayList<>();
        setSoundList(loadEntitiesSound(this));

        obtainPerks = new ArrayList<>();
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> {
            notObtainPerks = new ArrayList<>();
            notObtainPerks.addAll(generateAllPerks(this));
        });
    }
    private void setFramesFromClass(BaseClass baseClass) {
        setImgString(baseClass.getImgString());
        setUiString(baseClass.getUiString());
        setInventoryUIString(baseClass.getInventoryUIString());
        setStandStillFrames(baseClass.getStandStillFrames());
        setWalkFrames(baseClass.getWalkFrames());
        setNormalAttackFrames(baseClass.getNormalAttackFrames());
        setSpecialAttackFrames(baseClass.getSpecialAttackFrames());
        setDashFrames(baseClass.getDashFrames());
        setDeadFrames(baseClass.getDeadFrames());
        setAttackCombo(baseClass.getAttackCombo());
        setTimeCanSecondAttack(baseClass.getTimeCanSecondAttack());
        setFrameMakeDamageNormalAttack(baseClass.getFrameMakeDamageNormalAttack());
        setFrameMakeDamageSpecialAttack(baseClass.getFrameMakeDamageSpecialAttack());
        setKnockbackDistance(baseClass.getKnockbackDistance());
        setAnimationWidth(baseClass.getAnimationWidth());
        setAnimationHeight(baseClass.getAnimationHeight());
        setHitBoxWidth(baseClass.getHitBoxWidth());
        setHitBoxHeight(baseClass.getHitBoxHeight());
        setHitBoxXOffset(baseClass.getHitBoxXOffset());
        setHitBoxYOffset(baseClass.getHitBoxYOffset());
    }
    private void setStatsFromClass(BaseClass baseClass) {
        setPlayerClass(baseClass);
        setBaseMaxHp(baseClass.getHp());
        setCurrentHp(getBaseMaxHp());
        setAddMaxHp(0);
        setBaseAtk(baseClass.getAtk());
        setAddAtk(0);
        setBaseDef(baseClass.getDef());
        setAddDef(0);
        setBaseMovementSpeed(baseClass.getMovementSpeed());
        setAddMovementSpeed(0);
        setAddDashDistance(0);
        setEvadeRate(baseClass.getEvadeRate());
        setDamageIncrease(baseClass.getDamageIncrease());
        setDamageDecrease(baseClass.getDamageDecrease());
        setAttackRange(baseClass.getAttackRange());
        setBaseAttackSpeed(baseClass.getAttackSpeed());
        setAddAttackSpeed(0);
        setCritRate(baseClass.getCritRate());
        setCritDamage(baseClass.getCritDamage());
        setMaxDash(baseClass.getMaxDash());
        setRemainingDash(getMaxDash());
        setDashDistance(DEFAULT_DASH_DISTANCE);
        setDashTime(DEFAULT_DASH_TIME);
        setSpecialAttackCooldown(baseClass.getSpecialAttackCoolDown());
    }
    @Override
    public void setCommonStatus() {
        getPlayerClass().setCommonStatus();
    }
    @Override
    public void setUniqueStatus(double spawnX, double spawnY) {
        getPlayerClass().setUniqueStatus();
    }
    @Override
    public void move(BaseScene scene) {
        float speed = finalValuePercentFloat(getBaseMovementSpeed(), getAddMovementSpeed());
        float moveSpeed = MOVE_PER_FRAME * speed;
        borderL = Math.abs(scene.getTranslateX());
        borderR = Math.abs(scene.getTranslateX()) + gameWidth;
        double hitBoxX = getHitBox().getTranslateX();
        double hitBoxY = getHitBox().getTranslateY();
        double animationX = getAnimation().getTranslateX();
        double animationY = getAnimation().getTranslateY();
        double width = getHitBox().getFitWidth();
        double height = getHitBox().getFitHeight();

        checkDash(scene);
        if (getAnimation().isMoveLeft() && !getAnimation().isMoveRight()) {
            moveLeft(scene, animationX, hitBoxX, hitBoxY, width, height, moveSpeed);
        } else if (!getAnimation().isMoveLeft() && getAnimation().isMoveRight()) {
            moveRight(scene, animationX, hitBoxX, hitBoxY, width, height, moveSpeed);
        }
        if (!getAnimation().isJump()) {
            checkFall(scene, hitBoxX, hitBoxY, width, height);
        }
        if (getAnimation().isJump()) {
            moveBorderYAxis(scene, animationY);
            checkStateForJump();
            jump(scene, hitBoxX, hitBoxY, animationY, width, height);
        }
        if (scene instanceof ClassSelectionScene || scene instanceof MarketScene || scene instanceof BossScene) {
            actionForRestScene(scene);
        }
    }

    private void moveBorderYAxis(BaseScene scene, double animationY) {
        getSoundFromSoundList(getSoundList(), "PlayerWalk").stop();
        if (animationY < scene.getBgImgView().getFitHeight() - (double) gameHeight / 2 && animationY > (double) gameHeight / 2) {
            Platform.runLater(() -> {
                scene.setTranslateY(-getAnimation().getTranslateY() + (double) gameHeight / 2);
                scene.getPlayerUI().setTranslateY(-scene.getTranslateY() + gameHeight - healthUIHeight - uiOffset);
            });
        }
    }

    private void checkStateForJump() {
        getAnimation().setyVelocity(getAnimation().getyVelocity() + GRAVITY);
        if ((getAnimation().getState() == PLAYER_STAND_STILL ||
                getAnimation().getState() == PLAYER_WALK ||
                getAnimation().getState() == PLAYER_JUMP_UP ||
                getAnimation().getState() == PLAYER_JUMP_DOWN) &&
                (!(getPlayerClass() instanceof Anonymous))) {
            if (getAnimation().getyVelocity() > 0) {
                getAnimation().setState(States.PLAYER_JUMP_DOWN);
            } else {
                getAnimation().setState(States.PLAYER_JUMP_UP);
            }
        }
    }

    private void jump(BaseScene scene, double hitBoxX, double hitBoxY, double animationY, double width, double height) {
        Platform.runLater(() -> {
            if ((canMoveHere(this, hitBoxX, hitBoxY + getAnimation().getyVelocity(), width, height, scene.getLvlData())) &&
                    getAnimation().getyVelocity() <= 0) {
                getHitBox().setTranslateY(hitBoxY + getAnimation().getyVelocity());
                getAnimation().setTranslateY(animationY + getAnimation().getyVelocity());
            } else {
                if (getAnimation().getyVelocity() > 0) {
                    double distance = getAnimation().getyVelocity();
                    while (true) {
                        if (canMoveHere(this, getHitBox().getTranslateX(), getHitBox().getTranslateY() + 0.5, width, height, scene.getLvlData())) {
                            getHitBox().setTranslateY(getHitBox().getTranslateY() + 0.5);
                            getAnimation().setTranslateY(getAnimation().getTranslateY() + 0.5);
                            distance -= 0.5;
                        } else {
                            getAnimation().setyVelocity(0);
                            break;
                        }
                        if (distance < 0) break;
                    }
                    if (getAnimation().getyVelocity() == 0) {
                        getSoundFromSoundList(getSoundList(), "PlayerJump1").stop();
                        getSoundFromSoundList(getSoundList(), "PlayerJump2").stop();
                        getAnimation().setJump(false);
                        getAnimation().setCanJump(true);
                        if (getAnimation().getState() == States.PLAYER_JUMP_UP || getAnimation().getState() == States.PLAYER_JUMP_DOWN) {
                            getAnimation().setAnimationToDefault(this, scene);
                        }
                    }
                } else {
                    getAnimation().setyVelocity(0);
                }
            }
        });
    }

    private void checkFall(BaseScene scene, double hitBoxX, double hitBoxY, double width, double height) {
        if (getAnimation().isMoveLeft() || getAnimation().isMoveRight()) {
            getSoundFromSoundList(getSoundList(), "PlayerWalk").play();
        }
        if (canMoveHere(this, hitBoxX, hitBoxY + 0.5, width, height, scene.getLvlData())) {
            getAnimation().setJump(true);
            getAnimation().setyVelocity(0);
        }
    }

    private void moveRight(BaseScene scene, double animationX, double hitBoxX, double hitBoxY, double width, double height, double moveSpeed) {
        if (canMoveHere(this, hitBoxX + moveSpeed, hitBoxY, width, height, scene.getLvlData())) {
            Platform.runLater(() -> {
                getHitBox().setTranslateX(hitBoxX + moveSpeed);
                getAnimation().setTranslateX(animationX + moveSpeed);
            });
        }
        if (isNearBorderRight()
                && borderR < scene.getBgImgView().getFitWidth() - getHitBox().getFitWidth() / 2) {
            Platform.runLater(() -> {
                scene.setTranslateX(scene.getTranslateX() - moveSpeed);
                scene.getPlayerUI().setTranslateX(-scene.getTranslateX() + uiOffset);
            });
        }
    }

    private void moveLeft(BaseScene scene, double animationX, double hitBoxX, double hitBoxY, double width, double height, double moveSpeed) {
        if (canMoveHere(this, hitBoxX - moveSpeed, hitBoxY, width, height, scene.getLvlData())) {
            Platform.runLater(() -> {
                getHitBox().setTranslateX(hitBoxX - moveSpeed);
                getAnimation().setTranslateX(animationX - moveSpeed);
            });
        }
        if (isNearBorderLeft() && borderL - moveSpeed > 0) {
            Platform.runLater(() -> {
                scene.setTranslateX(scene.getTranslateX() + moveSpeed);
                scene.getPlayerUI().setTranslateX(-scene.getTranslateX() + uiOffset);
            });
        }
    }
  
    private void actionForRestScene(BaseScene scene) {
        if (getHitBox().getTranslateX() + getHitBoxWidth() * SCALE >=
                scene.getBgImgView().getFitWidth() - (double) gameWidth / 30
                && !(getPlayerClass() instanceof Anonymous)) {
            if (scene instanceof ClassSelectionScene) {
                actionForClassSelectionScene(scene);
            } else if (scene instanceof MarketScene){
                actionForMarketScene(scene);
            } else if (scene instanceof BossScene && scene.getPlayer().getMonstersInStage().isEmpty()){
                actionForEmptyBossScene(scene);
            }
        }
    }

    private void actionForEmptyBossScene(BaseScene scene) {
        Platform.runLater(() -> {
            scene.stopAllThreads();
            scene.getStage().getScene().setRoot(getFightScene(scene.getStage(), scene.getPlayer(), randomRoomType()));
            resetBossScene();
        });
    }

    private void actionForMarketScene(BaseScene scene) {
        scene.stopAllThreads();
        Platform.runLater(() -> {
            ArrayList<BaseObject> allObjects = new ArrayList<>(getObjectsInStage());
            for (BaseObject object : allObjects) {
                if (object instanceof BlackSmith blackSmith) {
                    blackSmith.setStop(true);
                }
            }
            scene.getStage().getScene().setRoot(getBossScene(scene.getStage(), scene.getPlayer()));
            resetMarketScene();
        });
    }

    private void actionForClassSelectionScene(BaseScene scene) {
        scene.stopAllThreads();
        Platform.runLater(() -> {
            ArrayList<BaseObject> allObjects = new ArrayList<>(getObjectsInStage());
            for (BaseObject object : allObjects) {
                if (object instanceof VendingMachine vendingMachine) {
                    vendingMachine.setStop(true);
                    break;
                }
            }
            scene.getStage().getScene().setRoot(
                    getFightScene(scene.getStage(), scene.getPlayer(),
                            randomRoomType()));
        });
    }

    public boolean canWarpToNearestMonster() {
        ArrayList<BaseMonster> allMonsters = new ArrayList<>(getMonstersInStage());
        for (BaseMonster eachMonster : allMonsters) {
            if (getMonstersInStage().contains(eachMonster)) {
                if (eachMonster.getAnimation().getState() != MONSTER_DEAD && eachMonster.getAnimation().getState() != MONSTER_SPAWN) {
                    if (Math.abs((getHitBox().getTranslateX() + getHitBox().getFitWidth() / 2) - (eachMonster.getHitBox().getTranslateX() + getHitBox().getFitWidth() / 2
                    )) <= DAGGER_SPECIAL_ATTACK_WARP_DISTANCE * PIXEL_PER_TILE) {
                        if (Math.abs((getHitBox().getTranslateY() + getHitBox().getFitHeight() / 2) - (eachMonster.getHitBox().getTranslateY() + getHitBox().getFitHeight() / 2
                        )) <= DAGGER_SPECIAL_ATTACK_WARP_DISTANCE * PIXEL_PER_TILE) return true;
                    }
                }
            }
        }
        return false;
    }
    public void warpToNearestMonster(BaseScene scene) {
        double distance = -1;
        double xDistance = 0;
        double yDistance = 0;
        ArrayList<BaseMonster> allMonsters = new ArrayList<>(getMonstersInStage());
        for (BaseMonster eachMonster : allMonsters) {
            if (getMonstersInStage().contains(eachMonster)) {
                if (eachMonster.getAnimation().getState() != MONSTER_DEAD) {
                    double deltaX = (eachMonster.getHitBox().getTranslateX() + getHitBox().getFitWidth() / 2) - (getHitBox().getTranslateX() + getHitBox().getFitWidth() / 2);
                    double deltaY = (eachMonster.getHitBox().getTranslateY() + getHitBox().getFitHeight() / 2) - (getHitBox().getTranslateY() + getHitBox().getFitHeight() / 2);
                    double finalDistance = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
                    if (distance == -1 || finalDistance < distance) {
                        distance = finalDistance;
                        xDistance = deltaX;
                        yDistance = deltaY;
                    }
                }
            }
        }
        moveToMonster(scene, xDistance, yDistance);
    }

    private void moveToMonster(BaseScene scene, double xDistance, double yDistance) {
        while (true) {
            if (canMoveHere(this, getHitBox().getTranslateX() + xDistance, getHitBox().getTranslateY() + yDistance,
                    getHitBox().getFitWidth(), getHitBox().getFitHeight(), scene.getLvlData())) {
                getAnimation().setTranslateX(getAnimation().getTranslateX() + xDistance);
                getHitBox().setTranslateX(getHitBox().getTranslateX() + xDistance);
                getAnimation().setTranslateY(getAnimation().getTranslateY() + yDistance);
                getHitBox().setTranslateY(getHitBox().getTranslateY() + yDistance);
                scene.setScenePosition();
                scene.getPlayerUI().setTranslateX(-scene.getTranslateX() + uiOffset);
                scene.getPlayerUI().setTranslateY(-scene.getTranslateY() + gameHeight - healthUIHeight -uiOffset);
                break;
            } else {
                yDistance -= 1;
            }
        }
    }
    @Override
    public boolean canAttack(BaseEntity baseEntity) {
        BaseMonster baseMonster = (BaseMonster) baseEntity;
        if (baseMonster.getAnimation().getState() == MONSTER_DEAD || baseMonster.getAnimation().getState() == MONSTER_SPAWN) {
            return false;
        }
        if (Math.abs((getHitBox().getTranslateY() + getHitBox().getFitHeight()) - (baseMonster.getHitBox().getTranslateY() + baseMonster.getHitBox().getFitHeight())) > PIXEL_PER_TILE) {
            return false;
        }
        if (getFaceDirection() == RIGHT) {
            if (getHitBox().getTranslateX() <= baseMonster.getHitBox().getTranslateX() + baseMonster.getHitBox().getFitWidth()) {
                float distance = (float) ((baseMonster.getHitBox().getTranslateX() - getHitBox().getTranslateX() - getHitBox().getFitWidth()) / PIXEL_PER_TILE);
                return distance <= getAttackRange();
            }
        } else {
            if (getHitBox().getTranslateX() + getHitBox().getFitWidth() >= baseMonster.getHitBox().getTranslateX()) {
                float distance = (float) ((getHitBox().getTranslateX() - baseMonster.getHitBox().getTranslateX() - baseMonster.getHitBox().getFitWidth()) / PIXEL_PER_TILE);
                return distance <= getAttackRange();
            }
        }
        return false;
    }
    @Override
    public void attack(Player player, BaseScene scene, int extraDamagePercent) {
        ArrayList<BaseMonster> allMonsters = new ArrayList<>(getMonstersInStage());
        for (BaseMonster eachMonster : allMonsters) {
            if (getMonstersInStage().contains(eachMonster)) {
                if (canAttack(eachMonster)) {
                    getSoundFromSoundList(getSoundList(), "PlayerWalk").stop();
                    eachMonster.isAttacked(this, scene, extraDamagePercent);
                }
            }
        }
    }
    @Override
    public void isAttacked(BaseEntity baseEntity, BaseScene scene, int extraDamagePercent){
        BaseMonster baseMonster = (BaseMonster) baseEntity;
        boolean canEvade = randomChance() * 100 < getEvadeRate() ? true : false;
        if (canEvade) {
            getSoundFromSoundList(getSoundList(), "PlayerDodge").play();
            addAndDeleteText(this, scene,
                    createText("Miss!", 50, BLACK));
        } else {
            immuneAfterAttacked(this);

            scene.blink(getAnimation());
            setDamageReceived(calculateActionAttack(baseMonster, this, false, extraDamagePercent));

            if (getDamageReceived() >= getCurrentHp()
                    && hasPerk(this, new UndeadPerk(this, TIER1), false)
                    && !isUndeadInCooldown()) {
                for (BasePerk perk: getObtainPerks()) {
                    if (perk instanceof UndeadPerk undeadPerk) {
                        undeadPerk.activateUndead(this);
                    }
                }
            } else {
                setCurrentHp(getCurrentHp() - getDamageReceived());
            }

            float chance = randomChance();
            if (hasPerk(this, new ThornPerk(this, TIER2_1), true) && chance < THORN_2_1_REFLECTCHANCE) {
                reflect(baseMonster, scene);
            } else if (hasPerk(this, new ThornPerk(this, TIER2_1), false) && chance < THORN_NORMAL_REFLECTCHANCE) {
                reflect(baseMonster, scene);
            }

            if (getCurrentHp() == 0) {
                if (!hasPerk(this, new UndeadPerk(this, TIER1), false)) {
                    dead(baseMonster, scene);
                } else {
                    if (isUndeadInCooldown()) {
                        dead(baseMonster, scene);
                    } else {
                        hurt(baseMonster, scene);
                    }
                }
            } else {
                hurt(baseMonster, scene);
            }
        }
    }

    @Override
    public void hurt(BaseEntity attacker, BaseScene scene) {
        BaseMonster baseMonster = (BaseMonster) attacker;
        getSoundFromSoundList(getSoundList(), "PlayerHurt").play();
        if (getHitBox().getTranslateX() < baseMonster.getHitBox().getTranslateX()) {
            knockBack(this, LEFT, scene);
        } else {
            knockBack(this, RIGHT, scene);
        }
    }
    @Override
    public void dead(BaseEntity attacker, BaseScene scene) {
        getSoundFromSoundList(getSoundList(), "PlayerDead").play();
        getAnimation().setState(PLAYER_DEAD);
        scene.stopAllThreads();
        stopAllBackgroundSongs();
        scene.getStage().getScene().setRoot(getDeathScene(scene.getStage()));
    }

    private void reflect(BaseMonster baseMonster, BaseScene scene) {
        int damage = getDamageReceived();
        int damageReflect = calculateReflectionAttack(damage, baseMonster);
        baseMonster.setCurrentHp(baseMonster.getCurrentHp() - damageReflect);
        baseMonster.setCurrentHp(baseMonster.getCurrentHp() - getDamageReceived());
        if (hasPerk(this, new ThornPerk(instances, TIER2_2), true)) {
            ThornPerk.slow(baseMonster);
        }
        if (baseMonster.getCurrentHp() == 0) {
            baseMonster.dead(this, scene);
        } else {
            baseMonster.hurt(this, scene);
        }
        addAndDeleteText(baseMonster, scene,
                createText(String.valueOf(damageReflect), 50, PURPLE));
    }
    private boolean isNearBorderLeft() {
        return getHitBox().getTranslateX() - borderL < 0.5 * gameWidth - getHitBox().getFitWidth()/2;
    }
    private boolean isNearBorderRight() {
        return getHitBox().getTranslateX() - borderL > 0.5 * gameWidth - getHitBox().getFitWidth()/2;
    }
    private void checkDash(BaseScene scene){
        if (isDash()) {
            dash(PIXEL_PER_TILE * finalValuePercentFloat(getDashDistance(),
                    getAddDashDistance()), getDashTime(), getFaceDirection(), scene);
        }
    }
    public void dash(double distance, int times, FaceDirection faceDirection, BaseScene scene) {
        if (getDashFrameCount() < times) {
            setDashFrameCount(getDashFrameCount() + 1);
            if (faceDirection == RIGHT) {
                Platform.runLater(() -> {
                    if (canMoveHere(this, getHitBox().getTranslateX() + distance / times, getHitBox().getTranslateY(),
                            getHitBox().getFitWidth(), getHitBox().getFitHeight(), scene.getLvlData())) {
                        getHitBox().setTranslateX(getHitBox().getTranslateX() + distance / times);
                        getAnimation().setTranslateX(getAnimation().getTranslateX() + distance / times);
                        if (isNearBorderRight()
                                && borderR < scene.getBgImgView().getFitWidth() - getHitBox().getFitWidth()/2) {
                            scene.setTranslateX(scene.getTranslateX() - distance / times);
                            scene.getPlayerUI().setTranslateX(-scene.getTranslateX() + uiOffset);
                        }
                    }
                });
            } else {
                Platform.runLater(() -> {
                    if (canMoveHere(this, getHitBox().getTranslateX() - distance / times, getHitBox().getTranslateY(),
                            getHitBox().getFitWidth(), getHitBox().getFitHeight(), scene.getLvlData())) {
                        getHitBox().setTranslateX(getHitBox().getTranslateX() - distance / times);
                        getAnimation().setTranslateX(getAnimation().getTranslateX() - distance / times);
                        if (isNearBorderLeft() && borderL - distance / times > 0) {
                            scene.setTranslateX(scene.getTranslateX() + distance / times);
                            scene.getPlayerUI().setTranslateX(-scene.getTranslateX() + uiOffset);
                        }
                    }
                });
            }
        } else {
            setDashFrameCount(0);
            if (getAnimation().getState() == PLAYER_DASH) {
                setDash(false);
                setRemainingDash(getRemainingDash() - 1);
                getAnimation().setAnimationToDefault(this, scene);
                getAnimation().setAnimationPlaying(false);
                AnimationTimer timer = new AnimationTimer() {
                    long startTime = System.nanoTime();
                    @Override
                    public void handle(long now) {
                        long elapsedTime = now - startTime;
                        double secondsElapsed = elapsedTime / 1_000_000_000.0;
                        if (secondsElapsed >= 0.1 && remainingDash > 0) {
                            setCanDash(true);
                            stop();
                        }
                    }
                };
                timer.start();
            } else {
                setDash(false);
                setDashDistance(DEFAULT_DASH_DISTANCE);
                setDashTime(DEFAULT_DASH_TIME);
            }
        }
    }

    public static void resetPlayer() {
        instances = null;
    }

    public static Player getPlayer() {
        if (instances == null) instances = new Player();
        return instances;
    }

    public ArrayList<BasePerk> getObtainPerks() {
        return obtainPerks;
    }

    public ArrayList<BaseMonster> getMonstersInStage() {
        return monstersInStage;
    }

    public ArrayList<BaseObject> getObjectsInStage() {
        return objectsInStage;
    }

    public float getBaseAttackSpeed() {
        return baseAttackSpeed;
    }

    public void setBaseAttackSpeed(float baseAttackSpeed) {
        if (baseAttackSpeed <= 1) baseAttackSpeed = 1;
        this.baseAttackSpeed = baseAttackSpeed;
    }

    public int getCritRate() {
        return critRate;
    }

    public void setCritRate(int critRate) {
        if (critRate <= 0) critRate = 0;
        this.critRate = critRate;
    }

    public int getCritDamage() {
        return critDamage;
    }

    public void setCritDamage(int critDamage) {
        if (critDamage <= 0) critDamage = 0;
        this.critDamage = critDamage;
    }

    public BaseClass getPlayerClass() {
        return playerClass;
    }

    public void setPlayerClass(BaseClass playerClass) {
        this.playerClass = playerClass;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        if (coins <= 0) coins = 0;
        this.coins = coins;
    }

    public int getAddAttackSpeed() {
        return addAttackSpeed;
    }

    public void setAddAttackSpeed(int addAttackSpeed) {
        this.addAttackSpeed = addAttackSpeed;
    }

    public ArrayList<BasePerk> getNotObtainPerks() {
        return notObtainPerks;
    }

    public int getSpecialAttackFrames() {
        return specialAttackFrames;
    }

    public void setSpecialAttackFrames(int specialAttackFrames) {
        this.specialAttackFrames = specialAttackFrames;
    }

    public int getAttackCombo() {
        return attackCombo;
    }

    public void setAttackCombo(int attackCombo) {
        this.attackCombo = attackCombo;
    }

    public float getTimeSinceLastAttack() {
        return timeSinceLastAttack;
    }

    public void setTimeSinceLastAttack(float timeSinceLastAttack) {
        this.timeSinceLastAttack = timeSinceLastAttack;
    }

    public float getTimeCanSecondAttack() {
        return timeCanSecondAttack;
    }

    public void setTimeCanSecondAttack(float timeCanSecondAttack) {
        this.timeCanSecondAttack = timeCanSecondAttack;
    }

    public boolean isSecondAttack() {
        return isSecondAttack;
    }

    public void setSecondAttack(boolean isSecondAttack) {
        this.isSecondAttack = isSecondAttack;
    }

    public String getUiString() {
        return uiString;
    }

    public void setUiString(String uiString) {
        this.uiString = uiString;
    }

    public String getInventoryUIString() {
        return inventoryUIString;
    }

    public void setInventoryUIString(String inventoryUIString) {
        this.inventoryUIString = inventoryUIString;
    }

    public boolean canInteract() {
        return canInteract;
    }

    public void setCanInteract(boolean canInteract) {
        this.canInteract = canInteract;
    }

    public BaseObject getObjectThatInteractWith() {
        return objectThatInteractWith;
    }

    public void setObjectThatInteractWith(BaseObject objectThatInteractWith) {
        this.objectThatInteractWith = objectThatInteractWith;
    }

    public int getRemainingDash() {
        return remainingDash;
    }

    public void setRemainingDash(int remainingDash) {
        this.remainingDash = remainingDash;
    }

    public int getMaxDash() {
        return maxDash;
    }

    public void setMaxDash(int maxDash) {
        this.maxDash = maxDash;
    }

    public float getDashDistance() {
        return dashDistance;
    }

    public void setDashDistance(float dashDistance) {
        this.dashDistance = dashDistance;
    }

    public boolean canDash() {
        return canDash;
    }

    public void setCanDash(boolean canDash) {
        this.canDash = canDash;
    }

    public int getDashFrames() {
        return dashFrames;
    }

    public void setDashFrames(int dashFrames) {
        this.dashFrames = dashFrames;
    }

    public ArrayList<Integer> getFrameMakeDamageSpecialAttack() {
        return frameMakeDamageSpecialAttack;
    }

    public void setFrameMakeDamageSpecialAttack(ArrayList<Integer> frameMakeDamageSpecialAttack) {
        this.frameMakeDamageSpecialAttack = frameMakeDamageSpecialAttack;
    }

    public int getDashTime() {
        return dashTime;
    }

    public void setDashTime(int dashTime) {
        this.dashTime = dashTime;
    }

    public float getSpecialAttackCooldown() {
        return specialAttackCooldown;
    }

    public void setSpecialAttackCooldown(float specialAttackCooldown) {
        this.specialAttackCooldown = specialAttackCooldown;
    }

    public boolean isSpecialAttackInCooldown() {
        return isSpecialAttackInCooldown;
    }

    public void setSpecialAttackInCooldown(boolean specialAttackInCooldown) {
        isSpecialAttackInCooldown = specialAttackInCooldown;
    }

    public boolean canGetDoubleReward() {
        return isTreasureHunterActivate;
    }

    public void setTreasureHunterActivate(boolean treasureHunterActivate) {
        this.isTreasureHunterActivate = treasureHunterActivate;
    }

    public int getAddDashDistance() {
        return addDashDistance;
    }

    public void setAddDashDistance(int addDashDistance) {
        this.addDashDistance = addDashDistance;
    }
}
