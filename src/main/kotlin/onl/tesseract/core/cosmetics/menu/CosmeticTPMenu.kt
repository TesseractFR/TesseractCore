package onl.tesseract.core.cosmetics.menu

import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import onl.tesseract.core.boutique.BoutiqueService
import onl.tesseract.core.boutique.menu.GlobalBoutiqueMenu
import onl.tesseract.core.cosmetics.CosmeticManager
import onl.tesseract.core.cosmetics.TeleportationAnimation
import onl.tesseract.lib.menu.ItemBuilder
import onl.tesseract.lib.menu.Menu
import onl.tesseract.lib.menu.MenuSize
import onl.tesseract.lib.service.ServiceContainer
import onl.tesseract.lib.util.ChatFormats
import onl.tesseract.lib.util.plus
import org.bukkit.Material
import org.bukkit.entity.Player
import java.util.*

class CosmeticTPMenu(val playerID: UUID, previous: Menu? = null) : AbstractCosmeticMenu(
    MenuSize.Three, "Particules de téléportation", previous) {

    override fun placeButtons(viewer: Player) {
        val boutiqueService = ServiceContainer[BoutiqueService::class.java]
        fill(ItemBuilder(Material.GRAY_STAINED_GLASS_PANE, " ").build())

        addBackButton()
        addCloseButton()

        TeleportationAnimation.entries
                .filter { it != TeleportationAnimation.ROSETTE }
                .forEachIndexed { index, animation ->
                    var hasAnimation =
                        CosmeticManager.hasCosmetic(playerID, animation)
                    if (animation == TeleportationAnimation.WATER && !hasAnimation) {
                        hasAnimation = true
                        CosmeticManager.giveCosmetic(playerID, animation)
                    }
                    val buttonIndex = if (index < 8) index else index + 1
                    placeCosmeticButton(buttonIndex, animation, hasAnimation, playerID) {
                        boutiqueService.setTpAnimation(playerID, it)
                    }
                }

        // VIP
        if (CosmeticManager.hasCosmetic(
                playerID,
                TeleportationAnimation.ROSETTE)) {
            addButton(
                22, ItemBuilder(Material.END_ROD)
                        .name("Rosace", NamedTextColor.LIGHT_PURPLE)
                        .lore()
                        .newline()
                        .append("Cliquez pour utiliser le filtre", TextColor.color(255, 177, 255))
                        .buildLore()
                        .build()) {
                close()
                boutiqueService.setTpAnimation(playerID, TeleportationAnimation.ROSETTE)
                viewer.sendMessage(ChatFormats.COSMETICS_SUCCESS + "Votre animation de téléportation a été changé.")
            }
        } else {
            addButton(
                22, ItemBuilder(Material.END_ROD)
                        .name("Rosace", NamedTextColor.LIGHT_PURPLE)
                        .lore()
                        .newline()
                        .append("Exclusive au ", TextColor.color(255, 177, 255))
                        .append("VIP", NamedTextColor.LIGHT_PURPLE)
                        .buildLore()
                        .build()) {
                GlobalBoutiqueMenu(playerID, this).open(viewer)
            }
        }
    }
}