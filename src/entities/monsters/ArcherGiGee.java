// A Monster that uses Missile as a weapon

package entities.monsters;

import animating.Animation;
import animating.HitBox;
import entities.bases.BaseGiGee;

import static utils.AnimationStatus.*;
import static utils.MonsterStatus.*;
public class ArcherGiGee extends BaseGiGee {
    public ArcherGiGee(double spawnX, double spawnY) {
        super();
        setUniqueStatus(spawnX, spawnY);
    }

    @Override
    public void setUniqueStatus(double spawnX, double spawnY) {
        setBaseMaxHp(ARCHER_HP);
        setCurrentHp(getBaseMaxHp());
        setBaseAtk(ARCHER_ATK);
        setBaseDef(ARCHER_DEF);
        setBaseMovementSpeed(MEDIUM_MOVEMENT_SPEED);
        setAttackRange(LONG_RANGE);
        setVisionRange(MEDIUM_VISION);
        setNormalAttackDelay(ARCHER_ATK_DELAY);
        setStandStillFrames(ARCHERGIGEE_STAND_STILL_FRAMES);
        setWalkFrames(ARCHERGIGEE_WALK_FRAMES);
        setNormalAttackFrames(ARCHERGIGEE_NORMAL_ATTACK_FRAMES);
        setDeadFrames(ARCHERGIGEE_DEAD_FRAMES);
        getFrameMakeDamageNormalAttack().add(19);
        setAnimation(new Animation(this, spawnX, spawnY));
        setHitBox(new HitBox(this));
    }

}
