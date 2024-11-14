package onl.tesseract.core.autoChatMessage;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class AutoChatMessages {

    public static Component voteMessage() {
        return Component.text("                                                                                ", NamedTextColor.GREEN, TextDecoration.STRIKETHROUGH)
                .append(Component.newline())
                .append(Component.text("            ")
                        .decoration(TextDecoration.STRIKETHROUGH, false)
                        .append(Component.text(" GAGNEZ DES RÉCOMPENSES EN VOTANT ", NamedTextColor.GREEN, TextDecoration.BOLD))
                        .append(Component.text("    "))
                        .append(Component.newline()))
                .append(Component.newline())
                .append(Component.text("     ")
                        .decoration(TextDecoration.STRIKETHROUGH, false)
                        .append(Component.text("Votez dès maintenant pour soutenir le serveur et recevez des points boutique pour obtenir des récompenses uniques !", NamedTextColor.DARK_GREEN))
                        .append(Component.newline())
                        .append(Component.newline())
                        .append(Component.text("           "))
                        .append(Component.text("Exécutez la commande ou cliquez ci-dessous : ", NamedTextColor.DARK_GREEN))
                        .append(Component.newline())
                        .append(Component.text("                               "))
                        .append(Component.text(" → ", NamedTextColor.DARK_GREEN, TextDecoration.BOLD)
                                .decoration(TextDecoration.STRIKETHROUGH, false)
                                .append(Component.text("/vote", NamedTextColor.GREEN, TextDecoration.BOLD))
                                .append(Component.text(" ← ", NamedTextColor.DARK_GREEN, TextDecoration.BOLD))
                                .clickEvent(net.kyori.adventure.text.event.ClickEvent.clickEvent(net.kyori.adventure.text.event.ClickEvent.Action.RUN_COMMAND, "/vote")))
                        .append(Component.newline()))
                .append(Component.text("                                                                                ", NamedTextColor.GREEN, TextDecoration.STRIKETHROUGH));
    }

    public static Component discordMessage() {
        return Component.text("                                                                                ", NamedTextColor.BLUE, TextDecoration.STRIKETHROUGH)
                .append(Component.newline())
                .append(Component.text("         ")
                        .decoration(TextDecoration.STRIKETHROUGH, false)
                        .append(Component.text(" DÉCOUVREZ NOTRE COMMUNAUTÉ DISCORD ! ", NamedTextColor.AQUA, TextDecoration.BOLD))
                        .append(Component.text("    "))
                        .append(Component.newline()))
                .append(Component.newline())
                .append(Component.text("    ")
                        .decoration(TextDecoration.STRIKETHROUGH, false)
                        .append(Component.text("Rejoignez notre serveur Discord pour être informés des dernières nouvelles et interagir avec notre communauté !", NamedTextColor.DARK_AQUA))
                        .append(Component.newline())
                        .append(Component.newline())
                        .append(Component.text("           "))
                        .append(Component.text("Exécutez la commande ou cliquez ci-dessous : ", NamedTextColor.BLUE))
                        .append(Component.newline())
                        .append(Component.text("                            "))
                        .append(Component.text(" → ", NamedTextColor.BLUE, TextDecoration.BOLD)
                                .decoration(TextDecoration.BOLD, false)
                                .append(Component.text("/discord", NamedTextColor.AQUA, TextDecoration.BOLD))
                                .clickEvent(net.kyori.adventure.text.event.ClickEvent.clickEvent(net.kyori.adventure.text.event.ClickEvent.Action.OPEN_URL, "https://discord.gg/4ajRytDJWK"))
                                .append(Component.text(" ← ", NamedTextColor.BLUE, TextDecoration.BOLD))))
                .append(Component.newline())
                .append(Component.text("                                                                                ", NamedTextColor.BLUE, TextDecoration.STRIKETHROUGH));
    }

    public static Component recrutementMessage() {
        return Component.text("                                                                                ", NamedTextColor.YELLOW, TextDecoration.STRIKETHROUGH)
                .append(Component.newline())
                .append(Component.text("                       ")
                        .decoration(TextDecoration.STRIKETHROUGH, false)
                        .append(Component.text(" TESSERACT RECRUTE !", NamedTextColor.YELLOW, TextDecoration.BOLD)
                                .append(Component.text(" ", NamedTextColor.GOLD, TextDecoration.BOLD)))
                        .append(Component.newline())
                        .append(Component.newline())
                        .append(Component.text("    "))
                        .append(Component.text("Nous recherchons de nouveaux membres pour contribuer au développement de nos projets ! ", NamedTextColor.GOLD))
                        .append(Component.text("Développeur JAVA, Scénariste, Community Manager", NamedTextColor.RED))
                        .append(Component.text(", et autres nombreux postes s'offrent à vous !", NamedTextColor.GOLD))
                        .decoration(TextDecoration.STRIKETHROUGH, false)
                        .append(Component.newline())
                        .append(Component.newline())
                        .append(Component.text("             ")
                                .append(Component.text("Rejoignez-nous dès maintenant (cliquez ici) : ", NamedTextColor.GOLD))
                                .append(Component.newline())
                                .append(Component.text("                           "))
                                .append(Component.text(" → ", NamedTextColor.GOLD, TextDecoration.BOLD)
                                        .decoration(TextDecoration.BOLD, false)
                                        .append(Component.text("Candidater", NamedTextColor.YELLOW, TextDecoration.BOLD)
                                                .clickEvent(net.kyori.adventure.text.event.ClickEvent.clickEvent(net.kyori.adventure.text.event.ClickEvent.Action.OPEN_URL, "https://www.tesseract.onl/candidature/")))
                                        .append(Component.text(" ← ", NamedTextColor.GOLD, TextDecoration.BOLD)))
                                .append(Component.newline())
                                .append(Component.text("                                                                                ", NamedTextColor.YELLOW, TextDecoration.STRIKETHROUGH))));
    }

}
