package onl.tesseract.core.cosmetics.menu

import net.kyori.adventure.text.format.NamedTextColor
import onl.tesseract.core.boutique.BoutiqueService
import onl.tesseract.core.cosmetics.CosmeticWithMaterial
import onl.tesseract.lib.menu.ItemBuilder
import onl.tesseract.lib.menu.Menu
import onl.tesseract.lib.menu.MenuSize
import onl.tesseract.lib.service.ServiceContainer
import onl.tesseract.lib.util.ItemLoreBuilder
import org.bukkit.Bukkit
import java.util.UUID

abstract class AbstractCosmeticMenu(
    size: MenuSize,
    title: String,
    previous: Menu? = null,
) : Menu(size, title, NamedTextColor.BLUE, previous) {

    protected fun <T : CosmeticWithMaterial> placeCosmeticButton(
        index: Int,
        cosmetic: T,
        possessed: Boolean,
        playerID: UUID,
        onBuy: (T) -> Unit,
    ) {
        val boutiqueService = ServiceContainer[BoutiqueService::class.java]
        val playerBoutiqueInfo = boutiqueService.getPlayerBoutiqueInfo(playerID)
        val lore = ItemLoreBuilder().newline()
        if (possessed)
            lore.append("Débloqué", NamedTextColor.GREEN)
                    .newline()
                    .append("Cliquez pour utiliser le cosmétique ${cosmetic.name}")
        else
            lore.append("Bloqué", NamedTextColor.RED).newline()
                    .append("Cliquez pour acheter ${cosmetic.name}")
                    .newline()
                    .append("Coût : ${cosmetic.price} lys d'or", NamedTextColor.GRAY)
                    .newline()
                    .append("Vous avez : ${playerBoutiqueInfo.marketCurrency} lys d'or", NamedTextColor.GRAY)

        addButton(
            index,
            ItemBuilder(cosmetic.material).name(cosmetic.name).enchanted(playerBoutiqueInfo.activeFlyFilter == cosmetic)
                    .lore(lore.get()).build()
        ) {
            if (possessed) {
                onBuy(cosmetic)
                this.close()
            } else {
                boutiqueService.tryToBuy(Bukkit.getPlayer(playerID) ?: return@addButton, this, cosmetic)
            }
        }
    }
}