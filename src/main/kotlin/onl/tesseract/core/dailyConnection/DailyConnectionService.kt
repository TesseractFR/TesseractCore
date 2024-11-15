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

class DailyConnectionService(
    val server: String,
    private val repository: DailyConnectionRepository,
) : Listener {

    fun hasPlayedToday(playerID: UUID, server: String?): Boolean {
        val lastConnection = getLastConnectionDate(playerID, server) ?: return false
        val now = LocalDateTime.now()
        return now.year == lastConnection.year && now.dayOfYear == lastConnection.dayOfYear
    }

    fun getLastConnectionDate(playerID: UUID, server: String?): LocalDateTime? {
        return getDatesForServer(playerID, server)?.lastConnection
    }

    fun getFirstConnectionDate(playerID: UUID, server: String?): LocalDateTime? {
        return getDatesForServer(playerID, server)?.firstConnection
    }

    private fun getDatesForServer(playerID: UUID, server: String?): PlayerConnectionDates? {
        val playerDates = repository.getPlayerDates(playerID)
        return playerDates.find { it.server == server }
    }

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val eventService = ServiceContainer[EventService::class.java]
        val now = LocalDateTime.now()
        val playerID = event.player.uniqueId

        if (getFirstConnectionDate(playerID, null) == null)
            eventService.callEvent(PlayerFirstConnectionEvent(playerID, now))
        if (getFirstConnectionDate(playerID, server) == null)
            eventService.callEvent(PlayerServerFirstConnectionEvent(playerID, now, server))
        if (!hasPlayedToday(playerID, null))
            eventService.callEvent(PlayerDailyConnectionEvent(playerID, now))
        if (!hasPlayedToday(playerID, server))
            eventService.callEvent(PlayerServerDailyConnectionEvent(playerID, now, server))

        val serverDate = getDatesForServer(event.player.uniqueId, server)
                ?: PlayerConnectionDates(event.player.uniqueId, server, now, now)
        val globalDate = getDatesForServer(event.player.uniqueId, null)
                ?: PlayerConnectionDates(event.player.uniqueId, null, now, now)

        serverDate.lastConnection = now
        globalDate.lastConnection = now
        repository.save(serverDate)
        repository.save(globalDate)
    }
}

class PlayerConnectionDates(
    val playerID: UUID,
    val server: String?,
    var lastConnection: LocalDateTime,
    var firstConnection: LocalDateTime,
)

interface DailyConnectionRepository {

    fun getPlayerDates(id: UUID): Collection<PlayerConnectionDates>

    fun save(dates: PlayerConnectionDates)
}