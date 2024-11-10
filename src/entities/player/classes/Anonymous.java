// A Class that can't be played. Being used as a dummy walking in ClassSelectionScene

package entities.player.classes;

import entities.bases.BaseClass;

import static utils.AnimationStatus.*;

public class Anonymous extends BaseClass {
    public Anonymous() {
        setUniqueStatus();
    }

    @Override
    public void setUniqueStatus() {
        setStandStillFrames(ANONYMOUS_STAND_STILL_FRAMES);
        setWalkFrames(ANONYMOUS_WALK_FRAMES);
    }
}
