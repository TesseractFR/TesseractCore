package onl.tesseract.core.command.staff;

import net.kyori.adventure.text.Component;
import onl.tesseract.core.boutique.BoutiqueService;
import onl.tesseract.core.boutique.PlayerBoutiqueInfo;
import onl.tesseract.lib.service.ServiceContainer;
import onl.tesseract.tesseractlib.TesseractLib;
import onl.tesseract.tesseractlib.player.TPlayer;
import onl.tesseract.tesseractlib.util.ChatFormats;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

public class MarketCurrencyCommand implements CommandExecutor {
    @Override
    @SuppressWarnings("deprecation")
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s,
                             @NotNull String[] args)
    {
        if (!commandSender.hasPermission("cosmetic.admin"))
        {
            commandSender.sendMessage("Vous n'avez pas la permission d'utiliser cette commande");
            return true;
        }
        if (args.length < 2)
        {
            return false;
        }

        OfflinePlayer player = Bukkit.getOfflinePlayer(args[1]);
        if (!player.hasPlayedBefore())
        {
            commandSender.sendMessage("Joueur invalide");
            return true;
        }


        new BukkitRunnable() {
            @Override
            public void run()
            {
                int amount;
                BoutiqueService boutiqueService = ServiceContainer.get(BoutiqueService.class);
                PlayerBoutiqueInfo playerBoutiqueInfo = boutiqueService.getPlayerBoutiqueInfo(player.getUniqueId());

                switch (args[0])
                {
                    case "give" -> {
                        if (args.length < 3)
                        {
                            commandSender.sendMessage("Veuillez indiquer un montant à donner");
                            break;
                        }
                        amount = Integer.parseInt(args[2]);
                        boutiqueService.addMarketCurrency(player.getUniqueId(), amount);
                        commandSender.sendMessage("Le joueur " + player.getName() + " a reçu " + amount + " lys d'or");
                        if(player.isOnline())
                            player.getPlayer().sendMessage(ChatFormats.COSMETICS.append(Component.text("Vous avez reçu "+amount+
                                                                                                    " lys d'or")));
                    }
                    case "remove" -> {
                        if (args.length < 3)
                        {
                            commandSender.sendMessage("Veuillez indiquer un montant à donner");
                            break;
                        }
                        amount = Integer.parseInt(args[2]);
                        boutiqueService.addMarketCurrency(player.getUniqueId(), -amount);
                        commandSender.sendMessage("Le joueur " + player.getName() + " a perdu " + amount + " lys d'or");
                        if(player.isOnline())
                            player.getPlayer().sendMessage(ChatFormats.COSMETICS.append(
                                    Component.text("Vous avez été déduit de "+amount+" lys d'or")));
                    }
                    case "get" -> commandSender.sendMessage(
                            "Le joueur " + player.getName() + " possède " + playerBoutiqueInfo.getMarketCurrency() + " lys d'or");
                }
            }
        }.runTaskAsynchronously(TesseractLib.instance);



        return true;
    }
}
