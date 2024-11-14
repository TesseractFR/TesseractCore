package onl.tesseract.core.vote.goal;

import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Defines a type of reward.
 */
public interface VoteGoalRewardType {
    /**
     * Named used to identify the type
     */
    String getName();

    /**
     * Deserialize a reward from a raw string
     */
    VoteGoalReward deserialize(final String raw) throws IllegalArgumentException;

    VoteGoalReward fromArgs(final String[] args) throws IllegalArgumentException;

    @Nullable List<String> tabCompletion(final String[] args);
}

