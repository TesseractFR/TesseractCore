package onl.tesseract.core.command;

import net.kyori.adventure.text.Component;
import onl.tesseract.core.cosmetics.menu.CosmeticMenu;
import onl.tesseract.tesseractlib.cosmetics.Cosmetic;
import onl.tesseract.core.cosmetics.CosmeticManager;
import onl.tesseract.tesseractlib.util.ChatFormats;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class CosmeticCommand implements CommandExecutor {
    @Override
    @SuppressWarnings("deprecation")
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s,
                             @NotNull String[] args)
    {

        if(!commandSender.hasPermission("cosmetic.admin") || args.length < 3){
            if(commandSender instanceof Player player){
                new CosmeticMenu(player.getUniqueId(),null).open(player);
            }
            return true;
        }
        OfflinePlayer player = Bukkit.getOfflinePlayer(args[1]);
        UUID uuid = player.getUniqueId();
        String type = args[2];
        Cosmetic cosmetic;

        try{
            cosmetic = CosmeticManager.stringToCosmetic(type, args[3]);
        }
        catch (IllegalArgumentException e)
        {
            commandSender.sendMessage(ChatFormats.COSMETICS_ERROR+ "Cosmetique inconnu.");
            return false;
        }


        switch (args[0])
        {
            case "give" -> {
                CosmeticManager.giveCosmetic(uuid, cosmetic);
                commandSender.sendMessage(ChatFormats.COSMETICS_SUCCESS
                                                  .append(Component.text("Tentative d'ajout effectuée.")));
                return true;
            }
            case "remove" -> {
                CosmeticManager.removeCosmetic(uuid, cosmetic);
                commandSender.sendMessage(ChatFormats.COSMETICS_SUCCESS
                        .append(Component.text("Tentative de retrait effectuée.")));
                return true;
            }
            default -> {
                commandSender.sendMessage(ChatFormats.COSMETICS_ERROR.append(Component.text("Commande inconnue.")));
                return true;
            }
        }
    }
}
