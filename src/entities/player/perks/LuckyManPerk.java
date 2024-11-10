// A Perk that lets player have a chance of getting tier2 perk easier

package entities.player.perks;

import entities.bases.BasePerk;
import entities.player.Player;
import enums.Tier;

import static enums.Rarity.RARE;

public class LuckyManPerk extends BasePerk {
    public LuckyManPerk(Player player, Tier tier) {
        super(player, tier, RARE);
    }

    @Override
    public boolean canUsePerk(Player player) {
        return true;
    }

    @Override
    public void activatePerk(Player player) {}

    @Override
    public void deactivatePerk(Player player) {}

    @Override
    public void setStartValues(Tier tier) {}
}
