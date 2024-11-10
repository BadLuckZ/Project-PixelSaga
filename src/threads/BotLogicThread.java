// This thread controls all monsters' behavior

package threads;

import animating.Animation;
import entities.bases.BaseMonster;
import entities.bases.BaseBoss;
import entities.bases.BaseGiGee;
import entities.monsters.KleeGiGee;
import entities.player.Player;
import javafx.application.Platform;
import javafx.scene.Node;
import objects.*;
import scenes.BaseScene;
import scenes.BossScene;
import scenes.FightScene;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static enums.FaceDirection.RIGHT;
import static enums.FaceDirection.switchDirection;
import static enums.States.*;
import static javafx.scene.paint.Color.WHITE;
import static utils.AnimationStatus.*;
import static utils.GameConstants.*;
import static utils.HelperMethods.*;
import static utils.MonsterStatus.*;
import static utils.ObjectStatus.*;

public class BotLogicThread extends Thread {
    private static BaseScene scene;
    private static Player player;
    private boolean isRunning = true;
    private long delayTime = 250;
    private static boolean isLaunchWave = true;
    private static int currentWaveNumber = 0;
    private static int maxWaveNumber;
    private static boolean isWaveClear = false;
    private static boolean hasCreatedText = false;

    public BotLogicThread(BaseScene scene) {
        this.scene = scene;
        this.player = scene.getPlayer();
    }

    public void stopThread() {
        isRunning = false;
    }

    @Override
    public void run() {
        if (scene instanceof FightScene|| scene instanceof BossScene) {
            try {
                while (isRunning) {
                    Platform.runLater(() -> {
                        checkWave();
                        ArrayList<BaseMonster> allMonsters = new ArrayList<>(player.getMonstersInStage());
                        for (BaseMonster eachMonster : allMonsters) {
                            if (player.getMonstersInStage().contains(eachMonster)) {
                                if (!(eachMonster.getAnimation().getState() == MONSTER_DEAD)) {
                                    if (eachMonster.isPlayerInVisionRange(scene) && eachMonster.isSpawn()) {
                                        eachMonster.setTimeSinceLastAttack(eachMonster.getTimeSinceLastAttack() + (float) delayTime / 1000);
                                        eachMonster.setCanSee(true);
                                        eachMonster.setCanAttack(false);
                                        if (eachMonster.isPlayerInAttackRange(player)) {
                                            checkCooldownAttack(eachMonster);
                                        }
                                    } else {
                                        if (!(eachMonster instanceof BaseBoss)) {
                                            createActionIdle(eachMonster);
                                        } else {
                                            if (!eachMonster.isSpawn()) {
                                                eachMonster.setTime(eachMonster.getTime() + (float) delayTime / 1000);
                                                if (eachMonster.getTime() >= SPAWN_TIME) eachMonster.setSpawn(true);
                                            }
                                        }
                                    }
                                } else {
                                    eachMonster.setWalk(false);
                                }
                            }
                        }
                    });
                    sleep(delayTime);
                }
            } catch (InterruptedException ignored) {}
        }
    }

    private void checkWave() {
        if (player.getMonstersInStage().isEmpty() && currentWaveNumber >= maxWaveNumber) {
            setWaveClear(true);
            try {
                createRewardZone((FightScene) scene);
            } catch (Exception e) {}
        } else if (player.getMonstersInStage().isEmpty() && currentWaveNumber < maxWaveNumber) {
            setWaveClear(false);
            setHasCreatedText(false);
            launchWave();
        }
    }

