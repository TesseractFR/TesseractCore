package onl.tesseract.core.achievement

import onl.tesseract.lib.repository.Repository

interface AchievementRepository : Repository<Achievement, Int> {

    fun getByName(name: String): Achievement?

    fun getAll(): List<Achievement>
}