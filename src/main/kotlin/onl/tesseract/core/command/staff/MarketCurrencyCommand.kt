package onl.tesseract.core.command.staff

import onl.tesseract.commandBuilder.CommandContext
import onl.tesseract.commandBuilder.annotation.Argument
import onl.tesseract.commandBuilder.annotation.Command
import onl.tesseract.commandBuilder.annotation.Perm
import onl.tesseract.core.boutique.BoutiqueService
import onl.tesseract.core.boutique.PlayerBoutiqueInfo
import onl.tesseract.lib.command.argument.IntegerCommandArgument
import onl.tesseract.lib.command.argument.OfflinePlayerArg
import onl.tesseract.lib.service.ServiceContainer
import onl.tesseract.lib.util.ChatFormats
import onl.tesseract.lib.util.plus
import org.bukkit.command.CommandSender

@Command(permission = Perm(value = "cosmetic.admin"))
class MarketCurrencyCommand : CommandContext() {

    @Command()
    fun give(
        @Argument("player") playerArg: OfflinePlayerArg,
        @Argument("amount") amount: IntegerCommandArgument,
        sender: CommandSender,
    ) {
        val player = playerArg.get()
        val boutiqueService: BoutiqueService = ServiceContainer[BoutiqueService::class.java]

        boutiqueService.addMarketCurrency(playerArg.get().uniqueId, amount.get())
        sender.sendMessage("Le joueur ${player.name} a reçu $amount lys d'or")
        player.player?.sendMessage(ChatFormats.COSMETICS + "Vous avez reçu $amount lys d'or")
    }

    @Command()
    fun remove(
        @Argument("player") playerArg: OfflinePlayerArg,
        @Argument("amount") amount: IntegerCommandArgument,
        sender: CommandSender,
    ) {
        val player = playerArg.get()
        val boutiqueService: BoutiqueService = ServiceContainer[BoutiqueService::class.java]

        boutiqueService.addMarketCurrency(playerArg.get().uniqueId, -amount.get())
        sender.sendMessage("Le joueur ${player.name} a perdu $amount lys d'or")
        player.player?.sendMessage(ChatFormats.COSMETICS + "Vous avez été déduit de $amount lys d'or")
    }

    @Command()
    fun get(@Argument("player") playerArg: OfflinePlayerArg, sender: CommandSender) {
        val boutiqueService: BoutiqueService = ServiceContainer[BoutiqueService::class.java]
        val playerBoutiqueInfo: PlayerBoutiqueInfo = boutiqueService.getPlayerBoutiqueInfo(playerArg.get().uniqueId)
        sender.sendMessage("Le joueur ${playerArg.get().name} possède ${playerBoutiqueInfo.marketCurrency} lys d'or")
    }
}