package onl.tesseract.tesseractlib.menu

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import onl.tesseract.core.persistence.hibernate.boutique.TPlayerInfoService
import onl.tesseract.lib.menu.ItemBuilder
import onl.tesseract.lib.menu.Menu
import onl.tesseract.lib.menu.MenuSize
import onl.tesseract.lib.player.Gender
import onl.tesseract.lib.service.ServiceContainer
import onl.tesseract.lib.util.ItemLoreBuilder
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

private val teteHomme = ItemBuilder(Material.PLAYER_HEAD).customHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2EwOGQwZGFiYzQzNGEwOTNmMDk4YmFmNTA1YjE2NWMxNGNiZTk2NDU3M2VkOGU5ZTYxODUxNTg5MTc5NTcwIn19fQ==", "")
private val teteFemme = ItemBuilder(Material.PLAYER_HEAD).customHead( "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWEyMjA2ODJhYjdmMjBmYmNlOTEzMDY2MGVjOTgyMjliMzMyMGEyMzlhNDc4MmViMTUzMzg1ZWRhOWJmYmZkOCJ9fX0=", "")
private val teteAutre = ItemBuilder(Material.PLAYER_HEAD).customHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmFkYzA0OGE3Y2U3OGY3ZGFkNzJhMDdkYTI3ZDg1YzA5MTY4ODFlNTUyMmVlZWQxZTNkYWYyMTdhMzhjMWEifX19", "")

class GenderMenu(private val player: Player, previous: Menu) :

        Menu(MenuSize.One, Component.text("Choisissez votre genre", NamedTextColor.LIGHT_PURPLE, TextDecoration.BOLD), previous) {

    override fun placeButtons(viewer: Player) {
        this.fill(ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).name(" ").build())

        addButton(2, createMaleItem()){
            player.sendMessage(Component.text("Vous avez bien changé votre genre en ${Gender.MALE.getName()} !", NamedTextColor.GREEN))
            val tPlayerInfo = ServiceContainer[TPlayerInfoService::class.java][player.uniqueId]
            tPlayerInfo.genre = Gender.MALE
            ServiceContainer[TPlayerInfoService::class.java].save(tPlayerInfo)

        }

        addButton(4, createFemaleItem()) {
            player.sendMessage(Component.text("Vous avez bien changé votre genre en ${Gender.FEMALE.getName()} !", NamedTextColor.GREEN))
            val tPlayerInfo = ServiceContainer[TPlayerInfoService::class.java][player.uniqueId]
            tPlayerInfo.genre = Gender.FEMALE
            ServiceContainer[TPlayerInfoService::class.java].save(tPlayerInfo)

        }

        addButton(6, createOtherItem()) {
            player.sendMessage(Component.text("Vous avez bien changé votre genre en ${Gender.OTHER.getName()} !", NamedTextColor.GREEN))
            val tPlayerInfo = ServiceContainer[TPlayerInfoService::class.java][player.uniqueId]
            tPlayerInfo.genre = Gender.OTHER
            ServiceContainer[TPlayerInfoService::class.java].save(tPlayerInfo)

        }

        this.addBackButton()
        this.addCloseButton()
    }

    private fun createMaleItem(): ItemStack {
        val ilb = ItemLoreBuilder()
                .newline()
                .append("Cliquez ici pour définir votre genre en ${Gender.MALE.getName()}.", NamedTextColor.GRAY)
        return teteHomme
                .name(Gender.MALE.getName(), NamedTextColor.GOLD, TextDecoration.BOLD)
                .lore(ilb.get())
                .build()
    }

    private fun createFemaleItem(): ItemStack {
        val ilb = ItemLoreBuilder()
                .newline()
                .append("Cliquez ici pour définir votre genre en ${Gender.FEMALE.getName()}.", NamedTextColor.GRAY)
        return teteFemme
                .name(Gender.FEMALE.getName(), NamedTextColor.GOLD, TextDecoration.BOLD)
                .lore(ilb.get())
                .build()
    }

    private fun createOtherItem(): ItemStack {
        val ilb = ItemLoreBuilder()
                .newline()
                .append("Cliquez ici pour définir votre genre en ${Gender.OTHER.getName()}.", NamedTextColor.GRAY)
        return teteAutre
                .name(Gender.OTHER.getName(), NamedTextColor.GOLD, TextDecoration.BOLD)
                .lore(ilb.get())
                .build()
    }
}