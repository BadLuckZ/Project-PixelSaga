// BaseClass for all GiGees in the game (ArcherGiGee, IceThrowerGiGee, KleeGiGee,
// PeasantGiGee, ThrowerGiGee)

package entities.bases;

import static utils.MonsterStatus.*;

public abstract class BaseGiGee extends BaseMonster {
    public BaseGiGee() {
        super();
        setCommonStatus();
    }
    @Override
    public void setCommonStatus() {
        setEvadeRate(LOW_EVADE_RATE);
        setDropRate(GIGEE_DROP_RATE);
        setCoinDrop(COIN_DROP_GIGEE);
        setKnockbackChance(HIGH_KNOCKBACK_CHANCE);
        setKnockbackDistance(HIGH_KNOCKBACK_DISTANCE);
    }
}
