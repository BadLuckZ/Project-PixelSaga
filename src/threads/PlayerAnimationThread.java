// This thread is used for creating player's animation

package threads;

import animating.Animation;
import entities.bases.BaseBoss;
import entities.bases.BaseEntity;
import entities.bases.BaseMonster;
import entities.monsters.*;
import entities.player.Player;
import entities.player.classes.Bow;
import entities.player.classes.BroadSword;
import entities.player.classes.Dagger;
import enums.States;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import objects.*;
import scenes.BaseScene;

import java.util.ArrayList;
import java.util.Arrays;

import static enums.FaceDirection.LEFT;
import static enums.FaceDirection.RIGHT;
import static enums.States.*;
import static utils.AnimationStatus.*;
import static utils.HelperMethods.*;
import static utils.ObjectStatus.*;
import static utils.PlayerStatus.*;

public class PlayerAnimationThread extends Thread {

    protected BaseScene scene;
    protected Player player;
    protected boolean isRunning = true;
    protected ArrayList<States> loopAnimationState =
            new ArrayList<>(Arrays.asList(PLAYER_STAND_STILL, PLAYER_WALK, MONSTER_STAND_STILL, MONSTER_WALK, PLAYER_JUMP_UP, PLAYER_JUMP_DOWN, PLAYER_DASH , BOSS_JUMP_UP, BOSS_JUMP_DOWN, BOSS_SKILL_THREE_WALK));
    protected ArrayList<States> oneTimeAnimationState =
            new ArrayList<>(Arrays.asList(PLAYER_NORMAL_ATTACK, MONSTER_NORMAL_ATTACK, MONSTER_DEAD, MONSTER_SPAWN));

    public int chargeCount = 0;
    public boolean isChargeAttack = false;
    public boolean isSwitchState = false;

    public PlayerAnimationThread(BaseScene scene) {
        this.player = scene.getPlayer();
        this.scene = scene;
    }

    public void stopThread() {
        isRunning = false;
    }

    @Override
    public void run() {
        try {
            while (isRunning) {
                Animation playerAnimation = player.getAnimation();
                States stateNow = playerAnimation.getState();
                States previousState = playerAnimation.getPreviousState();
                player.setTimeSinceLastAttack(player.getTimeSinceLastAttack() + (float) 1 / ANIMATION_FRAME);
                Platform.runLater(() -> {
                    if (loopAnimationState.contains(stateNow)) { //stand, walk
                        loopAnimationUpdateFrame(previousState, stateNow, playerAnimation);
                    } else if (oneTimeAnimationState.contains(stateNow)) { //normal attack
                        oneTimeAnimationUpdateFrame(previousState, stateNow, player, playerAnimation);
                    } else if (stateNow == PLAYER_SPECIAL_ATTACK) {
                        updatePlayerSpecialAttack(previousState, stateNow, playerAnimation);
                    }
                });
                makePlayerFrameAnimation(stateNow);
            }
        } catch (InterruptedException ignored) {}
    }

    private void makePlayerFrameAnimation(States stateNow) throws InterruptedException {
        if (stateNow == PLAYER_NORMAL_ATTACK) {
            sleep((long) ((1000 / ANIMATION_FRAME) / (finalValuePercentFloat(player.getBaseAttackSpeed(), player.getAddAttackSpeed()))));
        } else if (stateNow == PLAYER_SPECIAL_ATTACK) {
            if (player.getPlayerClass() instanceof Bow) {
                sleep((long) ((1000 / ANIMATION_FRAME) / (finalValuePercentFloat(BOW_SPECIAL_ATTACK_SPEED_MULTIPLIER, player.getAddAttackSpeed()))));
            } else if (player.getPlayerClass() instanceof BroadSword) {
                sleep((long) ((1000 / ANIMATION_FRAME) / (finalValuePercentFloat(BROADSWORD_SPECIAL_ATTACK_SPEED_MULTIPLIER, player.getAddAttackSpeed()))));
            } else if (player.getPlayerClass() instanceof Dagger) {
                sleep((long) ((1000 / ANIMATION_FRAME) / (finalValuePercentFloat(DAGGER_SPECIAL_ATTACK_SPEED_MULTIPLIER, player.getAddAttackSpeed()))));
            }
        } else if (stateNow == PLAYER_DASH) {
            sleep(1000 / (3L * player.getDashFrames()));
        }else {
            sleep(1000 / ANIMATION_FRAME);
        }
    }

