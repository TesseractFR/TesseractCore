package onl.tesseract.core.persistence.hibernate.vote

import onl.tesseract.core.persistence.hibernate.DaoUtils
import onl.tesseract.core.vote.PlayerVotePoints
import onl.tesseract.core.vote.PlayerVotePointsRepository
import java.util.UUID

object PlayerVotePointsHibernateRepository : PlayerVotePointsRepository {

    override fun removePoints(playerID: UUID, amount: Int) {
        DaoUtils.executeInsideTransaction { session ->
            val e = session.find(PlayerVotePointsEntity::class.java, playerID) ?: return
            e.amount -= amount
            session.merge(e)
        }
    }

    override fun save(entity: PlayerVotePoints) {
        DaoUtils.executeInsideTransaction { session ->
            session.merge(entity.toEntity())
        }
    }

    override fun getById(id: UUID): PlayerVotePoints? {
        DaoUtils.executeInsideTransaction { session ->
            return session.find(PlayerVotePointsEntity::class.java, id)?.toModel()
        }
        return null
    }

    override fun idOf(entity: PlayerVotePoints): UUID = entity.playerID
}