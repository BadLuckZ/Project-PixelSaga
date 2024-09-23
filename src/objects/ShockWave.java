// A Class that is for boss' attack

package objects;

import animating.Animation;
import animating.HitBox;
import entities.bases.BaseEntity;
import enums.FaceDirection;
import javafx.geometry.Rectangle2D;
import scenes.BaseScene;

import static utils.AnimationStatus.START_X;
import static utils.AnimationStatus.START_Y;
import static utils.ObjectStatus.*;

public class ShockWave extends BaseObject{
    public ShockWave(BaseEntity baseEntity, double posX, double posY, FaceDirection faceDirection, BaseScene scene) {
        super(baseEntity, posX, posY, faceDirection);
        setWidth(SHOCK_WAVE_WIDTH);
        setHeight(SHOCK_WAVE_HEIGHT);
        setCanIgnoreTile(true);
        setAnimation(new Animation(this, posX, posY));
        getAnimation().setViewport(new Rectangle2D(START_X, START_Y, getWidth(), getHeight()));
        setHitBox(new HitBox(this, SHOCK_WAVE_HITBOX_WIDTH, SHOCK_WAVE_HITBOX_HEIGHT, SHOCK_WAVE_HITBOX_X_OFFSET, SHOCK_WAVE_HITBOX_Y_OFFSET));
        runOneTimeAnimation(SHOCK_WAVE_FRAMES, SHOCK_WAVE_FRAMES_PER_SECOND, scene, true);
    }
}
