package onl.tesseract.core.command.staff;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.UUID;

public class SocialSpy implements CommandExecutor {
    /**
     * Set of players who are spying
     */
    private static final Collection<UUID> spies = new HashSet<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {
        if (! (sender instanceof Player player)) return false;

        if (spies.contains(player.getUniqueId())) {
            spies.remove(player.getUniqueId());
            sender.sendMessage(Component.text("Social spy désactivé", NamedTextColor.LIGHT_PURPLE));
        }
        else {
            spies.add(player.getUniqueId());
            sender.sendMessage(Component.text("Social spy activé", NamedTextColor.LIGHT_PURPLE));
        }

        return true;
    }

    public static Collection<UUID> getSpies() {
        return Collections.unmodifiableCollection(spies);
    }
}
