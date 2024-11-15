package onl.tesseract.core.command;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import onl.tesseract.core.TesseractCorePlugin;
import onl.tesseract.core.vote.VoteMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.UUID;
import java.util.logging.Level;

public class VoteCommand implements CommandExecutor {

    private static Class<? extends VoteMenu> voteMenuClass = VoteMenu.class;

    @Override
    public boolean onCommand(@NotNull final CommandSender sender, @NotNull final Command command, @NotNull final String label,
                             final @NotNull String[] args)
    {
        if (!(sender instanceof Player player))
            return false;

        VoteMenu voteMenu;
        try
        {
            Constructor<? extends VoteMenu> declaredConstructor = voteMenuClass.getDeclaredConstructor(UUID.class);
            voteMenu = declaredConstructor.newInstance(player.getUniqueId());
        }
        catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e)
        {
            TesseractCorePlugin.instance.getLogger().log(Level.SEVERE, "Failed to instantiate vote menu", e);
            sender.sendMessage(Component.text("Une erreur inattendue est survenue. Veuillez contactez un administrateur.", NamedTextColor.RED));
            return true;
        }
        voteMenu.open(player);
        return true;
    }

    public static void setVoteMenuClass(final Class<? extends VoteMenu> voteMenuClass)
    {
        VoteCommand.voteMenuClass = voteMenuClass;
    }
}