    protected void oneTimeAnimationUpdateFrame(States previousState, States stateNow, BaseEntity entity, Animation entityAnimation) {
        if (stateNow == PLAYER_NORMAL_ATTACK) {
            updatePlayerNormalAttack(previousState, stateNow, entityAnimation);
        } else if (stateNow == MONSTER_DEAD) {
            updateMonsterDead(previousState, stateNow, entity, entityAnimation);
        } else {
            if (stateNow == MONSTER_NORMAL_ATTACK && previousState == MONSTER_NORMAL_ATTACK) {
                if (entity.getFrameMakeDamageNormalAttack().contains(entityAnimation.getCurrentFrameIndex())) {
                    updateMonsterAttack(entity);
                }
                if (entity.getDashFrame().contains(entityAnimation.getCurrentFrameIndex())){
                    entity.setDash(true);
                }
            }
            if (previousState == stateNow) {
                if (entityAnimation.getCurrentFrameIndex() == entityAnimation.getNumberOfRows(entity, stateNow) - 1) {
                    entityAnimation.updateFrame(stateNow);
                    entityAnimation.setAnimationToDefault(entity, scene);
                    entityAnimation.setAnimationPlaying(false);
                } else {
                    entityAnimation.updateFrame(stateNow);
                }
            } else {
                prepareToUpdateAnimation(stateNow, entityAnimation);
            }
        }
    }

    protected void loopAnimationUpdateFrame(States previousState, States stateNow, Animation entityAnimation){
        if (previousState == stateNow) {
            entityAnimation.updateFrame(stateNow);
        } else {
            prepareToUpdateAnimation(stateNow, entityAnimation);
        }
    }

    private void updatePlayerNormalAttack(States previousState, States stateNow, Animation entityAnimation){
        getSoundFromSoundList(player.getSoundList(), "PlayerWalk").stop();
        if (player.getAttackCombo() == 1 && player.getPlayerClass().getClass().getSimpleName().equals("Bow")) {
            updateOneCombo(previousState, stateNow, entityAnimation);
        } else if (player.getAttackCombo() == 2) {
            twoComboUpdateFrames(previousState, stateNow, entityAnimation);
        }
    }

    private void updatePlayerSpecialAttack(States previousState, States stateNow, Animation entityAnimation){
        getSoundFromSoundList(player.getSoundList(), "PlayerWalk").stop();
        if (player.getPlayerClass() instanceof Bow) {
            updateBowSpecialAttack(previousState, stateNow, entityAnimation);
        } else if (player.getPlayerClass() instanceof BroadSword) {
            updateBroadSwordSpecialAttack(previousState, stateNow, entityAnimation);
        } else if (player.getPlayerClass() instanceof Dagger) {
            updateDaggerSpecialAttack(previousState, stateNow, entityAnimation);
        }
    }

    private void updateMonsterDead(States previousState, States stateNow, BaseEntity entity, Animation entityAnimation){
        if (previousState == stateNow) {
            if (entityAnimation.getCurrentFrameIndex() == entityAnimation.getNumberOfRows(entity, stateNow) - 1) {
                removeMonster(entity, stateNow);
            } else {
                entityAnimation.updateFrame(stateNow);
            }
        } else { // First Frame of Dead
            dropHpMonster(stateNow, entity, entityAnimation);
        }
    }

