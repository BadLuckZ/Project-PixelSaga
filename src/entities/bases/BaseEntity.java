// BaseClass for All Entities in the game (BaseMonster, Player)

package entities.bases;

import animating.Animation;
import animating.HitBox;
import interfaces.Attackable;
import interfaces.Moveable;
import enums.FaceDirection;
import utils.SoundLoader;

import java.util.ArrayList;

import static utils.HelperMethods.finalValuePercent;

public abstract class BaseEntity implements Moveable, Attackable {
    public static final int ATTACK_VARIATION = 5;
    private int baseMaxHp;
    private int addMaxHp;
    private int currentHp;
    private int baseAtk;
    private int addAtk;
    private int baseDef;
    private int addDef;
    private float baseMovementSpeed;
    private int addMovementSpeed;
    private int evadeRate;
    private int damageIncrease;
    private int damageDecrease;
    private FaceDirection faceDirection;
    private Animation animation;
    private HitBox hitBox;
    private float attackRange;
    private String imgString;
    private int standStillFrames;
    private int walkFrames;
    private int normalAttackFrames;
    private int deadFrames;
    private ArrayList<Integer> frameMakeDamageNormalAttack;
    private boolean canTakeDamage = true;
    private boolean isDash;
    private float knockbackDistance;
    private ArrayList<Integer> dashFrame;
    private int dashFrameCount;
    private int animationWidth;
    private int animationHeight;
    private int hitBoxWidth;
    private int hitBoxHeight;
    private int hitBoxXOffset;
    private int hitBoxYOffset;
    private ArrayList<SoundLoader> soundList;
    private int damageReceived;
    public abstract void setCommonStatus();
    public abstract void setUniqueStatus(double spawnX, double spawnY);

    public int getBaseMaxHp() {
        return baseMaxHp;
    }

    public void setBaseMaxHp(int baseMaxHp) {
        if (baseMaxHp <= 0) baseMaxHp = 0;
        this.baseMaxHp = baseMaxHp;
    }

    public int getAddMaxHp() {
        return addMaxHp;
    }

    public void setAddMaxHp(int addMaxHp) {
        this.addMaxHp = addMaxHp;
    }

    public int getCurrentHp() {
        return currentHp;
    }
    public void setCurrentHp(int currentHp) {
        if (currentHp <= 0) currentHp = 0;
        if (currentHp >= finalValuePercent(baseMaxHp, addMaxHp))
            currentHp = finalValuePercent(baseMaxHp, addMaxHp);
        this.currentHp = currentHp;
    }
    public int getBaseAtk() {
        return baseAtk;
    }

    public void setBaseAtk(int baseAtk) {
        if (baseAtk <= 1) baseAtk = 1;
        this.baseAtk = baseAtk;
    }

    public int getAddAtk() {
        return addAtk;
    }

    public void setAddAtk(int addAtk) {
        this.addAtk = addAtk;
    }

    public int getBaseDef() {
        return baseDef;
    }

    public void setBaseDef(int baseDef) {
        if (baseDef <= 0) baseDef = 0;
        this.baseDef = baseDef;
    }

    public int getAddDef() {
        return addDef;
    }

    public void setAddDef(int addDef) {
        this.addDef = addDef;
    }

    public float getBaseMovementSpeed() {
        return baseMovementSpeed;
    }

    public void setBaseMovementSpeed(float baseMovementSpeed) {
        if (baseMovementSpeed <= 0) baseMovementSpeed = 0;
        this.baseMovementSpeed = baseMovementSpeed;
    }


    public int getAddMovementSpeed() {
        return addMovementSpeed;
    }

    public void setAddMovementSpeed(int addMovementSpeed) {
        this.addMovementSpeed = addMovementSpeed;
    }

    public int getEvadeRate() {
        return evadeRate;
    }

    public void setEvadeRate(int evadeRate) {
        if (evadeRate <= 0) evadeRate = 0;
        this.evadeRate = evadeRate;
    }

    public int getDamageIncrease() {
        return damageIncrease;
    }

    public void setDamageIncrease(int damageIncrease) {
        this.damageIncrease = damageIncrease;
    }

    public int getDamageDecrease() {
        return damageDecrease;
    }

    public void setDamageDecrease(int damageDecrease) {
        this.damageDecrease = damageDecrease;
    }

