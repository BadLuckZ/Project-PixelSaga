// BaseClass for player's classes in the game (Anonymous, Bow, BroadSword, Dagger)

package entities.bases;

import enums.FaceDirection;
import java.util.ArrayList;

import static enums.FaceDirection.RIGHT;
import static utils.AnimationStatus.*;
import static utils.MonsterStatus.CLOSED_RANGE;
import static utils.PlayerStatus.*;

public abstract class BaseClass {
    private int hp;
    private int atk;
    private int def;
    private float movementSpeed;
    private int evadeRate;
    private int damageIncrease;
    private int damageDecrease;
    private FaceDirection faceDirection;
    private float attackRange;
    private float attackSpeed;
    private int critRate;
    private int critDamage;
    private String imgString;
    private String uiString;
    private String inventoryUIString;
    private int standStillFrames;
    private int walkFrames;
    private int normalAttackFrames;
    private int specialAttackFrames;
    private float specialAttackCoolDown;
    private int dashFrames;
    private int deadFrames;
    private int attackCombo;
    private float timeCanSecondAttack;
    private ArrayList<Integer> frameMakeDamageNormalAttack = new ArrayList<>();
    private ArrayList<Integer> frameMakeDamageSpecialAttack = new ArrayList<>();
    private float knockbackDistance;
    private int maxDash;
    private int animationWidth;
    private int animationHeight;
    private int hitBoxWidth;
    private int hitBoxHeight;
    private int hitBoxXOffset;
    private int hitBoxYOffset;

    public BaseClass() {
        setCommonStatus();
    }

    public void setCommonStatus() {
        setHp(LOW_HP);
        setAtk(LOW_ATK);
        setDef(LOW_DEF);
        setMovementSpeed(LOW_MOVEMENT_SPEED);
        setEvadeRate(LOW_EVADE_RATE);
        setDamageIncrease(0);
        setDamageDecrease(0);
        setFaceDirection(RIGHT);
        setAttackRange(CLOSED_RANGE);
        setCritRate(LOW_CRIT_RATE);
        setCritDamage(LOW_CRIT_DAMAGE);
        setMaxDash(NORMAL_MAX_DASH);
        setAnimationWidth(DEFAULT_ANIMATION_WIDTH);
        setAnimationHeight(DEFAULT_ANIMATION_HEIGHT);
        setHitBoxWidth(DEFAULT_HITBOX_WIDTH);
        setHitBoxHeight(DEFAULT_HITBOX_HEIGHT);
        setHitBoxXOffset(DEFAULT_HITBOX_X_OFFSET);
        setHitBoxYOffset(DEFAULT_HITBOX_Y_OFFSET);setImgString("characters/" + getClass().getSimpleName() + "_Sprite.png");
        setUiString("etcUI/" + getClass().getSimpleName() + "_UI.png");
        setInventoryUIString("etcUI/" + getClass().getSimpleName() + "Inventory_UI.png");
    }

    public abstract void setUniqueStatus();

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        if (hp <= 0) hp = 0;
        this.hp = hp;
    }

    public int getAtk() {
        return atk;
    }

    public void setAtk(int atk) {
        if (atk <= 1) atk = 1;
        this.atk = atk;
    }

    public int getDef() {
        return def;
    }

    public void setDef(int def) {
        if (def <= 0) def = 0;
        this.def = def;
    }

    public float getMovementSpeed() {
        return movementSpeed;
    }

    public void setMovementSpeed(float movementSpeed) {
        if (movementSpeed <= 0) movementSpeed = 0;
        this.movementSpeed = movementSpeed;
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

    public float getAttackSpeed() {
        return attackSpeed;
    }

    public void setAttackSpeed(float attackSpeed) {
        if (attackSpeed <= 0) attackSpeed = 0;
        this.attackSpeed = attackSpeed;
    }

    public int getCritRate() {
        return critRate;
    }

    public void setCritRate(int critRate) {
        if (critRate <= 0) critRate = 0;
        this.critRate = critRate;
    }

    public int getCritDamage() {
        return critDamage;
    }

    public void setCritDamage(int critDamage) {
        if (critDamage <= 0) critDamage = 0;
        this.critDamage = critDamage;
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

    public int getSpecialAttackFrames() {
        return specialAttackFrames;
    }

    public void setSpecialAttackFrames(int specialAttackFrames) {
        this.specialAttackFrames = specialAttackFrames;
    }

    public int getDeadFrames() {
        return deadFrames;
    }

    public void setDeadFrames(int deadFrames) {
        this.deadFrames = deadFrames;
    }

    public int getAttackCombo() {
        return attackCombo;
    }

    public void setAttackCombo(int attackCombo) {
        this.attackCombo = attackCombo;
    }

    public float getTimeCanSecondAttack() {
        return timeCanSecondAttack;
    }

    public void setTimeCanSecondAttack(float timeCanSecondAttack) {
        this.timeCanSecondAttack = timeCanSecondAttack;
    }

    public ArrayList<Integer> getFrameMakeDamageNormalAttack() {
        return frameMakeDamageNormalAttack;
    }

    public ArrayList<Integer> getFrameMakeDamageSpecialAttack() {
        return frameMakeDamageSpecialAttack;
    }

    public float getKnockbackDistance() {
        return knockbackDistance;
    }

    public void setKnockbackDistance(float knockbackDistance) {
        this.knockbackDistance = knockbackDistance;
    }

    public String getUiString() {
        return uiString;
    }

    public void setUiString(String uiString) {
        this.uiString = uiString;
    }

    public String getInventoryUIString() {
        return inventoryUIString;
    }

    public void setInventoryUIString(String inventoryUIString) {
        this.inventoryUIString = inventoryUIString;
    }
    public int getMaxDash() {
        return maxDash;
    }

    public void setMaxDash(int maxDash) {
        this.maxDash = maxDash;
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

    public int getDashFrames() {
        return dashFrames;
    }

    public void setDashFrames(int dashFrames) {
        this.dashFrames = dashFrames;
    }

    public float getSpecialAttackCoolDown() {
        return specialAttackCoolDown;
    }

    public void setSpecialAttackCoolDown(float specialAttackCoolDown) {
        this.specialAttackCoolDown = specialAttackCoolDown;
    }
}
