// A Class containing a lot of methods used in many classes

package utils;

import entities.bases.BaseEntity;
import entities.monsters.*;
import entities.bases.BaseMonster;
import entities.player.Player;
import entities.player.classes.BroadSword;
import entities.player.classes.Dagger;
import entities.bases.BasePerk;
import entities.player.perks.*;
import enums.FaceDirection;
import enums.States;
import enums.Tier;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import objects.*;
import scenes.BaseScene;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static entities.bases.BaseEntity.ATTACK_VARIATION;
import static entities.player.Player.resetPlayer;
import static entities.player.perks.UndeadPerk.getUndeadSound;
import static enums.FaceDirection.RIGHT;
import static enums.Rarity.*;
import static enums.Tier.*;
import static javafx.scene.paint.Color.BLACK;
import static javafx.scene.text.FontWeight.BOLD;
import static scenes.BossScene.resetBossScene;
import static scenes.ClassSelectionScene.resetClassSelectionScene;
import static scenes.FightScene.resetFightScene;
import static scenes.FightScene.setNumberOfFightScene;
import static scenes.MarketScene.resetMarketScene;
import static scenes.StartScene.resetStartScene;
import static threads.BotLogicThread.addWave;
import static threads.BotLogicThread.setHasCreatedText;
import static utils.AnimationStatus.*;
import static utils.GameConstants.*;
import static utils.PerkStatus.*;
import static utils.PlayerStatus.*;

public class HelperMethods {
    private static Random random = new Random();
    private final static SoundLoader PLAYER_WALK = new SoundLoader("PlayerWalk", "sfx/sample_walk.mp3");
    private final static SoundLoader PLAYER_DASH = new SoundLoader("PlayerDash", "sfx/sample_dash.wav");
    private final static SoundLoader PLAYER_JUMP1 = new SoundLoader("PlayerJump1", "sfx/sample_jump1.mp3");
    private final static SoundLoader PLAYER_JUMP2 = new SoundLoader("PlayerJump2", "sfx/sample_jump2.mp3");
    private final static SoundLoader PLAYER_COLLECT = new SoundLoader("PlayerCollect", "sfx/sample_collect.mp3");
    private final static SoundLoader PLAYER_UPGRADE = new SoundLoader("PlayerUpgrade", "sfx/sample_upgrade.mp3");
    private final static SoundLoader PLAYER_BEING_HIT = new SoundLoader("PlayerHurt", "sfx/sample_player_hurt.mp3");
    private final static SoundLoader PLAYER_DODGE = new SoundLoader("PlayerDodge", "sfx/sample_miss.mp3");
    public final static SoundLoader PLAYER_BUY_SUCCESS = new SoundLoader("BuyingSuccess", "sfx/sample_buying_success.mp3");
    public final static SoundLoader PLAYER_BUY_FAIL = new SoundLoader("BuyingFail", "sfx/sample_buying_fail.mp3");
    private final static SoundLoader PLAYER_BUY_MISS = new SoundLoader("BuyingMiss", "sfx/sample_buying_miss.wav");
    private final static SoundLoader PLAYER_BRUH = new SoundLoader("PlayerBruh", "sfx/sample_bruh.mp3");
    private final static SoundLoader PLAYER_DEAD = new SoundLoader("PlayerDead", "sfx/sample_player_dead.mp3");
    private final static SoundLoader MONSTER_BEING_HIT = new SoundLoader("MonsterHurt", "sfx/sample_monster_hurt.mp3");
    private final static SoundLoader MONSTER_DEAD = new SoundLoader("MonsterDead", "sfx/sample_monster_dead.mp3");
    private final static SoundLoader MONSTER_DODGE = new SoundLoader("MonsterDodge", "sfx/sample_miss.mp3");
    private final static SoundLoader BOSS_FIRE = new SoundLoader("BossFire", "sfx/sample_flame.mp3");
    private final static SoundLoader BOSS_STOMP_EFFECT = new SoundLoader("BossStomping", "sfx/sample_stomp.mp3");
    private final static SoundLoader DAGGER_COMBO_ATTACK1 = new SoundLoader("DaggerComboAttack1", "sfx/sample_dagger_combo1.mp3");
    private final static SoundLoader DAGGER_COMBO_ATTACK2 = new SoundLoader("DaggerComboAttack2", "sfx/sample_dagger_combo2.mp3");
    private final static SoundLoader BROADSWORD_ATTACK = new SoundLoader("BroadSwordAttack", "sfx/sample_broadsword_attack.mp3");
    private final static SoundLoader BROADSWORD_STOMP_EFFECT = new SoundLoader("BroadSwordStomping", "sfx/sample_stomp.mp3");
    private final static SoundLoader NORMAL_ARROW_ATTACK = new SoundLoader("NormalArrowAttack", "sfx/sample_normal_bow_attack.mp3");
    private final static SoundLoader CHARGE_ARROW_CHARGE = new SoundLoader("ChargeArrowCharging", "sfx/sample_charge_bow_charge.mp3");
    private final static SoundLoader CHARGE_ARROW_ATTACK = new SoundLoader("ChargeArrowAttack", "sfx/sample_charge_bow_attack.mp3");
    private final static SoundLoader MISSILE_ATTACK = new SoundLoader("MissileAttack", "sfx/sample_missile_attack.mp3");
    private final static SoundLoader THROWERBOMB_ATTACK = new SoundLoader("ThrowerBombAttack", "sfx/sample_throwerbomb.wav");
    private final static SoundLoader KLEE_BOMB = new SoundLoader("KleeBomb", "sfx/sample_kleebomb.mp3");
    private final static SoundLoader NUCLEAR_BOMB = new SoundLoader("NuclearBomb", "sfx/sample_nuclearbomb.mp3");
    private final static SoundLoader GET_HEAL = new SoundLoader("GetHeal", "sfx/sample_getheal.mp3");
    private final static SoundLoader MERCHANT_SELL = new SoundLoader("MerchantSell", "sfx/sample_merchant.mp3");
    private final static SoundLoader PERKCHEST_GET = new SoundLoader("PerkChestOpen", "sfx/sample_perkchest.mp3");
    private final static SoundLoader BASEREWARD_GET = new SoundLoader("BaseRewardGet", "sfx/sample_basereward.mp3");
    private final static SoundLoader DOOR_OPEN = new SoundLoader("DoorOpen", "sfx/sample_dooropen.mp3");

