// A Class that is for boss' and broadsword's attack

package objects;

import animating.Animation;
import animating.HitBox;
import entities.bases.BaseEntity;
import enums.FaceDirection;
import javafx.geometry.Rectangle2D;
import scenes.BaseScene;

import static utils.AnimationStatus.START_X;
import static utils.AnimationStatus.START_Y;
import static utils.HelperMethods.loadObjectsSound;
import static utils.ObjectStatus.*;

public class StompEffect extends BaseObject{
    public StompEffect(BaseEntity baseEntity, double posX, double posY, FaceDirection faceDirection, BaseScene scene) {
        super(baseEntity, posX, posY, faceDirection);
        setWidth(STOMP_EFFECT_WIDTH);
        setHeight(STOMP_EFFECT_HEIGHT);
        setCanIgnoreTile(true);
        setAnimation(new Animation(this, posX, posY));
        getAnimation().setViewport(new Rectangle2D(START_X, START_Y, getWidth(), getHeight()));
        setHitBox(new HitBox(this, STOMP_EFFECT_HITBOX_WIDTH, STOMP_EFFECT_HITBOX_HEIGHT, STOMP_EFFECT_HITBOX_X_OFFSET, STOMP_EFFECT_HITBOX_Y_OFFSET));
        setSoundList(loadObjectsSound(this));
        runOneTimeAnimation(STOMP_EFFECT_FRAMES, STOMP_EFFECT_FRAMES_PER_SECOND, scene, true);
    }
}
