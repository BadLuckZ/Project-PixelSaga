// A Perk that can decrease the price

package entities.player.perks;

import entities.bases.BasePerk;
import entities.player.Player;
import enums.Tier;

import static enums.Rarity.RARE;
import static enums.Tier.*;
import static utils.PerkStatus.DISCOUNT_2_2_FAILCHANCE;
import static utils.PerkStatus.DISCOUNT_NORMAL_FAILCHANCE;

public class DiscountMasterPerk extends BasePerk {
    private static float chance;
    public DiscountMasterPerk(Player player, Tier tier) {
        super(player, tier, RARE);
    }
    @Override
    public boolean canUsePerk(Player player) {
        return false;
    }

    @Override
    public void activatePerk(Player player) {

    }

    @Override
    public void deactivatePerk(Player player) {

    }

    @Override
    public void setStartValues(Tier tier) {
        chance = getTier() == TIER2_2 ? DISCOUNT_2_2_FAILCHANCE : DISCOUNT_NORMAL_FAILCHANCE;
    }

    public static float getChance() {
        return chance;
    }
}
