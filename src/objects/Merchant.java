// A Class that is for selling the healing potion

package objects;

import animating.Animation;
import animating.HitBox;
import entities.bases.BaseEntity;
import entities.player.Player;
import entities.player.perks.DiscountMasterPerk;
import enums.FaceDirection;
import interfaces.Tradable;
import javafx.geometry.Rectangle2D;
import scenes.BaseScene;

import static enums.Tier.*;
import static utils.AnimationStatus.GAP_WIDTH;
import static utils.HelperMethods.*;
import static utils.ObjectStatus.*;
import static utils.PerkStatus.*;

public class Merchant extends BaseObject implements Tradable {
    private int startX = 4;
    private int startY = 4;
    private boolean isSold = false;
    private int price;
    public Merchant(BaseEntity baseEntity, double posX, double posY, FaceDirection faceDirection) {
        super(baseEntity, posX, posY, faceDirection);
        generateNewPrice();
        setWidth(MERCHANT_WIDTH);
        setHeight(MERCHANT_HEIGHT);
        setCanIgnoreTile(true);
        setCanInteract(true);
        setAnimation(new Animation(this, posX, posY));
        setHitBox(new HitBox(this));
        getAnimation().setViewport(new Rectangle2D(startX, startY, MERCHANT_WIDTH, MERCHANT_HEIGHT));
    }

    @Override
    public boolean canSell(Player player) {
        return player.getCoins() >= price && !isSold &&
                player.getCurrentHp() != finalValuePercent(player.getBaseMaxHp(), player.getAddMaxHp());
    }

    @Override
    public void sell(Player player, BaseScene scene) {
        player.setCoins(player.getCoins() - price);
        getAnimation().setViewport(new Rectangle2D(startX + MERCHANT_WIDTH + GAP_WIDTH, startY, MERCHANT_WIDTH, MERCHANT_HEIGHT));
        if (hasPerk(player, new DiscountMasterPerk(player, TIER2_2), true) && randomChance() < DISCOUNT_2_2_FAILCHANCE) {
            getSoundFromSoundList(player.getSoundList(), "BuyingMiss").play();
        } else {
            getSoundFromSoundList(getSoundList(), "MerchantSell").play();
            getOwner().setCurrentHp(getOwner().getCurrentHp() + MERCHANT_HEAL_PERCENT * finalValuePercent(getOwner().getBaseMaxHp(), getOwner().getAddMaxHp()) / 100);
            removePotion(player, scene);
        }
        setSold(true);
        getSoundFromSoundList(player.getSoundList(), "BuyingSuccess").play();
        generateNewPrice();
    }

    public int calculateDiscountPrice(Player player) {
        int discount = 0;
        if (hasPerk(player, new DiscountMasterPerk(player, TIER1), true)) {
            discount = DISCOUNT_1_DISCOUNT;
        } else if (hasPerk(player, new DiscountMasterPerk(player, TIER2_1), true)) {
            discount = DISCOUNT_2_1_DISCOUNT;
        } else if (hasPerk(player, new DiscountMasterPerk(player, TIER2_2), true)) {
            discount = DISCOUNT_2_2_DISCOUNT;
        }
        setPrice(finalValuePercent(price, -discount));
        return price;
    }

    private void generateNewPrice() {
        price = randomInteger(MIN_POTION_PRICE, MAX_POTION_PRICE);
    }

    private void removePotion(Player player, BaseScene scene) {
        player.setCanInteract(false);
        player.setObjectThatInteractWith(null);
        scene.getChildren().remove(getHitBox());
        scene.getChildren().remove(getPopUp());
        player.getObjectsInStage().remove(this);
    }


    public boolean isSold() {
        return isSold;
    }

    public void setSold(boolean isSold) {
        this.isSold = isSold;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
