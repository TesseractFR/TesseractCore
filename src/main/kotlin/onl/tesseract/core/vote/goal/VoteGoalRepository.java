package onl.tesseract.core.vote.goal;

import onl.tesseract.core.TesseractCorePlugin;
import onl.tesseract.tesseractlib.TesseractLib;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.sql.*;
import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.UUID;
import java.util.logging.Level;

/**
 * Get vote goals from the database
 */
public final class VoteGoalRepository {
    /**
     * Find all current goals, i.e, goals that are started and not yet ended
     *
     * @return Collection of current goals
     */
    public static Collection<VoteGoal> getCurrentGoals()
    {
        try
        {
            final Connection connection = TesseractCorePlugin.instance.getBddManager().getBddConnection().getConnection();
            final PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM t_vote_goal WHERE start <= NOW() AND end > NOW()");
            final ResultSet resultSet = statement.executeQuery();

            final Collection<VoteGoal> goals = new HashSet<>();
            while (resultSet.next())
            {
                final int id = resultSet.getInt("id");
                final Instant start = resultSet.getTimestamp("start").toInstant();
                final Instant end = resultSet.getTimestamp("end").toInstant();
                final int quantity = resultSet.getInt("quantity");
                final String rewardTypeName = resultSet.getString("reward_type");
                final String rewardRaw = resultSet.getString("reward_raw");
                final VoteGoalRewardType type;
                final VoteGoalReward reward;
                try
                {
                    if (rewardTypeName == null)
                        reward = null;
                    else
                    {
                        type = VoteGoalRewardManager.getRewardType(rewardTypeName);
                        reward = type.deserialize(rewardRaw);
                    }
                }
                catch (IllegalArgumentException e)
                {
                    TesseractLib.logger().log(Level.WARNING, "Failed to load vote goal from database", e);
                    continue;
                }

                goals.add(new VoteGoal(id, start, end, quantity, reward));
            }
            return goals;
        }
        catch (SQLException throwables)
        {
            TesseractLib.instance.getLogger().log(Level.SEVERE, "Failed to execute sql statement", throwables);
        }
        return Collections.emptyList();
    }

    /**
     * Get the amount of votes made between two timestamps
     */
    public static int getVoteCount(final Timestamp start, final Timestamp end)
    {
        try
        {
            final Connection connection = TesseractCorePlugin.instance.getBddManager().getBddConnection().getConnection();
            final PreparedStatement statement = connection.prepareStatement(
                    "SELECT count(*) FROM t_vote WHERE date >= ? AND date <= ?");
            statement.setTimestamp(1, start);
            statement.setTimestamp(2, end);
            final ResultSet resultSet = statement.executeQuery();
            return resultSet.next() ? resultSet.getInt(1) : 0;
        }
        catch (SQLException throwables)
        {
            TesseractLib.instance.getLogger().log(Level.SEVERE, "Failed to execute sql statement", throwables);
            return 0;
        }
    }

    /**
     * Get the amount of votes made during the period of time defined by the given vote goal
     */
    public static int getVoteCount(final VoteGoal goal)
    {
        return getVoteCount(new Timestamp(goal.start().toEpochMilli()), new Timestamp(goal.end().toEpochMilli()));
    }

    /**
     * Insert a new vote goal into the database. An ID will be generated on the database, but won't be set on the given object.
     *
     * @param toCreate Goal to create. Should have an id of -1
     */
    public static void createVoteGoal(final VoteGoal toCreate)
    {
        try
        {
            final Connection connection = TesseractCorePlugin.instance.getBddManager().getBddConnection().getConnection();
            final PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO t_vote_goal (start, end, quantity, reward_type, reward_raw) VALUES (?, ?, ?, ?, ?)");
            statement.setTimestamp(1, new Timestamp(toCreate.start().toEpochMilli()));
            statement.setTimestamp(2, new Timestamp(toCreate.end().toEpochMilli()));
            statement.setInt(3, toCreate.requiredQuantity());
            statement.setString(4, toCreate.reward() == null ? null : toCreate.reward().getType().getName());
            statement.setString(5, toCreate.reward() == null ? null : toCreate.reward().serialize());
            statement.executeUpdate();
        }
        catch (SQLException throwables)
        {
            TesseractLib.instance.getLogger().log(Level.SEVERE, "Failed to execute sql statement", throwables);
        }
    }

    /**
     * Get a collection of all players that contributed to the completion of the given vote goal
     */
    public static Collection<OfflinePlayer> getContributors(final VoteGoal voteGoal)
    {
        try
        {
            Collection<OfflinePlayer> contributors = new HashSet<>();
            final Connection connection = TesseractCorePlugin.instance.getBddManager().getBddConnection().getConnection();
            final PreparedStatement statement = connection.prepareStatement(
                    "SELECT DISTINCT player_uuid FROM t_vote WHERE date >= ? AND date <= ?");
            statement.setTimestamp(1, new Timestamp(voteGoal.start().toEpochMilli()));
            statement.setTimestamp(2, new Timestamp(voteGoal.end().toEpochMilli()));
            final ResultSet resultSet = statement.executeQuery();
            while (resultSet.next())
            {
                contributors.add(Bukkit.getOfflinePlayer(UUID.fromString(resultSet.getString(1))));
            }

            return contributors;
        }
        catch (SQLException throwables)
        {
            TesseractLib.instance.getLogger().log(Level.SEVERE, "Failed to retrieve vote goal contributors", throwables);
            return Collections.emptyList();
        }
    }
}
