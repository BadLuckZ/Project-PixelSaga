// A Class that uses a broadsword as a weapon

package entities.player.classes;

import entities.bases.BaseClass;

import java.util.Arrays;

import static utils.AnimationStatus.*;
import static utils.MonsterStatus.LOW_KNOCKBACK_DISTANCE;
import static utils.MonsterStatus.MID_RANGE;
import static utils.PlayerStatus.*;

public class BroadSword extends BaseClass {
    public BroadSword() {
        super();
        setUniqueStatus();
    }

    @Override
    public void setUniqueStatus() {
        setHp(HIGH_HP);
        setAtk(HIGH_ATK);
        setDef(HIGH_DEF);
        setAttackRange(MID_RANGE);
        setAttackSpeed(BROADSWORD_NORMAL_ATTACK_SPEED_MULTIPLIER);
        setStandStillFrames(BROADSWORD_STAND_STILL_FRAMES);
        setWalkFrames(BROADSWORD_WALK_FRAMES);
        setNormalAttackFrames(BROADSWORD_NORMAL_ATTACK_FRAMES);
        setSpecialAttackFrames(BROADSWORD_SPECIAL_ATTACK_FRAMES);
        setSpecialAttackCoolDown(BROADSWORD_SPECIAL_ATTACK_COOLDOWN);
        setDashFrames(BROADSWORD_DASH_FRAMES);
        setDeadFrames(BROADSWORD_DEAD_FRAMES);
        setAttackCombo(2);
        setTimeCanSecondAttack(TIME_CAN_SECOND_ATTACK);
        getFrameMakeDamageNormalAttack().addAll(Arrays.asList(4, 12));
        getFrameMakeDamageSpecialAttack().add(2);
        setKnockbackDistance(LOW_KNOCKBACK_DISTANCE);
        setMaxDash(LOW_MAX_DASH);
        setAnimationWidth(BROADSWORD_ANIMATION_WIDTH);
        setAnimationHeight(BROADSWORD_ANIMATION_HEIGHT);
        setHitBoxWidth(BROADSWORD_HITBOX_WIDTH);
        setHitBoxHeight(BROADSWORD_HITBOX_HEIGHT);
        setHitBoxXOffset(BROADSWORD_HITBOX_X_OFFSET);
        setHitBoxYOffset(BROADSWORD_HITBOX_Y_OFFSET);
    }
}