    private void removeMonster(BaseEntity entity, States stateNow) {
        if (entity.getAnimation().getyVelocity() == 0) {
            entity.getAnimation().updateFrame(stateNow);
            player.getMonstersInStage().remove(entity);
            AnimationTimer timer = new AnimationTimer() {
                long startTime = System.nanoTime();
                @Override
                public void handle(long now) {
                    long elapsedTime = now - startTime;
                    double secondsElapsed = elapsedTime / 1_000_000_000.0;
                    if (secondsElapsed >= ((entity instanceof BigBoy) ? 2 : 5)) {
                        scene.getChildren().remove(entity.getAnimation());
                        stop();
                    }
                }
            };
            timer.start();
        }
    }

    private void dropHpMonster(States stateNow, BaseEntity entity, Animation entityAnimation) {
        if (randomChance() < ((BaseMonster) entity).getDropRate()){
            HPDropSmall hpDropSmall = new HPDropSmall(entity, entity.getHitBox().getTranslateX(),
                    entity.getHitBox().getTranslateY() + entity.getHitBox().getFitHeight() / 2, RIGHT);
            scene.getChildren().add(hpDropSmall.getAnimation());
            scene.getPlayer().getObjectsInStage().add(hpDropSmall);
        } if (entity instanceof BaseBoss) {
            HPDropLarge hpDropLarge = new HPDropLarge(entity, entity.getHitBox().getTranslateX(),
                    entity.getHitBox().getTranslateY() + entity.getHitBox().getFitHeight() / 2, RIGHT);
            scene.getChildren().add(hpDropLarge.getAnimation());
            scene.getPlayer().getObjectsInStage().add(hpDropLarge);
        }
        entityAnimation.setCurrentFrameIndex(0);
        entityAnimation.updateFrame(stateNow);
        entityAnimation.setPreviousState(stateNow);
    }

    private void updateMonsterAttack(BaseEntity entity) {
        if (entity instanceof PeasantGiGee) {
            entity.attack(player, scene, 0);
        } else if (entity instanceof ArcherGiGee) {
            updateArcherGiGeeAttack(entity);
        } else if (entity instanceof ThrowerGiGee) {
            updateThrowerGiGeeAttack(entity);
        }
    }

    private void updateArcherGiGeeAttack(BaseEntity entity) {
        BaseObject missile = new Missile(entity, entity.getHitBox().getTranslateX(),
                entity.getHitBox().getTranslateY() + entity.getHitBox().getFitHeight() / 2.5, RIGHT);
        getSoundFromSoundList(missile.getSoundList(), "MissileAttack").play();
        if (entity.getFaceDirection() == LEFT) {
            missile = new Missile(entity, entity.getHitBox().getTranslateX() - MISSILE_WIDTH + entity.getHitBox().getFitWidth(),
                    entity.getHitBox().getTranslateY() + entity.getHitBox().getFitHeight() / 2.5, LEFT);
        }
        scene.getChildren().add(missile.getAnimation());
        player.getObjectsInStage().add(missile);
    }

    private void updateThrowerGiGeeAttack(BaseEntity entity) {
        float throwTime = (entity instanceof IceThrowerGiGee) ? ICETHROWERGIGEE_THROW_TIME : THROWERGIGEE_THROW_TIME;
        String type = (entity instanceof IceThrowerGiGee) ? "Ice" : "Default";
        double speed = Math.abs((player.getHitBox().getTranslateX() + player.getHitBox().getFitWidth()/2) - (entity.getHitBox().getTranslateX() +
                entity.getHitBox().getFitWidth() / 2))/(throwTime * PIXEL_PER_TILE);
        double yVelocity = (((player.getHitBox().getTranslateY() + player.getHitBox().getFitHeight()/2) - (entity.getHitBox().getTranslateY() +
                entity.getHitBox().getFitHeight() / 2)) - 0.5 * GRAVITY_FOR_OBJECT * Math.pow(throwTime * MOVE_FRAME, 2))/(throwTime * MOVE_FRAME);
        createThrowerBomb(entity, type, speed, yVelocity);
    }

