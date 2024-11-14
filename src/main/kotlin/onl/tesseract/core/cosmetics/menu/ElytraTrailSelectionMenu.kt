package onl.tesseract.core.cosmetics.menu

import net.kyori.adventure.text.format.NamedTextColor
import onl.tesseract.core.boutique.BoutiqueService
import onl.tesseract.lib.menu.ItemBuilder
import onl.tesseract.lib.menu.Menu
import onl.tesseract.lib.menu.MenuSize
import onl.tesseract.lib.service.ServiceContainer
import onl.tesseract.core.cosmetics.CosmeticManager
import onl.tesseract.tesseractlib.cosmetics.ElytraTrails
import onl.tesseract.tesseractlib.util.ItemLoreBuilder
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import java.util.UUID

class ElytraTrailSelectionMenu(val playerID: UUID, previous: Menu? = null) : Menu(
    MenuSize.Three, "Sillages d'ailes", NamedTextColor.BLUE, previous
) {

    override fun placeButtons(viewer: Player) {
        // For each existing trails
        val boutiqueService = ServiceContainer[BoutiqueService::class.java]
        val playerBoutiqueInfo = boutiqueService.getPlayerBoutiqueInfo(playerID)
        fill(ItemBuilder(Material.GRAY_STAINED_GLASS_PANE, " ").build())
        ElytraTrails.entries.forEach { trail ->
            if (trail != ElytraTrails.NONE) {
                val hasTrail = CosmeticManager.hasCosmetic(playerID, ElytraTrails.getTypeName(), trail)

                val lore = ItemLoreBuilder().newline()
                if (hasTrail) lore.append("Débloqué", NamedTextColor.GREEN).newline()
                    .append("Cliquez pour utiliser le sillage ${trail.name}")
                else lore.append("Bloqué", NamedTextColor.RED).newline().append("Cliquez pour acheter ${trail.name}")
                    .newline().append("Coût : ${trail.price} lys d'or", NamedTextColor.GRAY).newline()
                    .append("Vous avez : ${playerBoutiqueInfo.marketCurrency} lys d'or", NamedTextColor.GRAY)

                addButton(
                    trail.index,
                    ItemBuilder(trail.material).name(trail.name).enchanted(playerBoutiqueInfo.activeTrail == trail)
                        .lore(lore.get()).build()
                ) {
                    if (hasTrail) {
                        boutiqueService.setActiveElytraTrail(playerID, trail)
                        this.close()
                    } else {
                        boutiqueService.tryToBuy(Bukkit.getPlayer(playerID) ?: return@addButton, this, trail)
                    }
                }
            } else {
                addButton(
                    22,
                    ItemBuilder(trail.getMaterial()).name(trail.name).enchanted(playerBoutiqueInfo.activeTrail == trail)
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