    public static int randomInteger(int start, int end) {
        Random random = new Random();
        return random.nextInt(start, end + 1);
    }

    public static float randomFloat(float start, float end) {
        Random random = new Random();
        return random.nextFloat(start, end);
    }

    public static float randomChance() {
        return random.nextFloat(0, 1);
    }

    public static int randomIntegerUsingPercent(int value, int percent) {
        int lowestValue = finalValuePercent(value, -percent);
        int highestValue = finalValuePercent(value, percent);
        return randomInteger(lowestValue, highestValue);
    }

    public static int finalValuePercent(int base, int percent) {
        return (int) (base + base * (float) percent / 100);
    }

    public static float finalValuePercentFloat(float base, int percent) {
        return base + base * (float) (percent) / 100;
    }

    public static int calculateActionAttack(BaseEntity attacker, BaseEntity attacked, boolean isCrit, int extraDamagePercent) {
        int atkAttacker = finalValuePercent(attacker.getBaseAtk(), attacker.getAddAtk());
        int defAttacked = finalValuePercent(attacked.getBaseDef(), attacked.getAddDef());
        int damage = atkAttacker - defAttacked;
        if (damage <= 0) return 1;
        int increaseDmg = attacker.getDamageIncrease() - attacked.getDamageDecrease();
        if (isCrit) {
            if (attacker instanceof Player) {
                Player player = (Player) attacker;
                damage = finalValuePercent(damage, player.getCritDamage());
            }
        }
        damage = (int) (damage * (1 + (double) extraDamagePercent / 100));
        return randomIntegerUsingPercent(
                finalValuePercent(damage, increaseDmg), ATTACK_VARIATION);
    }

    public static int calculateReflectionAttack(int damageReflect, BaseMonster attacked) {
        int defAttacked = finalValuePercent(attacked.getBaseDef(), attacked.getAddDef());
        int damage = damageReflect - defAttacked;
        if (damage <= 0) return 1;
        return randomIntegerUsingPercent(damage, ATTACK_VARIATION);
    }

    public static void removeFromUnobtain(Player player, BasePerk removePerk) {
        for (BasePerk perk : player.getNotObtainPerks()) {
            if (perk.toString().equals(removePerk.toString())) {
                player.getNotObtainPerks().remove(perk);
                break;
            }
        }
    }

