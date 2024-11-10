// A Perk that can increase evadeRate

package entities.player.perks;

import entities.player.Player;
import entities.bases.BasePerk;
import enums.Tier;

import static enums.Rarity.COMMON;
import static enums.Tier.TIER1;
import static enums.Tier.TIER2_1;
import static utils.PerkStatus.*;

public class JukeMasterPerk extends BasePerk {
    public JukeMasterPerk(Player player, Tier tier) {
        super(player, tier, COMMON);
    }

    @Override
    public boolean canUsePerk(Player player) {
        return true;
    }

    @Override
    public void activatePerk(Player player) {
        if (!hasActivated()) {
            setHasActivated(true);
            if (getTier() == TIER1) {
                player.setEvadeRate(player.getEvadeRate() + JUKE_1_ADDEVADERATE);
            } else if (getTier() == TIER2_1) {
                player.setEvadeRate(player.getEvadeRate() + JUKE_2_1_ADDEVADERATE);
            } else {
                player.setEvadeRate(player.getEvadeRate() + JUKE_2_2_ADDEVADERATE);
                player.setDamageDecrease(player.getDamageDecrease() - JUKE_2_2_MINUSDAMAGEDECREASE);
            }
        }
    }

    @Override
    public void deactivatePerk(Player player) {
        if (hasActivated()) {
            setHasActivated(false);
            if (getTier() == TIER1) {
                player.setEvadeRate(player.getEvadeRate() - JUKE_1_ADDEVADERATE);
            } else if (getTier() == TIER2_1) {
                player.setEvadeRate(player.getEvadeRate() - JUKE_2_1_ADDEVADERATE);
            } else {
                player.setEvadeRate(player.getEvadeRate() - JUKE_2_2_ADDEVADERATE);
                player.setDamageDecrease(player.getDamageDecrease() + JUKE_2_2_MINUSDAMAGEDECREASE);
            }
        }
    }

    @Override
    public void setStartValues(Tier tier) {

    }
}
