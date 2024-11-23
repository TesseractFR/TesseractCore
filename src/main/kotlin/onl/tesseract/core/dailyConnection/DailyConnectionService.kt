package onl.tesseract.core.dailyConnection

import onl.tesseract.core.dailyConnection.event.PlayerDailyConnectionEvent
import onl.tesseract.core.dailyConnection.event.PlayerFirstConnectionEvent
import onl.tesseract.core.dailyConnection.event.PlayerServerDailyConnectionEvent
import onl.tesseract.core.dailyConnection.event.PlayerServerFirstConnectionEvent
import onl.tesseract.lib.event.EventService
import onl.tesseract.lib.service.ServiceContainer
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import java.time.LocalDateTime
import java.util.UUID

val GLOBAL_SERVEUR = "Global"
/**
 * Service used to track a player's first and last connection to a server
 */
class DailyConnectionService(
    /**
     * Name of the current server
     */
    val server: String,
    private val repository: DailyConnectionRepository,
) : Listener {

    /**
     * Check if a player has joined the server today
     * @param playerID Player
     * @param server Name of the server to check, or null to check globally across servers
     */
    fun hasPlayedToday(playerID: UUID, server: String): Boolean {
        val lastConnection = getLastConnectionDate(playerID, server) ?: return false
        val now = LocalDateTime.now()
        return now.year == lastConnection.year && now.dayOfYear == lastConnection.dayOfYear
    }

    /**
     * @param playerID Player
     * @param server Name of the server to check, or null to check globally across servers
     * @return Date of last join, or null if the player never joined the server
     */
    fun getLastConnectionDate(playerID: UUID, server: String): LocalDateTime? {
        return getDatesForServer(playerID, server)?.lastConnection
    }

    /**
     * @param playerID Player
     * @param server Name of the server to check, or null to check globally across servers
     * @return Date of first join on the server, or null if the player never joined the server
     */
    fun getFirstConnectionDate(playerID: UUID, server: String): LocalDateTime? {
        return getDatesForServer(playerID, server)?.firstConnection
    }

    private fun getDatesForServer(playerID: UUID, server: String): PlayerConnectionDates? {
        val playerDates = repository.getPlayerDates(playerID)
        return playerDates.find { it.server == server }
    }

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val eventService = ServiceContainer[EventService::class.java]
        val now = LocalDateTime.now()
        val playerID = event.player.uniqueId

        if (getFirstConnectionDate(playerID, GLOBAL_SERVEUR) == null)
            eventService.callEvent(PlayerFirstConnectionEvent(playerID, now))
        if (getFirstConnectionDate(playerID, server) == null)
            eventService.callEvent(PlayerServerFirstConnectionEvent(playerID, now, server))
        if (!hasPlayedToday(playerID, GLOBAL_SERVEUR))
            eventService.callEvent(PlayerDailyConnectionEvent(playerID, now))
        if (!hasPlayedToday(playerID, server))
            eventService.callEvent(PlayerServerDailyConnectionEvent(playerID, now, server))

        val serverDate = getDatesForServer(event.player.uniqueId, server)
                ?: PlayerConnectionDates(event.player.uniqueId, server, now, now)
        val globalDate = getDatesForServer(event.player.uniqueId, GLOBAL_SERVEUR)
                ?: PlayerConnectionDates(event.player.uniqueId, GLOBAL_SERVEUR, now, now)

        serverDate.lastConnection = now
        globalDate.lastConnection = now
        repository.save(serverDate)
        repository.save(globalDate)
    }
}

class PlayerConnectionDates(
    val playerID: UUID,
    val server: String,
    var lastConnection: LocalDateTime,
    var firstConnection: LocalDateTime,
)

interface DailyConnectionRepository {

    fun getPlayerDates(id: UUID): Collection<PlayerConnectionDates>

    fun save(dates: PlayerConnectionDates)
}