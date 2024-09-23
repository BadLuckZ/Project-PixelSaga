// A Class that is for KleeGiGee's and boss' attack

package objects;

import animating.Animation;
import animating.HitBox;
import entities.bases.BaseEntity;
import entities.monsters.KleeGiGee;
import enums.FaceDirection;
import javafx.geometry.Rectangle2D;
import scenes.BaseScene;

import static utils.AnimationStatus.*;
import static utils.HelperMethods.loadObjectsSound;
import static utils.ObjectStatus.*;

public class Bomb extends BaseObject{
    private String type;
    public Bomb(BaseEntity baseEntity, double posX, double posY, FaceDirection faceDirection, BaseScene scene) {
        super(baseEntity, posX, posY, faceDirection);
        setWidth((baseEntity instanceof KleeGiGee) ? KLEE_BOMB_WIDTH : NUCLEAR_BOMB_WIDTH);
        setHeight((baseEntity instanceof KleeGiGee) ? KLEE_BOMB_HEIGHT : NUCLEAR_BOMB_HEIGHT);
        setCanIgnoreTile(true);
        type = (baseEntity instanceof KleeGiGee) ? "Klee" : "Nuclear";
        setImgString("objects/" + type + getClass().getSimpleName() + "_Sprite.png");
        setAnimation(new Animation(this, posX, posY));
        getAnimation().setViewport(new Rectangle2D(START_X, START_Y, getWidth(), getHeight()));
        setHitBox(new HitBox(this, (baseEntity instanceof KleeGiGee) ? KLEE_BOMB_HITBOX_WIDTH : NUCLEAR_BOMB_HITBOX_WIDTH,
                (baseEntity instanceof KleeGiGee) ? KLEE_BOMB_HITBOX_HEIGHT : NUCLEAR_BOMB_HITBOX_HEIGHT,
                (baseEntity instanceof KleeGiGee) ? KLEE_BOMB_HITBOX_X_OFFSET : NUCLEAR_BOMB_HITBOX_X_OFFSET,
                (baseEntity instanceof KleeGiGee) ? KLEE_BOMB_HITBOX_Y_OFFSET : NUCLEAR_BOMB_HITBOX_Y_OFFSET));
        setSoundList(loadObjectsSound(this));
        runOneTimeAnimation(BOMB_FRAMES, (baseEntity instanceof KleeGiGee) ? KLEE_BOMB_FRAMES_PER_SECOND
                : NUCLEAR_BOMB_FRAMES_PER_SECOND, scene, true);
    }
}