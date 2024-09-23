// Hitbox for all entities and objects

package animating;

import entities.bases.BaseEntity;
import javafx.scene.image.ImageView;
import objects.BaseObject;

import static utils.AnimationStatus.*;

public class HitBox extends ImageView {
    private BaseEntity baseEntity;
    public HitBox(BaseEntity baseEntity){
        setBaseEntity(baseEntity);
        setTranslateX(baseEntity.getAnimation().getTranslateX() + getBaseEntity().getHitBoxXOffset() * SCALE);
        setTranslateY(baseEntity.getAnimation().getTranslateY() + getBaseEntity().getHitBoxYOffset() * SCALE);
        setFitHeight(getBaseEntity().getHitBoxHeight() * SCALE);
        setFitWidth(getBaseEntity().getHitBoxWidth() * SCALE);
    }

    public HitBox(BaseObject baseObject) {
        this(baseObject, baseObject.getWidth(), baseObject.getHeight(), 0, 0);
    }

    public HitBox(BaseObject baseObject, int width, int height, int xOffset, int yOffset) {
        setTranslateX(baseObject.getAnimation().getTranslateX() + xOffset * SCALE);
        setTranslateY(baseObject.getAnimation().getTranslateY() + yOffset * SCALE);
        setFitHeight(height * SCALE);
        setFitWidth(width * SCALE);
    }
    public BaseEntity getBaseEntity() {
        return baseEntity;
    }

    public void setBaseEntity(BaseEntity baseEntity) {
        this.baseEntity = baseEntity;
    }
}
