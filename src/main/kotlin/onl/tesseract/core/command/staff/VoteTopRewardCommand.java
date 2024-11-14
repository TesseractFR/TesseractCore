package onl.tesseract.core.command.staff;

import onl.tesseract.core.vote.VoteTopRewardManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.jetbrains.annotations.NotNull;

public class VoteTopRewardCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull final CommandSender sender, @NotNull final Command command, @NotNull final String label,
                             final @NotNull String[] args)
    {
        if (!(sender instanceof ConsoleCommandSender))
        {
            sender.sendMessage("Cette commande ne peut se faire que depuis la console.");
            return true;
        }
        if (args.length != 3)
            return false;

        int[] rewards = new int[3];
        for (int i = 0; i < args.length; i++)
        {
            try
            {
                rewards[i] = Integer.parseInt(args[i]);
            }
            catch (NumberFormatException e)
            {
                sender.sendMessage(ChatColor.RED + "Nombre invalide");
            }
        }

        VoteTopRewardManager.giveReward(rewards);
        sender.sendMessage(ChatColor.GREEN + "Récompenses distribuées");
        return true;
    }
}
