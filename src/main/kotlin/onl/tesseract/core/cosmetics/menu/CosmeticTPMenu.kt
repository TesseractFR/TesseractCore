package onl.tesseract.core.cosmetics.menu

import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import onl.tesseract.core.boutique.BoutiqueService
import onl.tesseract.core.boutique.menu.GlobalBoutiqueMenu
import onl.tesseract.lib.menu.ItemBuilder
import onl.tesseract.lib.menu.Menu
import onl.tesseract.lib.menu.MenuSize
import onl.tesseract.lib.service.ServiceContainer
import onl.tesseract.tesseractlib.cosmetics.CosmeticManager
import onl.tesseract.tesseractlib.cosmetics.TeleportationAnimation
import onl.tesseract.tesseractlib.util.ChatFormats
import onl.tesseract.tesseractlib.util.ItemLoreBuilder
import onl.tesseract.tesseractlib.util.plus
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import java.util.UUID

class CosmeticTPMenu(val playerID: UUID, previous: Menu? = null) : Menu(
    MenuSize.Three, "Particules de téléportation", NamedTextColor.LIGHT_PURPLE, previous) {

    override fun placeButtons(viewer: Player) {
        val boutiqueService = ServiceContainer[BoutiqueService::class.java]
        val playerBoutiqueInfo = boutiqueService.getPlayerBoutiqueInfo(playerID)
        fill(ItemBuilder(Material.GRAY_STAINED_GLASS_PANE, " ").build())

        addBackButton()
        addCloseButton()

        TeleportationAnimation.entries
                .filter { it != TeleportationAnimation.ROSETTE }
                .forEachIndexed { index, animation ->
                    var hasAnimation =
                        CosmeticManager.hasCosmetic(playerID, TeleportationAnimation.getTypeName(), animation)
                    if (animation == TeleportationAnimation.WATER && !hasAnimation) {
                        hasAnimation = true
                        CosmeticManager.giveCosmetic(playerID, animation)
                    }

                    val lore = ItemLoreBuilder()
                            .newline()
                    if (hasAnimation) lore.append("Débloqué", NamedTextColor.GREEN)
                            .newline()
                            .append("Cliquez pour utiliser le filtre ${animation.name}")
                    else lore.append("Bloqué", NamedTextColor.RED)
                            .newline()
                            .append("Cliquez pour acheter ${animation.name}")
                            .newline()
                            .append("Coût : ${animation.price} lys d'or", NamedTextColor.GRAY)
                            .newline()
                            .append("Vous avez : ${playerBoutiqueInfo.marketCurrency} lys d'or", NamedTextColor.GRAY)

                    val buttonIndex = if (index < 8) index else index + 1
                    addButton(
                        buttonIndex, ItemBuilder(animation.getIcon())
                                .name(animation.getName())
                                .lore(lore.get())
                                .enchanted(playerBoutiqueInfo.activeTpAnimation == animation)
                                .build()) {
                        if (hasAnimation)
                            boutiqueService.setTpAnimation(playerID, animation)
                        else
                            boutiqueService.tryToBuy(Bukkit.getPlayer(playerID) ?: return@addButton, this, animation)
                        this.close()
                    }
                }

        // VIP
        if (CosmeticManager.hasCosmetic(
                playerID,
                TeleportationAnimation.getTypeName(),
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