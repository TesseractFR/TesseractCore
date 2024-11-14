package onl.tesseract.core.command.staff;

import onl.tesseract.tesseractlib.TesseractLib;
import onl.tesseract.core.vote.goal.*;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class VoteGoalCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull final CommandSender sender, @NotNull final Command command, @NotNull final String label,
                             final @NotNull String[] args)
    {
        if (args.length == 0)
            return false;

        if (args[0].equalsIgnoreCase("create") && args.length >= 4)
        {
            create(sender, args);
        }
        if (args[0].equalsIgnoreCase("list"))
        {
            list(sender, args);
        }
        else
            return false;
        return true;
    }

    public void create(final CommandSender sender, final String[] args)
    {
        SimpleDateFormat parser = new SimpleDateFormat("dd/MM/yyyy-HH:mm");
        Date start;
        Date end;
        int quantity;
        try
        {
            start = parser.parse(args[1]);
            end = parser.parse(args[2]);
            quantity = Integer.parseInt(args[3]);
        }
        catch (ParseException e)
        {
            sender.sendMessage(ChatColor.RED + "Date invalide");
            return;
        }
        catch (NumberFormatException e)
        {
            sender.sendMessage(ChatColor.RED + "Quantité invalide");
            return;
        }

        VoteGoalReward reward = null;
        VoteGoalRewardType rewardType;
        if (args.length >= 5)
        {
            try
            {
                rewardType = VoteGoalRewardManager.getRewardType(args[4]);
            }
            catch (IllegalArgumentException e)
            {
                sender.sendMessage(ChatColor.RED + "Type de récompense invalide");
                return;
            }
            try
            {
                reward = rewardType.fromArgs(Arrays.copyOfRange(args, 5, args.length));
            }
            catch (IllegalArgumentException e)
            {
                sender.sendMessage(ChatColor.RED + "Options de récompense invalides");
                return;
            }
        }
        VoteGoal newVoteGoal = new VoteGoal(-1, start.toInstant(), end.toInstant(), quantity, reward);
        VoteGoalRepository.createVoteGoal(newVoteGoal);
        sender.sendMessage(ChatColor.GREEN + "Vote goal créé !");
        TesseractLib.logger().info("Created new vote goal");
    }

    public void list(final CommandSender sender, final String[] args)
    {
        for (VoteGoal goal : VoteGoalManager.getGoals())
        {
            int voteCount = VoteGoalRepository.getVoteCount(goal);
            sender.sendMessage(String.format("- %s : %d/%d", goal.getPrintableRemainingDuration(), voteCount, goal.requiredQuantity()));
        }
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull final CommandSender sender, @NotNull final Command command, @NotNull final String alias,
                                                final @NotNull String[] args)
    {
        if (args.length == 1)
            return List.of("create", "list");

        if (args[0].equalsIgnoreCase("create"))
        {
            if (args.length <= 3)
                return List.of("dd/MM/yyyy-HH:mm");
            if (args.length == 4)
                return List.of("<quantity>");
            if (args.length == 5)
                return VoteGoalRewardManager.getRegisteredRewardTypes().stream().toList();
            try
            {
                VoteGoalRewardType type = VoteGoalRewardManager.getRewardType(args[4]);return type.tabCompletion(Arrays.copyOfRange(args, 5, args.length));
            }catch (IllegalArgumentException e)
            {
                return List.of("");
            }
        }

        return List.of("");
    }
}
