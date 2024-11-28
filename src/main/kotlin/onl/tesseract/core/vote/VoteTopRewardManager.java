package onl.tesseract.core.vote;

import kotlin.Pair;
import net.kyori.adventure.text.Component;
import onl.tesseract.core.boutique.BoutiqueService;
import onl.tesseract.lib.service.ServiceContainer;
import onl.tesseract.lib.util.ChatFormats;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;

public class VoteTopRewardManager {

    public static final Logger logger = LoggerFactory.getLogger(VoteTopRewardManager.class);

    private VoteTopRewardManager() {
    }

    public static void giveReward(final int[] rewards)
    {
        if (rewards.length != 3)
            throw new IllegalArgumentException("Expected array of length 3");
        List<Pair<UUID, Integer>> top = ServiceContainer.get(VoteService.class).getTop(1);

        int index = 0;
        for (final Pair<UUID, Integer> player : top)
        {
            if (index == 3)
                break;
            logger.info("Giving top {} vote reward to {}", (index + 1), player.getFirst().toString());
            giveReward(player.getFirst(), rewards[index++]);
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
