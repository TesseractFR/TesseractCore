package onl.tesseract.core.command;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import onl.tesseract.core.command.staff.SocialSpy;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class MsgCommand implements CommandExecutor {
    /**
     * Maps a receiver to its last sender.
     */
    static final Map<CommandSender, CommandSender> messages = new HashMap<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {
        if (args.length < 2)
            return false;

        Player other = Bukkit.getPlayerExact(args[0]);
        if (other != null)
        {
            // Get the message in one string
            StringBuilder message = new StringBuilder();
            for (int i = 1; i < args.length; i++)
                message.append(args[i]).append(" ");

            // Send
            sendMessage(sender, other, message.toString(), true);
        }
        else
            sender.sendMessage(Component.text("Joueur introuvable", NamedTextColor.RED));

        return true;
    }

    public static void sendMessage(@NotNull CommandSender sender, @NotNull CommandSender receiver, @NotNull String message , boolean doSound)
    {
        if (sender.equals(receiver)) return;
        if (receiver instanceof Player && !((OfflinePlayer) receiver).isOnline())
        {
            sender.sendMessage(Component.text(receiver.getName() + " s'est déconnecté.", NamedTextColor.RED));
            messages.remove(sender);
            messages.remove(receiver);
            return;
        }
        messages.put(receiver, sender);

        // Send the message to the receiver
        receiver.sendMessage(Component.text("Reçu de ", NamedTextColor.GOLD, TextDecoration.ITALIC)
                             .append(Component.text(sender.getName() + " » ", NamedTextColor.RED))
                             .clickEvent(ClickEvent.suggestCommand("/msg " + sender.getName() + " "))
                             .append(Component.text(message, NamedTextColor.AQUA, TextDecoration.ITALIC)));
        if (doSound && receiver instanceof Player player)
        {
            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 5, 1);
        }

        // Send feedback
        receiver.sendMessage(Component.text("Envoyé à ", NamedTextColor.GOLD, TextDecoration.ITALIC)
                                      .append(Component.text(sender.getName() + " » ", NamedTextColor.RED))
                                      .clickEvent(ClickEvent.suggestCommand("/msg " + sender.getName() + " "))
                                      .append(Component.text(message, NamedTextColor.GRAY, TextDecoration.ITALIC)));

        // Send to social spies
        SocialSpy.spies.forEach(spy -> {
            Player playerSpy = Bukkit.getPlayer(spy);
            if (playerSpy != null && !playerSpy.equals(sender) && !playerSpy.equals(receiver))
            {
                playerSpy.sendMessage(Component.text("Message de ", NamedTextColor.GRAY, TextDecoration.ITALIC)
                                         .append(Component.text(sender.getName(), NamedTextColor.RED))
                                         .append(Component.text(" envoyé à ", NamedTextColor.GRAY, TextDecoration.ITALIC))
                                         .append(Component.text(receiver.getName() + " » ", NamedTextColor.RED))
                                         .append(Component.text(message, NamedTextColor.GRAY, TextDecoration.ITALIC)));
            }
        });
    }

    @Nullable
    public static CommandSender getReplyTo(CommandSender sender)
    {
        if (messages.containsKey(sender))
            return messages.get(sender);

        else if (messages.containsValue(sender))
            for (CommandSender receiver : messages.keySet())
                if (messages.get(receiver).getName().equals(sender.getName()))
                    return receiver;
        return null;
    }
}
