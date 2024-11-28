package onl.tesseract.core.boutique.menu

import net.kyori.adventure.text.format.NamedTextColor
import onl.tesseract.core.boutique.BoutiqueService
import onl.tesseract.lib.menu.ItemBuilder
import onl.tesseract.lib.menu.Menu
import onl.tesseract.lib.menu.MenuSize
import onl.tesseract.lib.service.ServiceContainer
import onl.tesseract.core.cosmetics.familier.Pet
import onl.tesseract.core.cosmetics.familier.PetCategory
import org.bukkit.Material
import org.bukkit.entity.Player

class PetBoutiqueMenu(
    val player: Player,
    previous: Menu? = null,
) : Menu(MenuSize.Six, "Boutique des familiers", previous = previous) {

    override fun placeButtons(viewer: Player) {
        fill(ItemBuilder(Material.GRAY_STAINED_GLASS_PANE, " ").build())
        val boutiqueService = ServiceContainer[BoutiqueService::class.java]
        val playerBoutiqueInfo = boutiqueService.getPlayerBoutiqueInfo(player.uniqueId)

        var i = 0
        PetCategory.entries.forEach { category ->
            category.pets.forEach { pet ->
                if (boutiqueService.hasCosmetic(player.uniqueId, Pet.getTypeName(), pet)) {
                    addButton(
                        i++, ItemBuilder(Material.STRUCTURE_VOID)
                                .name(pet.getDisplayName())
                                .lore()
                                .append("Vous possédez déjà ce familier", NamedTextColor.GRAY)
                                .buildLore()
                                .build()
                    )
                } else {
                    addButton(
                        i++, ItemBuilder(pet.head)
                                .name(pet.getname())
                                .lore()
                                .append("Cliquez pour acheter ")
                                .append(pet.getname())
                                .newline()
                                .append("Coût : " + pet.price + " lys d'or", NamedTextColor.GRAY)
                                .append(
                                    "Vous avez : " + playerBoutiqueInfo.marketCurrency + " lys d'or",
                                    NamedTextColor.GRAY
                                )
                                .buildLore()
                                .build()
                    ) {
                        boutiqueService.tryToBuy(player, this, pet)
                    }
                }
            }
        }

        addBackButton()
        addCloseButton()
    }
}