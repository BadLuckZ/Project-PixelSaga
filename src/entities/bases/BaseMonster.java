// BaseClass for all monsters in the game (BaseMonster, BaseBoss)

package entities.bases;

import entities.monsters.BigBoy;
import entities.monsters.KleeGiGee;
import entities.monsters.PeasantGiGee;
import entities.monsters.ThrowerGiGee;
import entities.player.Player;
import entities.player.perks.VampirismPerk;
import enums.FaceDirection;
import enums.States;
import javafx.application.Platform;
import objects.BaseObject;
import objects.Fire;
import scenes.BaseScene;

import java.util.ArrayList;
import java.util.Arrays;

import static entities.player.perks.VampirismPerk.activateVampirism;
import static enums.FaceDirection.*;
import static enums.States.*;
import static enums.Tier.TIER1;
import static javafx.scene.paint.Color.*;
import static utils.AnimationStatus.*;
import static utils.GameConstants.airTile;
import static utils.GameConstants.passableTile;
import static utils.HelperMethods.*;
import static utils.MonsterStatus.*;
import static utils.PerkStatus.TREASURE_2_2_REWARDMULTIPLIER;

public abstract class BaseMonster extends BaseEntity {
    private float normalAttackDelay;
    private float addAttackDelay;
    private float dropRate;
    private int coinDrop;
    private float visionRange;
    private float walkTime;
    private float time;
    private float timeSinceLastAttack = 10;
    private float standTime;
    private float turningTime;
    private boolean isWalk;
    private boolean canSee = false, canAttack = false, isSpawn = false;
    private FaceDirection dashDirection = null;
    private boolean moreCoinDrop = false;
    private int knockbackChance;
    private boolean isSlowdownByThornPerk = false;
    private static final ArrayList<String> CLOSE_RANGE_MONSTERS = new ArrayList<>(Arrays.asList("KleeGiGee", "PeasantGiGee"));
    private static final ArrayList<States> ATTACK_STATES = new ArrayList<>(Arrays.asList(MONSTER_NORMAL_ATTACK, BOSS_SKILL_ONE, BOSS_SKILL_TWO, BOSS_SKILL_THREE_STAND_STILL));

    public BaseMonster() {
        setBaseStatus();
    }

    public void setBaseStatus() {
        setAddMaxHp(0);
        setAddAtk(0);
        setAddDef(0);
        setAddMovementSpeed(0);
        setDamageIncrease(0);
        setDamageDecrease(0);
        setImgString("monsters/" + getClass().getSimpleName() + "_Sprite.png");
        setFrameMakeDamageNormalAttack(new ArrayList<>());
        setDashFrame(new ArrayList<>());
        setDashFrameCount(0);
        setSoundList(loadEntitiesSound(this));
        setFaceDirection(randomStartDirection());
        setStandTime(getNewRandomTime());
        setTurningTime(TURNING_DELAY);
        setAnimationWidth(DEFAULT_ANIMATION_WIDTH);
        setAnimationHeight(DEFAULT_ANIMATION_HEIGHT);
        setHitBoxWidth(DEFAULT_HITBOX_WIDTH);
        setHitBoxHeight(DEFAULT_HITBOX_HEIGHT);
        setHitBoxXOffset(DEFAULT_HITBOX_X_OFFSET);
        setHitBoxYOffset(DEFAULT_HITBOX_Y_OFFSET);
    }