    private void createThrowerBomb(BaseEntity entity, String type, double speed, double yVelocity) {
        ThrowerBomb throwerBomb;
        if (entity.getFaceDirection() == RIGHT) {
            throwerBomb = new ThrowerBomb(entity, entity.getHitBox().getTranslateX() ,
                    entity.getHitBox().getTranslateY() + entity.getHitBox().getFitHeight() / 2.5, RIGHT, speed, yVelocity, type);
        } else {
            throwerBomb = new ThrowerBomb(entity, entity.getHitBox().getTranslateX() - THROWER_BOMB_WIDTH + entity.getHitBox().getFitWidth(),
                    entity.getHitBox().getTranslateY() + entity.getHitBox().getFitHeight() / 2.5, LEFT, speed, yVelocity, type);
        }
        scene.getChildren().add(throwerBomb.getAnimation());
        player.getObjectsInStage().add(throwerBomb);
    }

    protected void prepareToUpdateAnimation(States stateNow, Animation entityAnimation) {
        entityAnimation.setCurrentFrameIndex(0);
        entityAnimation.updateFrame(stateNow);
        entityAnimation.setPreviousState(stateNow);
    }

    private void updateOneCombo(States previousState, States stateNow, Animation entityAnimation) {
        if (previousState == stateNow) {
            if (entityAnimation.getCurrentFrameIndex() == entityAnimation.getNumberOfRows(player, stateNow) - 1) {
                entityAnimation.updateFrame(stateNow);
                createNormalArrow();
                entityAnimation.setAnimationToDefault(player, scene);
                entityAnimation.setAnimationPlaying(false);
            } else {
                entityAnimation.updateFrame(stateNow);
            }
        } else {
            prepareToUpdateAnimation(stateNow, entityAnimation);
        }
    }

    private void createNormalArrow() {
        BaseObject normalArrow = new NormalArrow(player, player.getHitBox().getTranslateX(),
                player.getHitBox().getTranslateY() + player.getHitBox().getFitHeight() / 3.25, RIGHT);
        getSoundFromSoundList(normalArrow.getSoundList(), "NormalArrowAttack").play();
        if (player.getFaceDirection() == LEFT) {
            normalArrow = new NormalArrow(player, player.getHitBox().getTranslateX() - NORMAL_ARROW_WIDTH + player.getHitBox().getFitWidth(),
                    player.getHitBox().getTranslateY() + player.getHitBox().getFitHeight() / 3.25, LEFT);
        }
        scene.getChildren().add(normalArrow.getAnimation());
        player.getObjectsInStage().add(normalArrow);
    }

    private void twoComboUpdateFrames(States previousState, States stateNow, Animation entityAnimation) {
        checkFrameAttack(entityAnimation);
        if (player.getTimeSinceLastAttack() < player.getTimeCanSecondAttack() || player.isSecondAttack()) {
            if (entityAnimation.getCurrentFrameIndex() == entityAnimation.getNumberOfRows(player, stateNow) - 1) {
                playAttackSoundCombo();
                entityAnimation.updateFrame(stateNow);
                entityAnimation.setAnimationToDefault(player, scene);
                entityAnimation.setAnimationPlaying(false);
                player.setSecondAttack(false);
            } else if (player.isSecondAttack()){
                entityAnimation.updateFrame(stateNow);
            } else {
                entityAnimation.setCurrentFrameIndex(entityAnimation.getNumberOfRows(player, stateNow)/2);
                entityAnimation.updateFrame(stateNow);
                entityAnimation.setPreviousState(stateNow);
                player.setSecondAttack(true);
            }

        } else if (previousState == stateNow) {
            if (entityAnimation.getCurrentFrameIndex() == entityAnimation.getNumberOfRows(player, stateNow)/2 - 1) {
                entityAnimation.updateFrame(stateNow);
                entityAnimation.setAnimationToDefault(player, scene);
                entityAnimation.setAnimationPlaying(false);
                player.setTimeSinceLastAttack(0);
            } else {
                entityAnimation.updateFrame(stateNow);
            }

        } else {
            prepareToUpdateAnimation(stateNow, entityAnimation);
        }
    }

