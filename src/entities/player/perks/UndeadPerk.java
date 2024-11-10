// A Perk that lets player not die for a short time when receiving a deadly attack

package entities.player.perks;

import entities.bases.BasePerk;
import entities.player.Player;
import enums.Tier;
import javafx.animation.AnimationTimer;
import utils.SoundLoader;

import static enums.Rarity.UNCOMMON;
import static enums.Tier.*;
import static utils.PerkStatus.*;

public class UndeadPerk extends BasePerk {
    private static boolean inCooldown = false;
    private static boolean hasStartTimer = false;
    private static int activationDuration;
    private static int cooldownDuration;
    private static long startTime;
    private final static SoundLoader UNDEAD_SOUND = new SoundLoader("UndeadActivate", "sfx/sample_undead_activate.mp3");;
    private static AnimationTimer deadTimer;
    private static AnimationTimer cooldownTimer;

    public UndeadPerk(Player player, Tier tier) {
        super(player, tier, UNCOMMON);
    }

    @Override
    public boolean canUsePerk(Player player) {
        return true;
    }

    @Override
    public void activatePerk(Player player) {

    }

    @Override
    public void deactivatePerk(Player player) {

    }

    @Override
    public void setStartValues(Tier tier) {

    }
  
    public static boolean isUndeadInCooldown() {
        return inCooldown;
    }
  
    private void createCooldownTimer() {
        cooldownTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                long elapsedTime = now - startTime;
                double elapsedSecond = elapsedTime / 1_000_000_000.0;
                if (elapsedSecond >= cooldownDuration) {
                    inCooldown = false;
                    hasStartTimer = false;
                    stop();
                }
            }
        };
    }

    private void createActivationTimer(Player player) {
        deadTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                long elapsedTime = now - startTime;
                double elapsedSecond = elapsedTime / 1_000_000_000.0;
                if (elapsedSecond >= activationDuration) {
                    if (getTier() == TIER2_1) {
                        player.setAddAtk(getPlayer().getAddAtk() - UNDEAD_2_1_ADDATK);
                        player.setAddAttackSpeed(getPlayer().getAddAttackSpeed() - UNDEAD_2_1_ADDATKSPEED);
                    }
                    inCooldown = true;
                    startTime = now;
                    cooldownTimer.start();
                    stop();
                }
            }
        };
    }

    public static SoundLoader getUndeadSound() {
        return UNDEAD_SOUND;
    }

    public void activateUndead(Player player) {
        if (!hasStartTimer) {
            activationDuration = getTier() == TIER2_1 ? UNDEAD_2_1_ACTIVATION : UNDEAD_NORMAL_ACTIVATION;
            cooldownDuration = getTier() == TIER2_2 ? UNDEAD_2_2_COOLDOWN : UNDEAD_NORMAL_COOLDOWN;
            createActivationTimer(player);
            createCooldownTimer();
            player.setCurrentHp(1);
            if (getTier() == TIER2_1) {
                player.setAddAtk(getPlayer().getAddAtk() + UNDEAD_2_1_ADDATK);
                player.setAddAttackSpeed(getPlayer().getAddAttackSpeed() + UNDEAD_2_1_ADDATKSPEED);
            }
            startTime = System.nanoTime();
            deadTimer.start();
            hasStartTimer = true;
            UNDEAD_SOUND.play();
        }
    }
}