    @Override
    public void isAttacked(BaseEntity baseEntity, BaseScene scene, int extraDamagePercent) {
        Player player = (Player) baseEntity;
        boolean canEvade = randomChance() * 100 < getEvadeRate() ? true : false;
        if (canEvade) {
            getSoundFromSoundList(getSoundList(), "MonsterDodge").play();
            addAndDeleteText(this, scene,
                    createText("Miss!", 50, BLACK));
        } else {
            if (hasPerk(player, new VampirismPerk(player, TIER1), false)){
                activateVampirism();
            }
            if (getAnimation().getState() != MONSTER_DEAD && getAnimation().getState() != MONSTER_SPAWN) {
                immuneAfterAttacked(this);
                scene.blink(getAnimation());

                boolean isCrit = randomChance() * 100 < player.getCritRate() ? true : false;
                setDamageReceived(calculateActionAttack(player, this, isCrit, extraDamagePercent));
                setCurrentHp(getCurrentHp() - getDamageReceived());

                if (!isCrit) {
                    addAndDeleteText(this, scene,
                            createText(String.valueOf(getDamageReceived()), 50, RED));
                } else {
                    addAndDeleteText(this, scene,
                            createText(String.valueOf(getDamageReceived()), 70, WHITE));
                }
                if (getCurrentHp() == 0) {
                    dead(player, scene);
                } else {
                    hurt(player, scene);
                }
            }
        }
    }

    @Override
    public void hurt(BaseEntity attacker, BaseScene scene) {
        Player player = (Player) attacker;
        getSoundFromSoundList(getSoundList(), "MonsterHurt").play();
        if (randomChance() < (float) getKnockbackChance() / 100) {
            if (getHitBox().getTranslateX() < player.getHitBox().getTranslateX()) {
                knockBack(this,LEFT, scene);
            } else {
                knockBack(this, RIGHT, scene);
            }
        }
    }
    @Override
    public void dead(BaseEntity attacker, BaseScene scene) {
        Player player = (Player) attacker;
        getSoundFromSoundList(getSoundList(), "MonsterDead").play();
        int coinGet = randomIntegerUsingPercent(
                getCoinDrop(), MONSTER_DROP_VARIATION);
        if (moreCoinDrop) {
            coinGet *= TREASURE_2_2_REWARDMULTIPLIER;
        }
        player.setCoins(player.getCoins() + coinGet);
        getAnimation().setState(MONSTER_DEAD);
        if (this instanceof BigBoy bigBoy) {
            bigBoy.activateNuclear(scene);
        }
        if (DASH_AND_JUMP_WHEN_DEATH_MONSTER.contains(getClass().getSimpleName())) {
            getAnimation().setJump(true);
            getAnimation().setyVelocity(-18 * SCALE);
        }
    }

    @Override
    public void move(BaseScene scene) {
        float speed = finalValuePercentFloat(getBaseMovementSpeed(), getAddMovementSpeed());
        float moveSpeed = MOVE_PER_FRAME * speed;
        double hitBoxX = getHitBox().getTranslateX();
        double hitBoxY = getHitBox().getTranslateY();
        double animationX = getAnimation().getTranslateX();
        double animationY = getAnimation().getTranslateY();
        double width = getHitBox().getFitWidth();
        double height = getHitBox().getFitHeight();
        if (!(this.getAnimation().getState() == MONSTER_DEAD)) {
            checkDash(scene);
            if (!ATTACK_STATES.contains(getAnimation().getState())) {
                if (canSee) {
                    prepareToAttack(scene, moveSpeed);
                } else if (isWalk) {
                    checkDirection(scene, hitBoxX, hitBoxY, animationX,
                            width, height, moveSpeed);
                }
            }
        } else {
            if (DASH_AND_JUMP_WHEN_DEATH_MONSTER.contains(getClass().getSimpleName())) {
                dash(PIXEL_PER_TILE * 4, 75, scene, false, scene.getPlayer());
            }
        }
        if (!getAnimation().isJump()) {
            checkFall(scene, hitBoxX, hitBoxY, width, height);
        }
        if (getAnimation().isJump()) {
            jump(scene, hitBoxX, hitBoxY, animationY, width, height);
        }
    }

    private void checkFall(BaseScene scene, double hitBoxX,
                           double hitBoxY, double width, double height) {
        if (canMoveHere(this, hitBoxX, hitBoxY + 0.5, width, height, scene.getLvlData())) {
            getAnimation().setJump(true);
            getAnimation().setyVelocity(0);
        }
    }

