package onl.tesseract.core.command;

import onl.tesseract.core.cosmetics.menu.ElytraTrailSelectionMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ElytraTrailCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s,
                             @NotNull String[] strings)
    {
        if(commandSender instanceof Player player)
            new ElytraTrailSelectionMenu(player.getUniqueId(),null).open(player);
        return true;
    }
}
