package onl.tesseract.core.boutique.menu

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import onl.tesseract.core.boutique.BoutiqueService
import onl.tesseract.lib.menu.ItemBuilder
import onl.tesseract.lib.menu.Menu
import onl.tesseract.lib.menu.MenuSize
import onl.tesseract.lib.service.ServiceContainer
import onl.tesseract.core.cosmetics.CosmeticPreview
import onl.tesseract.core.cosmetics.TeleportationAnimation
import org.bukkit.Material
import org.bukkit.entity.Player

class TPAnimationBoutiqueMenu(
    val player: Player,
    previous: Menu? = null,
) : Menu(MenuSize.Three, "Boutique des animations de téléportations", previous = previous) {

    override fun placeButtons(viewer: Player) {
        fill(ItemBuilder(Material.GRAY_STAINED_GLASS_PANE, " ").build())
        val boutiqueService = ServiceContainer[BoutiqueService::class.java]
        val playerBoutiqueInfo = boutiqueService.getPlayerBoutiqueInfo(player.uniqueId)

        var i = 1
        TeleportationAnimation.entries
            .filter { it != TeleportationAnimation.WATER && it != TeleportationAnimation.ROSETTE }
            .forEach { animation ->

                if (boutiqueService.hasCosmetic(player.uniqueId, TeleportationAnimation.getTypeName(), animation)) {
                    alreadyPossessedButton(i, animation)
                } else {
                    val previewString = if (CosmeticPreview.hasPreviewed(viewer.uniqueId, animation))
                        Component.text("Déjà prévisualisé", NamedTextColor.RED)
                    else Component.text("prévisualiser", NamedTextColor.GRAY)

                    addButton(i, ItemBuilder(animation.icon)
                        .name(animation.getName(), NamedTextColor.GOLD)
                        .lore()
                        .newline()
                        .append("Clic gauche : ", NamedTextColor.GRAY)
                        .append("Acheter", NamedTextColor.YELLOW)
                        .newline()
                        .append("Clic droit : ", NamedTextColor.GRAY)
                        .append(previewString)
                        .newline(2)
                        .append("Coût : ", NamedTextColor.GRAY)
                        .append("${animation.price} lys d'or", NamedTextColor.YELLOW)
                        .newline()
                        .append("Vous avez : ", NamedTextColor.GRAY)
                        .append("${playerBoutiqueInfo.marketCurrency} lys d'or", NamedTextColor.YELLOW)
                        .buildLore()
                        .build()
                    ) { event ->
                        if (event.isRightClick && !CosmeticPreview.hasPreviewed(viewer.uniqueId, animation))
                        {
                            close()
                            animation.animate(viewer.getLocation(), 3.0 * 20)
                            CosmeticPreview.setPreview(viewer.uniqueId, animation)
                        }
                        else if (event.isLeftClick)
                        {
                            boutiqueService.tryToBuy(player, this, animation)
                        }
                    }
                }
                if (++i == 8)
                    i = 10
                if (i == 13)
                    i++
            }
        addCloseButton()
        addBackButton()
    }

    private fun alreadyPossessedButton(index: Int, animation: TeleportationAnimation) {
        addButton(
            index, ItemBuilder(Material.STRUCTURE_VOID, animation.getName())
                .lore()
                .append("Vous possédez déjà ce effet de téléportation", NamedTextColor.GRAY)
                .buildLore()
                .build()
        )
    }
}