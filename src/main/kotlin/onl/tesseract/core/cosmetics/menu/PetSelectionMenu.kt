package onl.tesseract.core.cosmetics.menu

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import onl.tesseract.core.boutique.BoutiqueService
import onl.tesseract.lib.menu.ItemBuilder
import onl.tesseract.lib.menu.Menu
import onl.tesseract.lib.menu.MenuSize
import onl.tesseract.lib.service.ServiceContainer
import onl.tesseract.core.cosmetics.CosmeticManager
import onl.tesseract.core.cosmetics.familier.Pet
import onl.tesseract.core.cosmetics.familier.PetCategory
import onl.tesseract.core.cosmetics.familier.PetManager
import onl.tesseract.tesseractlib.util.ChatFormats
import onl.tesseract.tesseractlib.util.ItemLoreBuilder
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import java.util.UUID

class PetSelectionMenu(val playerID: UUID, val category: PetCategory, previous: Menu? = null) : Menu(
    MenuSize.Two, "Sélection d'un familier", NamedTextColor.BLUE, previous) {

    override fun placeButtons(viewer: Player) {
        val boutiqueService = ServiceContainer[BoutiqueService::class.java]
        val playerBoutiqueInfo = boutiqueService.getPlayerBoutiqueInfo(playerID)
        fill(ItemBuilder(Material.GRAY_STAINED_GLASS_PANE, " ").build())
        category.pets.forEachIndexed { index, pet ->
            val hasPet = CosmeticManager.hasCosmetic(playerID, Pet.getTypeName(), pet)
            val lore = ItemLoreBuilder().newline()
            if (hasPet) lore.append("Possédé", NamedTextColor.GREEN)
                    .newline(2)
                    .append("Cliquez pour invoquer ${pet.name}")
            else lore.append("Non possédé", NamedTextColor.RED)
                    .newline(2)
                    .append("Cliquez pour acheter ${pet.name}")
                    .newline()
                    .append("Coût : ${pet.price} lys d'or", NamedTextColor.GRAY)
                    .append("Vous avez : ${playerBoutiqueInfo.marketCurrency} lys d'or", NamedTextColor.GRAY)

            addButton(
                index,
                ItemBuilder(pet.head).name(pet.name, NamedTextColor.YELLOW)
                        .lore(lore.get())
                        .build()) {
                if (hasPet) {
                    PetManager.invokePet(viewer, pet)
                    this.close()
                } else {
                    boutiqueService.tryToBuy(Bukkit.getPlayer(playerID) ?: return@addButton, this, pet)
                }
            }
        }

        addButton(
            13,
            ItemBuilder(Material.NAME_TAG).name("Désinvocation", NamedTextColor.YELLOW)
                    .lore()
                    .newline()
                    .append("Cliquez pour désinvoquer votre familier", NamedTextColor.GRAY)
                    .buildLore()
                    .build()) {
            if (PetManager.hasPetInvocked(viewer)) {
                PetManager.invokePet(viewer, null)
                viewer.sendMessage(
                    ChatFormats.PET.append(
                        Component.text(
                            "Votre familier a été désinvoqué", NamedTextColor.GREEN)))
                this.close()
            } else {
                viewer.sendMessage(
                    ChatFormats.PET.append(
                        Component.text(
                            "Vous n'avez pas de familier invoqué", NamedTextColor.RED)));
            }
        }
        this.addBackButton()
        this.addCloseButton()
    }
}