    private void launchWave() {
        ArrayList<Animation> allHeadsToRemove = new ArrayList<>();
        for (Node monsterHead : scene.getChildren()) {
            if (monsterHead instanceof Animation && ((Animation) monsterHead).getState().equals(MONSTER_DEAD)) {
                allHeadsToRemove.add((Animation) monsterHead);
            }
        }
        scene.getChildren().removeAll(allHeadsToRemove);
        isLaunchWave = true;
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> {
            spawnMonsters(scene.getLvlData(), scene);
        });
    }

    private static void createRewardZone(FightScene scene) throws InterruptedException {
        currentWaveNumber++;
        if (isWaveClear && !hasCreatedText) {
            addAndDeleteStageStatus(scene, createText("Clear!", 200, WHITE));
            drawRewardArea(scene);
        }
    }

    private static void drawRewardArea(FightScene scene) {
        boolean findReward = false;
        BaseObject reward = null;
        for (int y = 0; y < scene.getLvlData().length - 1; y++) {
            if (findReward) {
                break;
            }
            for (int x = 0; x < scene.getLvlData()[0].length; x++) {
                if (scene.getLvlData()[y][x] == 99 || scene.getLvlData()[y][x] == 98) {
                    if (scene.getRoomType().equals("HP")) {
                        reward = new HPReward(player,
                                (x + 4.56) * PIXEL_PER_TILE, (y + 1) * PIXEL_PER_TILE - HP_REWARD_HEIGHT * SCALE,
                                RIGHT);
                    } else if (scene.getRoomType().equals("Coin")) {
                        reward = new CoinReward(player,
                                (x + 4.38) * PIXEL_PER_TILE, (y + 1) * PIXEL_PER_TILE - COIN_REWARD_HEIGHT * SCALE,
                                RIGHT);
                    } else if (scene.getRoomType().equals("PerkChest")) {
                        reward = new PerkChestReward(player,
                                (x + 5.344) * PIXEL_PER_TILE, (y + 0.594) * PIXEL_PER_TILE - PERK_CHEST_REWARD_HEIGHT * SCALE,
                                RIGHT);
                    } else if (scene.getRoomType().equals("Upgrade")) {
                        reward = new BlackSmith(player, (x + 6) * PIXEL_PER_TILE - (double) BLACK_SMITH_WIDTH.get(0) / 2 * SCALE,
                                (y + 1) * PIXEL_PER_TILE - BLACK_SMITH_HEIGHT.get(0) * SCALE,
                                RIGHT, "Armadillos");
                    }
                    scene.getChildren().add(reward.getAnimation());
                    player.getObjectsInStage().add(reward);
                    findReward = true;
                    break;
                }
            }
        }

    }

    public static void addWave() {
        if (isLaunchWave) {
            currentWaveNumber++;
            isLaunchWave = false;
        }
    }

    private void checkCooldownAttack(BaseMonster eachMonster) {
        if (eachMonster instanceof BaseBoss boss) {
            checkBossCooldownAttack(boss);
        } else {
            checkGiGeeCooldownAttack((BaseGiGee) eachMonster);
        }
    }

    private void checkBossCooldownAttack(BaseBoss boss) {
        if (boss.getTimeSinceLastAttack() > boss.getAttackDelay()) {
            boss.setTimeSinceLastAttack(0);
            boss.setCanAttack(true);
            if ((Math.abs(boss.getDeltaTilesX(player)) > boss.getSkillTwoAttackRange() && boss.getCurrentHp() > finalValuePercent(boss.getBaseMaxHp(), boss.getAddMaxHp()) * 0.35) ||
                    ((player.getAnimation().getState() != PLAYER_JUMP_UP && player.getAnimation().getState() != PLAYER_JUMP_DOWN) && (boss.getDeltaTilesY(player) < -3))) {
                if (boss.canSkill2()) {
                    boss.getAnimation().setState(BOSS_SKILL_TWO);
                    boss.setCanSkill2(false);
                    boss.setAttackDelay(BIGBOY_COOLDOWN_AFTER_SKILL_TWO);
                }
            } else if ((player.getAnimation().getState() != PLAYER_JUMP_UP && player.getAnimation().getState() != PLAYER_JUMP_DOWN) && (boss.getDeltaTilesY(player) > 3) &&
                    (boss.getAnimation().getState() != BOSS_SKILL_TWO)) {
                dropFromFloatingFloor(boss);
            } else {
                randomUseSkill(boss);
            }
        }
    }

    private void dropFromFloatingFloor(BaseBoss boss) {
        if (passableTile.contains(getTypeOfTile(boss.getHitBox().getTranslateX() + boss.getHitBox().getFitWidth(),
                boss.getHitBox().getTranslateY() + boss.getHitBox().getFitHeight() + 1, scene.getLvlData())) ||
                passableTile.contains(getTypeOfTile(boss.getHitBox().getTranslateX() + boss.getHitBox().getFitWidth() / 2,
                        boss.getHitBox().getTranslateY() + boss.getHitBox().getFitHeight() + 1, scene.getLvlData())) ||
                passableTile.contains(getTypeOfTile(boss.getHitBox().getTranslateX(),
                        boss.getHitBox().getTranslateY() + boss.getHitBox().getFitHeight() + 1, scene.getLvlData()))){
            boss.getAnimation().setTranslateY(boss.getAnimation().getTranslateY() + 2);
            boss.getHitBox().setTranslateY(boss.getHitBox().getTranslateY() + 2);
        }
    }

    private void randomUseSkill(BaseBoss boss) {
        boss.getSkillToRandom().clear();
        if (Math.abs(boss.getDeltaTilesX(player)) < boss.getSkillOneAttackRange() && boss.canSkill1()) boss.getSkillToRandom().add(1);
        if ((boss.getCurrentHp() < finalValuePercent(boss.getBaseMaxHp(), boss.getAddMaxHp()) * 0.35) && boss.canSkill2()) boss.getSkillToRandom().add(2);
        if (Math.abs(boss.getDeltaTilesX(player)) < boss.getSkillThreeAttackRange() && boss.canSkill3() &&
                (Math.abs(boss.getDeltaTilesX(player)) > BIGBOY_SKILL_THREEE_MIN_ATTACK_RANGE)) boss.getSkillToRandom().add(3);
        chooseSkill(boss);
    }

    private void chooseSkill(BaseBoss boss) {
        Collections.shuffle(boss.getSkillToRandom());
        for (Integer skillNumber : boss.getSkillToRandom()) {
            if (skillNumber == 1) {
                boss.getAnimation().setState(BOSS_SKILL_ONE);
                boss.setCanSkill1(false);
                boss.setAttackDelay(BIGBOY_COOLDOWN_AFTER_SKILL_ONE);
                break;
            } else if (skillNumber == 2) {
                boss.getAnimation().setState(BOSS_SKILL_TWO);
                boss.setCanSkill2(false);
                boss.setAttackDelay(BIGBOY_COOLDOWN_AFTER_SKILL_TWO);
                break;
            } else if (skillNumber == 3) {
                boss.getAnimation().setState(BOSS_SKILL_THREE_STAND_STILL);
                boss.setCanSkill3(false);
                boss.setAttackDelay(BIGBOY_COOLDOWN_AFTER_SKILL_THREE);
                break;
            }
        }
    }

    private void checkGiGeeCooldownAttack(BaseGiGee eachMonster) {
        if (eachMonster.getTimeSinceLastAttack() > eachMonster.getNormalAttackDelay()) {
            eachMonster.setTimeSinceLastAttack(0);
            eachMonster.setCanAttack(true);
            if (eachMonster instanceof KleeGiGee kleeGiGee) {
                kleeGiGee.setAddMovementSpeed(KLEEGIGEE_SPEED_INCREASE);
                kleeGiGee.activateBomb(scene);
            } else {
                eachMonster.getAnimation().setState(MONSTER_NORMAL_ATTACK);
            }
        }
    }

    private void createActionIdle(BaseMonster eachMonster) {
        eachMonster.setCanSee(false);
        eachMonster.setCanAttack(false);
        eachMonster.setTimeSinceLastAttack(eachMonster.getTimeSinceLastAttack() + (float) delayTime / 1000);
        if (!eachMonster.isSpawn()) {
            eachMonster.setTime(eachMonster.getTime() + (float) delayTime / 1000);
            if (eachMonster.getTime() >= SPAWN_TIME) eachMonster.setSpawn(true);
        }
        if (eachMonster.getAnimation().getState() == MONSTER_STAND_STILL) {
            standIdle(eachMonster);
        } else if (eachMonster.getAnimation().getState() == MONSTER_WALK) {
            walkIdle(eachMonster);
        }
    }

    private void standIdle(BaseMonster eachMonster) {
        if (eachMonster.getTime() >= eachMonster.getStandTime()) {
            switchDirection(eachMonster);
            eachMonster.getAnimation().setState(MONSTER_WALK);
            eachMonster.setWalk(true);
            eachMonster.setTime(0);
            eachMonster.setWalkTime(eachMonster.getNewRandomTime());
        } else {
            eachMonster.setWalk(false);
            eachMonster.setTime(eachMonster.getTime() + (float) delayTime / 1000);
        }
    }

    private void walkIdle(BaseMonster eachMonster) {
        if (eachMonster.getTime() >= eachMonster.getWalkTime()) {
            eachMonster.getAnimation().setState(MONSTER_STAND_STILL);
            eachMonster.setWalk(false);
            eachMonster.setTime(0);
            eachMonster.setStandTime(eachMonster.getNewRandomTime());
        } else {
            eachMonster.setWalk(true);
            eachMonster.setTime(eachMonster.getTime() + (float) delayTime / 1000);
        }
    }

    public static void setWaveClear(boolean isWaveClear) {
        BotLogicThread.isWaveClear = isWaveClear;
    }

    public static void setHasCreatedText(boolean hasCreatedText) {
        BotLogicThread.hasCreatedText = hasCreatedText;
    }

    public static int getCurrentWaveNumber() {
        return currentWaveNumber;
    }

    public static int getMaxWaveNumber() {
        return maxWaveNumber;
    }

    public static void setCurrentWaveNumber(int currentWaveNumber) {
        BotLogicThread.currentWaveNumber = currentWaveNumber;
    }

    public static void setMaxWaveNumber(int waveNumber) {
        BotLogicThread.maxWaveNumber = waveNumber;
    }
}
