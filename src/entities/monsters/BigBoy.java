// A Boss that uses Fire, Stomping Attack and Shock Wave Attack

package entities.monsters;

import animating.Animation;
import animating.HitBox;
import entities.bases.BaseBoss;
import entities.bases.BaseEntity;
import enums.FaceDirection;
import javafx.animation.AnimationTimer;
import objects.BaseObject;
import objects.Bomb;
import scenes.BaseScene;

import static enums.FaceDirection.RIGHT;
import static utils.AnimationStatus.*;
import static utils.HelperMethods.getSoundFromSoundList;
import static utils.MonsterStatus.*;
import static utils.ObjectStatus.*;

public class BigBoy extends BaseBoss {
    private double jumpDistance;
    public BigBoy(double spawnX, double spawnY) {
        super();
        setUniqueStatus(spawnX, spawnY);
    }

    @Override
    public void setUniqueStatus(double spawnX, double spawnY) {
        setBaseMaxHp(BIGBOY_HP);
        setCurrentHp(getBaseMaxHp());
        setBaseAtk(BIGBOY_ATK);
        setBaseDef(BIGBOY_DEF);
        setBaseMovementSpeed(MEDIUM_MOVEMENT_SPEED);
        setCooldownSkill1(BIGBOY_SKILL_ONE_COOLDOWN);
        setCooldownSkill2(BIGBOY_SKILL_TWO_COOLDOWN);
        setCooldownSkill3(BIGBOY_SKILL_THREE_COOLDOWN);
        setSkillOneAttackRange(BIGBOY_SKILL_ONE_ATTACK_RANGE);
        setSkillTwoAttackRange(BIGBOY_SKILL_TWO_ATTACK_RANGE);
        setSkillThreeAttackRange(BIGBOY_SKILL_THREE_ATTACK_RANGE);
        setStandStillFrames(BIGBOY_STAND_STILL_FRAMES);
        setWalkFrames(BIGBOY_WALK_FRAMES);
        setDeadFrames(BIGBOY_DEAD_FRAMES);
        setFaceDirection(FaceDirection.LEFT);
        getFrameMakeDamageSkillOne().add(5);
        getDashFrame().add(3);
        getFrameMakeDamageSkillTwo().add(3);
        getFrameMakeDamageSkillThree().add(4);
        setAnimationWidth(BIGBOY_ANIMATION_WIDTH);
        setAnimationHeight(BIGBOY_ANIMATION_HEIGHT);
        setHitBoxWidth(BIGBOY_HITBOX_WIDTH);
        setHitBoxHeight(BIGBOY_HITBOX_HEIGHT);
        setHitBoxXOffset(BIGBOY_HITBOX_X_OFFSET);
        setHitBoxYOffset(BIGBOY_HITBOX_Y_OFFSET);
        setAnimation(new Animation(this, spawnX, spawnY));
        setHitBox(new HitBox(this));
    }

    public void activateNuclear(BaseScene scene) {
        BaseEntity bigBoy = this;
        AnimationTimer bombTimer = new AnimationTimer() {
            long lastTime = System.nanoTime();
            long lastTimeBlink = System.nanoTime();
            float blinkTime = 0.2f;
            @Override
            public void handle(long now) {
                long elapsedTime1 = now - lastTime;
                double secondsElapsed1 = elapsedTime1 / 1_000_000_000.0;
                long elapsedTime2 = now - lastTimeBlink;
                double secondsElapsed2 = elapsedTime2 / 1_000_000_000.0;
                if (secondsElapsed1 > BIGBOY_NUCLEAR_TIME) {
                    getAnimation().setEffect(getAnimation().getDefaultBrightness());
                    BaseObject nuclearBomb = new Bomb(bigBoy, getAnimation().getTranslateX() - (NUCLEAR_BOMB_WIDTH * SCALE - getAnimation().getFitWidth()) / 2,
                            getHitBox().getTranslateY() - ((NUCLEAR_BOMB_HITBOX_HEIGHT + NUCLEAR_BOMB_HITBOX_Y_OFFSET) * SCALE -
                                    getHitBox().getFitHeight()), RIGHT, scene);
                    getSoundFromSoundList(nuclearBomb.getSoundList(), "NuclearBomb").play();
                    scene.getChildren().add(nuclearBomb.getAnimation());
                    scene.getPlayer().getObjectsInStage().add(nuclearBomb);
                    stop();
                }
                if (secondsElapsed2 > blinkTime) {
                    scene.blink(getAnimation());
                    lastTimeBlink = now;
                }
            }
        };
        bombTimer.start();
    }

    public double getJumpDistance() {
        return jumpDistance;
    }

    public void setJumpDistance(double jumpDistance) {
        this.jumpDistance = jumpDistance;
    }
}
