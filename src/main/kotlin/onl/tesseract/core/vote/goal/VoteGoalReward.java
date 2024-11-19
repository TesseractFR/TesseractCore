package onl.tesseract.core.vote.goal;

import java.util.UUID;

/**
 * A reward that can be given to a player. Custom rewards should implement this class, along with an implementation of {@link VoteGoalRewardType}
 */
public interface VoteGoalReward {
    /**
     * Get associated reward type
     */
    VoteGoalRewardType getType();

    /**
     * Give the reward to a specific player. This method is called by the {@link VoteGoalManager} for every player who participated in the vote goal
     */
    void give(final UUID player);

    /**
     * Give a reward to the server. Called once per vote goal
     */
    void giveAll();

    /**
     * Serializes the reward
     */
    String serialize();

    String toString();
}
