// A Class that is for interacting with the classes and the perks

package objects;

import animating.Animation;
import animating.HitBox;
import entities.bases.BaseEntity;
import entities.bases.BasePerk;
import enums.FaceDirection;

import java.util.ArrayList;
import java.util.Arrays;

import static utils.ObjectStatus.*;

public class Symbol extends BaseObject{
    private final ArrayList<String> CLASSLIST = new ArrayList<>(Arrays.asList("Bow", "BroadSword", "Dagger"));
    private String symbolType;
    private String popUpString;
    private BasePerk perk = null;
    public Symbol(BaseEntity baseEntity, double posX, double posY, FaceDirection faceDirection, String type, BasePerk basePerk) {
        super(baseEntity, posX, posY, faceDirection);
        setCanIgnoreTile(true);
        setCanInteract(true);
        setSymbolType(type);
        setPerk(basePerk);
        if (CLASSLIST.contains(type)) {
            setImgString("classes/" + getSymbolType() + getClass().getSimpleName() + "_Sprite.gif");
            setPopUpString("classes/" + getSymbolType() + getClass().getSimpleName() + "_Popup.png");
            setWidth(SYMBOL_WIDTH);
            setHeight(SYMBOL_HEIGHT);
        } else {
            setImgString(getPerk().getIconString());
            setPopUpString(null);
            setWidth(PERK_SYMBOL_WIDTH);
            setHeight(PERK_SYMBOL_HEIGHT);
        }
        setAnimation(new Animation(this, posX, posY));
        setHitBox(new HitBox(this));
    }

    public String getSymbolType() {
        return symbolType;
    }

    public void setSymbolType(String symbolType) {
        this.symbolType = symbolType;
    }

    public BasePerk getPerk() {
        return perk;
    }

    public void setPerk(BasePerk perk) {
        this.perk = perk;
    }

    public String getPopUpString() {
        return popUpString;
    }

    public void setPopUpString(String popUpString) {
        this.popUpString = popUpString;
    }
}