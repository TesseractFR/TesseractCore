package onl.tesseract.core.command;

import onl.tesseract.core.cosmetics.menu.PetTypeSelectionMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class FamilierCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
                             @NotNull String[] args)
    {
        if(sender instanceof Player player)
            new PetTypeSelectionMenu(player.getUniqueId(), null).open(player);
        return true;
    }

}