    private void playAttackSoundCombo() {
        if (player.getPlayerClass() instanceof Dagger) {
            getSoundFromSoundList(player.getSoundList(), "DaggerComboAttack1").stop();
            getSoundFromSoundList(player.getSoundList(), "DaggerComboAttack2").play();
        } else if (player.getPlayerClass() instanceof BroadSword) {
            getSoundFromSoundList(player.getSoundList(), "BroadSwordAttack").play();
        }
    }

    private void checkFrameAttack(Animation entityAnimation) {
        if (player.getFrameMakeDamageNormalAttack().contains(entityAnimation.getCurrentFrameIndex())){
            if (player.getPlayerClass() instanceof Dagger) {
                getSoundFromSoundList(player.getSoundList(), "DaggerComboAttack1").play();
                getSoundFromSoundList(player.getSoundList(), "DaggerComboAttack2").stop();
            } else if (player.getPlayerClass() instanceof BroadSword) {
                getSoundFromSoundList(player.getSoundList(), "BroadSwordAttack").play();
            }
            player.attack(player, scene, 0);
        }
    }

    private void updateBowSpecialAttack(States previousState, States stateNow, Animation entityAnimation) {
        int damageIncreasing = Math.min(BOW_SPECIAL_ATTACK_DAMAGE + BOW_SPECIAL_ATTACK_DAMAGE_INCREASE_PER_FRAME *
                (chargeCount - BOW_SPECIAL_ATTACK_CHARGE_TIME), BOW_SPECIAL_ATTACK_MAX_DAMAGE_INCREASE);
        ChargeArrow chargeArrow = new ChargeArrow(player, player.getHitBox().getTranslateX(),
                player.getHitBox().getTranslateY() + player.getHitBox().getFitHeight() / 3.25, player.getFaceDirection(), damageIncreasing);
        getSoundFromSoundList(chargeArrow.getSoundList(), "ChargeArrowCharging").play();
        getSoundFromSoundList(player.getSoundList(), "PlayerWalk").stop();
        if (entityAnimation.isHoldRMB()) {
            updateHoldRMB(previousState, stateNow, entityAnimation);
        } else {
            updateReleaseRMB(stateNow, entityAnimation, chargeArrow, damageIncreasing);
        }
    }

    private void updateHoldRMB(States previousState, States stateNow, Animation entityAnimation) {
        if (previousState == stateNow) {
            if (entityAnimation.getCurrentFrameIndex() == 4) {
                chargeCount += 1;
                entityAnimation.updateFrame(stateNow);
                entityAnimation.setCurrentFrameIndex(3);
            } else {
                chargeCount += 1;
                entityAnimation.updateFrame(stateNow);
            }
            if (chargeCount == BOW_SPECIAL_ATTACK_CHARGE_TIME) scene.blink(entityAnimation);
        } else {
            prepareToUpdateAnimation(stateNow, entityAnimation);
        }
    }

    private void updateReleaseRMB(States stateNow, Animation entityAnimation, ChargeArrow chargeArrow, int damageIncreasing) {
        getSoundFromSoundList(chargeArrow.getSoundList(), "ChargeArrowCharging").stop();
        if (chargeCount > (BOW_SPECIAL_ATTACK_CHARGE_TIME - 1) && !isChargeAttack) {
            successToChargeAttack(stateNow, entityAnimation, chargeArrow, damageIncreasing);
        } else {
            failToChargeAttack(stateNow, entityAnimation);
        }
    }

