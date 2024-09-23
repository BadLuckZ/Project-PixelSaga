// A Class that acts as a thrower's bomb

package objects;

import animating.Animation;
import animating.HitBox;
import entities.bases.BaseEntity;
import enums.FaceDirection;
import javafx.animation.AnimationTimer;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import scenes.BaseScene;

import java.util.Objects;

import static utils.AnimationStatus.*;
import static utils.HelperMethods.getSoundFromSoundList;
import static utils.ObjectStatus.*;

public class ThrowerBomb extends BaseObject{
    private boolean isActivate = false;
    private String throwerType;
    public ThrowerBomb(BaseEntity baseEntity, double posX, double posY, FaceDirection faceDirection, double speed, double yVelocity, String type) {
        super(baseEntity, posX, posY, faceDirection);
        setWidth(THROWER_BOMB_WIDTH);
        setHeight(THROWER_BOMB_HEIGHT);
        setSpeed((float)speed);
        setCanIgnoreTile(true);
        setCanFall(true);
        setThrowerType(type);
        if (!Objects.equals(getThrowerType(), "Default")) {
            setImgString("objects/Thrower" + type + "Bomb_Sprite.png");
        }
        setAnimation(new Animation(this, posX, posY));
        setHitBox(new HitBox(this));
        getAnimation().setJump(true);
        getAnimation().setyVelocity(yVelocity);
        canIgnoreTileTimer(type);
    }

    private void canIgnoreTileTimer(String type) {
        AnimationTimer timer = new AnimationTimer() {
            long lastTime = System.nanoTime();
            float throwTime = (Objects.equals(type, "Ice")) ? ICETHROWERGIGEE_THROW_TIME : THROWERGIGEE_THROW_TIME;
            @Override
            public void handle(long now) {
                long elapsedTime = now - lastTime;
                double secondsElapsed = elapsedTime / 1_000_000_000.0;
                if (secondsElapsed > throwTime) {
                    setCanIgnoreTile(false);
                    stop();
                }
            }
        };
        timer.start();
    }

    public void activateBomb(BaseScene scene) {
        getSoundFromSoundList(getSoundList(), "ThrowerBombAttack").play();
        setSpeed(NOT_MOVE);
        setCanIgnoreTile(true);
        setCanFall(false);
        setActivate(true);
        if (Objects.equals(getThrowerType(), "Default")) {
            setDefaultSetUp(scene);
        } else if (Objects.equals(getThrowerType(), "Ice")) {
            setIceSetUp(scene);
        }
    }

    private void setDefaultSetUp(BaseScene scene) {
        setWidth(THROWER_BOMB_ACTIVATE_WIDTH);
        setHeight(THROWER_BOMB_ACTIVATE_HEIGHT);
        Image image = new Image(String.valueOf(ClassLoader.getSystemResource("objects/ThrowerBombActivate_Sprite.png")));
        getAnimation().setImage(image);
        getAnimation().setViewport(new Rectangle2D(START_X, START_Y, getWidth(), getHeight()));
        getAnimation().setFitWidth(getWidth());
        getAnimation().setFitHeight(getHeight());
        getAnimation().setTranslateX(getAnimation().getTranslateX() - (double) (THROWER_BOMB_ACTIVATE_WIDTH - THROWER_BOMB_WIDTH) / 2);
        getAnimation().setTranslateY(getAnimation().getTranslateY() - (double) (THROWER_BOMB_ACTIVATE_HEIGHT - THROWER_BOMB_HEIGHT) / 2);
        setHitBox(new HitBox(this, THROWER_BOMB_ACTIVATE_HITBOX_WIDTH, THROWER_BOMB_ACTIVATE_HITBOX_HEIGHT, THROWER_BOMB_ACTIVATE_HITBOX_X_OFFSET, THROWER_BOMB_ACTIVATE_HITBOX_Y_OFFSET));
        runOneTimeAnimation(THROWER_BOMB_ACTIVATE_FRAMES, THROWER_BOMB_ACTIVATE_FRAMES_PER_SECOND, scene, true);
    }

    private void setIceSetUp(BaseScene scene) {
        setWidth(THROWER_ICE_BOMB_ACTIVATE_WIDTH);
        setHeight(THROWER_ICE_BOMB_ACTIVATE_HEIGHT);
        Image image = new Image(String.valueOf(ClassLoader.getSystemResource("objects/ThrowerIceBombActivate_Sprite.png")));
        getAnimation().setImage(image);
        getAnimation().setViewport(new Rectangle2D(START_X, START_Y, getWidth(), getHeight()));
        getAnimation().setFitWidth(getWidth());
        getAnimation().setFitHeight(getHeight());
        getAnimation().setTranslateX(getAnimation().getTranslateX() - (double) (THROWER_ICE_BOMB_ACTIVATE_WIDTH - THROWER_BOMB_WIDTH) / 2);
        getAnimation().setTranslateY(getAnimation().getTranslateY() - (double) (THROWER_ICE_BOMB_ACTIVATE_HEIGHT - THROWER_BOMB_HEIGHT) +
                (THROWER_ICE_BOMB_ACTIVATE_HEIGHT - THROWER_ICE_BOMB_ACTIVATE_HITBOX_HEIGHT - THROWER_ICE_BOMB_ACTIVATE_HITBOX_Y_OFFSET));
        setHitBox(new HitBox(this, THROWER_ICE_BOMB_ACTIVATE_HITBOX_WIDTH, THROWER_ICE_BOMB_ACTIVATE_HITBOX_HEIGHT,
                THROWER_ICE_BOMB_ACTIVATE_HITBOX_X_OFFSET, THROWER_ICE_BOMB_ACTIVATE_HITBOX_Y_OFFSET));
        runOneTimeAnimation(THROWER_ICE_BOMB_ACTIVATE_FRAMES, THROWER_ICE_BOMB_ACTIVATE_FRAMES_PER_SECOND, scene, true);
    }

    public boolean isActivate() {
        return isActivate;
    }

    public void setActivate(boolean activate) {
        isActivate = activate;
    }

    public String getThrowerType() {
        return throwerType;
    }

    public void setThrowerType(String throwerType) {
        this.throwerType = throwerType;
    }
}
