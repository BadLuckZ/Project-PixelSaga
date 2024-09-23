// A Thrower that can throw bombs that can slow player

package entities.monsters;

import animating.Animation;
import animating.HitBox;

import static utils.AnimationStatus.*;
import static utils.MonsterStatus.*;

public class IceThrowerGiGee extends ThrowerGiGee {
    public IceThrowerGiGee(double spawnX, double spawnY) {
        super(spawnX, spawnY);
    }

    @Override
    public void setUniqueStatus(double spawnX, double spawnY) {
        setBaseMaxHp(THROWER_HP);
        setCurrentHp(getBaseMaxHp());
        setBaseAtk(ICE_THROWER_ATK);
        setBaseDef(THROWER_DEF);
        setBaseMovementSpeed(LOW_MOVEMENT_SPEED);
        setAttackRange(LONG_RANGE);
        setVisionRange(MEDIUM_VISION);
        setNormalAttackDelay(THROWER_ATK_DELAY);
        setStandStillFrames(ICETHROWERGIGEE_STAND_STILL_FRAMES);
        setWalkFrames(ICETHROWERGIGEE_WALK_FRAMES);
        setNormalAttackFrames(ICETHROWERGIGEE_NORMAL_ATTACK_FRAMES);
        setDeadFrames(ICETHROWERGIGEE_DEAD_FRAMES);
        getFrameMakeDamageNormalAttack().add(13);
        setAnimation(new Animation(this, spawnX, spawnY));
        setHitBox(new HitBox(this));
    }

}
