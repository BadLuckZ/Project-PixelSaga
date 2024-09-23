// A class that is for getting a perk in the ClassSelectionScene

package objects;

import animating.Animation;
import animating.HitBox;
import entities.bases.BaseEntity;
import enums.FaceDirection;
import javafx.animation.AnimationTimer;
import javafx.geometry.Rectangle2D;

import static utils.AnimationStatus.*;
import static utils.HelperMethods.loadObjectsSound;
import static utils.ObjectStatus.*;

public class VendingMachine extends BaseObject{
    private boolean isStop = false;
    public VendingMachine(BaseEntity baseEntity, double posX, double posY, FaceDirection faceDirection) {
        super(baseEntity, posX, posY, faceDirection);
        setWidth(VENDING_MACHINE_WIDTH);
        setHeight(VENDING_MACHINE_HEIGHT);
        setCanIgnoreTile(true);
        setCanInteract(true);
        setAnimation(new Animation(this, posX, posY));
        getAnimation().setViewport(new Rectangle2D(START_X, START_Y, getWidth(), getHeight()));
        setHitBox(new HitBox(this));
        setSoundList(loadObjectsSound(this));
        runAnimation();
    }

    private void runAnimation() {
        AnimationTimer runAnimation = new AnimationTimer() {
            long lastTime = 0;
            int frameCount = 0;

            @Override
            public void handle(long now) {
                long elapsedTime = now - lastTime;
                double secondsElapsed = elapsedTime / 1_000_000_000.0;
                if (frameCount == VENDING_MACHINE_FRAMES) {
                    frameCount = 0;
                }
                if (secondsElapsed > (double) 1 / VENDING_MACHINE_FRAMES_PER_SECOND) {
                    getAnimation().setViewport(new Rectangle2D(START_X, START_Y + frameCount * (getHeight() + GAP_HEIGHT),
                            getWidth(), getHeight()));
                    lastTime = now;
                    frameCount += 1;
                }
                if (isStop()) stop();
            }
        };
        runAnimation.start();
    }

    public boolean isStop() {
        return isStop;
    }

    public void setStop(boolean stop) {
        this.isStop = stop;
    }
}
