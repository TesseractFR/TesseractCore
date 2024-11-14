package onl.tesseract.core.cosmetics.menu

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import onl.tesseract.lib.menu.ItemBuilder
import onl.tesseract.lib.menu.Menu
import onl.tesseract.lib.menu.MenuSize
import onl.tesseract.tesseractlib.cosmetics.familier.PetCategory
import onl.tesseract.tesseractlib.cosmetics.familier.PetManager
import onl.tesseract.tesseractlib.util.ChatFormats
import org.bukkit.Material
import org.bukkit.entity.Player
import java.util.UUID

class PetTypeSelectionMenu(val playerID: UUID, previous: Menu? = null) : Menu(
    MenuSize.Two, "Les familiers",
    NamedTextColor.BLUE, previous
) {

    override fun placeButtons(viewer: Player) {
        fill(ItemBuilder(Material.GRAY_STAINED_GLASS_PANE, " ").build())
        PetCategory.entries.forEachIndexed { index, petCategory ->
            addButton(
                index, ItemBuilder(petCategory.getHead())
                        .name("Familiers de type " + petCategory.name, NamedTextColor.YELLOW)
                        .lore()
                        .newline()
                        .append("Cliquez pour avoir la liste des familiers du type $petCategory", NamedTextColor.GRAY)
                        .buildLore()
                        .build()) {
                 PetSelectionMenu(playerID, petCategory, this).open(viewer)
            }
        }

        addButton(
            13, ItemBuilder(Material.NAME_TAG)
                    .name("Désinvocation", NamedTextColor.YELLOW)
                    .lore()
                    .newline()
                    .append("Cliquez pour désinvoquer votre familier", NamedTextColor.GRAY)
                    .buildLore()
                    .build()) {
            if (PetManager.invokedPets[viewer.uniqueId] != null) {
                PetManager.invokePet(viewer, null)
                viewer.sendMessage(
                    ChatFormats.PET.append(
                        Component.text(
                            "Votre familier a été désinvoqué",
                            NamedTextColor.GREEN)))
                this.close()
            }
        }
        this.addBackButton()
        this.addCloseButton()
    }
}