// A Class that acts as a hp drop from GiGee

package objects;

import animating.Animation;
import animating.HitBox;
import entities.bases.BaseEntity;
import enums.FaceDirection;

import static utils.ObjectStatus.*;

public class HPDropSmall extends BaseObject{
    public HPDropSmall(BaseEntity baseEntity, double posX, double posY, FaceDirection faceDirection) {
        super(baseEntity, posX, posY, faceDirection);
        setWidth(HP_DROP_SMALL_WIDTH);
        setHeight(HP_DROP_SMALL_HEIGHT);
        setCanFall(true);
        setAnimation(new Animation(this, posX, posY));
        setHitBox(new HitBox(this));
    }
}
