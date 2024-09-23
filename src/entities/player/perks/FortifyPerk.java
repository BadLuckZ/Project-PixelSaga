// A Perk that can decrease damage received

package entities.player.perks;

import entities.bases.BaseMonster;
import entities.player.Player;
import entities.bases.BasePerk;
import enums.Tier;

import java.util.ArrayList;

import static enums.Rarity.COMMON;
import static enums.Tier.TIER1;
import static enums.Tier.TIER2_1;
import static utils.PerkStatus.*;

public class FortifyPerk extends BasePerk {
    public FortifyPerk(Player player, Tier tier) {
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
                player.setDamageDecrease(player.getDamageDecrease() + FORTIFY_1_ADDDAMAGEDECREASE);
            } else if (getTier() == TIER2_1) {
                player.setDamageDecrease(player.getDamageDecrease() + FORTIFY_2_1_ADDDAMAGEDECREASE);
            } else {
                player.setDamageDecrease(player.getDamageDecrease() + FORTIFY_2_2_ADDDAMAGEDECREASE);
                ArrayList<BaseMonster> monsters = new ArrayList<>(player.getMonstersInStage());
                for (BaseMonster monster : monsters){
                    if (player.getMonstersInStage().contains(monster)) {
                        monster.setAddAttackDelay(monster.getAddAttackDelay() + FORTIFY2_2_ADDATKDELAY);
                    }
                }
            }
        }
    }

    @Override
    public void deactivatePerk(Player player) {
        if (hasActivated()) {
            setHasActivated(false);
            if (getTier() == TIER1) {
                player.setDamageDecrease(player.getDamageDecrease() - FORTIFY_1_ADDDAMAGEDECREASE);
            } else if (getTier() == TIER2_1) {
                player.setDamageDecrease(player.getDamageDecrease() - FORTIFY_2_1_ADDDAMAGEDECREASE);
            } else {
                player.setDamageDecrease(player.getDamageDecrease() - FORTIFY_2_2_ADDDAMAGEDECREASE);
                ArrayList<BaseMonster> monsters = new ArrayList<>(player.getMonstersInStage());
                for (BaseMonster monster : monsters){
                    if (player.getMonstersInStage().contains(monster)) {
                        monster.setAddAttackDelay(monster.getAddAttackDelay() - FORTIFY2_2_ADDATKDELAY);
                    }
                }
            }
        }
    }

    @Override
    public void setStartValues(Tier tier) {

    }
}
