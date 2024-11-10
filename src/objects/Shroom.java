// A Class that acts as a helper for jumping

package objects;

import animating.Animation;
import animating.HitBox;
import entities.bases.BaseEntity;
import enums.FaceDirection;
import javafx.geometry.Rectangle2D;

import static utils.AnimationStatus.START_X;
import static utils.AnimationStatus.START_Y;
import static utils.ObjectStatus.*;

public class Shroom extends BaseObject{
    public Shroom(BaseEntity baseEntity, double posX, double posY, FaceDirection faceDirection) {
        super(baseEntity, posX, posY, faceDirection);
        setWidth(SHROOM_WIDTH);
        setHeight(SHROOM_HEIGHT);
        setCanIgnoreTile(true);
        setAnimation(new Animation(this, posX, posY));
        getAnimation().setViewport(new Rectangle2D(START_X, START_Y, getWidth(), getHeight()));
        setHitBox(new HitBox(this));
    }
}
