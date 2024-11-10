// A Perk that increases critRate

package entities.player.perks;

import entities.player.Player;
import entities.bases.BasePerk;
import enums.Tier;

import static enums.Rarity.COMMON;
import static enums.Tier.TIER1;
import static enums.Tier.TIER2_1;
import static utils.PerkStatus.*;

public class PrecisionStrikePerk extends BasePerk {
    public PrecisionStrikePerk(Player player, Tier tier) {
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
                player.setCritRate(player.getCritRate() + PRECISION_1_ADDCRITRATE);
            } else if (getTier() == TIER2_1) {
                player.setCritRate(player.getCritRate() + PRECISION_2_1_ADDCRITRATE);
            } else {
                player.setCritRate(player.getCritRate() + PRECISION_2_2_ADDCRITRATE);
                player.setCritDamage(player.getCritDamage() - PRECISION_2_2_MINUSCRITDAMAGE);
            }
        }
    }

    @Override
    public void deactivatePerk(Player player) {
        if (hasActivated()) {
            setHasActivated(false);
            if (getTier() == TIER1) {
                player.setCritRate(player.getCritRate() - PRECISION_1_ADDCRITRATE);
            } else if (getTier() == TIER2_1) {
                player.setCritRate(player.getCritRate() - PRECISION_2_1_ADDCRITRATE);
            } else {
                player.setCritRate(player.getCritRate() - PRECISION_2_2_ADDCRITRATE);
                player.setCritDamage(player.getCritDamage() + PRECISION_2_2_MINUSCRITDAMAGE);
            }
        }
    }

    @Override
    public void setStartValues(Tier tier) {

    }
}
