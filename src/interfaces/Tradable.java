// Interface to show that the class can be bought

package interfaces;

import entities.player.Player;
import scenes.BaseScene;

public interface Tradable {
    boolean canSell(Player player);
    void sell(Player player, BaseScene scene);
}
