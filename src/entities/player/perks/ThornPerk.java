// A Perk that let player can reflect damage to the monster

package entities.player.perks;

import entities.bases.BaseMonster;
import entities.bases.BasePerk;
import entities.player.Player;
import enums.Tier;
import javafx.animation.AnimationTimer;

import static enums.Rarity.COMMON;
import static utils.PerkStatus.THORN_2_2_SLOW;
import static utils.PerkStatus.THORN_2_2_SLOWTIME;

public class ThornPerk extends BasePerk {

    public ThornPerk(Player player, Tier tier) {
        super(player, tier, COMMON);
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

    public static void slow(BaseMonster baseMonster) {
        if (!baseMonster.isSlowdownByThornPerk()) {
            baseMonster.setSlowdownByThornPerk(true);
            baseMonster.setAddMovementSpeed(baseMonster.getAddMovementSpeed() - THORN_2_2_SLOW);
            AnimationTimer slowTimer = new AnimationTimer() {
                long start = System.nanoTime();
                @Override
                public void handle(long now) {
                    long elapsedTime = now - start;
                    double elapsedSecond = elapsedTime / 1_000_000_000;
                    if (elapsedSecond >= THORN_2_2_SLOWTIME) {
                        baseMonster.setAddMovementSpeed(baseMonster.getAddMovementSpeed() + THORN_2_2_SLOW);
                        baseMonster.setSlowdownByThornPerk(false);
                        stop();
                    }
                }
            };
            slowTimer.start();
        }
    }
}
