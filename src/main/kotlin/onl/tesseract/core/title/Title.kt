package onl.tesseract.core.title

import onl.tesseract.lib.player.Gender

data class Title(
    val id: String,
    val nameM: String,
    val nameF: String,
)
{
    fun getDisplayName(gender: Gender): String {
        return if (gender == Gender.FEMALE)
            nameF
        else
            nameM
    }
}