    public static ArrayList<BasePerk> generateAllPerks(Player player) {
        ArrayList<BasePerk> perks = new ArrayList<>();
        perks.add(new DiscountMasterPerk(player, TIER1));
        perks.add(new ExtrovertPerk(player, TIER1));
        perks.add(new IntrovertPerk(player, TIER1));
        perks.add(new LuckyManPerk(player, TIER1));
        perks.add(new ThornPerk(player, TIER1));
        perks.add(new TreasureHunterPerk(player, TIER1));
        perks.add(new UndeadPerk(player, TIER1));
        perks.add(new VampirismPerk(player, TIER1));
        perks.add(new BerserkPerk(player, TIER1));
        perks.add(new FatalAttackPerk(player, TIER1));
        perks.add(new FortifyPerk(player, TIER1));
        perks.add(new JukeMasterPerk(player, TIER1));
        perks.add(new PerfectionistPerk(player, TIER1));
        perks.add(new PrecisionStrikePerk(player, TIER1));
        perks.add(new RapidFirePerk(player, TIER1));
        perks.add(new ReinforcedPerk(player, TIER1));
        return perks;
    }

    public static Button createButton(String text) {
        return createButton(text, null, BLACK);
    }

    public static Button createButton(String text, Color bgColor, Color textColor) {
        Button button = new Button(text);
        button.setFont(thaleahFatFontButton);
        button.setBackground(Background.fill(bgColor));
        button.setTextFill(textColor);
        return button;
    }

    public static Text createTextForInventory(String msg, int fontSize, Color color,
                                              double posX, double posY) {
        Text text = new Text(msg);
        text.setFill(color);
        text.setFont(thaleahFatFont);
        text.setStyle("-fx-font-size: " + fontSize + ";");
        text.setTranslateX(posX);
        text.setTranslateY(posY);
        return text;
    }

    public static Text createText(String msg, int fontSize, Color color) {
        return createText(msg, fontSize, color, BOLD);
    }

    public static Text createText(String msg, int fontSize, Color fontColor, FontWeight fontWeight) {
        Text text = new Text(msg);
        text.setFill(fontColor);
        text.setFont(thaleahFatFont);
        text.setStyle("-fx-font-size: " + fontSize + ";");
        return text;
    }


    public static boolean canMoveHere(BaseEntity entity, double x, double y, double width, double height, int[][] lvlData) {
        if (entity.getAnimation().getyVelocity() > 0) {
            return !isSolid(entity, x, y + height, lvlData) &&
                    !isSolid(entity, x + width, y + height, lvlData);
        } else {
            return !isSolid(entity, x, y, lvlData) &&
                    !isSolid(entity, x, y + height / 2, lvlData) &&
                    !isSolid(entity, x + width, y, lvlData) &&
                    !isSolid(entity, x + width, y + height / 2, lvlData) &&
                    !isSolid(entity, x, y + height, lvlData) &&
                    !isSolid(entity, x + width, y + height, lvlData);
        }
    }

    private static boolean isSolid(BaseEntity entity, double x, double y, int[][] lvlData) {
        if (x < 0 || x >= lvlData[0].length * PIXEL_PER_TILE)
            return true;
        if (y < 0 || y >= lvlData.length * PIXEL_PER_TILE)
            return true;

        double xIndex = x / PIXEL_PER_TILE;
        double yIndex = y / PIXEL_PER_TILE;
        double percentOfTile = y % PIXEL_PER_TILE;

        int value = lvlData[(int) yIndex][(int) xIndex];

        if (!airTile.contains(value)) {
            if (passableTile.contains(value)) {
                if (entity.getAnimation().getyVelocity() < 0 ||
                        percentOfTile > 1) {
                    return false;
                }

            }
            // player spawn at -1, monster spawn at -2,
            // background winning spawn at 99
            return true;
        }
        return false;
    }

    public static boolean canMoveHereForObject(double x, double y, double width, double height, int[][] lvlData) {
        return !isSolidForObject(x, y, lvlData) &&
                !isSolidForObject(x + width, y, lvlData) &&
                !isSolidForObject(x, y + height, lvlData) &&
                !isSolidForObject(x + width, y + height, lvlData);
    }

