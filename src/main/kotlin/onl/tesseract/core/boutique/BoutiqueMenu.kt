package onl.tesseract.core.boutique

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import onl.tesseract.lib.menu.Menu
import onl.tesseract.lib.menu.MenuService
import onl.tesseract.lib.menu.MenuSize
import onl.tesseract.lib.service.ServiceContainer
import onl.tesseract.core.persistence.hibernate.boutique.TPlayerInfoService
import onl.tesseract.lib.util.ChatFormats
import onl.tesseract.lib.util.ItemBuilder
import onl.tesseract.lib.util.ItemLoreBuilder
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

abstract class BoutiqueMenu(
    val player: Player,
    size: MenuSize,
    title: String,
    color: NamedTextColor,
    previous: Menu?
) : Menu(size, title, color, previous) {

    override fun placeButtons(viewer: Player) {
        fill(onl.tesseract.lib.menu.ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).name(" ").build())
        addBackButton()
        addCloseButton()
        addBoutiqueButton(viewer)
    }

    protected fun addBoutiqueButton(viewer: Player) {
        val playerInfo = ServiceContainer[TPlayerInfoService::class.java].get(player.uniqueId)
        val lore = ItemLoreBuilder()
            .newline()
            .append(Component.text("Vous avez ", NamedTextColor.GRAY))
            .append(Component.text(playerInfo.shop_point, NamedTextColor.DARK_AQUA))
            .append(Component.text(" Points boutique.", NamedTextColor.GRAY))
            .newline()
            .append(Component.text("Vous avez ", NamedTextColor.GRAY))
            .append(Component.text(playerInfo.market_currency, NamedTextColor.DARK_AQUA))
            .append(Component.text(" lys d'or.", NamedTextColor.GRAY))
            .newline()
            .append(Component.text("Cliquez ici pour acheter des lys d'or", NamedTextColor.GRAY))

        val nameComponent: Component = Component.text("Lys d'or et Points Boutique", NamedTextColor.GOLD)

        val itemStack = ItemBuilder(Material.RAW_GOLD)
            .name(nameComponent)
            .lore(lore.get())
            .build()
        this.addButton(size.size - 5, itemStack) { _ ->
            viewer.sendMessage(
                Component.text("[", NamedTextColor.GOLD)
                    .append(Component.text(" Cliquez ici pour acheter des lys d'or", NamedTextColor.YELLOW))
                    .append(Component.text("]", NamedTextColor.GOLD))
                    .clickEvent(
                        ClickEvent.openUrl("https://tesseract.craftingstore.net/")
                    )
            )
            this.close()
        }
    }

    protected fun getPriceLore(price: Int, containsShopPoint: Boolean): MutableList<Component?>? {
        val itemLore = ItemLoreBuilder().newline()
            .append("Prix : ", NamedTextColor.GRAY)
            .newline()
            .append(
                price.toString() + " lys d'or" + (if (containsShopPoint) " / points boutique" else ""),
                NamedTextColor.GOLD,
                TextDecoration.BOLD
            ).newline()
            .newline()
            .append("--- Clic gauche ---", NamedTextColor.LIGHT_PURPLE)
            .newline()
            .append("Acheter en lys d'or", NamedTextColor.AQUA)

        if (containsShopPoint) {
            itemLore.newline()
                .newline()
                .append("--- Clic droit ---", NamedTextColor.LIGHT_PURPLE)
                .newline()
                .append("Acheter en points boutique", NamedTextColor.AQUA)
        }
        return itemLore.get()
    }

    protected fun getPriceLore(price: Int): MutableList<Component?>? {
        return getPriceLore(price, true)
    }

    protected fun askBuyItem(itemStack: ItemStack, price: Int, withShopPoint: Boolean, viewer: Player) {
        val message = Component.text("Confirmer votre achat de ")
            .append(itemStack.displayName())
            .append(Component.text(" pour " + price + (if (withShopPoint) " points boutiques ?" else " lys d'or ?")))
        ServiceContainer[MenuService::class.java].openConfirmationMenu(viewer, message, this) {
            buyItem(itemStack, price, withShopPoint, viewer)
        }
    }

    private fun buyItem(itemStack: ItemStack, price: Int, withShopPoint: Boolean, viewer: Player) {
        var hasBuyItem: Boolean = if (withShopPoint) {
            buyItemWithShopPoint(itemStack, price)
        } else {
            buyItemWithLysDor(itemStack, price)
        }
        if (!hasBuyItem) {
            val moneyType = if (withShopPoint) "points boutique" else "lys d'or"
            viewer.sendMessage(
                ChatFormats.CHAT_ERROR.append(
                    Component.text("Vous n'avez pas suffisamment de $moneyType. Nécessite : $price.")
                )
            )
            close()
            return
        }
        viewer.sendMessage(ChatFormats.CHAT_SUCCESS.append(Component.text("Achat réussi !")))
    }

    private fun buyItemWithLysDor(itemStack: ItemStack, price: Int): Boolean {
        val playerInfo = ServiceContainer[TPlayerInfoService::class.java].get(player.uniqueId)
        if (playerInfo.market_currency < price) {
            return false
        }
        ServiceContainer[TPlayerInfoService::class.java].addMarketCurrency(playerInfo, -price)
        player.inventory.addItem(itemStack)
        return true
    }

    private fun buyItemWithShopPoint(itemStack: ItemStack, price: Int): Boolean {
        val playerInfo = ServiceContainer[TPlayerInfoService::class.java].get(player.uniqueId)
        if (playerInfo.shop_point < price) {
            return false
        }
        ServiceContainer[TPlayerInfoService::class.java].addShopPoint(playerInfo, -price)
        player.inventory.addItem(itemStack)
        return true
    }
}
