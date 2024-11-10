// A Class that acts as a hp drop from the boss

package objects;

import animating.Animation;
import animating.HitBox;
import entities.bases.BaseEntity;
import enums.FaceDirection;

import static utils.ObjectStatus.*;

public class HPDropLarge extends BaseObject{
    public HPDropLarge(BaseEntity baseEntity, double posX, double posY, FaceDirection faceDirection) {
        super(baseEntity, posX, posY, faceDirection);
        setWidth(HP_DROP_LARGE_WIDTH);
        setHeight(HP_DROP_LARGE_HEIGHT);
        setCanFall(true);
        setAnimation(new Animation(this, posX, posY));
        setHitBox(new HitBox(this));
    }
}
