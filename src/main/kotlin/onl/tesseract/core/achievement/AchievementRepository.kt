package onl.tesseract.core.achievement

import onl.tesseract.lib.repository.Repository
import java.util.UUID

interface AchievementRepository : Repository<Achievement, Int> {

    fun getByName(name: String): Achievement?

    fun getAll(): List<Achievement>

    fun getAchievements(player: UUID): Collection<Achievement>

    fun addAchievementToPlayer(player: UUID, achievement: Achievement)
}