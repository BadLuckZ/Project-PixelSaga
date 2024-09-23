// A Perk that can increase critDamage

package entities.player.perks;

import entities.player.Player;
import entities.bases.BasePerk;
import enums.Tier;

import static enums.Rarity.COMMON;
import static enums.Tier.TIER1;
import static enums.Tier.TIER2_1;
import static utils.PerkStatus.*;

public class FatalAttackPerk extends BasePerk {
    public FatalAttackPerk(Player player, Tier tier) {
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
                player.setCritDamage(player.getCritDamage() + FATAL_1_ADDCRITDAMAGE);
            } else if (getTier() == TIER2_1) {
                player.setCritDamage(player.getCritDamage() + FATAL_2_1_ADDCRITDAMAGE);
            } else {
                player.setCritDamage(player.getCritDamage() + FATAL_2_2_ADDCRITDAMAGE);
                player.setCritRate(player.getCritRate() - FATAL_2_2_MINUSCRITRATE);
            }
        }
    }

    @Override
    public void deactivatePerk(Player player) {
        if (hasActivated()) {
            setHasActivated(false);
            if (getTier() == TIER1) {
                player.setCritDamage(player.getCritDamage() - FATAL_1_ADDCRITDAMAGE);
            } else if (getTier() == TIER2_1) {
                player.setCritDamage(player.getCritDamage() - FATAL_2_1_ADDCRITDAMAGE);
            } else {
                player.setCritDamage(player.getCritDamage() - FATAL_2_2_ADDCRITDAMAGE);
                player.setCritRate(player.getCritRate() + FATAL_2_2_MINUSCRITRATE);
            }
        }
    }

    @Override
    public void setStartValues(Tier tier) {

    }
}
