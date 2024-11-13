package onl.tesseract.core.vote

import net.kyori.adventure.text.format.NamedTextColor
import onl.tesseract.lib.menu.ItemBuilder
import onl.tesseract.lib.menu.Menu
import onl.tesseract.lib.menu.MenuSize
import org.bukkit.Material
import org.bukkit.entity.Player
import java.util.UUID

class DefaultVoteRewardMenu(playerID: UUID, size: MenuSize, previous: Menu? = null) :
    AVoteRewardMenu(playerID, size, previous) {

    override fun placeButtons(viewer: Player) {
        fill(ItemBuilder(Material.GRAY_STAINED_GLASS_PANE, " ").build())
        addBackButton()
        addCloseButton()

        addNoRewardButton(4)
    }

    fun addNoRewardButton(index: Int) {
        addButton(
            index, ItemBuilder(Material.STRUCTURE_VOID)
                .name("Récompense", NamedTextColor.GOLD)
                .lore()
                .newline()
                .append(
                    "Il n'y a pour l'instant aucune récompense disponible pour ce serveur. Pas de panique ! Tes points de vote peuvent "
                            + "être utilisés sur les serveurs suivants :"
                )
                .newline()
                .append("→ SemiRP")
                .buildLore()
                .build()
        )
    }
}