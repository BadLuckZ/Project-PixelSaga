// A Perk that can increase atk when having low hp

package entities.player.perks;

import entities.player.Player;
import entities.bases.BasePerk;
import enums.Tier;

import static enums.Rarity.COMMON;
import static enums.Tier.*;
import static utils.HelperMethods.finalValuePercentFloat;
import static utils.PerkStatus.*;

public class BerserkPerk extends BasePerk {
    public BerserkPerk(Player player, Tier tier) {
        super(player, tier, COMMON);
    }

    @Override
    public boolean canUsePerk(Player player) {
        int baseMaxHp = player.getBaseMaxHp();
        int addMaxHp = player.getAddMaxHp();
        if (getTier() == TIER1) {
            return player.getCurrentHp() / finalValuePercentFloat(baseMaxHp, addMaxHp) <= BERSERK_1_CRITERIA;
        } else if (getTier() == TIER2_1){
            return player.getCurrentHp() / finalValuePercentFloat(baseMaxHp, addMaxHp) <= BERSERK_2_1_CRITERIA;
        } else {
            return player.getCurrentHp() / finalValuePercentFloat(baseMaxHp, addMaxHp) <= BERSERK_2_2_CRITERIA;
        }
    }

    @Override
    public void activatePerk(Player player) {
        if (!hasActivated()) {
            setHasActivated(true);
            if (getTier() == TIER1) {
                player.setAddAtk(player.getAddAtk() + BERSERK_1_ADDATK);
            } else if (getTier() == TIER2_1) {
                player.setAddAtk(player.getAddAtk() + BERSERK_2_1_ADDATK);
            } else if (getTier() == TIER2_2) {
                player.setAddAtk(player.getAddAtk() + BERSERK_2_2_ADDATK);
            }
        }
    }

    public void deactivatePerk(Player player) {
        if (hasActivated()) {
            setHasActivated(false);
            if (getTier() == TIER1) {
                player.setAddAtk(player.getAddAtk() - BERSERK_1_ADDATK);
            } else if (getTier() == TIER2_1) {
                player.setAddAtk(player.getAddAtk() - BERSERK_2_1_ADDATK);
            } else if (getTier() == TIER2_2) {
                player.setAddAtk(player.getAddAtk() - BERSERK_2_2_ADDATK);
            }
        }
    }

    @Override
    public void setStartValues(Tier tier) {

    }
}
