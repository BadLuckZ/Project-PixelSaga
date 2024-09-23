// This thread is used for creating monster's animation

package threads;

import animating.Animation;
import entities.bases.BaseBoss;
import entities.bases.BaseMonster;
import entities.monsters.BigBoy;
import enums.FaceDirection;
import enums.States;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import objects.BaseObject;
import objects.Fire;
import objects.ShockWave;
import objects.StompEffect;
import scenes.BaseScene;
import scenes.BossScene;

import java.util.ArrayList;
import java.util.Arrays;

import static enums.FaceDirection.RIGHT;
import static enums.States.*;
import static utils.AnimationStatus.*;
import static utils.HelperMethods.finalValuePercentFloat;
import static utils.HelperMethods.getSoundFromSoundList;
import static utils.MonsterStatus.*;
import static utils.ObjectStatus.*;

public class MonsterAnimationThread extends PlayerAnimationThread {
    private ArrayList<States> bossAnimationState =
            new ArrayList<>(Arrays.asList(BOSS_SKILL_ONE, BOSS_SKILL_TWO, BOSS_SKILL_THREE_STAND_STILL));
    private int frameCount = 1;
    private int speedMultiplier = 0; // for Boss
    private boolean isBossJump = false;
    public MonsterAnimationThread(BaseScene scene) {
        super(scene);
    }

    @Override
    public void run() {
        try {
            while (isRunning) {
                Platform.runLater(() -> {
                    ArrayList<BaseMonster> allMonsters = new ArrayList<>(player.getMonstersInStage());
                    for (BaseMonster eachMonster : allMonsters) {
                        if (player.getMonstersInStage().contains(eachMonster)) {
                            if (player.getMonstersInStage().contains(eachMonster)) {
                                Animation monsterAnimation = eachMonster.getAnimation();
                                States stateNow = monsterAnimation.getState();
                                States previousState = monsterAnimation.getPreviousState();
                                if (loopAnimationState.contains(stateNow)) { //stand, walk
                                    loopAnimationUpdateFrame(previousState, stateNow, monsterAnimation);
                                } else if (oneTimeAnimationState.contains(stateNow)) { //normal attack
                                    oneTimeAnimationUpdateFrame(previousState, stateNow, eachMonster, monsterAnimation);
                                } else if (bossAnimationState.contains(stateNow)) {
                                    BaseBoss boss = (BaseBoss) eachMonster;
                                    Animation bossAnimation = boss.getAnimation();
                                    updateBossAnimationFrame(previousState, stateNow, boss, bossAnimation);
                                }
                            }
                        }
                    }
                });
                makeBossFrameAnimation();
            }
        } catch (InterruptedException ignored) {}
    }

    private void makeBossFrameAnimation() throws InterruptedException {
        if (scene instanceof BossScene && !player.getMonstersInStage().isEmpty()) {
            BaseBoss boss = (BaseBoss) player.getMonstersInStage().get(0);
            if (boss.getAnimation().getState() == BOSS_SKILL_ONE) {
                sleep((long) ((1000 / ANIMATION_FRAME) / finalValuePercentFloat(BIGBOY_SKILL_ONE_ANIMATION_SPEED, speedMultiplier)));
            } else if (boss.getAnimation().getState() == BOSS_SKILL_TWO) {
                sleep((long) ((1000 / ANIMATION_FRAME) / BIGBOY_SKILL_TWO_ANIMATION_SPEED));
            } else {
                sleep(1000 / ANIMATION_FRAME);
            }
        } else {
            sleep(1000 / ANIMATION_FRAME);
        }
    }
    private void updateBossAnimationFrame(States previousState, States stateNow, BaseBoss boss, Animation bossAnimation) {
        if (stateNow == BOSS_SKILL_ONE) {
            updateBossSkillOne(previousState, stateNow, boss, bossAnimation);
        } else if (stateNow == BOSS_SKILL_TWO) {
            updateBossSkillTwoFirstPart(previousState, stateNow, boss, bossAnimation);
        } else if (stateNow == BOSS_SKILL_THREE_STAND_STILL) {
            updateBossSkillThree(previousState, stateNow, boss, bossAnimation);
        }
    }

