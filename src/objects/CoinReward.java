// A Class that acts as a reward for CoinRoom

package objects;

import animating.Animation;
import animating.HitBox;
import entities.bases.BaseEntity;
import entities.player.Player;
import entities.player.perks.TreasureHunterPerk;
import enums.FaceDirection;

import static enums.Tier.TIER2_2;
import static utils.HelperMethods.hasPerk;
import static utils.ObjectStatus.*;
import static utils.PerkStatus.TREASURE_2_2_REWARDMULTIPLIER;

public class CoinReward extends BaseReward {
    public CoinReward(BaseEntity baseEntity, double posX, double posY, FaceDirection faceDirection) {
        super(baseEntity, posX, posY, faceDirection);
        setWidth(COIN_REWARD_WIDTH);
        setHeight(COIN_REWARD_HEIGHT);
        setCanIgnoreTile(true);
        setCanInteract(true);
        setAnimation(new Animation(this, posX, posY));
        setHitBox(new HitBox(this));
    }

    @Override
    public void collect(Player player) {
        if (hasPerk(player, new TreasureHunterPerk(player, TIER2_2), true)) {
            player.setCoins(player.getCoins() + (int)(COIN_REWARD * TREASURE_2_2_REWARDMULTIPLIER));
        } else {
            player.setCoins(player.getCoins() + COIN_REWARD);
        }
    }
}