    private static boolean isSolidForObject(double x, double y, int[][] lvlData) {
        if (x < 0 || x >= lvlData[0].length * PIXEL_PER_TILE)
            return true;
        if (y < 0 || y > lvlData.length * PIXEL_PER_TILE)
            return true;

        double xIndex = x / PIXEL_PER_TILE;
        double yIndex = y / PIXEL_PER_TILE;

        int value = lvlData[(int) yIndex][(int) xIndex];

        if (!airTile.contains(value)) {
            // player spawn at -1, monster spawn at -2
            // background winning spawn at 99
            return true;
        }
        return false;
    }

    public static void spawnPlayer(Player player, int[][] lvlData) {
        boolean canFindPlayer = false;
        for (int y = 0; y < lvlData.length; y++) {
            if (canFindPlayer) {
                break;
            }
            for (int x = 0; x < lvlData[0].length; x++) {
                int value = lvlData[y][x];
                if (value == -1) {
                    SPAWN_X = x * PIXEL_PER_TILE - player.getAnimation().getFitWidth() / 2;
                    SPAWN_Y = y * PIXEL_PER_TILE - player.getHitBoxYOffset() * SCALE - (double) PIXEL_PER_TILE / 4;
                    player.getAnimation().setSpawnPoint(player, SPAWN_X, SPAWN_Y);
                    canFindPlayer = true;
                    break;
                }
            }
        }
    }

    public static int getTypeOfTile(double x, double y, int[][] lvlData) {
        int xIndex = (int) (x / PIXEL_PER_TILE);
        int yIndex = (int) (y / PIXEL_PER_TILE);
        if (xIndex >= lvlData[0].length) xIndex = lvlData[0].length - 1;
        if (xIndex <= 0) xIndex = 0;
        if (yIndex >= lvlData.length) yIndex = lvlData.length - 1;
        if (yIndex <= 0) yIndex = 0;
        return lvlData[yIndex][xIndex];
    }

    public static void spawnMonsters(int[][] lvlData, BaseScene baseScene) {
        addWave();
        for (int y = 0; y < lvlData.length - 1; y++) {
            for (int x = 0; x < lvlData[0].length; x++) {
                int value = lvlData[y][x];
                if (spawnMonsterTile.contains(value)) {
                    double xPos = x * PIXEL_PER_TILE;
                    double yPos = y * PIXEL_PER_TILE - (DEFAULT_ANIMATION_HEIGHT - PIXEL_PER_TILE);
                    float chance = randomChance();
                    if (value == -2 || value == 98) {
                        if (chance < 0.2) {
                            spawnMonster(baseScene.getPlayer(), "PeasantGiGee", baseScene, xPos, yPos);
                        } else if (chance < 0.4) {
                            spawnMonster(baseScene.getPlayer(), "ArcherGiGee", baseScene, xPos, yPos);
                        } else if (chance < 0.6) {
                            spawnMonster(baseScene.getPlayer(), "KleeGiGee", baseScene, xPos, yPos);
                        } else if (chance < 0.8) {
                            spawnMonster(baseScene.getPlayer(), "ThrowerGiGee", baseScene, xPos, yPos);
                        } else {
                            spawnMonster(baseScene.getPlayer(), "IceThrowerGiGee", baseScene, xPos, yPos);
                        }
                    } else if (value == -3) {
                        if (chance < 0.5) {
                            spawnMonster(baseScene.getPlayer(), "PeasantGiGee", baseScene, xPos, yPos);
                        } else {
                            spawnMonster(baseScene.getPlayer(), "KleeGiGee", baseScene, xPos, yPos);
                        }
                    } else if (value == -4) {
                        if (chance < 0.33) {
                            spawnMonster(baseScene.getPlayer(), "ArcherGiGee", baseScene, xPos, yPos);
                        } else if (chance < 0.66) {
                            spawnMonster(baseScene.getPlayer(), "ThrowerGiGee", baseScene, xPos, yPos);
                        } else {
                            spawnMonster(baseScene.getPlayer(), "IceThrowerGiGee", baseScene, xPos, yPos);
                        }
                    } else if (value == -9) {
                        xPos = x * PIXEL_PER_TILE;
                        yPos = y * PIXEL_PER_TILE - (BIGBOY_ANIMATION_HEIGHT - PIXEL_PER_TILE);
                        spawnMonster(baseScene.getPlayer(), "BigBoy", baseScene, xPos, yPos);
                    }
                }
            }
        }
    }

