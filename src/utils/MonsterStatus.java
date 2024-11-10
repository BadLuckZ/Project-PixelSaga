// A Class that contains the stats of all monsters in the game

package utils;

public class MonsterStatus {
    public static final float SPAWN_TIME = 2.5f;
    public static final int MIN_WALKTIME = 2;
    public static final int MAX_WALKTIME = 5;
    public static final int MONSTER_DROP_VARIATION = 30; // +- 30%
    public static final float TURNING_DELAY = 1.5f; // stand still for 1.5s before turning
    public static final float KLEE_TURNING_DELAY = 0.75f;
    public final static float LOW_MOVEMENT_SPEED = 1.5f;
    // tiles per second for Thrower
    public final static float MEDIUM_MOVEMENT_SPEED = 2.5f;
    // tiles per second for Peasant, Smart, Archer, Bigboy
    public final static float HIGH_MOVEMENT_SPEED = 4f;
    // tiles per second for Klee
    public final static int LOW_EVADE_RATE = 5;
    // Normal Monsters
    public final static int MEDIUM_EVADE_RATE = 10;
    // Bigboy
    public final static float SHORT_VISION = 8f; // 8 tiles
    // Normal Closed Range Monster except Klee
    public final static float MEDIUM_VISION = 12f; // 12 tiles
    // Normal Long Range Monster + Klee
    public final static float CLOSED_RANGE = 0.8f; // 0.8 tile
    // Dagger, SwordShield
    // Peasant, Smart, Shield, Klee
    public final static float MID_RANGE = 2f; // 2 tiles
    // BroadSword
    // Bigboy
    public final static float LONG_RANGE = 8f; // 8 tiles
    // Bow
    // Archer, Thrower
    public final static float GIGEE_DROP_RATE = 0.15f; // 15%
    public final static float BOSS_DROP_RATE = 0.1f; // 10%
    public final static int COIN_DROP_GIGEE = 5;
    public final static int COIN_DROP_BOSS = 300;
    public static final int LOW_KNOCKBACK_CHANCE = 5;
    public static final int HIGH_KNOCKBACK_CHANCE = 60;
    public static final float LOW_KNOCKBACK_DISTANCE = 0.1f;
    public static final float MEDIUM_KNOCKBACK_DISTANCE = 0.15f;
    public static final float HIGH_KNOCKBACK_DISTANCE = 0.2f;
    // ----------------------------------------

    // ----------------------------------------
    // Specific Values for each Monster Type
    public final static int PEASANT_HP = 80;
    public final static int PEASANT_ATK = 24;
    public final static int PEASANT_DEF = 6;
    public final static float PEASANT_ATK_DELAY = 3;
    public final static int KLEE_HP = 30;
    public final static int KLEE_ATK = 50;
    public final static int KLEE_DEF = 0;
    public final static float KLEE_ATK_DELAY = 6;
    public final static int ARCHER_HP = 40;
    public final static int ARCHER_ATK = 20;
    public final static int ARCHER_DEF = 2;
    public final static float ARCHER_ATK_DELAY = 6;
    public final static int THROWER_HP = 40;
    public final static int THROWER_ATK = 20;
    public final static int THROWER_DEF = 2;
    public final static float THROWER_ATK_DELAY = 8;
    public final static int ICE_THROWER_ATK = 10;
    public final static int BIGBOY_HP = 800;
    public final static int BIGBOY_ATK = 50;
    public final static int BIGBOY_DEF = 15;

    // BigBoy
    public final static float BIGBOY_SKILL_ONE_COOLDOWN = 1.5f;
    public final static float BIGBOY_COOLDOWN_AFTER_SKILL_ONE = 1.25f;
    public final static float BIGBOY_SKILL_TWO_COOLDOWN = 6;
    public final static float BIGBOY_COOLDOWN_AFTER_SKILL_TWO = 3.5f; // need to be more than 3.5
    public final static float BIGBOY_SKILL_THREE_COOLDOWN = 6;
    public final static float BIGBOY_COOLDOWN_AFTER_SKILL_THREE = 6;
    public final static float BIGBOY_SKILL_ONE_ATTACK_RANGE = 1.5f; // Stomp feet at range 0 - 2
    public final static float BIGBOY_SKILL_TWO_ATTACK_RANGE = 8; // Jump at range 8 - 12
    public static final float BIGBOY_SKILL_TWO_JUMP_TIME = 1.5f;
    public static final float BIGBOY_SKILL_TWO_MAX_JUMP_DISTANCE = 15;
    public static final int BIGBOY_SKILL_TWO_NUMBERS_OF_SHOCKWAVE = 5;
    public final static float BIGBOY_SKILL_THREE_ATTACK_RANGE = 8; // Flame Thrower at range 2 - 8
    public final static float BIGBOY_SKILL_THREEE_MIN_ATTACK_RANGE = 1;
    public final static float BIGBOY_NUCLEAR_TIME = 1.5f;
}