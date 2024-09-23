// A Perk that can increase atk when having many monsters nearby

package entities.player.perks;

import entities.bases.BasePerk;
import entities.player.Player;
import enums.Tier;

import static enums.Rarity.UNCOMMON;
import static enums.Tier.*;
import static utils.HelperMethods.countMonstersNearby;
import static utils.PerkStatus.*;

public class ExtrovertPerk extends BasePerk {
    private final static float IN_RANGE_TILES_X = EXTROVERT_TILES_X;
    private final static float IN_RANGE_TILES_Y = EXTROVERT_TILES_Y;
    private static int monsterCountBefore;
    private static int monsterCountAfter;

    public ExtrovertPerk(Player player, Tier tier) {
        super(player, tier, UNCOMMON);
    }

    @Override
    public boolean canUsePerk(Player player) {
        monsterCountBefore = monsterCountAfter;
        monsterCountAfter = countMonstersNearby(player, IN_RANGE_TILES_X, IN_RANGE_TILES_Y);
        return monsterCountAfter >= EXTROVERT_CRITERIA;
    }

    @Override
    public void activatePerk(Player player) {
        int delta = monsterCountAfter - monsterCountBefore;
        if (getTier() == TIER1) {
            player.setAddAtk(player.getAddAtk() + EXTROVERT_1_ADDATKPERMONSTER * delta);
        } else if (getTier() == TIER2_1) {
            player.setAddAtk(player.getAddAtk() + EXTROVERT_2_1_ADDATKPERMONSTER * delta);
        } else {
            player.setAddAtk(player.getAddAtk() + EXTROVERT_2_2_ADDATKPERMONSTER * delta);
        }
    }

    @Override
    public void deactivatePerk(Player player) {
        int delta = monsterCountBefore - monsterCountAfter;
        if (getTier() == TIER1) {
            player.setAddAtk(player.getAddAtk() - EXTROVERT_1_ADDATKPERMONSTER * delta);
        } else if (getTier() == TIER2_1) {
            player.setAddAtk(player.getAddAtk() - EXTROVERT_2_1_ADDATKPERMONSTER * delta);
        } else {
            player.setAddAtk(player.getAddAtk() - EXTROVERT_2_2_ADDATKPERMONSTER * delta);
        }
    }

    @Override
    public void setStartValues(Tier tier) {
        monsterCountBefore = 0;
        monsterCountAfter = 0;
        if (tier == TIER2_2) {
            getPlayer().setAddAtk(getPlayer().getAddAtk() - EXTROVERT_2_2_MINUSATKEXTRA);
        }
    }
}
