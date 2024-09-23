// A Thrower that can throw bombs to player

package entities.monsters;

import animating.Animation;
import animating.HitBox;
import entities.bases.BaseGiGee;

import static utils.AnimationStatus.*;
import static utils.MonsterStatus.*;

public class ThrowerGiGee extends BaseGiGee {
    public ThrowerGiGee(double spawnX, double spawnY) {
        super();
        setUniqueStatus(spawnX, spawnY);
    }

    @Override
    public void setUniqueStatus(double spawnX, double spawnY) {
        setBaseMaxHp(THROWER_HP);
        setCurrentHp(getBaseMaxHp());
        setBaseAtk(THROWER_ATK);
        setBaseDef(THROWER_DEF);
        setBaseMovementSpeed(LOW_MOVEMENT_SPEED);
        setAttackRange(LONG_RANGE);
        setVisionRange(MEDIUM_VISION);
        setNormalAttackDelay(THROWER_ATK_DELAY);
        setStandStillFrames(THROWERGIGEE_STAND_STILL_FRAMES);
        setWalkFrames(THROWERGIGEE_WALK_FRAMES);
        setNormalAttackFrames(THROWERGIGEE_NORMAL_ATTACK_FRAMES);
        setDeadFrames(THROWERGIGEE_DEAD_FRAMES);
        setHitBoxWidth(THROWERGIGEE_HITBOX_WIDTH);
        setHitBoxXOffset(THROWERGIGEE_HITBOX_X_OFFSET);
        getFrameMakeDamageNormalAttack().add(13);
        setAnimation(new Animation(this, spawnX, spawnY));
        setHitBox(new HitBox(this));
    }

}
