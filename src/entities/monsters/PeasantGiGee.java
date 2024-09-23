// A Monster that attacks by using his sword

package entities.monsters;

import animating.Animation;
import animating.HitBox;
import entities.bases.BaseGiGee;

import static utils.AnimationStatus.*;
import static utils.MonsterStatus.*;

public class PeasantGiGee extends BaseGiGee {
    public PeasantGiGee(double spawnX, double spawnY) {
        super();
        setUniqueStatus(spawnX, spawnY);
    }

    @Override
    public void setUniqueStatus(double spawnX, double spawnY) {
        setBaseMaxHp(PEASANT_HP);
        setCurrentHp(getBaseMaxHp());
        setBaseAtk(PEASANT_ATK);
        setBaseDef(PEASANT_DEF);
        setBaseMovementSpeed(MEDIUM_MOVEMENT_SPEED);
        setAttackRange(CLOSED_RANGE);
        setVisionRange(SHORT_VISION);
        setNormalAttackDelay(PEASANT_ATK_DELAY);
        setStandStillFrames(PEASANTGIGEE_STAND_STILL_FRAMES);
        setWalkFrames(PEASANTGIGEE_WALK_FRAMES);
        setNormalAttackFrames(PEASANTGIGEE_NORMAL_ATTACK_FRAMES);
        setDeadFrames(PEASANTGIGEE_DEAD_FRAMES);
        getFrameMakeDamageNormalAttack().add(8);
        getDashFrame().add(7);
        setAnimation(new Animation(this, spawnX, spawnY));
        setHitBox(new HitBox(this));
    }

}
