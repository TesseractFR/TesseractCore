package onl.tesseract.core.boutique.menu

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.format.NamedTextColor
import onl.tesseract.core.boutique.BoutiqueService
import onl.tesseract.lib.menu.ItemBuilder
import onl.tesseract.lib.menu.Menu
import onl.tesseract.lib.menu.MenuSize
import onl.tesseract.lib.service.ServiceContainer
import onl.tesseract.core.cosmetics.CosmeticManager
import onl.tesseract.core.cosmetics.ElytraTrails
import onl.tesseract.core.cosmetics.FlyFilter
import onl.tesseract.core.cosmetics.TeleportationAnimation
import onl.tesseract.core.cosmetics.familier.Pet
import onl.tesseract.lib.util.ItemLoreBuilder
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import java.util.UUID

class GlobalBoutiqueMenu(val playerID: UUID, previous: Menu? = null) :
    Menu(MenuSize.Three, "Boutique globale", NamedTextColor.AQUA, previous) {

    override fun placeButtons(viewer: Player) {
        val player = Bukkit.getPlayer(playerID) ?: return
        val boutiqueService = ServiceContainer[BoutiqueService::class.java]
        val playerBoutiqueInfo = boutiqueService.getPlayerBoutiqueInfo(playerID)
        fill(ItemBuilder(Material.GRAY_STAINED_GLASS_PANE, " ").build())

        val totalPlayerTrail = CosmeticManager.getTotalPossessed(playerID, ElytraTrails.getTypeName())
        addButton(
            10, ItemBuilder(Material.ELYTRA)
                .name("Sillages des ailes")
                .color(NamedTextColor.LIGHT_PURPLE)
                .lore(
                    ItemLoreBuilder()
                        .append("$totalPlayerTrail/${ElytraTrails.entries.size} possédés", NamedTextColor.GRAY)
                        .newline(2)
                        .append("Customisez les particules de vos ailes", NamedTextColor.GRAY)
                        .get()
                )
                .build()
        ) {
            ElytraTrailBoutiqueMenu(playerID, this).open(viewer)
        }
        val totalPlayerFlyFilter = CosmeticManager.getTotalPossessed(playerID, FlyFilter.getTypeName())
        addButton(
            12, ItemBuilder(Material.BLAZE_POWDER)
                .name("Filtre de vol & jetpack")
                .color(NamedTextColor.DARK_GREEN)
                .lore(
                    ItemLoreBuilder()
                        .append("$totalPlayerFlyFilter/${FlyFilter.entries.size} possédés", NamedTextColor.GRAY)
                        .newline(2)
                        .append(
                            "Des filtres qui apparaissent lorsque vous voler en Créatif ou lors de l'utilisation du jetpack en Semi-RP",
                            NamedTextColor.GRAY
                        )
                        .get()
                )
                .build()
        ) {
            FlyFilterBoutiqueMenu(playerID, this).open(viewer)
        }

        val totalPlayerPet = CosmeticManager.getTotalPossessed(playerID, Pet.getTypeName())
        addButton(
            14, ItemBuilder(Material.LEAD)
                .name("Familier")
                .color(NamedTextColor.BLUE)
                .lore(
                    ItemLoreBuilder()
                        .append("$totalPlayerPet/${Pet.entries.size} possédés", NamedTextColor.GRAY)
                        .newline(2)
                        .append("De petits familiers qui vous suivent partout", NamedTextColor.GRAY)
                        .get()
                )
                .build()
        ) {
             PetBoutiqueMenu(player, this).open(viewer)
        }
        val totalTPAnimationPossessed =
            CosmeticManager.getTotalPossessed(playerID, TeleportationAnimation.getTypeName())
        addButton(
            16, ItemBuilder(Material.ENDER_PEARL)
                .name("Animations de Téléportation")
                .color(NamedTextColor.DARK_GREEN)
                .lore(
                    ItemLoreBuilder()
                        .append(
                            "$totalTPAnimationPossessed/${TeleportationAnimation.entries.size} possédés",
                            NamedTextColor.GRAY
                        )
                        .newline(2)
                        .append("Customisez vos particules de téléportation", NamedTextColor.GRAY)
                        .get()
                )
                .build()
        ) {
             TPAnimationBoutiqueMenu(player, this).open(viewer)
        }
        addButton(
            22,
            ItemBuilder(Material.RAW_GOLD)
                .name("Lys d'or", NamedTextColor.GOLD)
                .lore(
                    ItemLoreBuilder()
                        .newline()
                        .append("Vous avez ", NamedTextColor.GRAY)
                        .append(playerBoutiqueInfo.marketCurrency.toString(), NamedTextColor.DARK_AQUA)
                        .append(" lys d'or", NamedTextColor.GRAY)
                        .newline()
                        .append("Cliquez ici pour acheter des lys d'or", NamedTextColor.GRAY)
                        .get()
                )
                .build()
        ) {
            viewer.sendMessage(
                Component.text()
                    .append(Component.text("[", NamedTextColor.GOLD))
                    .append(Component.text("Cliquez ici pour acheter des lys d'or", NamedTextColor.GOLD))
                    .append(Component.text("]", NamedTextColor.GOLD))
                    .clickEvent(ClickEvent.openUrl("https://tesseract.craftingstore.net/"))
            )
        }
        addBackButton()
        addCloseButton()
    }
}