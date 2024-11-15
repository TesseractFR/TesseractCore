package onl.tesseract.core.vote

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import onl.tesseract.core.boutique.BoutiqueService
import onl.tesseract.lib.chat.ChatEntryService
import onl.tesseract.lib.menu.ItemBuilder
import onl.tesseract.lib.menu.Menu
import onl.tesseract.lib.menu.MenuSize
import onl.tesseract.lib.service.ServiceContainer
import onl.tesseract.core.persistence.hibernate.vote.VoteRepository
import onl.tesseract.lib.util.ChatFormats
import onl.tesseract.lib.util.plus
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import java.util.UUID

abstract class AVoteRewardMenu(val playerID: UUID, size: MenuSize, previous: Menu? = null) :
    Menu(size, "Récompenses", NamedTextColor.AQUA, previous) {

    protected fun addLysDorButton(viewer: Player, index: Int) {
        val boutiqueService = ServiceContainer[BoutiqueService::class.java]

        addButton(
            index, ItemBuilder(Material.RAW_GOLD)
                .name("Lys d'or", NamedTextColor.GOLD)
                .lore()
                .newline()
                .append("Échanger des points de vote contre des lys d'or", NamedTextColor.GRAY)
                .newline(2)
                .append("1 point", NamedTextColor.YELLOW)
                .append(" = ", NamedTextColor.GRAY)
                .append(" 1 Lys d'or", NamedTextColor.YELLOW)
                .buildLore()
                .build()
        ) {
            askAmount(viewer) { amount ->
                boutiqueService.addMarketCurrency(playerID, amount)
                Bukkit.getPlayer(playerID)
                    ?.sendMessage(ChatFormats.VOTE + "Vous avez reçu $amount Lys d'or !")
            }
        }
    }

    protected fun askAmount(viewer: Player, callback: (Int) -> Unit) {
        close()
        ServiceContainer[ChatEntryService::class.java].getChatEntry(
            viewer,
            Component.text("Combien de points de vote voulez-vous échanger ?")
        ) { amountStr ->
            try {
                val amount = amountStr.toInt()
                if (amount <= 0) return@getChatEntry
                if (VoteRepository.getKeys(playerID) >= amount) {
                    callback(amount)
                    VoteRepository.removePoints(playerID, amount)
                } else {
                    viewer.sendMessage(ChatFormats.CHAT_ERROR + "Vous n'avez pas suffisamment de points de vote")
                }
            } catch (_: NumberFormatException) {
                viewer.sendMessage(ChatFormats.CHAT_ERROR + "Nombre invalide")
            }
        }
    }

    protected fun hasAmount(amount: Int): Boolean {
        return VoteRepository.getKeys(playerID) >= amount
    }

    protected fun usePoints(viewer: Player, amount: Int, callback: (Int) -> Unit) {
        close()
        if (!hasAmount(amount)) {
            viewer.sendMessage(ChatFormats.CHAT_ERROR + "Vous n'avez pas suffisamment de points de vote")
            return
        }
        VoteRepository.removePoints(playerID, amount)
        callback(amount)
    }
}