    private void successToChargeAttack(States stateNow, Animation entityAnimation, ChargeArrow chargeArrow, int damageIncreasing) {
        isChargeAttack = true;
        entityAnimation.setEffect(entityAnimation.getDefaultBrightness());
        getSoundFromSoundList(chargeArrow.getSoundList(), "ChargeArrowAttack").play();
        entityAnimation.setCurrentFrameIndex(5);
        entityAnimation.updateFrame(stateNow);

        if (player.getFaceDirection() == LEFT) {
            chargeArrow = new ChargeArrow(player, player.getHitBox().getTranslateX() - CHARGE_ARROW_WIDTH + player.getHitBox().getFitWidth(),
                    player.getHitBox().getTranslateY() + player.getHitBox().getFitHeight() / 3.25, player.getFaceDirection(), damageIncreasing);
        }
        scene.getChildren().add(chargeArrow.getAnimation());
        player.getObjectsInStage().add(chargeArrow);
        createSpecialAttackCooldownTimer();
    }

    private void failToChargeAttack(States stateNow, Animation entityAnimation) {
        entityAnimation.updateFrame(stateNow);
        entityAnimation.setEffect(entityAnimation.getDefaultBrightness());
        chargeCount = 0;
        isChargeAttack = false;
        entityAnimation.setAnimationToDefault(player, scene);
        entityAnimation.setAnimationPlaying(false);
    }

    private void createSpecialAttackCooldownTimer() {
        player.setSpecialAttackInCooldown(true);
        AnimationTimer specialAttackCoolDown = new AnimationTimer() {
            long startTime = System.nanoTime();
            @Override
            public void handle(long now) {
                long elapsedTime = now - startTime;
                double secondsElapsed = elapsedTime / 1_000_000_000.0;
                if (secondsElapsed > player.getSpecialAttackCooldown()) {
                    player.setSpecialAttackInCooldown(false);
                    scene.getSpecialAttackCoolDown().setOpacity(0);
                    scene.getSpecialAttackCoolDown().setWidth((scene.getSpecialAttackIcon().getFitWidth()));
                    scene.getSpecialAttackCoolDown().setTranslateX(scene.getSpecialAttackIcon().getTranslateX());
                    scene.blink(scene.getSpecialAttackIcon());
                    stop();
                } else {
                    double percentWidth = 1 - (secondsElapsed / player.getSpecialAttackCooldown());
                    double iconWidth = scene.getSpecialAttackIcon().getFitWidth();
                    scene.getSpecialAttackCoolDown().setOpacity(0.6);
                    scene.getSpecialAttackCoolDown().setWidth(percentWidth * iconWidth);
                    scene.getSpecialAttackCoolDown().setTranslateX(scene.getSpecialAttackIcon().getTranslateX()
                            + (1 - percentWidth) * iconWidth);
                }
            }
        };
        specialAttackCoolDown.start();
    }

    private void updateBroadSwordSpecialAttack(States previousState, States stateNow, Animation entityAnimation) {
        if (!isSwitchState) {
            if (previousState == stateNow) {
                if (player.getFrameMakeDamageNormalAttack().contains(entityAnimation.getCurrentFrameIndex())) {
                    getSoundFromSoundList(player.getSoundList(), "BroadSwordAttack").play();
                    player.attack(player, scene, BROADSWORD_SPECIAL_ATTACK_DAMAGE);
                }
                if (entityAnimation.getCurrentFrameIndex() == entityAnimation.getNumberOfRows(player, PLAYER_NORMAL_ATTACK) - 1) {
                    jumpBroadSword(entityAnimation);
                } else {
                    entityAnimation.updateFrame(PLAYER_NORMAL_ATTACK);
                }
            } else {
                entityAnimation.setCurrentFrameIndex(0);
                entityAnimation.updateFrame(PLAYER_NORMAL_ATTACK);
                entityAnimation.setPreviousState(stateNow);
            }
        } else {
            if (entityAnimation.getCurrentFrameIndex() == entityAnimation.getNumberOfRows(player,stateNow) - 1) {
                entityAnimation.updateFrame(stateNow);
                entityAnimation.setAnimationToDefault(player, scene);
                entityAnimation.setAnimationPlaying(false);
                isSwitchState = false;
                createSpecialAttackCooldownTimer();
            } else {
                if (player.getFrameMakeDamageSpecialAttack().contains(entityAnimation.getCurrentFrameIndex())) {
                    createBroadSwordLastAttack(stateNow, entityAnimation);
                } else {
                    entityAnimation.updateFrame(stateNow);
                }
            }
        }
    }

