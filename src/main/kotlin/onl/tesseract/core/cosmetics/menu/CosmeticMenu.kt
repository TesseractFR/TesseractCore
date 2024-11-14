package onl.tesseract.core.cosmetics.menu

import net.kyori.adventure.text.format.NamedTextColor
import onl.tesseract.lib.menu.ItemBuilder
import onl.tesseract.lib.menu.Menu
import onl.tesseract.lib.menu.MenuSize
import onl.tesseract.tesseractlib.cosmetics.CosmeticManager
import onl.tesseract.tesseractlib.cosmetics.ElytraTrails
import onl.tesseract.tesseractlib.cosmetics.FlyFilter
import onl.tesseract.tesseractlib.cosmetics.familier.Pet
import org.bukkit.Material
import org.bukkit.entity.Player
import java.util.UUID

class CosmeticMenu(val playerID: UUID, previous: Menu? = null) :
    Menu(MenuSize.Three, "Menu des cosmétiques", NamedTextColor.AQUA, previous) {

    override fun placeButtons(viewer: Player) {
        fill(ItemBuilder(Material.GRAY_STAINED_GLASS_PANE, " ").build())

        val totalPlayerTrail = CosmeticManager.getTotalPossessed(playerID, ElytraTrails.getTypeName())
        addButton(10, ItemBuilder(Material.ELYTRA)
            .name("Sillages des ailes", NamedTextColor.LIGHT_PURPLE)
            .lore()
            .newline()
            .append("$totalPlayerTrail/${ElytraTrails.entries.size} possédés", NamedTextColor.GRAY)
            .newline(2)
            .append("Customisez les particules de vos ailes", NamedTextColor.GRAY)
            .buildLore()
            .build()) {
            ElytraTrailSelectionMenu(playerID, this).open(viewer)
        }

        val totalPlayerFlyFilter = CosmeticManager.getTotalPossessed(playerID, FlyFilter.getTypeName())
        addButton(12, ItemBuilder(Material.BLAZE_POWDER)
            .name("Filtre de vol & jetpack", NamedTextColor.DARK_GREEN)
            .lore()
            .newline()
            .append("$totalPlayerFlyFilter/${FlyFilter.entries.size} possédés", NamedTextColor.GRAY)
            .newline(2)
            .append("Des filtres qui apparaissent lorsque vous voler en Créatif ou lors de l'utilisation du jetpack en Semi-RP", NamedTextColor.GRAY)
            .buildLore()
            .build()) {
            FlyFilterSelectionMenu(playerID, this).open(viewer)
        }

        val totalPlayerPet = CosmeticManager.getTotalPossessed(playerID, Pet.getTypeName())
        addButton(14, ItemBuilder(Material.LEAD)
            .name("Familier", NamedTextColor.BLUE)
            .lore()
            .newline()
            .append("$totalPlayerPet/${Pet.entries.size} possédés", NamedTextColor.GRAY)
            .newline(2)
            .append("De petits familiers qui vous suivent partout", NamedTextColor.GRAY)
            .buildLore()
            .build()) {
            PetTypeSelectionMenu(playerID, this).open(viewer)
        }

        addButton(16, ItemBuilder(Material.ENDER_PEARL)
            .name("Téléportations", NamedTextColor.BLUE)
            .lore()
            .newline()
            .append("Customisez les particules de téléportation", NamedTextColor.GRAY)
            .buildLore()
            .build()) {
             CosmeticTPMenu(playerID, this).open(viewer)
        }

        addBackButton()
        addCloseButton()
    }
}