// A Monster that attacks by sacrificing himself

package entities.monsters;

import animating.Animation;
import animating.HitBox;
import entities.bases.BaseEntity;
import entities.bases.BaseGiGee;
import javafx.animation.AnimationTimer;
import objects.Bomb;
import scenes.BaseScene;

import static enums.FaceDirection.RIGHT;
import static enums.States.MONSTER_DEAD;
import static utils.AnimationStatus.*;
import static utils.HelperMethods.getSoundFromSoundList;
import static utils.MonsterStatus.*;
import static utils.ObjectStatus.*;

public class KleeGiGee extends BaseGiGee {
    public KleeGiGee(double spawnX, double spawnY) {
        super();
        setUniqueStatus(spawnX, spawnY);
    }

    @Override
    public void setUniqueStatus(double spawnX, double spawnY) {
        setBaseMaxHp(KLEE_HP);
        setCurrentHp(getBaseMaxHp());
        setBaseAtk(KLEE_ATK);
        setBaseDef(KLEE_DEF);
        setBaseMovementSpeed(HIGH_MOVEMENT_SPEED);
        setAttackRange(CLOSED_RANGE);
        setVisionRange(MEDIUM_VISION);
        setNormalAttackDelay(KLEE_ATK_DELAY);
        setTurningTime(KLEE_TURNING_DELAY);
        setStandStillFrames(KLEEGIGEE_STAND_STILL_FRAMES);
        setWalkFrames(KLEEGIGEE_WALK_FRAMES);
        setNormalAttackFrames(KLEEGIGEE_NORMAL_ATTACK_FRAMES);
        setDeadFrames(KLEEGIGEE_DEAD_FRAMES);
        setAnimation(new Animation(this, spawnX, spawnY));
        setHitBox(new HitBox(this));
    }

    public void activateBomb(BaseScene scene) {
        BaseEntity klee = this;
        AnimationTimer bombTimer = new AnimationTimer() {
            long lastTime = System.nanoTime();
            long lastTimeBlink = System.nanoTime();
            float blinkTime = 0.3f;
            @Override
            public void handle(long now) {
                long elapsedTime1 = now - lastTime;
                double secondsElapsed1 = elapsedTime1 / 1_000_000_000.0;
                long elapsedTime2 = now - lastTimeBlink;
                double secondsElapsed2 = elapsedTime2 / 1_000_000_000.0;
                if (secondsElapsed1 > KLEEGIGEE_BOMB_TIME) {
                    getAnimation().setEffect(getAnimation().getDefaultBrightness());
                    if (!(klee.getAnimation().getState() == MONSTER_DEAD)) {
                        dead(scene.getPlayer(), scene);
                        Bomb bomb = new Bomb(klee, getAnimation().getTranslateX() - (KLEE_BOMB_WIDTH * SCALE - getAnimation().getFitWidth()) / 2,
                                getHitBox().getTranslateY() - ((KLEE_BOMB_HITBOX_HEIGHT + KLEE_BOMB_HITBOX_Y_OFFSET) * SCALE - getHitBox().getFitHeight()), RIGHT, scene);
                        getSoundFromSoundList(bomb.getSoundList(), "KleeBomb").play();
                        scene.getChildren().add(bomb.getAnimation());
                        scene.getPlayer().getObjectsInStage().add(bomb);
                    }
                    stop();
                }
                if (secondsElapsed2 > blinkTime) {
                    scene.blink(getAnimation());
                    lastTimeBlink = now;
                }
                if (getAnimation().getState() == MONSTER_DEAD) {
                    getAnimation().setEffect(getAnimation().getDefaultBrightness());
                }
            }
        };
        bombTimer.start();
    }

}