    public FaceDirection getFaceDirection() {
        return faceDirection;
    }

    public void setFaceDirection(FaceDirection faceDirection) {
        this.faceDirection = faceDirection;
    }

    public float getAttackRange() {
        return attackRange;
    }

    public void setAttackRange(float attackRange) {
        this.attackRange = attackRange;
    }

    public Animation getAnimation() {
        return animation;
    }

    public void setAnimation(Animation animation) {
        this.animation = animation;
    }

    public HitBox getHitBox() {
        return hitBox;
    }

    public void setHitBox(HitBox hitBox) {
        this.hitBox = hitBox;
    }

    public String getImgString() {
        return imgString;
    }

    public void setImgString(String imgString) {
        this.imgString = imgString;
    }

    public int getStandStillFrames() {
        return standStillFrames;
    }

    public void setStandStillFrames(int standStillFrames) {
        this.standStillFrames = standStillFrames;
    }

    public int getWalkFrames() {
        return walkFrames;
    }

    public void setWalkFrames(int walkFrames) {
        this.walkFrames = walkFrames;
    }

    public int getNormalAttackFrames() {
        return normalAttackFrames;
    }

    public void setNormalAttackFrames(int normalAttackFrames) {
        this.normalAttackFrames = normalAttackFrames;
    }

    public int getDeadFrames() {
        return deadFrames;
    }

    public void setDeadFrames(int deadFrames) {
        this.deadFrames = deadFrames;
    }

    public ArrayList<Integer> getFrameMakeDamageNormalAttack() {
        return frameMakeDamageNormalAttack;
    }

    public void setFrameMakeDamageNormalAttack(ArrayList<Integer> frameMakeDamageNormalAttack) {
        this.frameMakeDamageNormalAttack = frameMakeDamageNormalAttack;
    }

    public boolean canTakeDamage() {
        return canTakeDamage;
    }

    public void setCanTakeDamage(boolean canTakeDamage) {
        this.canTakeDamage = canTakeDamage;
    }

    public float getKnockbackDistance() {
        return knockbackDistance;
    }

    public void setKnockbackDistance(float knockbackDistance) {
        this.knockbackDistance = knockbackDistance;
    }

    public ArrayList<Integer> getDashFrame() {
        return dashFrame;
    }

    public void setDashFrame(ArrayList<Integer> dashFrame) {
        this.dashFrame = dashFrame;
    }

    public int getDashFrameCount() {
        return dashFrameCount;
    }

    public void setDashFrameCount(int dashFrameCount) {
        this.dashFrameCount = dashFrameCount;
    }

    public boolean isDash() {
        return isDash;
    }

    public void setDash(boolean dash) {
        isDash = dash;
    }

    public int getAnimationWidth() {
        return animationWidth;
    }

    public void setAnimationWidth(int animationWidth) {
        this.animationWidth = animationWidth;
    }

    public int getAnimationHeight() {
        return animationHeight;
    }

    public void setAnimationHeight(int animationHeight) {
        this.animationHeight = animationHeight;
    }

    public int getHitBoxWidth() {
        return hitBoxWidth;
    }

    public void setHitBoxWidth(int hitBoxWidth) {
        this.hitBoxWidth = hitBoxWidth;
    }

    public int getHitBoxHeight() {
        return hitBoxHeight;
    }

    public void setHitBoxHeight(int hitBoxHeight) {
        this.hitBoxHeight = hitBoxHeight;
    }

    public int getHitBoxXOffset() {
        return hitBoxXOffset;
    }

    public void setHitBoxXOffset(int hitBoxXOffset) {
        this.hitBoxXOffset = hitBoxXOffset;
    }

    public int getHitBoxYOffset() {
        return hitBoxYOffset;
    }

    public void setHitBoxYOffset(int hitBoxYOffset) {
        this.hitBoxYOffset = hitBoxYOffset;
    }

    public ArrayList<SoundLoader> getSoundList() {
        return soundList;
    }

    public void setSoundList(ArrayList<SoundLoader> soundList) {
        this.soundList = soundList;
    }

    public int getDamageReceived() {
        return damageReceived;
    }

    public void setDamageReceived(int damageReceived) {
        this.damageReceived = damageReceived;
    }
}
