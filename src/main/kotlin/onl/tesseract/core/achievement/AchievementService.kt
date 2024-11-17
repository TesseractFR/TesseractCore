package onl.tesseract.core.achievement

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.format.NamedTextColor
import onl.tesseract.lib.util.ChatFormats
import org.bukkit.Bukkit
import java.util.UUID

class AchievementService(private val repository: AchievementRepository) {

    fun getByName(name: String): Achievement? {
        return repository.getByName(name)
    }

    fun getPlayerAchievements(playerUUID: UUID): Collection<Achievement> {
        return repository.getAchievements(playerUUID)
    }

    fun hasAchievement(playerUUID: UUID, achievement: Achievement): Boolean {
        return getPlayerAchievements(playerUUID).contains(achievement)
    }

    fun addAchievement(playerUUID: UUID, achievement: Achievement, announce: Boolean = true) {
        if (hasAchievement(playerUUID, achievement)) return

        repository.addAchievementToPlayer(playerUUID, achievement)
        val player = Bukkit.getPlayer(playerUUID) ?: return
        player.sendMessage(ChatFormats.HAUT_FAIT.append(Component.text("Vous avez obtenu le haut-fait ")))
        player.sendMessage(
            Component.empty().append(Component.text("      « ").color(NamedTextColor.AQUA))
                .append(Component.text(achievement.displayName).color(NamedTextColor.AQUA))
                .hoverEvent(HoverEvent.showText(Component.text(achievement.condition).color(NamedTextColor.AQUA)))
                .append(Component.text(" » ").color(NamedTextColor.AQUA))
        )

        if (announce) {
            Bukkit.getOnlinePlayers()
                .filter { it.uniqueId != playerUUID }
                .forEach {
                    it.sendMessage(
                        ChatFormats.HAUT_FAIT.append(Component.text(player.name + " a obtenu le haut-fait "))
                    )
                    it.sendMessage(
                        Component.empty().append(Component.text("      « ").color(NamedTextColor.AQUA))
                            .append(Component.text(achievement.displayName).color(NamedTextColor.AQUA)).hoverEvent(
                                HoverEvent.showText(
                                    Component.text(achievement.condition).color(NamedTextColor.AQUA)
                                )
                            ).append(Component.text(" » ").color(NamedTextColor.AQUA))
                    )
                }
        }
    }
}
