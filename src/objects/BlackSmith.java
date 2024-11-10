// A Class that is to upgrade perks and act as a reward for UpgradeRoom

package objects;

import animating.Animation;
import animating.HitBox;
import entities.bases.BaseEntity;
import enums.FaceDirection;
import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.geometry.Rectangle2D;
import javafx.util.Duration;
import scenes.BaseScene;

import java.util.Objects;

import static utils.AnimationStatus.*;
import static utils.ObjectStatus.*;

public class BlackSmith extends BaseObject{
    private String type;
    private boolean isStop;
    public BlackSmith(BaseEntity baseEntity, double posX, double posY, FaceDirection faceDirection, String type) {
        super(baseEntity, posX, posY, faceDirection);
        setType(type);
        int typeNumber = (Objects.equals(getType(), "Armadillos")) ? 0 : 1;
        setWidth(BLACK_SMITH_WIDTH.get(typeNumber));
        setHeight(BLACK_SMITH_HEIGHT.get(typeNumber));
        setCanIgnoreTile(true);
        setCanInteract(true);
        setImgString("objects/" + type + getClass().getSimpleName() + "_Sprite.png");
        setAnimation(new Animation(this, posX, posY));
        getAnimation().setViewport(new Rectangle2D(START_X, START_Y, getWidth(), getHeight()));
        setHitBox(new HitBox(this, BLACK_SMITH_HITBOX_WIDTH.get(typeNumber), BLACK_SMITH_HITBOX_HEIGHT.get(typeNumber),
                BLACK_SMITH_HITBOX_X_OFFSET.get(typeNumber), BLACK_SMITH_HITBOX_Y_OFFSET.get(typeNumber)));
        runAnimation(typeNumber);
    }

    private void runAnimation(int typeNumber) {
        AnimationTimer runAnimation = new AnimationTimer() {
            long lastTime = 0;
            int frameCount = 0;

            @Override
            public void handle(long now) {
                long elapsedTime = now - lastTime;
                double secondsElapsed = elapsedTime / 1_000_000_000.0;
                if (frameCount == 1) {
                    if (secondsElapsed > BLACK_SMITH_PAUSE_TIME.get(typeNumber)) {
                        frameCount += 1;
                    }
                } else {
                    if (secondsElapsed > (double) 1 / BLACK_SMITH_FRAMES_PER_SECOND.get(typeNumber)) {
                        getAnimation().setViewport(new Rectangle2D(START_X, START_Y + frameCount * (getHeight() + GAP_HEIGHT),
                                getWidth(), getHeight()));
                        lastTime = now;
                        frameCount += 1;
                    }
                    if (frameCount == BLACK_SMITH_FRAMES.get(typeNumber)) frameCount = 0;
                    if (isStop()) stop();
                }
            }
        };
        runAnimation.start();
    }
     public void disappear(BaseScene scene) {
        scene.getPlayer().setObjectThatInteractWith(null);
        scene.getPlayer().setCanInteract(false);
        scene.getPlayer().getObjectsInStage().remove(this);
         FadeTransition fadeOut = new FadeTransition(Duration.seconds(2), this.getAnimation());
         fadeOut.setFromValue(1.0); // Fully visible
         fadeOut.setToValue(0.0);   // Completely transparent
         fadeOut.play();
         fadeOut.setOnFinished(e -> {
             scene.getChildren().remove(this.getAnimation());
         });
     }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isStop() {
        return isStop;
    }

    public void setStop(boolean stop) {
        isStop = stop;
    }
}
