// A Perk that lets player have a chance of healing when dealing damage

package entities.player.perks;

import entities.bases.BasePerk;
import entities.player.Player;
import enums.Tier;

import static enums.Rarity.UNCOMMON;
import static enums.Tier.TIER2_1;
import static enums.Tier.TIER2_2;
import static utils.HelperMethods.finalValuePercentFloat;
import static utils.HelperMethods.randomChance;
import static utils.PerkStatus.*;

public class VampirismPerk extends BasePerk {
    private static boolean canGainedHP;
    private static float vampirismChance;
    public VampirismPerk(Player player, Tier tier) {
        super(player, tier, UNCOMMON);
    }
    @Override
    public boolean canUsePerk(Player player) {
        return canGainedHP && randomChance() < vampirismChance;
    }

    @Override
    public void activatePerk(Player player) {
        setCanGainedHP(false);
        if (getTier() == TIER2_1) {
            player.setCurrentHp((int) (player.getCurrentHp() +
                                VAMP_2_1_DRAINPERHIT * finalValuePercentFloat(
                                        player.getBaseMaxHp(), player.getAddMaxHp())));
        } else {
            player.setCurrentHp((int) (player.getCurrentHp() +
                    VAMP_NORMAL_DRAINPERHIT * finalValuePercentFloat(
                            player.getBaseMaxHp(), player.getAddMaxHp())));
        }
    }

    @Override
    public void deactivatePerk(Player player) {
        setCanGainedHP(false);
    }

    @Override
    public void setStartValues(Tier tier) {
        vampirismChance = tier == TIER2_2 ? VAMP_2_2_DRAINCHANCE : VAMP_NORMAL_DRAINCHANCE;
        canGainedHP = false;
    }

    public static void activateVampirism() {
        setCanGainedHP(true);
    }

    public static void setCanGainedHP(boolean canGainedHP) {
        VampirismPerk.canGainedHP = canGainedHP;
    }

}
