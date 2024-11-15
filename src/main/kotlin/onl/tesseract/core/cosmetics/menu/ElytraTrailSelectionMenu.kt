package onl.tesseract.core.cosmetics.menu

import onl.tesseract.core.boutique.BoutiqueService
import onl.tesseract.core.cosmetics.CosmeticManager
import onl.tesseract.core.cosmetics.ElytraTrails
import onl.tesseract.lib.menu.ItemBuilder
import onl.tesseract.lib.menu.Menu
import onl.tesseract.lib.menu.MenuSize
import onl.tesseract.lib.service.ServiceContainer
import org.bukkit.Material
import org.bukkit.entity.Player
import java.util.UUID

class ElytraTrailSelectionMenu(val playerID: UUID, previous: Menu? = null) : AbstractCosmeticMenu(
    MenuSize.Three, "Sillages d'ailes", previous
) {

    override fun placeButtons(viewer: Player) {
        // For each existing trails
        val boutiqueService = ServiceContainer[BoutiqueService::class.java]
        val playerBoutiqueInfo = boutiqueService.getPlayerBoutiqueInfo(playerID)
        fill(ItemBuilder(Material.GRAY_STAINED_GLASS_PANE, " ").build())
        ElytraTrails.entries.forEach { trail ->
            if (trail != ElytraTrails.NONE) {
                val hasTrail = CosmeticManager.hasCosmetic(playerID, ElytraTrails.getTypeName(), trail)

                placeCosmeticButton(trail.index, trail, hasTrail, playerID) {
                    boutiqueService.setActiveElytraTrail(playerID, it)
                }
            } else {
                addButton(
                    22,
                    ItemBuilder(trail.material).name(trail.name).enchanted(playerBoutiqueInfo.activeTrail == trail)
                        .build()
                ) {
                    boutiqueService.setActiveElytraTrail(playerID, trail)
                    this.close()
                }
            }
        }

        addBackButton()
        addCloseButton()
    }
}