// A Perk that can increase atk when having low monsters nearby

package entities.player.perks;

import entities.bases.BasePerk;
import entities.player.Player;
import enums.Tier;

import static enums.Rarity.COMMON;
import static enums.Tier.*;
import static utils.HelperMethods.countMonstersNearby;
import static utils.PerkStatus.*;

public class IntrovertPerk extends BasePerk {
    private final static float IN_RANGE_TILES_X = INTROVERT_TILES_X;
    private final static float IN_RANGE_TILES_Y = INTROVERT_TILES_Y;
    private static int monsterCount;
    private static int latestValue;

    public IntrovertPerk(Player player, Tier tier) {
        super(player, tier, COMMON);
    }

    @Override
    public boolean canUsePerk(Player player) {
        monsterCount = countMonstersNearby(player, IN_RANGE_TILES_X, IN_RANGE_TILES_Y);
        return true;
    }

    @Override
    public void activatePerk(Player player) {
        int toAddValue;
        if (getTier() == TIER1) {
            toAddValue = INTROVERT_1_BASEADDATK - INTROVERT_1_MINUSADDATKPERMONSTER * monsterCount;
            if (toAddValue < 0) toAddValue = 0;
        } else if (getTier() == TIER2_1) {
            toAddValue = INTROVERT_2_1_BASEADDATK - INTROVERT_2_1_MINUSADDATKPERMONSTER * monsterCount;
            if (toAddValue < 0) toAddValue = 0;
        } else {
            toAddValue = INTROVERT_2_2_BASEADDATK - INTROVERT_2_2_MINUSADDATKPERMONSTER * monsterCount;
        }
        if (latestValue != toAddValue) {
            player.setAddAtk(player.getAddAtk() - latestValue);
            player.setAddAtk(player.getAddAtk() + toAddValue);
            latestValue = toAddValue;
        }
    }

    @Override
    public void deactivatePerk(Player player) {
        player.setAddAtk(player.getAddAtk() - latestValue);
    }

    @Override
    public void setStartValues(Tier tier) {
        monsterCount = 0;
        latestValue = 0;
    }
}
