// A Class that acts as an ArcherGiGee's attack

package objects;

import animating.Animation;
import animating.HitBox;
import entities.bases.BaseEntity;
import enums.FaceDirection;

import static utils.ObjectStatus.*;

public class Missile extends BaseObject{
    public Missile(BaseEntity baseEntity, double posX, double posY, FaceDirection faceDirection) {
        super(baseEntity, posX, posY, faceDirection);
        setWidth(MISSILE_WIDTH);
        setHeight(MISSILE_HEIGHT);
        setSpeed(MISSILE_SPEED);
        setAnimation(new Animation(this, posX, posY));
        setHitBox(new HitBox(this));
    }
}
