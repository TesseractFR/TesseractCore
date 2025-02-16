package onl.tesseract.core.command

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import onl.tesseract.commandBuilder.CommandContext
import onl.tesseract.commandBuilder.annotation.Argument
import onl.tesseract.commandBuilder.annotation.Command
import onl.tesseract.commandBuilder.annotation.CommandBody
import onl.tesseract.commandBuilder.annotation.Env
import onl.tesseract.core.command.argument.TimeNameArg
import org.bukkit.entity.Player


@Command(
    name = "ptime",
    playerOnly = true,
    args = [Argument(value = "time", clazz = TimeNameArg::class)])
class PTimeCommand : CommandContext() {
    @CommandBody
    fun onCommand(@Env(key = "time") time: Hours, sender: Player): Boolean {

        if (time == Hours.RESET) {
            sender.resetPlayerTime()
            sender.sendMessage(Component.text("Votre temps a été réinitialisé.", NamedTextColor.GREEN));
            return true
        }

        sender.setPlayerTime(time.value, false)
        sender.sendMessage(Component.text("Temps fixé à ${time.value}", NamedTextColor.GREEN))

        return true
    }

    enum class Hours
        (var value: Long) {
        RESET(0),
        NOON(6000),
        SUNSET(12000),
        NIGHT(13000),
        MIDNIGHT(18000),
        SUNRISE(23000),
    }
}