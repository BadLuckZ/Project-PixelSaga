// A Class that acts as a reward for HPRoom

package objects;

import animating.Animation;
import animating.HitBox;
import entities.bases.BaseEntity;
import entities.player.Player;
import enums.FaceDirection;

import static utils.HelperMethods.calculateNewHp;
import static utils.ObjectStatus.*;

public class HPReward extends BaseReward{
    public HPReward(BaseEntity baseEntity, double posX, double posY, FaceDirection faceDirection) {
        super(baseEntity, posX, posY, faceDirection);
        setWidth(HP_REWARD_WIDTH);
        setHeight(HP_REWARD_HEIGHT);
        setCanIgnoreTile(true);
        setCanInteract(true);
        setAnimation(new Animation(this, posX, posY));
        setHitBox(new HitBox(this));
    }

    @Override
    public void collect(Player player) {
        calculateNewHp(player, HP_REWARD_MAX_HP_INCREASE, HP_REWARD_HEAL);
    }
}
