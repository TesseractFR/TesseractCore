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
import onl.tesseract.lib.task.TaskScheduler
import org.bukkit.Material
import org.bukkit.entity.Player

class TPAnimationBoutiqueMenu(
    val player: Player,
    previous: Menu? = null,
) : Menu(MenuSize.Three, "Boutique des animations de téléportations", previous = previous) {

    override fun placeButtons(viewer: Player) {
        fill(ItemBuilder(Material.GRAY_STAINED_GLASS_PANE, " ").build())

        var buttonIndex = 1
        TeleportationAnimation.entries
            .filter { it != TeleportationAnimation.WATER && it != TeleportationAnimation.ROSETTE }
            .forEach { animation ->
                placeAnimationButton(animation, buttonIndex, viewer)
                if (++buttonIndex == 8)
                    buttonIndex = 10
                if (buttonIndex == 13)
                    buttonIndex++
            }
        addCloseButton()
        addBackButton()
    }

    private fun placeAnimationButton(
        animation: TeleportationAnimation,
        index: Int,
        viewer: Player,
    ) {
        val boutiqueService = ServiceContainer[BoutiqueService::class.java]
        val playerBoutiqueInfo = boutiqueService.getPlayerBoutiqueInfo(player.uniqueId)
        if (boutiqueService.hasCosmetic(player.uniqueId, TeleportationAnimation.getTypeName(), animation)) {
            alreadyPossessedButton(index, animation)
            return
        }

        val previewString = if (CosmeticPreview.hasPreviewed(viewer.uniqueId, animation))
            Component.text("Déjà prévisualisé", NamedTextColor.RED)
        else
            Component.text("prévisualiser", NamedTextColor.GRAY)

        addButton(
            index, ItemBuilder(animation.icon)
                    .name(animation.displayName)
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
            if (event.isRightClick && !CosmeticPreview.hasPreviewed(viewer.uniqueId, animation)) {
                close()
                animation.animate(
                    ServiceContainer[TaskScheduler::class.java].plugin,
                    viewer.location,
                    3.0 * 20)
                CosmeticPreview.setPreview(viewer.uniqueId, animation)
            } else if (event.isLeftClick) {
                boutiqueService.tryToBuy(player, this, animation)
            }
        }
    }

    private fun alreadyPossessedButton(index: Int, animation: TeleportationAnimation) {
        addButton(
            index, ItemBuilder(Material.STRUCTURE_VOID)
                .name(animation.displayName)
                .lore()
                .append("Vous possédez déjà ce effet de téléportation", NamedTextColor.GRAY)
                .buildLore()
                .build()
        )
    }
}