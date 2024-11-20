package onl.tesseract.core.persistence.hibernate.achievement

import onl.tesseract.core.achievement.Achievement
import onl.tesseract.core.achievement.AchievementRepository
import onl.tesseract.core.persistence.hibernate.DaoUtils
import java.util.UUID

object AchievementHibernateRepository : AchievementRepository {

    override fun getById(id: Int): Achievement? {
        DaoUtils.executeInsideTransaction { session ->
            return session[AchievementEntity::class.java, id]?.toModel()
        }
        return null
    }

    override fun getByName(name: String): Achievement? {
        DaoUtils.executeInsideTransaction { session ->
            val query = session.createQuery("FROM AchievementEntity WHERE name = :name", AchievementEntity::class.java)
            query.setParameter("name", name)
            return query.uniqueResult().toModel()
        }
        return null
    }

    override fun save(entity: Achievement) {
        DaoUtils.executeInsideTransaction { session ->
            session.merge(AchievementEntity.fromModel(entity))
        }
    }

    override fun getAll(): List<Achievement> {
        DaoUtils.executeInsideTransaction { session ->
            val query =
                session.createQuery("SELECT AchievementEntity a FROM AchievementEntity ", AchievementEntity::class.java)
            return query.resultList.map { it.toModel() }
        }
        return listOf()
    }

    override fun getAchievements(player: UUID): Collection<Achievement> {
        DaoUtils.executeInsideTransaction { session ->
            val query = session.createQuery(
                "SELECT AchievementEntity FROM PlayerAchievementsEntity p JOIN AchievementEntity a ON p.achievementID = a.id AND p.player = :player",
                AchievementEntity::class.java
            )
            query.setParameter("player", player)
            return query.resultList.map { it.toModel() }
        }
        return emptyList()
    }

    override fun addAchievementToPlayer(
        player: UUID,
        achievement: Achievement
    ) {
        DaoUtils.executeInsideTransaction { session ->
            session.merge(PlayerAchievementsEntity(player, achievement.id))
        }
    }

    override fun idOf(entity: Achievement): Int = entity.id
}