package onl.tesseract.core.cosmetics.menu

import net.kyori.adventure.text.format.NamedTextColor
import onl.tesseract.core.boutique.BoutiqueService
import onl.tesseract.lib.menu.ItemBuilder
import onl.tesseract.lib.menu.Menu
import onl.tesseract.lib.menu.MenuSize
import onl.tesseract.lib.service.ServiceContainer
import onl.tesseract.core.cosmetics.CosmeticManager
import onl.tesseract.core.cosmetics.FlyFilter
import onl.tesseract.lib.util.ItemLoreBuilder
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import java.util.UUID

class FlyFilterSelectionMenu(val playerID: UUID, previous: Menu? = null) : Menu(
    MenuSize.Three, "Filtre de vol", NamedTextColor.BLUE, previous
) {

    override fun placeButtons(viewer: Player) {
        // For each existing trails
        val boutiqueService = ServiceContainer[BoutiqueService::class.java]
        val playerBoutiqueInfo = boutiqueService.getPlayerBoutiqueInfo(playerID)
        fill(ItemBuilder(Material.GRAY_STAINED_GLASS_PANE, " ").build())
        FlyFilter.entries.forEach { filter ->
            if (filter != FlyFilter.NONE) {
                val hasFilter = CosmeticManager.hasCosmetic(playerID, FlyFilter.getTypeName(), filter)

                val lore = ItemLoreBuilder().newline()
                if (hasFilter) lore.append("Débloqué", NamedTextColor.GREEN).newline()
                    .append("Cliquez pour utiliser le filtre ${filter.name}")
                else lore.append("Bloqué", NamedTextColor.RED).newline().append("Cliquez pour acheter ${filter.name}")
                    .newline().append("Coût : ${filter.price} lys d'or", NamedTextColor.GRAY).newline()
                    .append("Vous avez : ${playerBoutiqueInfo.marketCurrency} lys d'or", NamedTextColor.GRAY)

                addButton(
                    filter.index,
                    ItemBuilder(filter.material).name(filter.name).enchanted(playerBoutiqueInfo.activeFlyFilter == filter)
                        .lore(lore.get()).build()
                ) {
                    if (hasFilter) {
                        boutiqueService.setActiveFlyFilter(playerID, filter)
                        this.close()
                    } else {
                        boutiqueService.tryToBuy(Bukkit.getPlayer(playerID) ?: return@addButton, this, filter)
                    }
                }
            } else {
                addButton(
                    22,
                    ItemBuilder(filter.getMaterial()).name(filter.name).enchanted(playerBoutiqueInfo.activeFlyFilter == filter)
                        .build()
                ) {
                    boutiqueService.setActiveFlyFilter(playerID, filter)
                    this.close()
                }
            }
        }

        addBackButton()
        addCloseButton()
    }
}