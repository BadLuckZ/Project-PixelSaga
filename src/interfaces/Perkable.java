// Interface to show that the class can act as a perk

package interfaces;

import entities.player.Player;

public interface Perkable {
    boolean canUsePerk(Player player);
    void activatePerk(Player player);
    void deactivatePerk(Player player);
}
