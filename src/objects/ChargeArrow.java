// A Class that acts as a charge arrow for Bow class

package objects;

import animating.Animation;
import animating.HitBox;
import entities.bases.BaseEntity;
import enums.FaceDirection;

import static utils.ObjectStatus.*;

public class ChargeArrow extends BaseObject{
    private int damageIncreasing;
    public ChargeArrow(BaseEntity baseEntity, double posX, double posY, FaceDirection faceDirection, int damageIncreasing) {
        super(baseEntity, posX, posY, faceDirection);
        setWidth(CHARGE_ARROW_WIDTH);
        setHeight(CHARGE_ARROW_HEIGHT);
        setSpeed(CHARGE_ARROW_SPEED);
        setAnimation(new Animation(this, posX, posY));
        setHitBox(new HitBox(this));
        setDamageIncreasing(damageIncreasing);
    }

    public int getDamageIncreasing() {
        return damageIncreasing;
    }

    public void setDamageIncreasing(int damageIncreasing) {
        this.damageIncreasing = damageIncreasing;
    }
}
