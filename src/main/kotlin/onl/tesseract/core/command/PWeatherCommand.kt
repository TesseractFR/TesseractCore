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
    fun onCommand(@Env(key = "weather") weather: Weather, sender: Player): Boolean {

        if (weather == Weather.RESET) {
            sender.resetPlayerWeather()
            sender.sendMessage(Component.text("Votre météo a été réinitialisé.", NamedTextColor.GREEN));
            return true
        }

        weather.weatherType?.let { sender.setPlayerWeather(it) }
        sender.sendMessage(Component.text("Météo fixé à ${weather.weatherType}", NamedTextColor.GREEN))

        return true
    }


    enum class Weather(val weatherType: WeatherType?) {
        RESET(null),
        CLEAR(WeatherType.CLEAR),
        RAIN(WeatherType.DOWNFALL)
    }


}