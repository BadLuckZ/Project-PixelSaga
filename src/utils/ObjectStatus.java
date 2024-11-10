// A Class that contains the stats of all objects in the game

package utils;

import java.util.ArrayList;
import java.util.Arrays;

public class ObjectStatus {
    public static final int NOT_MOVE = 0;
    // Normal Arrow
    public static final int NORMAL_ARROW_WIDTH = 81;
    public static final int NORMAL_ARROW_HEIGHT = 21;
    public static final int NORMAL_ARROW_SPEED = 12;

    // Charge Arrow
    public static final int CHARGE_ARROW_WIDTH = 93;
    public static final int CHARGE_ARROW_HEIGHT = 27;
    public static final int CHARGE_ARROW_SPEED = 24;

    // Missile
    public static final int MISSILE_WIDTH = 129;
    public static final int MISSILE_HEIGHT = 27;
    public static final int MISSILE_SPEED = 18;

    // HPDropLarge
    public static final int HP_DROP_LARGE_WIDTH = 45;
    public static final int HP_DROP_LARGE_HEIGHT = 45;
    public static final float HP_DROP_LARGE_HEAL = 35; // heal 35% maxHp

    // HPDropSmall
    public static final int HP_DROP_SMALL_WIDTH = 27;
    public static final int HP_DROP_SMALL_HEIGHT = 36;
    public static final float HP_DROP_SMALL_HEAL = 5; // heal 5% maxHp

    // Symbol
    public static final int SYMBOL_WIDTH = 140;
    public static final int SYMBOL_HEIGHT = 140;
    public static final int PERK_SYMBOL_WIDTH = 96;
    public static final int PERK_SYMBOL_HEIGHT = 99;

    // CoinReward
    public static final int COIN_REWARD_WIDTH = 288;
    public static final int COIN_REWARD_HEIGHT = 114;
    public static final int COIN_REWARD = 200;

    //HPReward
    public static final int HP_REWARD_WIDTH = 252;
    public static final int HP_REWARD_HEIGHT = 144;
    public static final int HP_REWARD_MAX_HP_INCREASE = 25;
    public static final int HP_REWARD_HEAL = 50;

    //PerkChestReward
    public static final int PERK_CHEST_REWARD_WIDTH = 106;
    public static final int PERK_CHEST_REWARD_HEIGHT = 94;

    // Door
    public static final int DOOR_WIDTH = 291;
    public static final int DOOR_HEIGHT = 309;

    // Bomb
    public static final int KLEE_BOMB_WIDTH = 562;
    public static final int KLEE_BOMB_HEIGHT = 400;
    public static final int KLEE_BOMB_HITBOX_X_OFFSET = 165;
    public static final int KLEE_BOMB_HITBOX_Y_OFFSET = 235;
    public static final int KLEE_BOMB_HITBOX_WIDTH = 250;
    public static final int KLEE_BOMB_HITBOX_HEIGHT = 105;
    public static final int BOMB_FRAMES = 10;
    public static final int KLEE_BOMB_FRAMES_PER_SECOND = 12;
    public static final int NUCLEAR_BOMB_WIDTH = 1015;
    public static final int NUCLEAR_BOMB_HEIGHT = 595;
    public static final int NUCLEAR_BOMB_HITBOX_X_OFFSET = 160;
    public static final int NUCLEAR_BOMB_HITBOX_Y_OFFSET = 190;
    public static final int NUCLEAR_BOMB_HITBOX_WIDTH = 670;
    public static final int NUCLEAR_BOMB_HITBOX_HEIGHT = 310;
    public static final int NUCLEAR_BOMB_FRAMES_PER_SECOND = 8;

    //ThrowerBomb
    public static final int THROWER_BOMB_WIDTH = 45;
    public static final int THROWER_BOMB_HEIGHT = 45;
    public static final int THROWER_BOMB_ACTIVATE_WIDTH = 562;
    public static final int THROWER_BOMB_ACTIVATE_HEIGHT = 454;
    public static final int THROWER_BOMB_ACTIVATE_HITBOX_X_OFFSET = 165;
    public static final int THROWER_BOMB_ACTIVATE_HITBOX_Y_OFFSET = 142;
    public static final int THROWER_BOMB_ACTIVATE_HITBOX_WIDTH = 208;
    public static final int THROWER_BOMB_ACTIVATE_HITBOX_HEIGHT = 182;
    public static final int THROWER_BOMB_ACTIVATE_FRAMES = 10;
    public static final int THROWER_BOMB_ACTIVATE_FRAMES_PER_SECOND = 18;

