// A Perk that increases Attack Speed

package entities.player.perks;

import entities.player.Player;
import entities.bases.BasePerk;
import enums.Tier;

import static enums.Rarity.COMMON;
import static enums.Tier.TIER1;
import static enums.Tier.TIER2_1;
import static utils.PerkStatus.*;

public class RapidFirePerk extends BasePerk {
    public RapidFirePerk(Player player, Tier tier) {
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
                player.setAddAttackSpeed(player.getAddAttackSpeed() + RAPID_NORMAL_ADDATKSPEED);
            } else if (getTier() == TIER2_1) {
                player.setAddAttackSpeed(player.getAddAttackSpeed() + RAPID_2_1_ADDATKSPEED);
            } else {
                player.setAddAttackSpeed(player.getAddAttackSpeed() + RAPID_NORMAL_ADDATKSPEED);
                player.setAddMovementSpeed(player.getAddMovementSpeed() + RAPID_2_2_ADDMOVEMENTSPEED);
            }
        }
    }

    @Override
    public void deactivatePerk(Player player) {
        if (hasActivated()) {
            setHasActivated(false);
            if (getTier() == TIER1) {
                player.setAddAttackSpeed(player.getAddAttackSpeed() - RAPID_NORMAL_ADDATKSPEED);
            } else if (getTier() == TIER2_1) {
                player.setAddAttackSpeed(player.getAddAttackSpeed() - RAPID_2_1_ADDATKSPEED);
            } else {
                player.setAddAttackSpeed(player.getAddAttackSpeed() - RAPID_NORMAL_ADDATKSPEED);
                player.setAddMovementSpeed(player.getAddMovementSpeed() - RAPID_2_2_ADDMOVEMENTSPEED);
            }
        }
    }

    @Override
    public void setStartValues(Tier tier) {

    }
}
