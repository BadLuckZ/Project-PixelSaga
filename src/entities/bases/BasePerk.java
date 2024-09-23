// BaseClass for all perks in the game (inside entities.bases.player.perks)

package entities.bases;

import entities.player.Player;
import entities.player.perks.DiscountMasterPerk;
import enums.Rarity;
import interfaces.Perkable;
import interfaces.Tradable;
import interfaces.Obtainable;
import interfaces.Upgradable;
import enums.Tier;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import scenes.BaseScene;

import static enums.Rarity.*;
import static enums.Tier.*;
import static utils.GameConstants.scaleForUI;
import static utils.HelperMethods.*;
import static utils.PerkStatus.*;
import static utils.PlayerStatus.MAX_PERK_OBTAIN;

public abstract class BasePerk implements Obtainable, Upgradable, Tradable, Perkable {
    private Tier tier = TIER1;
    private static Player player;
    private int originalPrice;
    private int newPrice;
    private int upgradeCost;
    private Rarity rarity;
    private boolean hasActivated = false;
    private final String UI_STRING = "perks/" + this + "_UI.png";
    private final String ICON_STRING = "perks/" + this + "_Icon.gif";

    public BasePerk(Player player, Tier tier, Rarity rarity) {
        setPlayer(player);
        setTier(tier);
        setStartValues(getTier());
        setRarity(rarity);
        setOriginalPrice(getRarity());
    }

    @Override
    public boolean canSell(Player player) {
        if (hasPerk(player, new DiscountMasterPerk(player, TIER1), false)) {
            return !hasPerk(player, this, false) && player.getCoins() >= getNewPrice() && player.getObtainPerks().size() < MAX_PERK_OBTAIN;
        } else {
            return !hasPerk(player, this, false) && player.getCoins() >= getOriginalPrice() && player.getObtainPerks().size() < MAX_PERK_OBTAIN;
        }
    }

    @Override
    public void sell(Player player, BaseScene scene) {
        if (hasPerk(player, new DiscountMasterPerk(player, TIER1), false)) {
            player.setCoins(player.getCoins() - getNewPrice());
        } else {
            player.setCoins(player.getCoins() - getOriginalPrice());
        }
        if (hasPerk(player, new DiscountMasterPerk(player, TIER2_2), true)
                && randomChance() < new DiscountMasterPerk(player, TIER2_2).getChance()) {
            getSoundFromSoundList(player.getSoundList(), "BuyingMiss").play();
        } else {
            collect(player);
            getSoundFromSoundList(player.getSoundList(), "BuyingSuccess").play();
        }
    }

    @Override
    public void collect(Player player) {
        getSoundFromSoundList(player.getSoundList(), "PlayerCollect").play();
        player.getObtainPerks().add(this);
        removeFromUnobtain(player, this);
    }

    @Override
    public boolean canUpgrade(Player player) {
        if (hasPerk(player, new DiscountMasterPerk(player, TIER1), false)) {
            setUpgradeCost((int)(PRICE_UPGRADE_PERCENT * getNewPrice()));
        } else {
            setUpgradeCost((int)(PRICE_UPGRADE_PERCENT * getOriginalPrice()));
        }
        return player.getCoins() >= getUpgradeCost() && getTier() == TIER1;
    }

    @Override
    public void upgrade(Player player, Tier tier) {
        if (hasPerk(player, new DiscountMasterPerk(player, TIER2_2), true) && randomChance() < DISCOUNT_2_2_FAILCHANCE) {
            getSoundFromSoundList(player.getSoundList(), "BuyingMiss").play();
        } else {
            player.setCoins(player.getCoins() - getUpgradeCost());
            deactivatePerk(player);
            int index = player.getObtainPerks().indexOf(this);
            player.getObtainPerks().remove(this);
            setTier(tier);
            setStartValues(getTier());
            player.getObtainPerks().add(index, this);
            getSoundFromSoundList(player.getSoundList(), "PlayerUpgrade").stop();
            getSoundFromSoundList(player.getSoundList(), "PlayerUpgrade").play();
        }
    }

    public abstract void setStartValues(Tier tier);

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

    public Tier getTier() {
        return tier;
    }

    public void setTier(Tier tier) {
        this.tier = tier;
    }

    public int getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(Rarity rarity) {
        if (rarity == COMMON) {
            this.originalPrice = randomIntegerUsingPercent(COMMON_PRICE, PRICE_VARIATION);
        } else if (rarity == UNCOMMON) {
            this.originalPrice = randomIntegerUsingPercent(UNCOMMON_PRICE, PRICE_VARIATION);
        } else if (rarity == RARE) {
            this.originalPrice = randomIntegerUsingPercent(RARE_PRICE, PRICE_VARIATION);
        } else {
            this.originalPrice = 0;
        }
    }

    public int getNewPrice() {
        return newPrice;
    }

    public void setNewPrice(int price) {
        int discount = 0;
        if (hasPerk(player, new DiscountMasterPerk(player, TIER1), true)) {
            discount = DISCOUNT_1_DISCOUNT;
        } else if (hasPerk(player, new DiscountMasterPerk(player, TIER2_1), true)){
            discount = DISCOUNT_2_1_DISCOUNT;
        } else if (hasPerk(player, new DiscountMasterPerk(player, TIER2_2), true)) {
            discount = DISCOUNT_2_2_DISCOUNT;
        }
        this.newPrice = finalValuePercent(price, -discount);
    }

    public boolean hasActivated() {
        return hasActivated;
    }

    public void setHasActivated(boolean activated) {
        this.hasActivated = activated;
    }

    public String getIconString() {
        return ICON_STRING;
    }

    public ImageView getImageAtRowColumn(Tier tier, int column) {
        int row = tier == TIER1 ? 0 : tier == TIER2_1 ? 1 : 2;
        ImageView imageView = new ImageView(new Image(ClassLoader.getSystemResource(UI_STRING).toString()));
        int width = column == 0 ? 724 : 596;
        int height = 796;
        double x = column == 0 ? 5 : 729 + 4 * column + width * (column - 1);
        double y = 5 + (4 + height) * row;
        imageView.setViewport(new Rectangle2D(x, y, width, height));
        imageView.setFitWidth(width * scaleForUI);
        imageView.setFitHeight(height * scaleForUI);
        return imageView;
    }

    public Rarity getRarity() {
        return rarity;
    }

    public void setRarity(Rarity rarity) {
        this.rarity = rarity;
    }

    public static Player getPlayer() {
        return player;
    }

    public static void setPlayer(Player player) {
        BasePerk.player = player;
    }

    public int getUpgradeCost() {
        return upgradeCost;
    }

    public void setUpgradeCost(int upgradeCost) {
        this.upgradeCost = upgradeCost;
    }
}