    private void jumpBroadSword(Animation entityAnimation) {
        entityAnimation.updateFrame(PLAYER_NORMAL_ATTACK);
        entityAnimation.setCurrentFrameIndex(0);
        entityAnimation.setJump(true);
        entityAnimation.setyVelocity(BROADSWORD_SPECIAL_ATTACK_JUMP_HEIGHT);
        isSwitchState = true;
        player.setDashTime(300);
        player.setDashDistance(20);
        player.setDash(true);
    }

    private void createBroadSwordLastAttack(States stateNow, Animation entityAnimation) {
        if (!entityAnimation.isJump()) {
            BaseObject stompEffect;
            if (player.getFaceDirection() == RIGHT) {
                stompEffect = new StompEffect(player, player.getAnimation().getTranslateX() - (STOMP_EFFECT_WIDTH * SCALE - player.getAnimation().getFitWidth()) / 4,
                        player.getHitBox().getTranslateY() - ((STOMP_EFFECT_HITBOX_HEIGHT + STOMP_EFFECT_HITBOX_Y_OFFSET) * SCALE - player.getHitBox().getFitHeight()), player.getFaceDirection(), scene);
            } else {
                stompEffect = new StompEffect(player, player.getAnimation().getTranslateX() - (STOMP_EFFECT_WIDTH * SCALE - player.getAnimation().getFitWidth()) * 3 / 4,
                        player.getHitBox().getTranslateY() - ((STOMP_EFFECT_HITBOX_HEIGHT + STOMP_EFFECT_HITBOX_Y_OFFSET) * SCALE - player.getHitBox().getFitHeight()), player.getFaceDirection(), scene);
            }
            getSoundFromSoundList(player.getSoundList(), "BroadSwordStomping").play();
            scene.getChildren().add(stompEffect.getAnimation());
            player.getObjectsInStage().add(stompEffect);
            entityAnimation.updateFrame(stateNow);
            player.setDashTime(0);
        }
    }

    private void updateDaggerSpecialAttack(States previousState, States stateNow, Animation entityAnimation) {
        if (previousState == stateNow) {
            if (player.getFrameMakeDamageSpecialAttack().contains(entityAnimation.getCurrentFrameIndex())) {
                createCritRateBuffTimer();
            }
            if (entityAnimation.getCurrentFrameIndex() == entityAnimation.getNumberOfRows(player, stateNow) - 1) {
                entityAnimation.updateFrame(stateNow);
                entityAnimation.setAnimationToDefault(player, scene);
                entityAnimation.setAnimationPlaying(false);
            } else {
                entityAnimation.updateFrame(stateNow);
            }
        } else {
            prepareToUpdateAnimation(stateNow, entityAnimation);
            createSpecialAttackCooldownTimer();
        }
    }

    private void createCritRateBuffTimer() {
        int critRate = player.getCritRate();
        player.setCritRate(player.getCritRate() + DAGGER_SPECIAL_ATTACK_CRIT_CHANCE);
        player.attack(player, scene, DAGGER_SPECIAL_ATTACK_DAMAGE);
        AnimationTimer critRateIncreaseTimer = new AnimationTimer() {
            long startTime = System.nanoTime();
            @Override
            public void handle(long now) {
                long elapsedTime = now - startTime;
                double secondsElapsed = elapsedTime / 1_000_000_000.0;
                if (secondsElapsed > DAGGER_SPECIAL_ATTACK_CRIT_BUFF_DURATION) {
                    player.setCritRate(critRate);
                    stop();
                }
            }
        };
        critRateIncreaseTimer.start();
    }
}