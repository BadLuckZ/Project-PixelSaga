// A Class that contains the stats of all perks in the game

package utils;

public class PerkStatus {
    public final static float PRICE_UPGRADE_PERCENT = 1.5f;
    public static final int PRICE_VARIATION = 25; //+-25%
    public final static int COMMON_PRICE = 150;
    public final static int UNCOMMON_PRICE = 200;
    public final static int RARE_PRICE = 250;

    // BerserkPerk
    public final static float BERSERK_1_CRITERIA = 0.3f;
    public final static float BERSERK_2_1_CRITERIA = 0.4f;
    public final static float BERSERK_2_2_CRITERIA = 0.2f;
    public final static int BERSERK_1_ADDATK = 20;
    public final static int BERSERK_2_1_ADDATK = 25;
    public final static int BERSERK_2_2_ADDATK = 40;

    // DiscountMasterPerk
    public final static float DISCOUNT_NORMAL_FAILCHANCE = 0f;
    public final static float DISCOUNT_2_2_FAILCHANCE = 0.4f;
    public final static int DISCOUNT_1_DISCOUNT = 15;
    public final static int DISCOUNT_2_1_DISCOUNT = 25;
    public final static int DISCOUNT_2_2_DISCOUNT = 50;

    // ExtrovertPerk
    public final static float EXTROVERT_TILES_X = 4f;
    public final static float EXTROVERT_TILES_Y = 0.25f;
    public final static int EXTROVERT_CRITERIA = 2;
    public final static int EXTROVERT_1_ADDATKPERMONSTER = 3;
    public final static int EXTROVERT_2_1_ADDATKPERMONSTER = 5;
    public final static int EXTROVERT_2_2_ADDATKPERMONSTER = 10;
    public final static int EXTROVERT_2_2_MINUSATKEXTRA = 12;

    // FatalAttackPerk
    public final static int FATAL_1_ADDCRITDAMAGE = 20;
    public final static int FATAL_2_1_ADDCRITDAMAGE = 35;
    public final static int FATAL_2_2_ADDCRITDAMAGE = 50;
    public final static int FATAL_2_2_MINUSCRITRATE = 5;

    // FortifyAttackPerk
    public static final int FORTIFY_1_ADDDAMAGEDECREASE = 15;
    public static final int FORTIFY_2_1_ADDDAMAGEDECREASE = 25;
    public static final int FORTIFY_2_2_ADDDAMAGEDECREASE = 10;
    public static final int FORTIFY2_2_ADDATKDELAY = 30;

    // IntrovertPerk
    public final static float INTROVERT_TILES_X = 6f;
    public final static float INTROVERT_TILES_Y = 0.25f;
    public final static int INTROVERT_1_BASEADDATK = 12;
    public final static int INTROVERT_1_MINUSADDATKPERMONSTER = 4;
    public final static int INTROVERT_2_1_BASEADDATK = 18;
    public final static int INTROVERT_2_1_MINUSADDATKPERMONSTER = 3;
    public final static int INTROVERT_2_2_BASEADDATK = 30;
    public final static int INTROVERT_2_2_MINUSADDATKPERMONSTER = 15;

    // JukeMasterPerk
    public final static int JUKE_1_ADDEVADERATE = 15;
    public final static int JUKE_2_1_ADDEVADERATE = 25;
    public final static int JUKE_2_2_ADDEVADERATE = 50;
    public final static int JUKE_2_2_MINUSDAMAGEDECREASE = 40;

    // LuckyManPerk
    public final static float LUCKYMAN_NORMAL_TIERCHANCE = 0.08f;
    public final static float LUCKYMAN_2_1_TIERCHANCE = 0.12f;
    public final static float LUCKYMAN_2_2_DROPCHANCE = 0.1f;

    // PerfectionistPerk
    public final static float PERFECTION_1_CRITERIA = 0.8f;
    public final static float PERFECTION_2_1_CRITERIA = 0.7f;
    public final static float PERFECTION_2_2_CRITERIA = 1f;
    public final static int PERFECTION_1_ADDATK = 10;
    public final static int PERFECTION_2_1_ADDATK = 15;
    public final static int PERFECTION_2_2_ADDATK = 25;

    // PrecisionStrikePerk
    public final static int PRECISION_1_ADDCRITRATE = 10;
    public final static int PRECISION_2_1_ADDCRITRATE = 20;
    public final static int PRECISION_2_2_ADDCRITRATE = 30;
    public final static int PRECISION_2_2_MINUSCRITDAMAGE = 20;

    // RapidFirePerk
    public final static int RAPID_NORMAL_ADDATKSPEED = 15;
    public final static int RAPID_2_1_ADDATKSPEED = 25;
    public final static int RAPID_2_2_ADDMOVEMENTSPEED = 15;

    // ReinforcedPerk
    public final static int REINFORCED_1_ADDDEF = 25;
    public final static int REINFORCED_2_1_ADDDEF = 35;
    public final static int REINFORCED_2_2_ADDDEF = 50;
    public final static int REINFORCED_2_2_MINUSMOVEMENTSPEED = 25;

    // ThornPerk
    public final static float THORN_NORMAL_REFLECTCHANCE = 0.6f;
    public final static float THORN_2_1_REFLECTCHANCE = 1f;
    public final static int THORN_2_2_SLOWTIME = 3;
    public final static int THORN_2_2_SLOW = 80;

    // TreasureHunterPerk
    public final static float TREASURE_NORMAL_REWARDCHANCE = 0.3f;
    public final static float TREASURE_2_1_REWARDCHANCE = 0.5f;
    public final static float TREASURE_2_2_REWARDMULTIPLIER = 1.25f;

    // UndeadPerk
    public final static int UNDEAD_NORMAL_ACTIVATION = 3;
    public final static int UNDEAD_2_1_ACTIVATION = 5;
    public final static int UNDEAD_NORMAL_COOLDOWN = 60;
    public final static int UNDEAD_2_2_COOLDOWN = 30;
    public final static int UNDEAD_2_1_ADDATK = 20;
    public final static int UNDEAD_2_1_ADDATKSPEED = 50;

    // VampirismPerk
    public final static float VAMP_NORMAL_DRAINCHANCE = 0.15f;
    public final static float VAMP_2_2_DRAINCHANCE = 0.25f;
    public final static float VAMP_NORMAL_DRAINPERHIT = 0.01f;
    public final static float VAMP_2_1_DRAINPERHIT = 0.02f;
}
