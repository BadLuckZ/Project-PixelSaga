// A Class that acts as a normal arrow for Bow class

package objects;

import animating.Animation;
import animating.HitBox;
import entities.bases.BaseEntity;
import enums.FaceDirection;

import static utils.ObjectStatus.*;

public class NormalArrow extends BaseObject {
    public NormalArrow(BaseEntity baseEntity, double posX, double posY, FaceDirection faceDirection) {
        super(baseEntity, posX, posY, faceDirection);
        setWidth(NORMAL_ARROW_WIDTH);
        setHeight(NORMAL_ARROW_HEIGHT);
        setSpeed(NORMAL_ARROW_SPEED);
        setAnimation(new Animation(this, posX, posY));
        setHitBox(new HitBox(this));
        setDistance(getWidth());
    }
}
