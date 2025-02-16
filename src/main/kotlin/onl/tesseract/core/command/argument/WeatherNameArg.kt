package onl.tesseract.core.command.argument

import onl.tesseract.commandBuilder.CommandArgument
import onl.tesseract.commandBuilder.CommandArgumentBuilderSteps
import onl.tesseract.core.command.PWeatherCommand.Weather

class WeatherNameArg(name: String) : CommandArgument<Weather>(name) {

    override fun define(builder: CommandArgumentBuilderSteps.Parser<Weather>) {
        builder.parser { input, _ -> Weather.valueOf(input) }
                .tabCompleter { _, _ -> Weather.entries.map { it.name } }
                .errorHandler(IllegalArgumentException::class.java, "Météo invalide")
    }
}