    private static void spawnMonster(Player player, String monsterType, BaseScene baseScene,
                                     double xPos, double yPos) {
        BaseMonster monster = new PeasantGiGee(xPos, yPos);
        if (monsterType == "ArcherGiGee") {
            monster = new ArcherGiGee(xPos, yPos);
        } else if (monsterType == "KleeGiGee") {
            monster = new KleeGiGee(xPos, yPos);
        } else if (monsterType == "ThrowerGiGee") {
            monster = new ThrowerGiGee(xPos, yPos);
        } else if (monsterType == "IceThrowerGiGee") {
            monster = new IceThrowerGiGee(xPos, yPos);
        } else if (monsterType == "BigBoy") {
            monster = new BigBoy(xPos, yPos);
        }
        if (hasPerk(player, new LuckyManPerk(player, TIER2_2), true)) {
            monster.setDropRate(monster.getDropRate() + LUCKYMAN_2_2_DROPCHANCE);
        }
        baseScene.getPlayer().getMonstersInStage().add(monster);
        BaseMonster finalMonster = monster;
        Platform.runLater(() -> baseScene.getChildren().add(finalMonster.getAnimation()));
    }

    public static ArrayList<SoundLoader> loadEntitiesSound(BaseEntity baseEntity) {
        ArrayList<SoundLoader> soundTracks = new ArrayList<>();
        if (baseEntity instanceof Player) {
            soundTracks.add(PLAYER_WALK);
            soundTracks.add(PLAYER_DASH);
            soundTracks.add(PLAYER_JUMP1);
            soundTracks.add(PLAYER_JUMP2);
            soundTracks.add(PLAYER_COLLECT);
            soundTracks.add(PLAYER_UPGRADE);
            soundTracks.add(PLAYER_BEING_HIT);
            soundTracks.add(PLAYER_DEAD);
            soundTracks.add(PLAYER_DODGE);
            if (((Player) baseEntity).getPlayerClass() instanceof Dagger) {
                soundTracks.add(DAGGER_COMBO_ATTACK1);
                soundTracks.add(DAGGER_COMBO_ATTACK2);
            } else if (((Player) baseEntity).getPlayerClass() instanceof BroadSword) {
                soundTracks.add(BROADSWORD_ATTACK);
                soundTracks.add(BROADSWORD_STOMP_EFFECT);
            }
            soundTracks.add(PLAYER_BUY_SUCCESS);
            soundTracks.add(PLAYER_BUY_FAIL);
            soundTracks.add(PLAYER_BUY_MISS);
            soundTracks.add(PLAYER_BRUH);
        } else {
            soundTracks.add(MONSTER_BEING_HIT);
            soundTracks.add(MONSTER_DEAD);
            soundTracks.add(MONSTER_DODGE);
            if (baseEntity instanceof BigBoy) {
                soundTracks.add(BOSS_STOMP_EFFECT);
            }
        }
        return soundTracks;
    }

    public static ArrayList<SoundLoader> loadObjectsSound(BaseObject baseObject) {
        ArrayList<SoundLoader> soundTracks = new ArrayList<>();
        if (baseObject instanceof NormalArrow) {
            soundTracks.add(NORMAL_ARROW_ATTACK);
        } else if (baseObject instanceof ChargeArrow) {
            soundTracks.add(CHARGE_ARROW_CHARGE);
            soundTracks.add(CHARGE_ARROW_ATTACK);
        } else if (baseObject instanceof Missile) {
            soundTracks.add(MISSILE_ATTACK);
        } else if (baseObject instanceof Fire) {
            soundTracks.add(BOSS_FIRE);
        } else if (baseObject instanceof HPDropSmall || baseObject instanceof HPDropLarge) {
            soundTracks.add(GET_HEAL);
        } else if (baseObject instanceof ThrowerBomb) {
            soundTracks.add(THROWERBOMB_ATTACK);
        } else if (baseObject instanceof Merchant) {
            soundTracks.add(MERCHANT_SELL);
        } else if (baseObject instanceof PerkChestReward) {
            soundTracks.add(PERKCHEST_GET);
        } else if (baseObject instanceof BaseReward) {
            soundTracks.add(BASEREWARD_GET);
        } else if (baseObject instanceof Door) {
            soundTracks.add(DOOR_OPEN);
        } else if (baseObject instanceof Bomb) {
            soundTracks.add(KLEE_BOMB);
            soundTracks.add(NUCLEAR_BOMB);
        }
        return soundTracks;
    }

