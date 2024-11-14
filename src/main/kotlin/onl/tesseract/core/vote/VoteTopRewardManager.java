package onl.tesseract.core.vote;

import net.kyori.adventure.text.Component;
import onl.tesseract.core.boutique.BoutiqueService;
import onl.tesseract.lib.service.ServiceContainer;
import onl.tesseract.tesseractlib.TesseractLib;
import onl.tesseract.core.persistence.hibernate.vote.VoteRepository;
import onl.tesseract.tesseractlib.util.ChatFormats;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.LinkedHashMap;
import java.util.UUID;
import java.util.logging.Level;

public class VoteTopRewardManager {

    public static void giveReward(final int[] rewards)
    {
        if (rewards.length != 3)
            throw new IllegalArgumentException("Expected array of length 3");
        LinkedHashMap<UUID, Integer> top = VoteRepository.getTop(1);

        int index = 0;
        for (final UUID player : top.keySet())
        {
            if (index == 3)
                break;
            TesseractLib.logger().log(Level.INFO, "Giving top " + (index + 1) + " vote reward to " + player.toString());
            giveReward(player, rewards[index++]);
        }
    }

    private static void giveReward(final UUID uuid, final int reward)
    {
        BoutiqueService boutiqueService = ServiceContainer.get(BoutiqueService.class);
        boutiqueService.addMarketCurrency(uuid, reward);
        Player player = Bukkit.getPlayer(uuid);
        if (player != null)
            player.sendMessage(ChatFormats.VOTE.append(Component.text("Tu est au top du classement des votes ! Tu as reçu " + reward + " lys d'or !")));
    }
}
