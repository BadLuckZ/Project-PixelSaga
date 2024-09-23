// A Class that uses a dagger as a weapon

package entities.player.classes;

import entities.bases.BaseClass;

import java.util.Arrays;

import static utils.AnimationStatus.*;
import static utils.AnimationStatus.DAGGER_DEAD_FRAMES;
import static utils.MonsterStatus.MEDIUM_KNOCKBACK_DISTANCE;
import static utils.PlayerStatus.*;

public class Dagger extends BaseClass {
    public Dagger() {
        super();
        setUniqueStatus();
    }

    @Override
    public void setUniqueStatus() {
        setAtk(LOW_ATK);
        setMovementSpeed(HIGH_MOVEMENT_SPEED);
        setAttackSpeed(DAGGER_NORMAL_ATTACK_SPEED_MULTIPLIER);
        setCritRate(HIGH_CRIT_RATE);
        setCritDamage(HIGH_CRIT_DAMAGE);
        setStandStillFrames(DAGGER_STAND_STILL_FRAMES);
        setWalkFrames(DAGGER_WALK_FRAMES);
        setNormalAttackFrames(DAGGER_NORMAL_ATTACK_FRAMES);
        setSpecialAttackFrames(DAGGER_SPECIAL_ATTACK_FRAMES);
        setSpecialAttackCoolDown(DAGGER_SPECIAL_ATTACK_COOLDOWN);
        setDashFrames(DAGGER_DASH_FRAMES);
        setDeadFrames(DAGGER_DEAD_FRAMES);
        setAttackCombo(2);
        setTimeCanSecondAttack(TIME_CAN_SECOND_ATTACK);
        getFrameMakeDamageNormalAttack().addAll(Arrays.asList(1, 3));
        getFrameMakeDamageSpecialAttack().add(2);
        setKnockbackDistance(MEDIUM_KNOCKBACK_DISTANCE);
    }
}