    public static SoundLoader getSoundFromSoundList(ArrayList<SoundLoader> soundList, String name) {
        for (SoundLoader sound : soundList) {
            if (sound.getName().equals(name)) {
                return sound;
            }
        }
        return null;
    }

    public static String randomRoomType() {
        int number = randomInteger(0, ROOM_TYPES.size() - 1);
        return ROOM_TYPES.get(number);
    }

    public static void calculateNewHp(BaseEntity baseEntity, int maxHpPercent, int healPercent) {
        float percentHp = baseEntity.getCurrentHp() / (float) (baseEntity.getBaseMaxHp());
        baseEntity.setAddMaxHp(baseEntity.getAddMaxHp() + maxHpPercent);
        int maxHp = finalValuePercent(baseEntity.getBaseMaxHp(), baseEntity.getAddMaxHp());
        baseEntity.setCurrentHp(finalValuePercent((int) (maxHp * percentHp), healPercent));
    }

    public static int countMonstersNearby(Player player, float tilesX, float tilesY) {
        ArrayList<BaseMonster> monstersInRangeList = new ArrayList<>();
        ArrayList<BaseMonster> allMonsters = new ArrayList<>(player.getMonstersInStage());
        for (BaseMonster monster : allMonsters) {
            if (player.getMonstersInStage().contains(monster)) {
                double monsterPosX = monster.getHitBox().getTranslateX();
                double playerPosX = player.getHitBox().getTranslateX();
                double monsterPosY = monster.getHitBox().getTranslateY();
                double playerPosY = player.getHitBox().getTranslateY();
                if (Math.abs(monsterPosX - playerPosX) / PIXEL_PER_TILE <= tilesX &&
                        Math.abs(monsterPosY - playerPosY) / PIXEL_PER_TILE <= tilesY &&
                        !(monstersInRangeList.contains(monster)) &&
                        monster.getAnimation().getState() != States.MONSTER_DEAD) {
                    monstersInRangeList.add(monster);
                } else if ((((Math.abs(monsterPosX - playerPosX) / PIXEL_PER_TILE > tilesX ||
                        Math.abs(monsterPosY - playerPosY) / PIXEL_PER_TILE > tilesY) ||
                        monster.getAnimation().getState() == States.MONSTER_DEAD)
                        && monstersInRangeList.contains(monster))) {
                    monstersInRangeList.remove(monster);
                }
            }
        }
        return monstersInRangeList.size();
    }

    public static boolean hasPerk(Player player, BasePerk basePerk, boolean useTier) {
        if (useTier) {
            for (BasePerk perk : player.getObtainPerks()) {
                if (perk.getClass().getSimpleName().equals(basePerk.toString()) &&
                        perk.getTier() == basePerk.getTier()) {
                    return true;
                }
            }
        } else {
            for (BasePerk perk : player.getObtainPerks()) {
                if (perk.getClass().getSimpleName().equals(basePerk.toString())) {
                    return true;
                }
            }
        }
        return false;
    }

    public static ArrayList<BasePerk> createPerkChoices(Player player, ArrayList<BasePerk> toRandom, int number) {
        ArrayList<BasePerk> result = new ArrayList<>();
        List<BasePerk> commonPerks = toRandom.stream().filter(basePerk -> basePerk.getRarity() == COMMON).collect(Collectors.toList());
        List<BasePerk> uncommonPerks = toRandom.stream().filter(basePerk -> basePerk.getRarity() == UNCOMMON).collect(Collectors.toList());
        List<BasePerk> rarePerks = toRandom.stream().filter(basePerk -> basePerk.getRarity() == RARE).collect(Collectors.toList());
        while (result.size() < number) {
            int randomIndex;
            BasePerk selectedPerk = null;
            float randomValue = randomChance();
            if (randomValue < 0.5f) {
                if (commonPerks.size() > 0) {
                    randomIndex = randomInteger(0, commonPerks.size() - 1);
                    selectedPerk = commonPerks.get(randomIndex);
                }
            } else if (randomValue < 0.85f) {
                if (uncommonPerks.size() > 0) {
                    randomIndex = randomInteger(0, uncommonPerks.size() - 1);
                    selectedPerk = uncommonPerks.get(randomIndex);
                }
            } else {
                if (rarePerks.size() > 0) {
                    randomIndex = randomInteger(0, rarePerks.size() - 1);
                    selectedPerk = rarePerks.get(randomIndex);
                }
            }
            if (selectedPerk != null && hasPerk(player, new LuckyManPerk(player, TIER1), false)) {
                float chance = randomChance();
                if ((hasPerk(player, new LuckyManPerk(player, TIER2_1), true) && chance < LUCKYMAN_2_1_TIERCHANCE) ||
                        (hasPerk(player, new LuckyManPerk(player, TIER2_1), false) && chance < LUCKYMAN_NORMAL_TIERCHANCE)) {
                    Tier[] tier2 = {TIER2_1, TIER2_2};
                    Tier randomTier = tier2[randomInteger(0, 1)];
                    selectedPerk.setTier(randomTier);
                }
            }
            if (selectedPerk != null) {
                addPerkToResult(selectedPerk, result);
            }
        }
        return result;
    }

