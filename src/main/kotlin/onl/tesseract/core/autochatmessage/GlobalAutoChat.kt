package onl.tesseract.core.autochatmessage

object GlobalAutoChat {

    val instance: AutoChatMessage = AutoChatMessage()
    private var started: Boolean = false

    @Synchronized
    fun startOnce() {
        if (!started) {
            instance.start()
            started = true
        }
    }
}
