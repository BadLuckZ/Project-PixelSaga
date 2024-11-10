// A Class that contains Animation's constants

package utils;

import java.util.ArrayList;
import java.util.Arrays;

public class AnimationStatus {
    public static double SPAWN_X = 1300;
    public static double SPAWN_Y = 500;
    public static final int START_X = 4;
    public static final int START_Y = 25;
    public static final int DEFAULT_ANIMATION_WIDTH = 184;
    public static final int DEFAULT_ANIMATION_HEIGHT = 184;
    public static final int GAP_WIDTH = 8;
    public static final int GAP_HEIGHT = 8;
    public static final int DEFAULT_HITBOX_WIDTH = 46;
    public static final int DEFAULT_HITBOX_HEIGHT = 92;
    public static final int DEFAULT_HITBOX_X_OFFSET = 71;
    public static final int DEFAULT_HITBOX_Y_OFFSET = 63;

    public static final float SCALE = 0.75f;
    // change to 1 if you use 2k screen and change to 0.75f if you use full hd screen

    public static final int PIXEL_PER_TILE = (int) (96 * SCALE);
    public static final int MOVE_FRAME = 60;
    public static final int ANIMATION_FRAME = 8;
    public static final float MOVE_PER_FRAME = (float) PIXEL_PER_TILE /MOVE_FRAME;
    public static final float JUMP_HEIGHT = 2.5f; // x tiles
    public static final float GRAVITY = 1 * SCALE;
    public static final float GRAVITY_FOR_OBJECT = GRAVITY / 3;
    public static final double JUMP_VELOCITY = -14 * Math.sqrt(JUMP_HEIGHT)  * SCALE;
    // number of frames for each animation and each entity
    public static final int JUMP_FRAME = 1;
    public static final ArrayList<String> DASH_AND_JUMP_WHEN_DEATH_MONSTER = new ArrayList<>(Arrays.asList("PeasantGiGee", "ArcherGiGee", "ThrowerGiGee", "IceThrowerGiGee"));

    // Anonymous
    public static final int ANONYMOUS_STAND_STILL_FRAMES = 6;
    public static final int ANONYMOUS_WALK_FRAMES = 5;

    // Bow
    public static final int BOW_STAND_STILL_FRAMES = 6;
    public static final int BOW_WALK_FRAMES = 7;
    public static final int BOW_NORMAL_ATTACK_FRAMES = 7;
    public static final int BOW_SPECIAL_ATTACK_FRAMES = 7;
    public static final int BOW_DASH_FRAME = 1;
    public static final int BOW_DEAD_FRAMES = 0;

    // Bow special attack
    public static final int BOW_SPECIAL_ATTACK_CHARGE_TIME = 4; // 4 frames
    public static final float BOW_SPECIAL_ATTACK_COOLDOWN = 6;
    public static final int BOW_SPECIAL_ATTACK_DAMAGE = 30; //damage x1.20
    public static final int BOW_SPECIAL_ATTACK_DAMAGE_INCREASE_PER_FRAME = 8; // more charge time, more damage
    public static final int BOW_SPECIAL_ATTACK_MAX_DAMAGE_INCREASE = 120; // max dmg increase from charge time

    // BroadSword
    public static final int BROADSWORD_STAND_STILL_FRAMES = 6;
    public static final int BROADSWORD_WALK_FRAMES = 8;
    public static final int BROADSWORD_NORMAL_ATTACK_FRAMES = 15;
    public static final int BROADSWORD_SPECIAL_ATTACK_FRAMES = 6;
    public static final int BROADSWORD_DASH_FRAMES = 5;
    public static final int BROADSWORD_DEAD_FRAMES = 0;
    public static final int BROADSWORD_ANIMATION_WIDTH = 472;
    public static final int BROADSWORD_ANIMATION_HEIGHT = 400;
    public static final int BROADSWORD_HITBOX_WIDTH = 54;
    public static final int BROADSWORD_HITBOX_HEIGHT = 99;
    public static final int BROADSWORD_HITBOX_X_OFFSET = 210;
    public static final int BROADSWORD_HITBOX_Y_OFFSET = 173;

    // BroadSword special attack
    public static final int BROADSWORD_SPECIAL_ATTACK_JUMP_HEIGHT = -15;
    public static final float BROADSWORD_SPECIAL_ATTACK_COOLDOWN = 8; // 8 seconds
    public static final int BROADSWORD_SPECIAL_ATTACK_DAMAGE = 60;