    private void jump(BaseScene scene, double hitBoxX, double hitBoxY, double animationY, double width, double height) {
        getAnimation().setyVelocity(getAnimation().getyVelocity() + GRAVITY);
        Platform.runLater(() -> {
            if ((canMoveHere(this, hitBoxX, hitBoxY + getAnimation().getyVelocity(), width, height, scene.getLvlData())) &&
                    getAnimation().getyVelocity() <= 0){
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
                        getAnimation().setJump(false);
                    }
                } else {
                    getAnimation().setJump(false);
                }
            }
        });
    }

    private void checkDirection(BaseScene scene, double hitBoxX,
                                double hitBoxY, double animationX,
                                double width, double height, float moveSpeed) {
        if (getFaceDirection() == LEFT) {
            checkWalk(LEFT, scene, hitBoxX, hitBoxY, animationX, width, height, moveSpeed);
        } else {
            checkWalk(RIGHT, scene, hitBoxX, hitBoxY, animationX, width, height, moveSpeed);
        }
    }

    private void checkWalk(FaceDirection faceDirection, BaseScene scene, double hitBoxX,
                           double hitBoxY, double animationX,
                           double width, double height, float moveSpeed) {
        if (faceDirection == LEFT) {
            moveSpeed = moveSpeed * -1;
        }
        if (canWalk(faceDirection, hitBoxX, moveSpeed, hitBoxY, width, height, scene.getLvlData())) {
            walk(hitBoxX, animationX, moveSpeed, scene);
        } else {
            setTime(getWalkTime());
        }
    }

    private boolean canWalk(FaceDirection faceDirection, double hitBoxX, double speed, double hitBoxY, double width, double height, int[][] lvlData) {
        double value = faceDirection == RIGHT ? PIXEL_PER_TILE / 2.0 : -PIXEL_PER_TILE / 2.0;
        return canMoveHere(this, hitBoxX + speed, hitBoxY, width, height, lvlData) &&
                !canMoveHere(this, hitBoxX + value, hitBoxY + 1, width, height, lvlData);
    }

    private void walk(double hitBoxX, double animationX, float speed, BaseScene scene) {
        Platform.runLater(() -> {
            getHitBox().setTranslateX(hitBoxX + speed);
            getAnimation().setTranslateX(animationX + speed);
            if (this instanceof BigBoy) {
                ArrayList<BaseObject> allObjects = new ArrayList<>(scene.getPlayer().getObjectsInStage());
                for (BaseObject fire : allObjects) {
                    if (fire instanceof Fire) {
                        fire.getHitBox().setTranslateX(fire.getHitBox().getTranslateX() + speed);
                        fire.getAnimation().setTranslateX(fire.getAnimation().getTranslateX() + speed);
                        break;
                    }
                }
            }
        });
    }

    @Override
    public boolean canAttack(BaseEntity baseEntity) {
        Player player = (Player) baseEntity;
        if (Math.abs(getHitBox().getTranslateY() - player.getHitBox().getTranslateY()) > PIXEL_PER_TILE) {
            return false;
        }
        if (getFaceDirection() == RIGHT) {
            if (getHitBox().getTranslateX() - 0.3 * PIXEL_PER_TILE <= player.getHitBox().getTranslateX()) {
                float distance = (float) ((player.getHitBox().getTranslateX() - getHitBox().getTranslateX() - getHitBox().getFitWidth()) / PIXEL_PER_TILE);
                return distance <= getAttackRange();
            }
        } else {
            if (getHitBox().getTranslateX() + 0.3 * PIXEL_PER_TILE >= player.getHitBox().getTranslateX()) {
                float distance = (float) ((getHitBox().getTranslateX() - player.getHitBox().getTranslateX() - getHitBox().getFitWidth()) / PIXEL_PER_TILE);
                return distance <= getAttackRange();
            }
        }
        return false;
    }

    private void prepareToAttack(BaseScene scene, float moveSpeed) {
        if (this instanceof BaseBoss && !ATTACK_STATES.contains(getAnimation().getState())) {
            if (Math.abs(getDeltaTilesX(scene.getPlayer())) > 0.1) {
                checkMoveToPlayer(scene.getPlayer(), moveSpeed, scene);
            } else {
                getAnimation().setState(MONSTER_STAND_STILL);
            }
        }
        if (!canAttack && !(this instanceof BaseBoss)) {
            if (this instanceof KleeGiGee) {
                checkMoveToPlayer(scene.getPlayer(), moveSpeed, scene);
            } else if (!isPlayerInAttackRange(scene.getPlayer())) {
                checkMoveToPlayer(scene.getPlayer(), moveSpeed, scene);
            } else {
                getAnimation().setState(MONSTER_STAND_STILL);
            }
        } else {
            if (isPlayerAtRight(scene.getPlayer())) {
                Platform.runLater(() -> {
                    getAnimation().setScaleX(1);
                    setFaceDirection(RIGHT);
                });
            } else if (isPlayerAtLeft(scene.getPlayer())){
                Platform.runLater(() -> {
                    getAnimation().setScaleX(-1);
                    setFaceDirection(LEFT);
                });
            }
        }
    }
    @Override
    public void attack(Player player, BaseScene scene, int extraDamagePercent) {
        if (canAttack(player)){
            if (player.canTakeDamage()) player.isAttacked(this, scene, extraDamagePercent);
        }
    }
    
    private void checkDash(BaseScene scene) {
        if (isDash()){
            if (this instanceof BigBoy) {
                dash(((BigBoy) this).getJumpDistance(), (int) (BIGBOY_SKILL_TWO_JUMP_TIME * MOVE_FRAME), scene, true, scene.getPlayer());
            }
            if (!isPlayerInAttackRange(scene.getPlayer()) || getDashFrameCount() >= 1) {
                if (this instanceof PeasantGiGee) {
                    dash(PIXEL_PER_TILE * 0.8f, 6, scene, true, scene.getPlayer());
                }
            } else {
                setDash(false);
            }
        }
    }

    private void dash(double distance, int times, BaseScene scene, boolean toPlayer, Player player){
        if (getDashFrameCount() < times){
            setDashFrameCount(getDashFrameCount() + 1);
            if (toPlayer) {
                if (getDashFrameCount() == 1) setDashDirection(getFaceDirection());
                if (getDashDirection() == RIGHT) {
                    if (canMoveHere(this, getHitBox().getTranslateX() + distance / times, getHitBox().getTranslateY(),
                            getHitBox().getFitWidth(), getHitBox().getFitHeight(), scene.getLvlData())) {
                        Platform.runLater(() -> {
                            getHitBox().setTranslateX(getHitBox().getTranslateX() + distance / times);
                            getAnimation().setTranslateX(getAnimation().getTranslateX() + distance / times);
                        });
                    }
                } else {
                    if (canMoveHere(this, getHitBox().getTranslateX() - distance / times, getHitBox().getTranslateY(),
                            getHitBox().getFitWidth(), getHitBox().getFitHeight(), scene.getLvlData())) {
                        Platform.runLater(() -> {
                            getHitBox().setTranslateX(getHitBox().getTranslateX() - distance / times);
                            getAnimation().setTranslateX(getAnimation().getTranslateX() - distance / times);
                        });
                    }
                }
            } else {
                if (getDashFrameCount() == 1)
                    setDashDirection(isPlayerAtRight(player) ? LEFT : RIGHT);
                if (getDashDirection() == LEFT) {
                    if (canMoveHere(this ,getHitBox().getTranslateX() - distance / times, getHitBox().getTranslateY(),
                            getHitBox().getFitWidth(), getHitBox().getFitHeight(), scene.getLvlData())) {
                        Platform.runLater(() -> {
                            getHitBox().setTranslateX(getHitBox().getTranslateX() - distance / times);
                            getAnimation().setTranslateX(getAnimation().getTranslateX() - distance / times);
                        });
                    }
                } else {
                    if (canMoveHere(this, getHitBox().getTranslateX() + distance / times, getHitBox().getTranslateY(),
                            getHitBox().getFitWidth(), getHitBox().getFitHeight(), scene.getLvlData())) {
                        Platform.runLater(() -> {
                            getHitBox().setTranslateX(getHitBox().getTranslateX() + distance / times);
                            getAnimation().setTranslateX(getAnimation().getTranslateX() + distance / times);
                        });
                    }
                }
            }
        } else {
            setDashDirection(null);
            setDashFrameCount(0);
            setDash(false);
        }
    }

    public boolean isPlayerInVisionRange(BaseScene scene) {
        if (this instanceof BaseBoss) return true;
        if (this instanceof ThrowerGiGee) {
            if (Math.abs(getDeltaTilesX(scene.getPlayer())) < getVisionRange() &&
                    Math.abs(getDeltaTilesY(scene.getPlayer())) < getVisionRange() * 0.75 + 0.01) {
                return checkAttack(scene);
            }
        }
        return Math.abs(getDeltaTilesX(scene.getPlayer())) < getVisionRange()
                && Math.abs(getDeltaTilesY(scene.getPlayer())) < 1 &&
                checkAttack(scene) && !(this.getAnimation().getState() == MONSTER_DEAD);
    }

    private boolean checkAttack(BaseScene scene) {
        double getX = getHitBox().getTranslateX();
        double getY = getHitBox().getTranslateY();
        double getPlayerX = scene.getPlayer().getHitBox().getTranslateX();
        if (this instanceof ThrowerGiGee) {
            return canThrowerGiGeeAttack(scene, getX, getY);
        } else {
            if (getPlayerX < getX) {
                return canOtherAttack(RIGHT, getX, getPlayerX,  getY, scene);
            } else {
                return canOtherAttack(LEFT, getX, getPlayerX, getY, scene);
            }
        }
    }

    private boolean canOtherAttack(FaceDirection direction, double getX, double getPlayerX, double Y, BaseScene scene) {
        double lowerX = getX, higherX = getPlayerX;
        int multiply = 1;
        if (direction == RIGHT) {
            lowerX = getPlayerX; higherX = getX; multiply *= -1;
        }
        while (lowerX < higherX) {
            if (!canMoveHere(this, getX, Y,
                    getHitBox().getFitWidth(), getHitBox().getFitHeight(), scene.getLvlData())) {
                return false;
            }
            if (CLOSE_RANGE_MONSTERS.contains(getClass().getSimpleName())) {
                if (canMoveHere(this, getX, Y + 1,
                        getHitBox().getFitWidth(), getHitBox().getFitHeight(), scene.getLvlData())) {
                    return false;
                }
            }
            getX += multiply * PIXEL_PER_TILE / 2;
            if (direction == RIGHT) higherX = getX;
            else lowerX = getX;
        }
        return true;
    }

    private boolean canThrowerGiGeeAttack(BaseScene scene, double getX, double getY) {
        double getMidX = getX + getHitBox().getFitWidth()/2;
        double getMidY = getY + getHitBox().getFitHeight()/2;
        int xTile = (int) (getDeltaTilesX(scene.getPlayer()));
        int yTile = (int) (getDeltaTilesY(scene.getPlayer()));

        int xDirection = xTile >= 0 ? 1 : -1;
        int yDirection = yTile >= 0 ? 1 : -1;
        boolean isCheck = true; boolean hasWay = true;

        for (int i = 0; i <= Math.abs(xTile); i++) {
            int adjustedI = i * xDirection;
            int tileType = getTypeOfTile(getMidX + (adjustedI * PIXEL_PER_TILE), getMidY, scene.getLvlData());
            if (!passableTile.contains(tileType) && !airTile.contains(tileType)) {
                isCheck = false;
                break;
            }
        }
        if (isCheck) {
            for (int j = 0; j <= Math.abs(yTile); j++) {
                int adjustedJ = j * yDirection;
                int tileType = getTypeOfTile(getMidX + (xTile * PIXEL_PER_TILE), getMidY + (adjustedJ * PIXEL_PER_TILE), scene.getLvlData());
                if (!passableTile.contains(tileType) && !airTile.contains(tileType)) {
                    hasWay = false;
                    break;
                }
            }
            if (hasWay) return true;
        }

        isCheck = true; hasWay = true;
        for (int j = 0; j <= Math.abs(yTile); j++) {
            int adjustedJ = j * yDirection;
            int tileType = getTypeOfTile(getMidX, getMidY + (adjustedJ * PIXEL_PER_TILE), scene.getLvlData());
            if (!passableTile.contains(tileType) && !airTile.contains(tileType)) {
                isCheck = false;
                break;
            }
        }
        if (isCheck) {
            for (int i = 0; i <= Math.abs(xTile); i++) {
                int adjustedI = i * xDirection;
                int tileType = getTypeOfTile(getMidX + (adjustedI * PIXEL_PER_TILE), getMidY + (yTile * PIXEL_PER_TILE), scene.getLvlData());
                if (!passableTile.contains(tileType) && !airTile.contains(tileType)) {
                    hasWay = false;
                    break;
                }
            }
            if (hasWay) return true;
        }
        return false;
    }

    private void checkMoveToPlayer(Player player, float speed, BaseScene scene) {
        double hitBoxX = getHitBox().getTranslateX();
        double hitBoxY = getHitBox().getTranslateY();
        double animationX = getAnimation().getTranslateX();
        double width = getHitBox().getFitWidth();
        double height = getHitBox().getFitHeight();

        if (isPlayerAtRight(player)) {
            moveToPlayer(RIGHT, hitBoxX, hitBoxY, animationX, width, height, speed, scene);
        } else if (isPlayerAtLeft(player)){
            moveToPlayer(LEFT, hitBoxX, hitBoxY, animationX, width, height, speed, scene);
        }
    }

    private void moveToPlayer(FaceDirection toMoveDirection, double hitBoxX,
                              double hitBoxY, double animationX, double width,
                              double height, float speed, BaseScene scene) {
        int scale = 1;
        if (toMoveDirection == LEFT) {
            speed = speed * -1;
            scale *= -1;
        }
        if (getFaceDirection() == toMoveDirection) {
            time = 0;
            if (getAnimation().getState() != MONSTER_WALK && getAnimation().getState() != BOSS_SKILL_THREE_WALK) getAnimation().setState(MONSTER_WALK);
            if (canWalk(toMoveDirection, hitBoxX, speed, hitBoxY, width, height, scene.getLvlData())) {
                walk(hitBoxX, animationX, speed, scene);
            }
        } else {
            if (time > turningTime) {
                getAnimation().setScaleX(scale);
                setFaceDirection(toMoveDirection);
                if (getAnimation().getState() != MONSTER_WALK && getAnimation().getState() != BOSS_SKILL_THREE_WALK) getAnimation().setState(MONSTER_WALK);
                if (canWalk(toMoveDirection, hitBoxX, speed, hitBoxY, width, height, scene.getLvlData())) {
                    walk(hitBoxX, animationX, speed, scene);
                }
            } else {
                getAnimation().setState(MONSTER_STAND_STILL);
                time += 1.0 /MOVE_FRAME;
            }
        }
    }

    public boolean isPlayerInAttackRange(Player player) {
        if (getAnimation().getState() != MONSTER_DEAD) {
            if (this instanceof BaseBoss) return true;
            if (this instanceof ThrowerGiGee) {
                if (Math.abs(getDeltaTilesY(player)) > getAttackRange() * 0.75 + 0.01) return false;
            } else {
                if (Math.abs(getDeltaTilesY(player)) > 0.5) return false;
            }
            if (getFaceDirection() == RIGHT) {
                return getDeltaTilesX(player) < getAttackRange() && 0 <= getDeltaTilesX(player);
            } else {
                return Math.abs(getDeltaTilesX(player)) < getAttackRange() && getDeltaTilesX(player) <= 0;
            }
        }
        return false;
    }

    public float getDeltaTilesX(Player player) {
        double deltaPixelX = player.getHitBox().getTranslateX()
                - getHitBox().getTranslateX();
        if (this instanceof BaseBoss) {
            if (deltaPixelX > 0 && player.getHitBox().getTranslateX() + player.getHitBox().getFitWidth() <
                    getHitBox().getTranslateX() + getHitBox().getFitWidth()) {
                return 0;
            }
        }
        if (deltaPixelX > 0) {
            deltaPixelX -= getHitBox().getFitWidth();
        } else {
            deltaPixelX += player.getHitBox().getFitWidth();
        }
        return (float) (deltaPixelX / PIXEL_PER_TILE);
    }

    public float getDeltaTilesY(Player player) {
        double deltaPixelY = (player.getHitBox().getTranslateY() + player.getHitBox().getFitHeight())
                - (getHitBox().getTranslateY() + getHitBox().getFitHeight());
        return (float) (deltaPixelY / PIXEL_PER_TILE);
    }

    public float getNormalAttackDelay() {
        return normalAttackDelay;
    }

    public void setNormalAttackDelay(float baseAttackDelay) {
        if (baseAttackDelay <= 0.5) baseAttackDelay = 0.5f;
        this.normalAttackDelay = baseAttackDelay;
    }

    public float getAddAttackDelay() {
        return addAttackDelay;
    }

    public void setAddAttackDelay(float addAttackDelay) {
        this.addAttackDelay = addAttackDelay;
    }

    public float getDropRate() {
        return dropRate;
    }

    public void setDropRate(float dropRate) {
        this.dropRate = dropRate;
    }

    public int getCoinDrop() {
        return coinDrop;
    }

    public void setCoinDrop(int coinDrop) {
        this.coinDrop = coinDrop;
    }

    public float getVisionRange() {
        return visionRange;
    }

    public void setVisionRange(float visionRange) {
        if (visionRange <= 1) visionRange = 1;
        this.visionRange = visionRange;
    }

    public float getWalkTime() {
        return walkTime;
    }

    public void setWalkTime(float walkTime) {
        this.walkTime = walkTime;
    }

    public float getNewRandomTime() {
        return randomFloat(MIN_WALKTIME, MAX_WALKTIME);
    }

    public float getTime() {
        return time;
    }

    public void setTime(float time) {
        this.time = time;
    }

    public float getStandTime() {
        return standTime;
    }

    public void setStandTime(float standTime) {
        this.standTime = standTime;
    }

    public void setWalk(boolean walk) {
        isWalk = walk;
    }

    public void setCanSee(boolean canSee) {
        this.canSee = canSee;
    }

    public void setCanAttack(boolean canAttack) {
        this.canAttack = canAttack;
    }
    public boolean isPlayerAtRight(Player player) {
        return player.getHitBox().getTranslateX() > getHitBox().getTranslateX() + getHitBox().getFitWidth();
    }
    public boolean isPlayerAtLeft(Player player) {
        return player.getHitBox().getTranslateX() + player.getHitBox().getFitWidth() < getHitBox().getTranslateX();
    }

    public void setTurningTime(float turningTime) {
        this.turningTime = turningTime;
    }

    public float getTimeSinceLastAttack() {
        return timeSinceLastAttack;
    }

    public void setTimeSinceLastAttack(float timeSinceLastAttack) {
        this.timeSinceLastAttack = timeSinceLastAttack;
    }

    public int getKnockbackChance() {
        return knockbackChance;
    }

    public void setKnockbackChance(int knockbackChance) {
        this.knockbackChance = knockbackChance;
    }
    public boolean isSpawn() {
        return isSpawn;
    }

    public void setSpawn(boolean spawn) {
        isSpawn = spawn;
    }

    public FaceDirection getDashDirection() {
        return dashDirection;
    }

    public void setDashDirection(FaceDirection dashDirection) {
        this.dashDirection = dashDirection;
    }

    public void setMoreCoinDrop(boolean moreCoinDrop) {
        this.moreCoinDrop = moreCoinDrop;
    }

    public boolean isSlowdownByThornPerk() {
        return isSlowdownByThornPerk;
    }

    public void setSlowdownByThornPerk(boolean slowdownByThornPerk) {
        isSlowdownByThornPerk = slowdownByThornPerk;
    }
}
