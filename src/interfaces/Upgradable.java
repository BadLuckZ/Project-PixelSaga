// Interface to show that the class can be upgraded

package interfaces;

import entities.player.Player;
import enums.Tier;

public interface Upgradable {
    boolean canUpgrade(Player player);
    void upgrade(Player player, Tier tier);
}
