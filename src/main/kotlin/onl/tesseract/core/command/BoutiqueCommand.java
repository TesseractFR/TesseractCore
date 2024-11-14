package onl.tesseract.core.command;

import onl.tesseract.core.boutique.menu.BoutiqueMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class BoutiqueCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s,
                             @NotNull String[] strings)
    {
        if(commandSender instanceof Player player)
            new BoutiqueMenu(player.getUniqueId()).open(player);
        return true;
    }

}
