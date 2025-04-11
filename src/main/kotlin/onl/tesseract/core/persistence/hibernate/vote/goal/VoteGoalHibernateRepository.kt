package onl.tesseract.core.persistence.hibernate.vote.goal

import onl.tesseract.core.persistence.hibernate.DaoUtils
import onl.tesseract.core.vote.VoteGoalRepository
import onl.tesseract.core.vote.goal.VoteGoal
import java.sql.Timestamp

object VoteGoalHibernateRepository : VoteGoalRepository {
    override fun create(goal: VoteGoal) {
        DaoUtils.executeInsideTransaction { session ->
            session.persist(goal.toEntity())
        }
    }

    override fun save(entity: VoteGoal): VoteGoal {
        DaoUtils.executeInsideTransaction { session ->
            session.merge(entity.toEntity())
        }
        return entity
    }

    override fun getCurrentGoals(): Collection<VoteGoal> {
        DaoUtils.executeInsideTransaction { session ->
            val query = session.createQuery("FROM VoteGoalEntity e WHERE e.start <= NOW() AND e.end > NOW()", VoteGoalEntity::class.java)
            return query.resultList.map { it.toModel() }
        }
        return emptyList()
    }

    override fun getAll(): Collection<VoteGoal> {
        DaoUtils.executeInsideTransaction { session ->
            val query = session.createQuery("FROM VoteGoalEntity", VoteGoalEntity::class.java)
            return query.resultList.map { it.toModel() }
        }
        return emptyList()
    }

    override fun getById(id: Int): VoteGoal? {
        DaoUtils.executeInsideTransaction { session ->
            val goal = session.find(VoteGoalEntity::class.java, id)
            return goal?.toModel()
        }
        return null
    }

    override fun idOf(entity: VoteGoal): Int = entity.id

    private fun VoteGoal.toEntity(): VoteGoalEntity {
        return VoteGoalEntity(
            this.id,
            Timestamp.from(this.start),
            Timestamp.from(this.end),
            this.requiredQuantity(),
            this.reward().type.name,
            this.reward().serialize(),
        )
    }
}