package onl.tesseract.core.dailyConnection.event

import org.bukkit.event.Event
import org.bukkit.event.HandlerList
import java.time.LocalDateTime
import java.util.UUID

class PlayerServerFirstConnectionEvent(val playerID: UUID, val date: LocalDateTime, val server: String) : Event() {

    override fun getHandlers(): HandlerList {
        return handlerList
    }

    companion object {
        @JvmStatic
        val handlerList = HandlerList()
    }
}