    // Dagger
    public static final int DAGGER_STAND_STILL_FRAMES = 6;
    public static final int DAGGER_WALK_FRAMES = 3;
    public static final int DAGGER_NORMAL_ATTACK_FRAMES = 4;
    public static final int DAGGER_SPECIAL_ATTACK_FRAMES = 5;
    public static final int DAGGER_DASH_FRAMES = 6;
    public static final int DAGGER_DEAD_FRAMES = 0;
    // Dagger special attack
    public static final float DAGGER_SPECIAL_ATTACK_WARP_DISTANCE = 8;
    public static final float DAGGER_SPECIAL_ATTACK_COOLDOWN = 6;
    public static final int DAGGER_SPECIAL_ATTACK_DAMAGE = 60;
    public static final int DAGGER_SPECIAL_ATTACK_CRIT_CHANCE = 40; // increase 40% crit rate when use special attack
    public static final float DAGGER_SPECIAL_ATTACK_CRIT_BUFF_DURATION = 2;

    // Monster
    public static final int SPAWN_FRAMES = 15;
    // PeasantGiGee
    public static final int PEASANTGIGEE_STAND_STILL_FRAMES = 6;
    public static final int PEASANTGIGEE_WALK_FRAMES = 5;
    public static final int PEASANTGIGEE_NORMAL_ATTACK_FRAMES = 11;
    public static final int PEASANTGIGEE_DEAD_FRAMES = 10;

    // KleeGiGee
    public static final int KLEEGIGEE_STAND_STILL_FRAMES = 8;
    public static final int KLEEGIGEE_WALK_FRAMES = 8;
    public static final int KLEEGIGEE_NORMAL_ATTACK_FRAMES = 0;
    public static final int KLEEGIGEE_DEAD_FRAMES = 6;
    public static final int KLEEGIGEE_SPEED_INCREASE = 20; // adjust speed during bomb activating
    public static final float KLEEGIGEE_BOMB_TIME = 1.25f;

    // ArcherGiGee
    public static final int ARCHERGIGEE_STAND_STILL_FRAMES = 6;
    public static final int ARCHERGIGEE_WALK_FRAMES = 5;
    public static final int ARCHERGIGEE_NORMAL_ATTACK_FRAMES = 22;
    public static final int ARCHERGIGEE_DEAD_FRAMES = 10;

    // ThrowerGiGee
    public static final int THROWERGIGEE_STAND_STILL_FRAMES = 4;
    public static final int THROWERGIGEE_WALK_FRAMES = 4;
    public static final int THROWERGIGEE_NORMAL_ATTACK_FRAMES = 17;
    public static final int THROWERGIGEE_DEAD_FRAMES = 8;
    public static final int THROWERGIGEE_HITBOX_WIDTH = 100;
    public static final int THROWERGIGEE_HITBOX_X_OFFSET = 45;
    public static final float THROWERGIGEE_THROW_TIME = 2;

    // IceThrowerGiGee
    public static final int ICETHROWERGIGEE_STAND_STILL_FRAMES = 4;
    public static final int ICETHROWERGIGEE_WALK_FRAMES = 4;
    public static final int ICETHROWERGIGEE_NORMAL_ATTACK_FRAMES = 17;
    public static final int ICETHROWERGIGEE_DEAD_FRAMES = 8;
    public static final float ICETHROWERGIGEE_THROW_TIME = 1.5f;
    public static final int ICETHROWERGIGEE_SLOW_AMOUNT = 40; // decrease movement speed 40%
    public static final int ICETHROWERGIGEE_MAX_SLOW_AMOUNT = 80; // decrease movement speed 40%
    public static final int ICETHROWERGIGEE_SLOW_DURATION = 2;

    // BigBoy
    public static final int BIGBOY_STAND_STILL_FRAMES = 1;
    public static final int BIGBOY_WALK_FRAMES = 10;
    public static final int BIGBOY_SKILL_ONE_FRAMES = 10;
    public static final ArrayList<Integer> BIGBOY_SKILL_ONE_FRAMES_HOLD = new ArrayList<>(Arrays.asList(1,3,3,5,1,1,1,2,1,1));
    public static final float BIGBOY_SKILL_ONE_ANIMATION_SPEED = 2;
    public static final int BIGBOY_SKILL_TWO_FRAMES = 7;
    public static final ArrayList<Integer> BIGBOY_SKILL_TWO_FRAMES_HOLD = new ArrayList<>(Arrays.asList(1,3,3,1,1,1,1));
    public static final float BIGBOY_SKILL_TWO_ANIMATION_SPEED = 1.5f;
    public static final int BIGBOY_SKILL_THREE_STAND_FRAMES = 5;
    public static final int BIGBOY_SKILL_THREE_WALK_FRAMES = 10;
    public static final int BIGBOY_DEAD_FRAMES = 1;
    public static final int BIGBOY_ANIMATION_WIDTH = 394;
    public static final int BIGBOY_ANIMATION_HEIGHT = 304;
    public static final int BIGBOY_HITBOX_WIDTH = 212;
    public static final int BIGBOY_HITBOX_HEIGHT = 207;
    public static final int BIGBOY_HITBOX_X_OFFSET = 68;
    public static final int BIGBOY_HITBOX_Y_OFFSET = 91;
}
