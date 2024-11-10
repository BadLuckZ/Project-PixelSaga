// A Perk that lets player have a chance of getting reward 2 times

package entities.player.perks;

import entities.bases.BaseMonster;
import entities.bases.BasePerk;
import entities.player.Player;
import enums.Tier;

import java.util.ArrayList;

import static enums.Rarity.RARE;
import static enums.Tier.TIER2_1;
import static enums.Tier.TIER2_2;
import static utils.HelperMethods.randomChance;
import static utils.PerkStatus.TREASURE_2_1_REWARDCHANCE;
import static utils.PerkStatus.TREASURE_NORMAL_REWARDCHANCE;

public class TreasureHunterPerk extends BasePerk {
    public static float chance;
    public static boolean canRandom;

    public TreasureHunterPerk(Player player, Tier tier) {
        super(player, tier, RARE);
    }

    @Override
    public boolean canUsePerk(Player player) {
        return true;
    }

    @Override
    public void activatePerk(Player player) {
        if (canRandom) {
            player.setTreasureHunterActivate(randomChance() < chance);
            setCanRandomTreasure(false);
        }
        if (getTier() == TIER2_2) {
            ArrayList<BaseMonster> allMonsters = new ArrayList<>(player.getMonstersInStage());
            for (BaseMonster monster : allMonsters) {
                if (player.getMonstersInStage().contains(monster)) {
                    monster.setMoreCoinDrop(true);
                }
            }
        }
    }

    @Override
    public void deactivatePerk(Player player) {
        if (getTier() == TIER2_2) {
            ArrayList<BaseMonster> allMonsters = new ArrayList<>(player.getMonstersInStage());
            for (BaseMonster monster : allMonsters) {
                if (player.getMonstersInStage().contains(monster)) {
                    monster.setMoreCoinDrop(false);
                }
            }
        }
    }

    @Override
    public void setStartValues(Tier tier) {
        chance = getTier() == TIER2_1 ? TREASURE_2_1_REWARDCHANCE : TREASURE_NORMAL_REWARDCHANCE;
        canRandom = false;
    }

    public static void setCanRandomTreasure(boolean canRandom) {
        TreasureHunterPerk.canRandom = canRandom;
    }
}