    //IceThrowerBomb
    public static final int THROWER_ICE_BOMB_ACTIVATE_WIDTH = 586;
    public static final int THROWER_ICE_BOMB_ACTIVATE_HEIGHT = 400;
    public static final int THROWER_ICE_BOMB_ACTIVATE_HITBOX_X_OFFSET = 145;
    public static final int THROWER_ICE_BOMB_ACTIVATE_HITBOX_Y_OFFSET = 170;
    public static final int THROWER_ICE_BOMB_ACTIVATE_HITBOX_WIDTH = 280;
    public static final int THROWER_ICE_BOMB_ACTIVATE_HITBOX_HEIGHT = 110;
    public static final int THROWER_ICE_BOMB_ACTIVATE_FRAMES = 10;
    public static final int THROWER_ICE_BOMB_ACTIVATE_FRAMES_PER_SECOND = 18;

    //StompEffect
    public static final int STOMP_EFFECT_WIDTH = 628;
    public static final int STOMP_EFFECT_HEIGHT = 221;
    public static final int STOMP_EFFECT_HITBOX_X_OFFSET = 80;
    public static final int STOMP_EFFECT_HITBOX_Y_OFFSET = 120;
    public static final int STOMP_EFFECT_HITBOX_WIDTH = 460;
    public static final int STOMP_EFFECT_HITBOX_HEIGHT = 80;
    public static final int STOMP_EFFECT_FRAMES = 3;
    public static final int STOMP_EFFECT_FRAMES_PER_SECOND = 12;

    //ShockWave
    public static final int SHOCK_WAVE_WIDTH = 313;
    public static final int SHOCK_WAVE_HEIGHT = 367;
    public static final int SHOCK_WAVE_HITBOX_X_OFFSET = 83;
    public static final int SHOCK_WAVE_HITBOX_Y_OFFSET = 133;
    public static final int SHOCK_WAVE_HITBOX_WIDTH = 130;
    public static final int SHOCK_WAVE_HITBOX_HEIGHT = 195;
    public static final int SHOCK_WAVE_FRAMES = 9;
    public static final int SHOCK_WAVE_FRAMES_PER_SECOND = 12;
    public static final int SHOCK_WAVE_EXTRA_DAMAGE = -60;

    //FIRE
    public static final int FIRE_WIDTH = 1150;
    public static final int FIRE_HEIGHT = 610;
    public static final int FIRE_HITBOX_X_OFFSET = 120;
    public static final int FIRE_HITBOX_Y_OFFSET = 240;
    public static final int FIRE_HITBOX_WIDTH = 710;
    public static final int FIRE_HITBOX_HEIGHT = 225;
    public static final int FIRE_FRAMES = 14;
    public static final int FIRE_FRAMES_PER_SECOND = 10;
    public static final int FIRE_EXTRA_DAMAGE = -85;
    public static final int FIRE_TIME = 4;

    //VendingMachine
    public static final int VENDING_MACHINE_WIDTH = 511;
    public static final int VENDING_MACHINE_HEIGHT = 400;
    public static final int VENDING_MACHINE_FRAMES = 20;
    public static final int VENDING_MACHINE_FRAMES_PER_SECOND = 12;

    //Shroom
    public static final int SHROOM_WIDTH = 94;
    public static final int SHROOM_HEIGHT = 94;
    public static final int SHROOM_FRAMES = 4;
    public static final int SHROOM_FRAMES_PER_SECOND = 8;

    //Merchant
    public static final int MERCHANT_WIDTH = 172;
    public static final int MERCHANT_HEIGHT = 157;
    public static final int MERCHANT_HEAL_PERCENT = 40;
    public static final int MIN_POTION_PRICE = 100;
    public static final int MAX_POTION_PRICE = 120;

    //BlackSmith
    public static final ArrayList<Integer> BLACK_SMITH_WIDTH = new ArrayList<>(Arrays.asList(565,670));
    public static final ArrayList<Integer> BLACK_SMITH_HEIGHT = new ArrayList<>(Arrays.asList(343,577));
    public static final ArrayList<Integer> BLACK_SMITH_HITBOX_X_OFFSET = new ArrayList<>(Arrays.asList(155,110));
    public static final ArrayList<Integer> BLACK_SMITH_HITBOX_Y_OFFSET = new ArrayList<>(Arrays.asList(185,410));
    public static final ArrayList<Integer> BLACK_SMITH_HITBOX_WIDTH = new ArrayList<>(Arrays.asList(260,360));
    public static final ArrayList<Integer> BLACK_SMITH_HITBOX_HEIGHT = new ArrayList<>(Arrays.asList(160,170));

    public static final ArrayList<Integer> BLACK_SMITH_FRAMES = new ArrayList<>(Arrays.asList(4,6));
    public static final ArrayList<Integer> BLACK_SMITH_FRAMES_PER_SECOND = new ArrayList<>(Arrays.asList(15,3));
    public static final ArrayList<Integer> BLACK_SMITH_PAUSE_TIME = new ArrayList<>(Arrays.asList(3,1));
}
