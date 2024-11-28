package onl.tesseract.core.cosmetics.menu

import onl.tesseract.core.boutique.BoutiqueService
import onl.tesseract.core.cosmetics.CosmeticManager
import onl.tesseract.core.cosmetics.FlyFilter
import onl.tesseract.lib.menu.ItemBuilder
import onl.tesseract.lib.menu.Menu
import onl.tesseract.lib.menu.MenuSize
import onl.tesseract.lib.service.ServiceContainer
import org.bukkit.Material
import org.bukkit.entity.Player
import java.util.*

class FlyFilterSelectionMenu(val playerID: UUID, previous: Menu? = null) : AbstractCosmeticMenu(
    MenuSize.Three, "Filtre de vol", previous
) {

    override fun placeButtons(viewer: Player) {
        // For each existing trails
        val boutiqueService = ServiceContainer[BoutiqueService::class.java]
        val playerBoutiqueInfo = boutiqueService.getPlayerBoutiqueInfo(playerID)
        fill(ItemBuilder(Material.GRAY_STAINED_GLASS_PANE, " ").build())
        FlyFilter.entries.forEach { filter ->
            if (filter != FlyFilter.NONE) {
                val hasFilter = CosmeticManager.hasCosmetic(playerID, filter)
                placeCosmeticButton(filter.index, filter, hasFilter, playerID) {
                    boutiqueService.setActiveFlyFilter(playerID, it)
                }
            } else {
                addButton(
                    22,
                    ItemBuilder(filter.material).name(filter.name).enchanted(playerBoutiqueInfo.activeFlyFilter == filter)
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