    private static void addPerkToResult(BasePerk selectedPerk, ArrayList<BasePerk> result) {
        boolean hasPerk = false;
        for (BasePerk perk: result) {
            if (perk.toString() == selectedPerk.toString()) {
                hasPerk = true;
                break;
            }
        }
        if (!hasPerk) {
            result.add(selectedPerk);
        }
    }

    public static ArrayList<String> createDoorChoices(ArrayList<String> toRandom, int number) {
        ArrayList<String> result = new ArrayList<>();
        while (result.size() < number) {
            int randomNumber = randomInteger(0, toRandom.size() - 1);
            String choice = toRandom.get(randomNumber);
            result.add(choice);
            toRandom.remove(choice);
        }
        return result;
    }

    public static void createColumns(GridPane gridPane, int[] array) {
        for (int i: array) {
            ColumnConstraints column = new ColumnConstraints();
            column.setPercentWidth(i);
            column.setHalignment(HPos.CENTER);
            gridPane.getColumnConstraints().add(column);
        }
    }

    public static void createRows(GridPane gridPane, int[] array) {
        for (int i: array) {
            RowConstraints row = new RowConstraints();
            row.setPercentHeight(i);
            gridPane.getRowConstraints().add(row);
        }
    }

    public static ArrayList<SoundLoader> getAllSounds() {
        ArrayList<SoundLoader> sounds = new ArrayList<>();
        sounds.add(PLAYER_WALK);
        sounds.add(PLAYER_JUMP1);
        sounds.add(PLAYER_JUMP2);
        sounds.add(PLAYER_BEING_HIT);
        sounds.add(PLAYER_DEAD);
        sounds.add(DAGGER_COMBO_ATTACK1);
        sounds.add(DAGGER_COMBO_ATTACK2);
        sounds.add(MONSTER_BEING_HIT);
        sounds.add(MONSTER_DEAD);
        sounds.add(NORMAL_ARROW_ATTACK);
        sounds.add(CHARGE_ARROW_CHARGE);
        sounds.add(CHARGE_ARROW_ATTACK);
        sounds.add(MISSILE_ATTACK);
        sounds.add(GET_HEAL);
        sounds.add(START_SCENE_SONG);
        sounds.add(CLASS_SELECTION_SCENE_SONG);
        sounds.add(FIGHT_SCENE_SONG1);
        sounds.add(FIGHT_SCENE_SONG2);
        sounds.add(FIGHT_SCENE_SONG3);
        sounds.add(FIGHT_SCENE_SONG4);
        sounds.add(FIGHT_SCENE_SONG5);
        sounds.add(FIGHT_SCENE_SONG6);
        sounds.add(MARKET_SCENE_SONG);
        sounds.add(PLAYER_BUY_SUCCESS);
        sounds.add(PLAYER_BUY_FAIL);
        sounds.add(PLAYER_BUY_MISS);
        sounds.add(PLAYER_BRUH);
        sounds.add(BOSS_SCENE_SONG);
        sounds.add(BOSS_FIRE);
        sounds.add(BROADSWORD_STOMP_EFFECT);
        sounds.add(BOSS_STOMP_EFFECT);
        sounds.add(BROADSWORD_ATTACK);
        sounds.add(THROWERBOMB_ATTACK);
        sounds.add(KLEE_BOMB);
        sounds.add(NUCLEAR_BOMB);
        sounds.add(PLAYER_DASH);
        sounds.add(PLAYER_COLLECT);
        sounds.add(PLAYER_UPGRADE);
        sounds.add(PLAYER_DODGE);
        sounds.add(MONSTER_DODGE);
        sounds.add(MERCHANT_SELL);
        sounds.add(PERKCHEST_GET);
        sounds.add(INVENTORY);
        sounds.add(BASEREWARD_GET);
        sounds.add(DOOR_OPEN);
        sounds.add(getUndeadSound());
        return sounds;
    }

