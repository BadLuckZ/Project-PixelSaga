// A Class that contains Game's constants

package utils;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.Arrays;

public class GameConstants {
    public static int gameWidth;
    public static int gameHeight;
    public static int uiOffset;
    public static int healthWidth;
    public static int healthHeight;
    public static int healthUIWidth;
    public static int healthUIHeight;
    public static double scaleForUI;
    public static double edgeRightOfHealthBar;
    public static double edgeTopOfHealthBar;
    public static final ArrayList<String> ROOM_TYPES = new ArrayList<>(Arrays.asList("HP", "Coin", "PerkChest", "Upgrade"));

    // Initial Player's camera border
    public static double borderL = 0;
    public static double borderR = borderL + gameWidth;

    // Background Songs
    public final static SoundLoader START_SCENE_SONG = new SoundLoader("StartSceneSong", "sfx/sample_startscene_song.mp3");
    public final static SoundLoader CLASS_SELECTION_SCENE_SONG = new SoundLoader("ClassSelectionSceneSong","sfx/sample_classselectionscene_song.mp3");
    public final static SoundLoader FIGHT_SCENE_SONG1 = new SoundLoader("FightSceneSong1", "sfx/sample_fightscene_song1.mp3");
    public final static SoundLoader FIGHT_SCENE_SONG2 = new SoundLoader("FightSceneSong2", "sfx/sample_fightscene_song2.mp3");
    public final static SoundLoader FIGHT_SCENE_SONG3 = new SoundLoader("FightSceneSong3", "sfx/sample_fightscene_song3.mp3");
    public final static SoundLoader FIGHT_SCENE_SONG4 = new SoundLoader("FightSceneSong4", "sfx/sample_fightscene_song4.mp3");
    public final static SoundLoader FIGHT_SCENE_SONG5 = new SoundLoader("FightSceneSong5", "sfx/sample_fightscene_song5.mp3");
    public final static SoundLoader FIGHT_SCENE_SONG6 = new SoundLoader("FightSceneSong6", "sfx/sample_fightscene_song6.mp3");
    public final static SoundLoader MARKET_SCENE_SONG = new SoundLoader("MarketSceneSong", "sfx/sample_marketscene_song.mp3");
    public final static SoundLoader BOSS_SCENE_SONG = new SoundLoader("BossSceneSong", "sfx/sample_bossscene_song.mp3");
    public final static SoundLoader INVENTORY = new SoundLoader("Inventory", "sfx/sample_inventory.mp3");

    //template
    public static ArrayList<Integer> passableTile = new ArrayList<>(Arrays.asList(48,49,50,51,52));
    public static ArrayList<Integer> airTile = new ArrayList<>(Arrays.asList(-9, -4, -3, -2, -1, 11, 97, 98, 99));
    public static ArrayList<Integer> spawnMonsterTile = new ArrayList<>(Arrays.asList(-9, -4, -3, -2, 98));
    public static Font thaleahFatFont = Font.loadFont(ClassLoader.getSystemResourceAsStream("fonts/ThaleahFat.ttf"), 14);
    public static Font thaleahFatFontButton = Font.loadFont(ClassLoader.getSystemResourceAsStream("fonts/ThaleahFat.ttf"), 90);
    public static final Color GREEN_FOR_UI = Color.rgb(0, 144,104);

    // Sound Volume
    public static double soundVolume = 0.05;

    public static void setGameWidth(int gameWidth) {
        GameConstants.gameWidth = gameWidth;
        healthWidth = (int) (0.157 * gameWidth);
        healthUIWidth = (int) (0.238 * gameWidth);
        edgeRightOfHealthBar = 0.968 * healthUIWidth;
    }

    public static void setGameHeight(int gameHeight) {
        GameConstants.gameHeight = gameHeight;
        healthHeight = (int) (0.022 * gameHeight);
        healthUIHeight = (int) (0.15 * gameHeight);
        edgeTopOfHealthBar = 0.755 * healthUIHeight;
        uiOffset = (int) (0.01 * gameHeight);
        scaleForUI = gameHeight / 1440.0;
    }

    public static void stopAllBackgroundSongs() {
        START_SCENE_SONG.stop();
        CLASS_SELECTION_SCENE_SONG.stop();
        FIGHT_SCENE_SONG1.stop();
        FIGHT_SCENE_SONG2.stop();
        FIGHT_SCENE_SONG3.stop();
        FIGHT_SCENE_SONG4.stop();
        FIGHT_SCENE_SONG5.stop();
        FIGHT_SCENE_SONG6.stop();
        MARKET_SCENE_SONG.stop();
        BOSS_SCENE_SONG.stop();
    }
}
