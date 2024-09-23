// A Class that contains the stats of all classes' stats in the game

package utils;

public class PlayerStatus {
    public final static int START_COINS = 0;
    public final static int MAX_PERK_OBTAIN = 6;
    public final static int LOW_HP = 200; // Dagger, Bow
    public final static int HIGH_HP = 300; // BroadSword
    public final static int LOW_ATK = 20; // Dagger, Bow
    public final static int HIGH_ATK = 28; // BroadSword
    public final static int LOW_DEF = 6; // Dagger and Bow
    public final static int HIGH_DEF = 10; // BroadSword
    public final static float LOW_MOVEMENT_SPEED = 3.5f;
    // tiles per second for BroadSword User
    public final static float MEDIUM_MOVEMENT_SPEED = 4.5f;
    // tiles per second for Bow User
    public final static float HIGH_MOVEMENT_SPEED = 5.5f;
    // tiles per second for Dagger User
    public final static float DAGGER_NORMAL_ATTACK_SPEED_MULTIPLIER = 1;
    // set default attack speed for Dagger (Base = 2 frame = 0.250s)
    public final static float DAGGER_SPECIAL_ATTACK_SPEED_MULTIPLIER = 1.5f;
    public final static float BOW_NORMAL_ATTACK_SPEED_MULTIPLIER = 2.3f;
    // set default attack speed for Bow (Base = 7 frame = 0.875s)
    // 2 mean (7 / 2 = 0.38s)
    public final static float BOW_SPECIAL_ATTACK_SPEED_MULTIPLIER = 1;
    public final static float BROADSWORD_NORMAL_ATTACK_SPEED_MULTIPLIER = 1.7f;
    // set default attack speed for BroadSword (Base = 7 frame = 0.875s)
    // 2 mean (7 / 1.4 = 0.625s)
    public final static float BROADSWORD_SPECIAL_ATTACK_SPEED_MULTIPLIER = 2;
    public final static int LOW_EVADE_RATE = 5;
    public final static int HIGH_EVADE_RATE = 15; // Only Bow User
    public final static int LOW_CRIT_RATE = 5;
    public final static int HIGH_CRIT_RATE = 15; // Only Dagger User
    public final static int LOW_CRIT_DAMAGE = 50;
    public final static int HIGH_CRIT_DAMAGE = 75; // Only Dagger User
    public final static int NORMAL_MAX_DASH = 2;
    public final static int LOW_MAX_DASH = 1;
    public static final float DEFAULT_DASH_DISTANCE = 5;
    public static final int DEFAULT_DASH_TIME = 20;
    public static final float DASH_COOLDOWN = 2;
    public static final float TIME_CAN_SECOND_ATTACK = 0.375f;
    public static final float PLAYER_DAMAGE_COOLDOWN = 0.125f;
}
