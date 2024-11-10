// A Class that is a base class for some rewards

package objects;

import entities.bases.BaseEntity;
import enums.FaceDirection;
import interfaces.Obtainable;

public abstract class BaseReward extends BaseObject implements Obtainable {
    public BaseReward(BaseEntity owner, double posX, double posY, FaceDirection faceDirection) {
        super(owner, posX, posY, faceDirection);
    }
}
