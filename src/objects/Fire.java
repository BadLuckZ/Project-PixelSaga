// A Class that acts as a boss' fire

package objects;

import animating.Animation;
import animating.HitBox;
import entities.bases.BaseBoss;
import entities.bases.BaseEntity;
import enums.FaceDirection;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import scenes.BaseScene;

import static enums.States.BOSS_SKILL_THREE_WALK;
import static utils.AnimationStatus.*;
import static utils.HelperMethods.getSoundFromSoundList;
import static utils.ObjectStatus.*;

public class Fire extends BaseObject {
    public Fire(BaseEntity baseEntity, double posX, double posY, FaceDirection faceDirection, BaseScene scene) {
        super(baseEntity, posX, posY, faceDirection);
        setWidth(FIRE_WIDTH);
        setHeight(FIRE_HEIGHT);
        setCanIgnoreTile(true);
        setAnimation(new Animation(this, posX, posY));
        getAnimation().setViewport(new Rectangle2D(START_X, START_Y, getWidth(), getHeight()));
        setHitBox(new HitBox(this, FIRE_HITBOX_WIDTH, FIRE_HITBOX_HEIGHT, FIRE_HITBOX_X_OFFSET, FIRE_HITBOX_Y_OFFSET));
        runRepeatAnimation(scene);
    }

    private void runRepeatAnimation(BaseScene scene) {
        BaseObject fire = this;
        Platform.runLater(() -> {
            AnimationTimer runAnimation = new AnimationTimer() {
                long lastTime = 0;
                int frameCount = 0, count = 0;
                boolean forceFinish = false;

                @Override
                public void handle(long now) {
                    long elapsedTime = now - lastTime;
                    double secondsElapsed = elapsedTime / 1_000_000_000.0;
                    if (frameCount == 7 && count < FIRE_TIME) {
                        frameCount = 3;
                        count += 1;
                    }
                    if (frameCount == FIRE_FRAMES) {
                        stopFire(scene, fire);
                        stop();
                    }
                    if (secondsElapsed > (double) 1 / FIRE_FRAMES_PER_SECOND) {
                        getAnimation().setViewport(new Rectangle2D(START_X, START_Y + frameCount * (getHeight() + GAP_HEIGHT),
                                getWidth(), getHeight()));
                        lastTime = now;
                        frameCount += 1;
                    }
                    if (getOwner().getAnimation().getState() != BOSS_SKILL_THREE_WALK && !forceFinish) {
                        count = FIRE_TIME;
                        frameCount = 7;
                        forceFinish = true;
                    }
                }
            };
            runAnimation.start();
        });
    }

    private void stopFire(BaseScene scene, BaseObject fire) {
        scene.getChildren().remove(fire.getAnimation());
        scene.getPlayer().getObjectsInStage().remove(fire);
        BaseBoss boss = (BaseBoss) getOwner();
        boss.getAnimation().setAnimationToDefault(boss, scene);
        boss.setAttackDelay(2);
        getSoundFromSoundList(fire.getSoundList(), "BossFire").stop();
        createBossCooldownSkill3(boss);
    }

    private void createBossCooldownSkill3(BaseBoss boss) {
        AnimationTimer timer = new AnimationTimer() {
            long startTime = System.nanoTime();
            @Override
            public void handle(long now) {
                long elapsedTime = now - startTime;
                double secondsElapsed = elapsedTime / 1_000_000_000.0;
                if (secondsElapsed >= boss.getCooldownSkill3()) {
                    boss.setCanSkill3(true);
                    stop();
                }
            }
        };
        timer.start();
    }
}