    private void createBossCooldown(BaseBoss boss, States state) {
        AnimationTimer timer = new AnimationTimer() {
            long startTime = System.nanoTime();

            @Override
            public void handle(long now) {
                long elapsedTime = now - startTime;
                double secondsElapsed = elapsedTime / 1_000_000_000.0;
                if (state == BOSS_SKILL_ONE) {
                    if (secondsElapsed >= boss.getCooldownSkill1()) {
                        boss.setCanSkill1(true);
                        stop();
                    }
                } else if (state == BOSS_SKILL_TWO) {
                    if (secondsElapsed >= boss.getCooldownSkill2()) {
                        boss.setCanSkill2(true);
                        stop();
                    }
                }
            }
        };
        timer.start();
    }

    private void updateBossSkillOne(States previousState, States stateNow, BaseBoss boss, Animation bossAnimation) {
        if (previousState == stateNow) {
            if (boss.getFrameMakeDamageSkillOne().contains(bossAnimation.getCurrentFrameIndex())) {
                createStompEffect(boss, bossAnimation);
            }
            if (bossAnimation.getCurrentFrameIndex() == bossAnimation.getNumberOfRows(boss, stateNow) - 1) {
                bossAnimation.updateFrame(stateNow);
                bossAnimation.setAnimationToDefault(boss, scene);
                if (Math.abs(boss.getDeltaTilesX(player)) < 0.5 && Math.abs(boss.getDeltaTilesY(player)) < 0.3) {
                    boss.setCanSkill1(true);
                    speedMultiplier += 20;
                } else {
                    speedMultiplier = 0;
                    createBossCooldown(boss, stateNow);
                }
            } else {
                if (frameCount == BIGBOY_SKILL_ONE_FRAMES_HOLD.get(bossAnimation.getCurrentFrameIndex() - 1)) {
                    bossAnimation.updateFrame(stateNow);
                    frameCount = 1;
                } else {
                    frameCount += 1;
                }
            }
        } else {
            prepareToUpdateAnimation(stateNow, bossAnimation);
        }
    }

    private void createStompEffect(BaseBoss boss, Animation bossAnimation) {
        BaseObject stompEffect;
        if (boss.getFaceDirection() == RIGHT) {
            stompEffect = new StompEffect(boss, bossAnimation.getTranslateX() - (STOMP_EFFECT_WIDTH * SCALE - bossAnimation.getFitWidth()) / 4,
                    boss.getHitBox().getTranslateY() - ((STOMP_EFFECT_HITBOX_HEIGHT + STOMP_EFFECT_HITBOX_Y_OFFSET) * SCALE - boss.getHitBox().getFitHeight()), boss.getFaceDirection(), scene);
        } else {
            stompEffect = new StompEffect(boss, bossAnimation.getTranslateX() - (STOMP_EFFECT_WIDTH * SCALE - bossAnimation.getFitWidth()) * 3 / 4,
                    boss.getHitBox().getTranslateY() - ((STOMP_EFFECT_HITBOX_HEIGHT + STOMP_EFFECT_HITBOX_Y_OFFSET) * SCALE - boss.getHitBox().getFitHeight()), boss.getFaceDirection(), scene);
        }
        getSoundFromSoundList(boss.getSoundList(), "BossStomping").play();
        scene.getChildren().add(stompEffect.getAnimation());
        player.getObjectsInStage().add(stompEffect);
    }

    private void updateBossSkillTwoFirstPart(States previousState, States stateNow, BaseBoss boss, Animation bossAnimation) {
        if (previousState == stateNow) {
            if (boss.getDashFrame().contains(bossAnimation.getCurrentFrameIndex()) && !isBossJump) {
                jumpBossToPlayer(boss, bossAnimation);
            }
            if (boss.getFrameMakeDamageSkillTwo().contains(bossAnimation.getCurrentFrameIndex())) {
                updateBossSkillTwoLastPart(stateNow, boss, bossAnimation);
            } else if (bossAnimation.getCurrentFrameIndex() == bossAnimation.getNumberOfRows(boss, stateNow) - 1) {
                bossAnimation.updateFrame(stateNow);
                bossAnimation.setAnimationToDefault(boss, scene);
                createBossCooldown(boss, stateNow);
            } else {
                if (frameCount == BIGBOY_SKILL_TWO_FRAMES_HOLD.get(bossAnimation.getCurrentFrameIndex() - 1)) {
                    bossAnimation.updateFrame(stateNow);
                    frameCount = 1;
                } else {
                    frameCount += 1;
                }
            }
        } else {
            prepareToUpdateAnimation(stateNow, bossAnimation);
            isBossJump = false;
        }
    }

