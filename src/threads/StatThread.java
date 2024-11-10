// This thread is used for controlling stats by activating perks and restoring dash amounts

package threads;

import entities.player.Player;
import entities.bases.BasePerk;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import scenes.BaseScene;

import static utils.PlayerStatus.DASH_COOLDOWN;

public class StatThread extends Thread{
    private BaseScene baseScene;
    private Player player;
    private boolean isRunning = true, isDashRestored = false;
    public StatThread(BaseScene baseScene) {
        this.baseScene = baseScene;
        this.player = baseScene.getPlayer();
    }

    public void stopThread(){
        isRunning = false;
    }

    @Override
    public void run() {
        try {
            while (isRunning) {
                for (BasePerk perk : player.getObtainPerks()) {
                    if (perk.canUsePerk(player)) {
                        perk.activatePerk(player);
                    } else {
                        perk.deactivatePerk(player);
                    }
                }
                restoreDash();
                sleep(100);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void restoreDash() {
        if (player.getRemainingDash() < player.getMaxDash()) {
            if (!isDashRestored) {
                isDashRestored = true;
                AnimationTimer dashCooldownTimer = new AnimationTimer() {
                    long startTime = System.nanoTime();
                    @Override
                    public void handle(long now) {
                        long elapsedTime = now - startTime;
                        double secondsElapsed = elapsedTime / 1_000_000_000.0;
                        if (secondsElapsed > DASH_COOLDOWN) {
                            isDashRestored = false;
                            getDash();
                            stop();
                        } else {
                            Platform.runLater(() -> {
                                double percentWidth = 1 - (secondsElapsed / DASH_COOLDOWN);
                                double iconWidth = baseScene.getDashIcon().getFitWidth();
                                baseScene.getDashCoolDown().setOpacity(0.6);
                                baseScene.getDashCoolDown().setWidth(percentWidth * iconWidth);
                                baseScene.getDashCoolDown().setTranslateX(baseScene.getDashIcon().getTranslateX()
                                        + (1 - percentWidth) * iconWidth);
                            });
                        }
                    }
                };
                dashCooldownTimer.start();
            }
        }
    }

    private void getDash() {
        if (player.getRemainingDash() + 1 <= player.getMaxDash()) player.setRemainingDash(player.getRemainingDash() + 1);
        Platform.runLater(() -> {
            baseScene.getDashCoolDown().setOpacity(0);
            baseScene.getDashCoolDown().setWidth(baseScene.getDashIcon().getFitWidth());
            baseScene.getDashCoolDown().setTranslateX(baseScene.getDashIcon().getTranslateX());
            baseScene.blink(baseScene.getDashIcon());
        });
    }
}
