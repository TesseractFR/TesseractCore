package onl.tesseract.core.command.argument

import onl.tesseract.commandBuilder.CommandArgument
import onl.tesseract.commandBuilder.CommandArgumentBuilderSteps
import onl.tesseract.core.command.PTimeCommand.Hours

class TimeNameArg(name: String) : CommandArgument<Hours>(name) {

    override fun define(builder: CommandArgumentBuilderSteps.Parser<Hours>) {
        builder.parser { input, _ -> Hours.valueOf(input) }
                .tabCompleter { _, _ -> Hours.entries.map { it.name } }
                .errorHandler(IllegalArgumentException::class.java, "Heure invalide")
    }
}