    private void jumpBossToPlayer(BaseBoss boss, Animation bossAnimation) {
        isBossJump = true;
        bossAnimation.setyVelocity((((player.getHitBox().getTranslateY() + player.getHitBox().getFitHeight()/2) - (boss.getHitBox().getTranslateY() +
                boss.getHitBox().getFitHeight()/2)) - 0.5 * GRAVITY * Math.pow(BIGBOY_SKILL_TWO_JUMP_TIME * MOVE_FRAME, 2))/(BIGBOY_SKILL_TWO_JUMP_TIME * MOVE_FRAME));
        double jumpDistance = Math.abs(boss.getDeltaTilesX(player));
        if (jumpDistance > BIGBOY_SKILL_TWO_MAX_JUMP_DISTANCE) jumpDistance = BIGBOY_SKILL_TWO_MAX_JUMP_DISTANCE;
        ((BigBoy) boss).setJumpDistance(jumpDistance * PIXEL_PER_TILE);
        boss.setDash(true);
        bossAnimation.setJump(true);
    }

    private void updateBossSkillTwoLastPart(States stateNow, BaseBoss boss, Animation bossAnimation) {
        if (bossAnimation.getyVelocity() == 0) {
            bossAnimation.updateFrame(stateNow);
            createStompEffectAndShockWave(boss, bossAnimation);
            createMultipleShockWave(boss, bossAnimation);
        } else {
            int index = bossAnimation.getCurrentFrameIndex();
            bossAnimation.setCurrentFrameIndex(0);
            if (bossAnimation.getyVelocity() < 0) {
                bossAnimation.updateFrame(BOSS_JUMP_UP);
            } else {
                bossAnimation.updateFrame(BOSS_JUMP_DOWN);
            }
            bossAnimation.setCurrentFrameIndex(index);
        }
    }

    private void createStompEffectAndShockWave(BaseBoss boss, Animation bossAnimation) {
        BaseObject stompEffect;
        BaseObject shockWave;
        if (boss.getFaceDirection() == RIGHT) {
            stompEffect = new StompEffect(boss, bossAnimation.getTranslateX() - (STOMP_EFFECT_WIDTH * SCALE - bossAnimation.getFitWidth()) / 4,
                    boss.getHitBox().getTranslateY() - ((STOMP_EFFECT_HITBOX_HEIGHT + STOMP_EFFECT_HITBOX_Y_OFFSET) * SCALE - boss.getHitBox().getFitHeight()), boss.getFaceDirection(), scene);
            shockWave = new ShockWave(boss, bossAnimation.getTranslateX() - (SHOCK_WAVE_WIDTH * SCALE - bossAnimation.getFitWidth()) / 4,
                    boss.getHitBox().getTranslateY() - ((SHOCK_WAVE_HITBOX_HEIGHT + SHOCK_WAVE_HITBOX_Y_OFFSET) * SCALE - boss.getHitBox().getFitHeight()), boss.getFaceDirection(), scene);
        } else {
            stompEffect = new StompEffect(boss, bossAnimation.getTranslateX() - (STOMP_EFFECT_WIDTH * SCALE - bossAnimation.getFitWidth()) * 3 / 4,
                    boss.getHitBox().getTranslateY() - ((STOMP_EFFECT_HITBOX_HEIGHT + STOMP_EFFECT_HITBOX_Y_OFFSET) * SCALE - boss.getHitBox().getFitHeight()), boss.getFaceDirection(), scene);
            shockWave = new ShockWave(boss, bossAnimation.getTranslateX() - (SHOCK_WAVE_WIDTH * SCALE - bossAnimation.getFitWidth()) * 3 / 4,
                    boss.getHitBox().getTranslateY() - ((SHOCK_WAVE_HITBOX_HEIGHT + SHOCK_WAVE_HITBOX_Y_OFFSET) * SCALE - boss.getHitBox().getFitHeight()), boss.getFaceDirection(), scene);
        }
        getSoundFromSoundList(boss.getSoundList(), "BossStomping").play();
        scene.getChildren().addAll(stompEffect.getAnimation(), shockWave.getAnimation());
        player.getObjectsInStage().addAll(Arrays.asList(stompEffect, shockWave));
    }

    private void createMultipleShockWave(BaseBoss boss, Animation bossAnimation) {
        AnimationTimer timer = new AnimationTimer() {
            long startTime = System.nanoTime();
            int count = 0;
            double xPos = bossAnimation.getTranslateX();
            double yPos = boss.getHitBox().getTranslateY();
            FaceDirection faceDirection = boss.getFaceDirection();
            @Override
            public void handle(long now) {
                long elapsedTime = now - startTime;
                double secondsElapsed = elapsedTime / 1_000_000_000.0;
                if (secondsElapsed >= 0.25) {
                    count += 1;
                    startTime = now;
                    createShockWave(boss, bossAnimation, faceDirection, xPos, yPos, count);
                }
                if (count == BIGBOY_SKILL_TWO_NUMBERS_OF_SHOCKWAVE) stop();
            }
        };
        timer.start();
    }

