package onl.tesseract.core.autochatmessage

import net.kyori.adventure.text.Component
import onl.tesseract.core.TesseractCorePlugin
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable

/**
 * Période d'envoi de message 5*60*20 = 5min
 */
private const val PERIOD = (7.5 * 60 * 20).toInt()

open class AutoChatMessage {

    private val messages: MutableList<Component> = ArrayList()
    private var currentMessageIndex = 0

    fun addMessage(message: Component) {
        messages.add(message)
    }

    fun start() {
        object : BukkitRunnable() {
            override fun run() {
                if (messages.isNotEmpty()) {
                    val message = messages[currentMessageIndex++]
                    Bukkit.getOnlinePlayers().forEach { p: Player? -> p!!.sendMessage(message) }
                    currentMessageIndex %= messages.size
                }
            }
        }.runTaskTimer(TesseractCorePlugin.instance, 0, PERIOD.toLong())
    }
}
