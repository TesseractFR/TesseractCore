package onl.tesseract.core.boutique.menu

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import onl.tesseract.core.boutique.BoutiqueService
import onl.tesseract.core.cosmetics.CosmeticManager
import onl.tesseract.core.cosmetics.ElytraTrails
import onl.tesseract.lib.menu.ItemBuilder
import onl.tesseract.lib.menu.Menu
import onl.tesseract.lib.menu.MenuSize
import onl.tesseract.lib.service.ServiceContainer
import onl.tesseract.lib.util.ItemLoreBuilder
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import java.util.*

class ElytraTrailBoutiqueMenu(
    val playerID: UUID,
    previous: Menu? = null
) : Menu(MenuSize.Three, Component.text("Boutique des illages d'ailes"), previous) {

    override fun placeButtons(viewer: Player) {
        val boutiqueService = ServiceContainer[BoutiqueService::class.java]
        val playerInfo = boutiqueService.getPlayerBoutiqueInfo(playerID)
        fill(ItemBuilder(Material.GRAY_STAINED_GLASS_PANE, " ").build())
        ElytraTrails.entries.forEach { trail ->
            if (trail == ElytraTrails.NONE) return@forEach
            if (!CosmeticManager.hasCosmetic(playerID, trail)) {
                addButton(
                    trail.getIndex(),
                    ItemBuilder(trail.material)
                        .name(trail.getDisplayName())
                        .lore(
                            ItemLoreBuilder()
                                .append("Cliquez pour acheter ")
                                .append(trail.getDisplayName())
                                .newline()
                                .append("Coût : ${trail.price} lys d'or", NamedTextColor.GRAY)
                                .newline()
                                .append("Vous avez : ${playerInfo.marketCurrency} lys d'or", NamedTextColor.GRAY)
                                .get()
                        )
                        .build()
                ) {
                    boutiqueService.tryToBuy(Bukkit.getPlayer(playerID) ?: return@addButton, this, trail)
                }
            } else {
                addButton(trail.index, ItemBuilder(Material.STRUCTURE_VOID)
                    .name(trail.getDisplayName())
                    .lore(ItemLoreBuilder().append("Vous possédez déjà ce sillage", NamedTextColor.GRAY).get())
                    .build())
            }
        }
        addBackButton()
        addCloseButton()
    }
}