// A Class that uses a bow as a weapon

package entities.player.classes;

import entities.bases.BaseClass;

import static utils.AnimationStatus.*;
import static utils.MonsterStatus.MEDIUM_KNOCKBACK_DISTANCE;
import static utils.MonsterStatus.LONG_RANGE;
import static utils.PlayerStatus.*;

public class Bow extends BaseClass {
    public Bow() {
        super();
        setUniqueStatus();
    }
    @Override
    public void setUniqueStatus() {
        setAtk(LOW_ATK);
        setMovementSpeed(MEDIUM_MOVEMENT_SPEED);
        setEvadeRate(HIGH_EVADE_RATE);
        setAttackRange(LONG_RANGE);
        setAttackSpeed(BOW_NORMAL_ATTACK_SPEED_MULTIPLIER);
        setStandStillFrames(BOW_STAND_STILL_FRAMES);
        setWalkFrames(BOW_WALK_FRAMES);
        setNormalAttackFrames(BOW_NORMAL_ATTACK_FRAMES);
        setSpecialAttackFrames(BOW_SPECIAL_ATTACK_FRAMES);
        setSpecialAttackCoolDown(BOW_SPECIAL_ATTACK_COOLDOWN);
        setDashFrames(BOW_DASH_FRAME);
        setDeadFrames(BOW_DEAD_FRAMES);
        setAttackCombo(1);
        setTimeCanSecondAttack(0);
        setKnockbackDistance(MEDIUM_KNOCKBACK_DISTANCE);
    }
}
