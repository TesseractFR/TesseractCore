package onl.tesseract.core.boutique.menu

import net.kyori.adventure.text.format.NamedTextColor
import onl.tesseract.core.boutique.BoutiqueService
import onl.tesseract.lib.menu.ItemBuilder
import onl.tesseract.lib.menu.Menu
import onl.tesseract.lib.menu.MenuSize
import onl.tesseract.lib.service.ServiceContainer
import onl.tesseract.core.cosmetics.CosmeticManager
import onl.tesseract.core.cosmetics.FlyFilter
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import java.util.UUID

class FlyFilterBoutiqueMenu(
    val playerID: UUID,
    previous: Menu? = null,
) : Menu(MenuSize.Three, "Boutique des filtres de vol", previous = previous) {

    override fun placeButtons(viewer: Player) {
        fill(ItemBuilder(Material.GRAY_STAINED_GLASS_PANE, " ").build())
        val boutiqueService = ServiceContainer[BoutiqueService::class.java]
        val playerBoutiqueInfo = boutiqueService.getPlayerBoutiqueInfo(playerID)

        FlyFilter.entries.filter { it != FlyFilter.NONE }.forEach { filter ->
            if (!CosmeticManager.hasCosmetic(playerID, FlyFilter.getTypeName(), filter)) {
                addButton(
                    filter.index, ItemBuilder(filter.material, filter.name)
                        .lore()
                        .append("Cliquez pour acheter ${filter.getName()}")
                        .newline()
                        .append("Coût : ${filter.price} lys d'or", NamedTextColor.GRAY)
                        .newline()
                        .append("Vous avez : ${playerBoutiqueInfo.marketCurrency} lys d'or", NamedTextColor.GRAY)
                        .buildLore()
                        .build()
                ) {
                    boutiqueService.tryToBuy(Bukkit.getPlayer(playerID) ?: return@addButton, this, filter)
                }
            } else {
                addButton(
                    filter.index,
                    ItemBuilder(Material.STRUCTURE_VOID)
                        .name(filter.getName())
                        .lore()
                        .append("Vous possédez déjà ce sillage", NamedTextColor.GRAY)
                        .buildLore()
                        .build()
                )
            }
        }

        addCloseButton()
        addBackButton()
    }
}