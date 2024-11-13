package onl.tesseract.core.boutique.menu

import net.kyori.adventure.text.format.NamedTextColor
import onl.tesseract.lib.menu.ItemBuilder
import onl.tesseract.lib.menu.Menu
import onl.tesseract.lib.menu.MenuSize
import org.bukkit.Material
import org.bukkit.entity.Player
import java.util.UUID

class BoutiqueMenu(val playerID: UUID) : Menu(MenuSize.Three, "Boutique de Tesseract", NamedTextColor.BLUE) {

    override fun placeButtons(viewer: Player) {
        addButton(
            13, ItemBuilder(Material.AMETHYST_CLUSTER)
                .name("Tous les serveurs", NamedTextColor.LIGHT_PURPLE)
                .lore()
                .newline()
                .append("Cliquez pour afficher les cosmetiques disponibles sur tout les serveurs.", NamedTextColor.GRAY)
                .buildLore().build()
        ) {
            GlobalBoutiqueMenu(playerID, this).open(viewer)
        }
    }
}