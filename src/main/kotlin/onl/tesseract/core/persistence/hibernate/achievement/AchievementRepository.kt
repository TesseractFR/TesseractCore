package onl.tesseract.core.persistence.hibernate.achievement

import onl.tesseract.core.achievement.Achievement
import onl.tesseract.core.achievement.AchievementRepository
import onl.tesseract.core.persistence.hibernate.DaoUtils

object AchievementHibernateRepository : AchievementRepository {

    override fun getById(id: Int): Achievement? {
        DaoUtils.executeInsideTransaction { session ->
            return session.get(AchievementEntity::class.java, id)?.toModel()
        }
        return null
    }

    override fun getByName(name: String): Achievement? {
        DaoUtils.executeInsideTransaction { session ->
            val query = session.createQuery("FROM Achievement WHERE name = :name", AchievementEntity::class.java)
            query.setParameter("name", name)
            return query.uniqueResult().toModel()
        }
        return null
    }

    override fun save(entity: Achievement) {
        DaoUtils.executeInsideTransaction { session ->
            session.persist(entity)
        }
    }

    override fun getAll(): List<Achievement> {
        DaoUtils.executeInsideTransaction { session ->
            val query = session.createQuery("SELECT Achievement a FROM Achievement", AchievementEntity::class.java)
            return query.resultList.map { it.toModel() }
        }
        return listOf()
    }

    override fun idOf(entity: Achievement): Int = entity.id
}