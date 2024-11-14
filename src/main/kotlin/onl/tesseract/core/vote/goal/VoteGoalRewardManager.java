package onl.tesseract.core.vote.goal;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashMap;

public class VoteGoalRewardManager {
    private static final HashMap<String, VoteGoalRewardType> rewardTypes = new HashMap<>();

    public static void registerRewardType(final VoteGoalRewardType reward)
    {
        rewardTypes.put(reward.getName(), reward);
    }

    @NotNull
    public static VoteGoalRewardType getRewardType(final String name) throws IllegalArgumentException
    {
        if (!rewardTypes.containsKey(name))
            throw new IllegalArgumentException("Unknown reward type " + name);
        return rewardTypes.get(name);
    }

    public static Collection<String> getRegisteredRewardTypes()
    {
        return rewardTypes.keySet();
    }
}
