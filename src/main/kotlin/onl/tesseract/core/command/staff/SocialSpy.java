package onl.tesseract.core.command.staff;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;

public class SocialSpy implements CommandExecutor {
    /**
     * Set of players who are spying
     */
    static public final Collection<UUID> spies = new HashSet<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {
        if (! (sender instanceof Player player)) return false;

        if (spies.contains(player.getUniqueId())) {
            spies.remove(player.getUniqueId());
            sender.sendMessage(ChatColor.LIGHT_PURPLE + "Social spy désactivé");
        }
        else {
            spies.add(player.getUniqueId());
            sender.sendMessage(ChatColor.LIGHT_PURPLE + "Social spy activé");
        }

        return true;
    }
}
