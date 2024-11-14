package onl.tesseract.core.boutique

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.format.NamedTextColor
import onl.tesseract.lib.menu.Menu
import onl.tesseract.lib.menu.MenuService
import onl.tesseract.lib.repository.Repository
import onl.tesseract.lib.service.ServiceContainer
import onl.tesseract.tesseractlib.cosmetics.Cosmetic
import onl.tesseract.tesseractlib.cosmetics.CosmeticManager
import onl.tesseract.tesseractlib.cosmetics.ElytraTrails
import onl.tesseract.tesseractlib.cosmetics.FlyFilter
import onl.tesseract.tesseractlib.cosmetics.TeleportationAnimation
import onl.tesseract.tesseractlib.util.ChatFormats
import org.bukkit.entity.Player
import java.util.UUID

class BoutiqueService(private val repository: BoutiqueRepository) {

    fun getPlayerBoutiqueInfo(playerID: UUID): PlayerBoutiqueInfo {
        return repository.getById(playerID) ?: PlayerBoutiqueInfo(playerID)
    }

    fun hasCosmetic(playerID: UUID, type: String, cosmetic: Cosmetic): Boolean {
        return CosmeticManager.hasCosmetic(playerID, type, cosmetic)
    }

    fun buyCosmetic(playerID: UUID, cosmetic: Cosmetic) {
        if (getPlayerBoutiqueInfo(playerID).marketCurrency < cosmetic.price) return

        repository.addMarketCurrency(playerID, -cosmetic.price)
        CosmeticManager.giveCosmetic(playerID, cosmetic)
    }

    fun addMarketCurrency(playerID: UUID, amount: Int) {
        repository.addMarketCurrency(playerID, amount)
    }

    fun tryToBuy(player: Player, mainMenu: Menu, cosmetic: Cosmetic) {
        val playerBoutiqueInfo = getPlayerBoutiqueInfo(player.uniqueId)
        if (playerBoutiqueInfo.marketCurrency >= cosmetic.price) {
            ServiceContainer[MenuService::class.java].openConfirmationMenu(
                player,
                Component.text("Êtes vous sur de vouloir acheter le cosmétique " + cosmetic.getName()),
                mainMenu
            ) {
                buyCosmetic(player.uniqueId, cosmetic)
            }
        } else {
            mainMenu.close()
            player.sendMessage(
                ChatFormats.COSMETICS_ERROR.append(
                    Component.text("Vous n'avez pas assez de lys d'or, cliquez ici pour en acheter").hoverEvent(
                        HoverEvent.showText(
                            Component.text(
                                "Cliquez ici pour accéder à la boutique.",
                                NamedTextColor.GOLD
                            )
                        )
                    ).clickEvent(ClickEvent.openUrl("https://tesseract.craftingstore.net/"))
                )
            );
        }
    }

    fun setActiveElytraTrail(playerID: UUID, trail: ElytraTrails) {
        repository.setActiveTrail(playerID, trail)
    }

    fun setActiveFlyFilter(playerID: UUID, filter: FlyFilter) {
        repository.setActiveFilter(playerID, filter)
    }

    fun setTpAnimation(playerID: UUID, animation: TeleportationAnimation) {
        // TODO
    }
}

interface BoutiqueRepository : Repository<PlayerBoutiqueInfo, UUID> {

    fun addMarketCurrency(id: UUID, amount: Int)

    fun setActiveTrail(id: UUID, trail: ElytraTrails)
    fun setActiveFilter(id: UUID, filter: FlyFilter)
    fun setActiveTpAnimation(id: UUID, animation: TeleportationAnimation)
}