    private void createShockWave(BaseBoss boss, Animation bossAnimation, FaceDirection faceDirection, double xPos, double yPos, int count) {
        BaseObject shockWave1, shockWave2;
        if (faceDirection == RIGHT) {
            shockWave1 = new ShockWave(boss, (xPos - (SHOCK_WAVE_WIDTH * SCALE - bossAnimation.getFitWidth()) / 4) + count * PIXEL_PER_TILE,
                    yPos - ((SHOCK_WAVE_HITBOX_HEIGHT + SHOCK_WAVE_HITBOX_Y_OFFSET) * SCALE - boss.getHitBox().getFitHeight()), faceDirection, scene);
            shockWave2 = new ShockWave(boss, (xPos - (SHOCK_WAVE_WIDTH * SCALE - bossAnimation.getFitWidth()) / 4) - count * PIXEL_PER_TILE,
                    yPos - ((SHOCK_WAVE_HITBOX_HEIGHT + SHOCK_WAVE_HITBOX_Y_OFFSET) * SCALE - boss.getHitBox().getFitHeight()), faceDirection, scene);
        } else {
            shockWave1 = new ShockWave(boss, (xPos- (SHOCK_WAVE_WIDTH * SCALE - bossAnimation.getFitWidth()) * 3 / 4) + count * PIXEL_PER_TILE,
                    yPos - ((SHOCK_WAVE_HITBOX_HEIGHT + SHOCK_WAVE_HITBOX_Y_OFFSET) * SCALE - boss.getHitBox().getFitHeight()), faceDirection, scene);
            shockWave2 = new ShockWave(boss, (xPos - (SHOCK_WAVE_WIDTH * SCALE - bossAnimation.getFitWidth()) * 3 / 4) - count * PIXEL_PER_TILE,
                    yPos - ((SHOCK_WAVE_HITBOX_HEIGHT + SHOCK_WAVE_HITBOX_Y_OFFSET) * SCALE - boss.getHitBox().getFitHeight()), faceDirection, scene);
        }
        scene.getChildren().addAll(shockWave1.getAnimation(), shockWave2.getAnimation());
        player.getObjectsInStage().addAll(Arrays.asList(shockWave1, shockWave2));
    }

    private void updateBossSkillThree(States previousState, States stateNow, BaseBoss boss, Animation bossAnimation) {
        if (previousState == stateNow) {
            if (boss.getFrameMakeDamageSkillThree().contains(bossAnimation.getCurrentFrameIndex())) {
                createFire(boss, bossAnimation);
            }
            if (bossAnimation.getCurrentFrameIndex() == bossAnimation.getNumberOfRows(boss, stateNow) - 1) {
                bossAnimation.updateFrame(stateNow);
                bossAnimation.setState(BOSS_SKILL_THREE_WALK);
                bossAnimation.setCurrentFrameIndex(0);
            } else {
                bossAnimation.updateFrame(stateNow);
            }
        } else {
            prepareToUpdateAnimation(stateNow, bossAnimation);
        }
    }

    private void createFire(BaseBoss boss, Animation bossAnimation) {
        BaseObject fire;
        if (boss.getFaceDirection() == RIGHT) {
            fire = new Fire(boss, bossAnimation.getTranslateX() + bossAnimation.getFitWidth() * 0.15,
                    boss.getHitBox().getTranslateY() - ((FIRE_HITBOX_HEIGHT + FIRE_HITBOX_Y_OFFSET) * SCALE - boss.getHitBox().getFitHeight() * 0.77), boss.getFaceDirection(), scene);
        } else {
            fire = new Fire(boss, bossAnimation.getTranslateX() - (FIRE_WIDTH * SCALE - bossAnimation.getFitWidth() * 0.85),
                    boss.getHitBox().getTranslateY() - ((FIRE_HITBOX_HEIGHT + FIRE_HITBOX_Y_OFFSET) * SCALE - boss.getHitBox().getFitHeight() * 0.77), boss.getFaceDirection(), scene);
        }
        getSoundFromSoundList(fire.getSoundList(), "BossFire").play();
        scene.getChildren().add(fire.getAnimation());
        player.getObjectsInStage().add(fire);
    }
}
