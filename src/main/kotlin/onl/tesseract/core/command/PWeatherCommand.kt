package onl.tesseract.core.command

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import onl.tesseract.commandBuilder.CommandContext
import onl.tesseract.commandBuilder.annotation.Argument
import onl.tesseract.commandBuilder.annotation.Command
import onl.tesseract.commandBuilder.annotation.CommandBody
import onl.tesseract.commandBuilder.annotation.Env
import onl.tesseract.core.command.argument.WeatherNameArg
import org.bukkit.WeatherType
import org.bukkit.entity.Player


@Command(
    name = "ptime",
    playerOnly = true,
    args = [Argument(value = "weather", clazz = WeatherNameArg::class)])
class PWeatherCommand : CommandContext() {
    @CommandBody
    fun onCommand(@Env(key = "weather") weather: Weather, sender: Player) {

        if (weather == Weather.RESET) {
            sender.resetPlayerWeather()
            sender.sendMessage(Component.text("Votre météo a été réinitialisée.", NamedTextColor.GREEN));
            return
        }

        weather.weatherType?.let { sender.setPlayerWeather(it) }
        sender.sendMessage(Component.text("Météo fixée à ${weather.displayName}", NamedTextColor.GREEN))
    }

    enum class Weather(
        val weatherType: WeatherType?,
        val displayName: String
    ) {
        RESET(null, "DEFAUT"),
        CLEAR(WeatherType.CLEAR, "CLAIR"),
        RAIN(WeatherType.DOWNFALL, "PLUVIEUX")
    }

}