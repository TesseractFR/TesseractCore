package onl.tesseract.core.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ReplyToMsg implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {
        if (args.length == 0)
            return false;

        CommandSender receiver = MsgCommand.getReplyTo(sender);
        if (receiver != null)
        {
            // Get the message in one string
            StringBuilder message = new StringBuilder();
            for (String arg : args) message.append(arg).append(" ");

            // Reply
            MsgCommand.sendMessage(sender, receiver, message.toString(), false);
        }
        else
            sender.sendMessage(ChatColor.RED + "Il n'y a personne à qui répondre.");
        return true;
    }
}
