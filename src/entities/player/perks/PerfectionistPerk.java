// A Perk that increase atk when having a lot of hp

package entities.player.perks;

import entities.player.Player;
import entities.bases.BasePerk;
import enums.Tier;

import static enums.Rarity.COMMON;
import static enums.Tier.TIER1;
import static enums.Tier.TIER2_1;
import static utils.HelperMethods.finalValuePercentFloat;
import static utils.PerkStatus.*;

public class PerfectionistPerk extends BasePerk {
    public PerfectionistPerk(Player player, Tier tier) {
        super(player, tier, COMMON);
    }

    @Override
    public boolean canUsePerk(Player player) {
        int baseMaxHp = player.getBaseMaxHp();
        int addMaxHp = player.getAddMaxHp();
        if (getTier() == TIER1) {
            return player.getCurrentHp() / finalValuePercentFloat(baseMaxHp, addMaxHp) >= PERFECTION_1_CRITERIA;
        } else if (getTier() == TIER2_1){
            return player.getCurrentHp() / finalValuePercentFloat(baseMaxHp, addMaxHp) >= PERFECTION_2_1_CRITERIA;
        } else {
            return player.getCurrentHp() / finalValuePercentFloat(baseMaxHp, addMaxHp) >= PERFECTION_2_2_CRITERIA;
        }
    }

    @Override
    public void activatePerk(Player player) {
        if (!hasActivated()) {
            setHasActivated(true);
            if (getTier() == TIER1) {
                player.setAddAtk(player.getAddAtk() + PERFECTION_1_ADDATK);
            } else if (getTier() == TIER2_1) {
                player.setAddAtk(player.getAddAtk() + PERFECTION_2_1_ADDATK);
            } else {
                player.setAddAtk(player.getAddAtk() + PERFECTION_2_2_ADDATK);
            }
        }
    }

    @Override
    public void deactivatePerk(Player player) {
        if (hasActivated()) {
            setHasActivated(false);
            if (getTier() == TIER1) {
                player.setAddAtk(player.getAddAtk() - PERFECTION_1_ADDATK);
            } else if (getTier() == TIER2_1) {
                player.setAddAtk(player.getAddAtk() - PERFECTION_2_1_ADDATK);
            } else {
                player.setAddAtk(player.getAddAtk() - PERFECTION_2_2_ADDATK);
            }
        }
    }

    @Override
    public void setStartValues(Tier tier) {

    }
}
