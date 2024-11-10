// A Class that acts as a door to go to the next scene

package objects;

import animating.Animation;
import animating.HitBox;
import entities.bases.BaseEntity;
import enums.FaceDirection;

import java.util.Objects;

import static utils.ObjectStatus.*;

public class Door extends BaseObject{
    private String doorType;
    private boolean open = false;
    public Door(BaseEntity owner, double posX, double posY, FaceDirection faceDirection, String type) {
        super(owner, posX, posY, faceDirection);
        this.doorType = type;
        setWidth(DOOR_WIDTH);
        setHeight(DOOR_HEIGHT);
        setCanIgnoreTile(true);
        setCanInteract(true);
        setImgString("objects/" + doorType + getClass().getSimpleName() + "_Sprite.png");
        setAnimation(new Animation(this, posX, posY));
        setHitBox(new HitBox(this));
    }

    public String getDoorType() {
        return doorType;
    }
    public void openDoor() {
        if (!Objects.equals(doorType, "Broken")) {
            setImgString("objects/" + doorType + getClass().getSimpleName() + "_Sprite.gif");
            setAnimation(new Animation(this, getAnimation().getTranslateX(), getAnimation().getTranslateY()));
            setOpen(true);
        }
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }
}
