// Interface to show that the class can attack

package interfaces;

import entities.bases.BaseEntity;
import entities.player.Player;
import scenes.BaseScene;

public interface Attackable {
    boolean canAttack(BaseEntity baseEntity);
    void attack(Player player, BaseScene baseScene, int extraDamagePercent);
    void isAttacked(BaseEntity baseEntity, BaseScene scene, int extraDamagePercent);
    void hurt(BaseEntity attacker, BaseScene scene);
    void dead(BaseEntity attacker, BaseScene scene);
}
