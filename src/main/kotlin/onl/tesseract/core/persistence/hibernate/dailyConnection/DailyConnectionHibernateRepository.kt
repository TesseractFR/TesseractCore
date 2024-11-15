package onl.tesseract.core.persistence.hibernate.dailyConnection

import onl.tesseract.core.dailyConnection.DailyConnectionRepository
import onl.tesseract.core.dailyConnection.PlayerConnectionDates
import onl.tesseract.core.persistence.hibernate.DaoUtils
import java.util.UUID

object DailyConnectionHibernateRepository : DailyConnectionRepository {

    override fun getPlayerDates(id: UUID): Collection<PlayerConnectionDates> {
        DaoUtils.executeInsideTransaction {
            val query = it.createQuery("FROM PlayerConnectionDatesEntity e WHERE e.id.playerID = :player", PlayerConnectionDatesEntity::class.java)
            query.setParameter("player", id.toString())
            return query.resultList.map { it.toModel() }
        }
        return emptyList()
    }

    override fun save(dates: PlayerConnectionDates) {
        DaoUtils.executeInsideTransaction {
            it.persist(dates.toEntity())
        }
    }
}

fun PlayerConnectionDates.toEntity(): PlayerConnectionDatesEntity {
    return PlayerConnectionDatesEntity(
        PlayerConnectionDatesEntity.CompositeId(this.playerID, this.server),
        this.lastConnection,
        this.firstConnection,
    )
}

fun PlayerConnectionDatesEntity.toModel(): PlayerConnectionDates {
    return PlayerConnectionDates(
        this.id.playerID,
        this.id.server,
        this.lastConnection,
        this.firstConnection,
    )
}