// A Class that acts as a reward for PerkRoom

package objects;

import animating.Animation;
import animating.HitBox;
import entities.bases.BaseEntity;
import enums.FaceDirection;
import javafx.geometry.Rectangle2D;

import static utils.AnimationStatus.*;
import static utils.HelperMethods.getSoundFromSoundList;
import static utils.ObjectStatus.*;

public class PerkChestReward extends BaseObject{
    private int startX = 4;
    private int startY = 4;
    private boolean isOpen = false;
    public PerkChestReward(BaseEntity baseEntity, double posX, double posY, FaceDirection faceDirection) {
        super(baseEntity, posX, posY, faceDirection);
        setWidth(PERK_CHEST_REWARD_WIDTH);
        setHeight(PERK_CHEST_REWARD_HEIGHT);
        setCanIgnoreTile(true);
        setCanInteract(true);
        setAnimation(new Animation(this, posX, posY));
        setHitBox(new HitBox(this));
        getAnimation().setViewport(new Rectangle2D(startX, startY, PERK_CHEST_REWARD_WIDTH, PERK_CHEST_REWARD_HEIGHT));
    }

    public void openChest() {
        getSoundFromSoundList(getSoundList(), "PerkChestOpen").play();
        getAnimation().setViewport(new Rectangle2D(startX + PERK_CHEST_REWARD_WIDTH + GAP_WIDTH, startY, PERK_CHEST_REWARD_WIDTH, PERK_CHEST_REWARD_HEIGHT));
        setOpen(true);
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }
}
