// Enums for checking entities' and objects' direction

package enums;

import entities.bases.BaseMonster;

import static utils.HelperMethods.randomInteger;

public enum FaceDirection {
    LEFT, RIGHT;

    public static FaceDirection randomStartDirection() {
        int value = randomInteger(0, 1);
        if (value == 0) return LEFT;
        else return RIGHT;
    }

    public static void switchDirection(BaseMonster baseMonster) {
        if (baseMonster.getFaceDirection() == LEFT) {
            baseMonster.setFaceDirection(RIGHT);
            baseMonster.getAnimation().setScaleX(1);
        } else {
            baseMonster.setFaceDirection(LEFT);
            baseMonster.getAnimation().setScaleX(-1);
        }
    }
}