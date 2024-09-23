// Animation for all entities and objects

package animating;

import entities.bases.BaseBoss;
import entities.bases.BaseEntity;
import entities.bases.BaseMonster;
import entities.player.Player;
import enums.FaceDirection;
import enums.States;
import javafx.geometry.Rectangle2D;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import objects.BaseObject;
import scenes.BaseScene;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static enums.FaceDirection.LEFT;
import static utils.AnimationStatus.*;
import static enums.States.*;

public class Animation extends ImageView {
    private BaseEntity baseEntity;
    private ColorAdjust colorAdjust = new ColorAdjust();
    private int currentFrameIndex;
    private States previousState;
    private States state;
    private double yVelocity = 0;
    private boolean isMoveLeft = false, isMoveRight = false, willMoveLeft = false, willMoveRight = false,
            isAnimationPlaying = false, isHoldRMB = false, isJump = false, canJump = true;

    public Animation(BaseEntity baseEntity, double posX, double posY) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> {
            Image image = new Image(
                    ClassLoader.getSystemResource(
                            baseEntity.getImgString()).toString(),
                    true);
            setImage(image);
        });
        setBaseEntity(baseEntity);
        setFitHeight(baseEntity.getAnimationHeight() * SCALE);
        setFitWidth(baseEntity.getAnimationWidth() * SCALE);
        if (baseEntity instanceof Player) {
            setState(PLAYER_STAND_STILL);
            setPreviousState(PLAYER_STAND_STILL);
            updateFrame(PLAYER_STAND_STILL);
        } else if (baseEntity instanceof BaseBoss) {
            setState(MONSTER_STAND_STILL);
            setPreviousState(MONSTER_STAND_STILL);
            updateFrame(MONSTER_STAND_STILL);
        } else if (baseEntity instanceof BaseMonster) {
            setState(MONSTER_SPAWN);
            setPreviousState(MONSTER_SPAWN);
            updateFrame(MONSTER_SPAWN);
        }

        setSpawnPoint(baseEntity, posX, posY);
        currentFrameIndex = 0;
    }

    public Animation(BaseObject baseObject, double posX, double posY) {
        super(baseObject.getImgString());
        setState(OBJECT);
        setFitHeight(baseObject.getHeight() * SCALE);
        setFitWidth(baseObject.getWidth() * SCALE);
        setSpawnPoint(baseObject, posX, posY);
    }


    public void setSpawnPoint(BaseEntity entity, double posX, double posY) {
        if (entity.getFaceDirection() == LEFT){
            setScaleX(-1);
        }
        setTranslateX(posX);
        setTranslateY(posY);
        try {
            baseEntity.getHitBox().setTranslateX(posX + baseEntity.getHitBoxXOffset() * SCALE);
            baseEntity.getHitBox().setTranslateY(posY + baseEntity.getHitBoxYOffset() * SCALE);
        } catch (Exception e){}
    }
    public void setSpawnPoint(BaseObject object, double posX, double posY) {
        if (object.getFaceDirection() == LEFT){
            setScaleX(-1);
        }
        setTranslateX(posX);
        setTranslateY(posY);
    }

    public void updateFrame(States state){
        int columnNumber = getColumnNumber(state);
        int numberOfRows = getNumberOfRows(baseEntity, state);
        int y = START_Y + (currentFrameIndex % numberOfRows) * (baseEntity.getAnimationHeight() + GAP_HEIGHT);
        int x = START_X + columnNumber * (baseEntity.getAnimationWidth() + GAP_WIDTH);
        setViewport(new Rectangle2D(x, y, baseEntity.getAnimationWidth(), baseEntity.getAnimationHeight()));
        currentFrameIndex = (currentFrameIndex + 1) % numberOfRows;
    }

    public int getNumberOfRows(BaseEntity baseEntity, States state) {
        if (state == PLAYER_STAND_STILL || state == MONSTER_STAND_STILL) {
            return baseEntity.getStandStillFrames();
        } else if (state == PLAYER_WALK || state == MONSTER_WALK) {
            return baseEntity.getWalkFrames();
        } else if (state == PLAYER_NORMAL_ATTACK || state == MONSTER_NORMAL_ATTACK) {
            return baseEntity.getNormalAttackFrames();
        } else if (state == PLAYER_SPECIAL_ATTACK) {
            return ((Player) baseEntity).getSpecialAttackFrames();
        } else if (state == PLAYER_DASH) {
            return ((Player) baseEntity).getDashFrames();
        } else if (state == PLAYER_DEAD || state == MONSTER_DEAD) {
            return baseEntity.getDeadFrames();
        } else if (state == PLAYER_JUMP_UP || state == PLAYER_JUMP_DOWN || state == BOSS_JUMP_UP || state == BOSS_JUMP_DOWN) {
            return JUMP_FRAME;
        } else if (state == MONSTER_SPAWN) {
            return SPAWN_FRAMES;
        } else if (state == BOSS_SKILL_ONE) {
            return BIGBOY_SKILL_ONE_FRAMES;
        } else if (state == BOSS_SKILL_TWO) {
            return BIGBOY_SKILL_TWO_FRAMES;
        } else if (state == BOSS_SKILL_THREE_STAND_STILL) {
            return BIGBOY_SKILL_THREE_STAND_FRAMES;
        } else if (state == BOSS_SKILL_THREE_WALK) {
            return BIGBOY_SKILL_THREE_WALK_FRAMES;
        } else {
            return 1;
        }
    }

    public int getColumnNumber(States state) {
        if (state == PLAYER_STAND_STILL || state == MONSTER_STAND_STILL) {
            return 0;
        } else if (state == PLAYER_WALK || state == MONSTER_WALK) {
            return 1;
        } else if (state == PLAYER_NORMAL_ATTACK || state == MONSTER_NORMAL_ATTACK || state == BOSS_SKILL_ONE) {
            return 2;
        } else if (state == PLAYER_SPECIAL_ATTACK || state == MONSTER_SPAWN || state == BOSS_SKILL_TWO) {
            return 3;
        } else if (state == PLAYER_DASH || state == MONSTER_DEAD || state == BOSS_SKILL_THREE_STAND_STILL) {
            return 4;
        } else if (state == PLAYER_JUMP_UP || state == BOSS_SKILL_THREE_WALK) {
            return 5;
        } else if (state == PLAYER_JUMP_DOWN) {
            return 6;
        } else if (state == PLAYER_DEAD || state == BOSS_JUMP_UP) {
            return 7;
        } else if (state == BOSS_JUMP_DOWN) {
            return 8;
        } else {
            return 0;
        }
    }

    public ColorAdjust getDefaultBrightness() {
        colorAdjust.setBrightness(0);
        return colorAdjust;
    }

    public void setAnimationToDefault(BaseEntity entity, BaseScene scene){
        if (entity instanceof Player) {
            if (scene.getPressedKeys().contains(KeyCode.A) && !scene.getPressedKeys().contains(KeyCode.D)) {
                setState(PLAYER_WALK);
                setCurrentFrameIndex(0);
                setMoveLeft(true);
                setMoveRight(false);
                setScaleX(-1);
            } else if (!scene.getPressedKeys().contains(KeyCode.A) && scene.getPressedKeys().contains(KeyCode.D)) {
                setState(PLAYER_WALK);
                setCurrentFrameIndex(0);
                setMoveLeft(false);
                setMoveRight(true);
                setScaleX(1);
            } else {
                setState(PLAYER_STAND_STILL);
                setMoveLeft(false);
                setMoveRight(false);
            }
            setCurrentFrameIndex(0);
            setWillMoveLeft(false);
            setWillMoveRight(false);
        } else if (entity instanceof BaseMonster){
            setState(MONSTER_STAND_STILL);
            setCurrentFrameIndex(0);
        }
    }

    public void setDisableMovement(){
        setState(PLAYER_STAND_STILL);
        setCurrentFrameIndex(0);
        setMoveLeft(false);
        setMoveRight(false);
        setWillMoveLeft(false);
        setWillMoveRight(false);
    }

    public BaseEntity getBaseEntity() {
        return baseEntity;
    }

    public void setBaseEntity(BaseEntity baseEntity) {
        this.baseEntity = baseEntity;
    }

    public int getCurrentFrameIndex() {
        return currentFrameIndex;
    }

    public void setCurrentFrameIndex(int currentFrameIndex) {
        this.currentFrameIndex = currentFrameIndex;
    }

    public States getState() {
        return state;
    }

    public void setState(States state) {
        this.state = state;
    }

    public boolean isMoveLeft() {
        return isMoveLeft;
    }

    public void setMoveLeft(boolean moveLeft) {
        isMoveLeft = moveLeft;
        if (isMoveLeft) baseEntity.setFaceDirection(LEFT);
    }

    public boolean isMoveRight() {
        return isMoveRight;
    }

    public void setMoveRight(boolean moveRight) {
        isMoveRight = moveRight;
        if (isMoveRight) baseEntity.setFaceDirection(FaceDirection.RIGHT);
    }

    public boolean willMoveLeft() {
        return willMoveLeft;
    }

    public void setWillMoveLeft(boolean willMoveLeft) {
        this.willMoveLeft = willMoveLeft;
    }

    public boolean willMoveRight() {
        return willMoveRight;
    }

    public void setWillMoveRight(boolean willMoveRight) {
        this.willMoveRight = willMoveRight;
    }

    public boolean isJump() {
        return isJump;
    }

    public void setJump(boolean jump) {
        isJump = jump;
    }

    public boolean canJump() {
        return canJump;
    }

    public void setCanJump(boolean canJump) {
        this.canJump = canJump;
    }

    public boolean isAnimationPlaying() {
        return isAnimationPlaying;
    }

    public void setAnimationPlaying(boolean animationPlaying) {
        isAnimationPlaying = animationPlaying;
    }

    public boolean isHoldRMB() {
        return isHoldRMB;
    }

    public void setHoldRMB(boolean holdRMB) {
        isHoldRMB = holdRMB;
    }

    public States getPreviousState() {
        return previousState;
    }

    public void setPreviousState(States previousState) {
        this.previousState = previousState;
    }

    public double getyVelocity() {
        return yVelocity;
    }

    public void setyVelocity(double yVelocity) {
        this.yVelocity = yVelocity;
    }

}