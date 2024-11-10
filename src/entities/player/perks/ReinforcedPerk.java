// A Perk that increases def

package entities.player.perks;

import entities.player.Player;
import entities.bases.BasePerk;
import enums.Tier;

import static enums.Rarity.COMMON;
import static enums.Tier.TIER1;
import static enums.Tier.TIER2_1;
import static utils.PerkStatus.*;

public class ReinforcedPerk extends BasePerk {
    public ReinforcedPerk(Player player, Tier tier) {
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
                player.setAddDef(player.getAddDef() + REINFORCED_1_ADDDEF);
            } else if (getTier() == TIER2_1) {
                player.setAddDef(player.getAddDef() + REINFORCED_2_1_ADDDEF);
            } else {
                player.setAddDef(player.getAddDef() + REINFORCED_2_2_ADDDEF);
                player.setAddMovementSpeed(player.getAddMovementSpeed() - REINFORCED_2_2_MINUSMOVEMENTSPEED);
            }
        }
    }

    @Override
    public void deactivatePerk(Player player) {
        if (hasActivated()) {
            setHasActivated(false);
            if (getTier() == TIER1) {
                player.setAddDef(player.getAddDef() - REINFORCED_1_ADDDEF);
            } else if (getTier() == TIER2_1) {
                player.setAddDef(player.getAddDef() - REINFORCED_2_1_ADDDEF);
            } else {
                player.setAddDef(player.getAddDef() - REINFORCED_2_2_ADDDEF);
                player.setAddMovementSpeed(player.getAddMovementSpeed() + REINFORCED_2_2_MINUSMOVEMENTSPEED);
            }
        }
    }

    @Override
    public void setStartValues(Tier tier) {

    }
}