    public static void addAndDeleteText(BaseEntity baseEntity, BaseScene scene, Text text) {
        Platform.runLater(() -> {
            ArrayList<Text> toRemoveTexts = new ArrayList<>();
            text.setTranslateX(baseEntity.getHitBox().getTranslateX());
            text.setTranslateY(baseEntity.getHitBox().getTranslateY() - 50 * SCALE);
            scene.getChildren().add(text);
            toRemoveTexts.add(text);
            for (Text t: toRemoveTexts) {
                long startTime = System.nanoTime();
                AnimationTimer timer = new AnimationTimer() {
                    @Override
                    public void handle(long now) {
                        long elapsedTime = now - startTime;
                        double secondsElapsed = elapsedTime / 1_000_000_000.0;
                        if (secondsElapsed >= 1) {
                            scene.getChildren().remove(t);
                            stop();
                        }
                    }
                };
                timer.start();
            }
            toRemoveTexts.clear();
        });
    }

    public static void addAndDeleteStageStatus(BaseScene scene, Text text) {
        scene.getChildren().add(text);
        long startTime = System.nanoTime();
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                long elapsedTime = now - startTime;
                double secondsElapsed = elapsedTime / 1_000_000_000.0;
                if (secondsElapsed >= 1) {
                    scene.getChildren().remove(text);
                    stop();
                } else {
                    text.setTranslateX(-scene.getTranslateX() + (double) gameWidth / 3);
                    text.setTranslateY(-scene.getTranslateY() + gameHeight / (4 * SCALE));
                }
            }
        };
        timer.start();
        setHasCreatedText(true);
    }

    public static void knockBack(BaseEntity baseEntity, FaceDirection direction, BaseScene scene) {
        double knockBackDistance;
        if (direction == RIGHT) {
            knockBackDistance = baseEntity.getKnockbackDistance() * -1;
        } else {
            knockBackDistance = baseEntity.getKnockbackDistance();
        }
        if (canMoveHere(baseEntity, baseEntity.getHitBox().getTranslateX() - knockBackDistance * PIXEL_PER_TILE,
                baseEntity.getHitBox().getTranslateY(), baseEntity.getHitBox().getFitWidth(), baseEntity.getHitBox().getFitHeight(), scene.getLvlData())) {
            Platform.runLater(() -> {
                baseEntity.getHitBox().setTranslateX(baseEntity.getHitBox().getTranslateX() - knockBackDistance * PIXEL_PER_TILE);
                baseEntity.getAnimation().setTranslateX(baseEntity.getAnimation().getTranslateX() - knockBackDistance * PIXEL_PER_TILE);
            });
        }
    }

    public static void immuneAfterAttacked(BaseEntity baseEntity) {
        baseEntity.setCanTakeDamage(false);
        long startTime = System.nanoTime();
        AnimationTimer immuneTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                long elapsedTime = now - startTime;
                double secondsElapsed = elapsedTime / 1_000_000_000.0;
                if (secondsElapsed >= PLAYER_DAMAGE_COOLDOWN) {
                    baseEntity.setCanTakeDamage(true);
                    stop();
                }
            }
        };
        immuneTimer.start();
    }

    public static void resetAllScenes() {
        resetStartScene();
        resetClassSelectionScene();
        resetFightScene();
        resetMarketScene();
        resetBossScene();
        setNumberOfFightScene(0);
        resetPlayer();
    }

    public static void createSymbol(BaseScene baseScene, String type, double posX, double posY, BasePerk basePerk) {
        Symbol symbol = new Symbol(baseScene.getPlayer(), baseScene.getBgImg().getWidth() * posX * SCALE,
                baseScene.getBgImg().getHeight()/posY * SCALE, RIGHT, type, basePerk);
        baseScene.getChildren().add(symbol.getAnimation());
        baseScene.getPlayer().getObjectsInStage().add(symbol);
    }

    public static ImageView loadCoinUI() {
        return new ImageView(new Image(ClassLoader.getSystemResource("etcUI/Coin_UI.gif").toString()));
    }
}
