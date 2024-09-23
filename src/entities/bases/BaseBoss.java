// BaseClass for the Boss in the game (BigBoy)

package entities.bases;

import java.util.ArrayList;

import static utils.MonsterStatus.*;

public abstract class BaseBoss extends BaseMonster {
    private float cooldownSkill1;
    private float cooldownSkill2;
    private float cooldownSkill3;
    private float skillOneAttackRange;
    private float skillTwoAttackRange;
    private float skillThreeAttackRange;
    private boolean canSkill1 = true, canSkill2 = true, canSkill3 = true;
    private ArrayList<Integer> skillToRandom = new ArrayList<>();
    private float attackDelay = 0;
    private ArrayList<Integer> frameMakeDamageSkillOne = new ArrayList<>();
    private ArrayList<Integer> frameMakeDamageSkillTwo = new ArrayList<>();
    private ArrayList<Integer> frameMakeDamageSkillThree = new ArrayList<>();

    public BaseBoss() {
        super();
        setCommonStatus();
    }

    @Override
    public void setCommonStatus() {
        setEvadeRate(MEDIUM_EVADE_RATE);
        setDropRate(BOSS_DROP_RATE);
        setCoinDrop(COIN_DROP_BOSS);
        setKnockbackChance(LOW_KNOCKBACK_CHANCE);
        setKnockbackDistance(LOW_KNOCKBACK_DISTANCE);
    }

    public float getCooldownSkill1() {
        return cooldownSkill1;
    }

    public void setCooldownSkill1(float cooldownSkill1) {
        this.cooldownSkill1 = cooldownSkill1;
    }

    public float getCooldownSkill2() {
        return cooldownSkill2;
    }

    public void setCooldownSkill2(float cooldownSkill2) {
        this.cooldownSkill2 = cooldownSkill2;
    }

    public float getCooldownSkill3() {
        return cooldownSkill3;
    }

    public void setCooldownSkill3(float cooldownSkill3) {
        this.cooldownSkill3 = cooldownSkill3;
    }

    public float getSkillOneAttackRange() {
        return skillOneAttackRange;
    }

    public void setSkillOneAttackRange(float skillOneAttackRange) {
        this.skillOneAttackRange = skillOneAttackRange;
    }

    public float getSkillTwoAttackRange() {
        return skillTwoAttackRange;
    }

    public void setSkillTwoAttackRange(float skillTwoAttackRange) {
        this.skillTwoAttackRange = skillTwoAttackRange;
    }

    public float getSkillThreeAttackRange() {
        return skillThreeAttackRange;
    }

    public void setSkillThreeAttackRange(float skillThreeAttackRange) {
        this.skillThreeAttackRange = skillThreeAttackRange;
    }

    public ArrayList<Integer> getSkillToRandom() {
        return skillToRandom;
    }

    public boolean canSkill1() {
        return canSkill1;
    }

    public void setCanSkill1(boolean canSkill1) {
        this.canSkill1 = canSkill1;
    }

    public boolean canSkill2() {
        return canSkill2;
    }

    public void setCanSkill2(boolean canSkill2) {
        this.canSkill2 = canSkill2;
    }

    public boolean canSkill3() {
        return canSkill3;
    }

    public void setCanSkill3(boolean canSkill3) {
        this.canSkill3 = canSkill3;
    }

    public float getAttackDelay() {
        return attackDelay;
    }

    public void setAttackDelay(float attackDelay) {
        this.attackDelay = attackDelay;
    }

    public ArrayList<Integer> getFrameMakeDamageSkillOne() {
        return frameMakeDamageSkillOne;
    }

    public ArrayList<Integer> getFrameMakeDamageSkillTwo() {
        return frameMakeDamageSkillTwo;
    }

    public ArrayList<Integer> getFrameMakeDamageSkillThree() {
        return frameMakeDamageSkillThree;
    }
}
