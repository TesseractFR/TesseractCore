package onl.tesseract.core.persistence.hibernate.vote

import onl.tesseract.core.persistence.hibernate.DaoUtils
import onl.tesseract.core.vote.PlayerVote
import onl.tesseract.core.vote.PlayerVoteRepository
import java.sql.Timestamp
import java.time.Instant
import java.util.*

object PlayerVoteHibernateRepository : PlayerVoteRepository {

    override fun getLastVote(playerID: UUID, site: String): PlayerVote? {
        DaoUtils.executeInsideTransaction { session ->
            val query = session.createQuery(
                "FROM PlayerVoteEntity p WHERE p.playerID = :playerID ORDER BY date DESC LIMIT 1",
                PlayerVoteEntity::class.java)

            return query.uniqueResult()?.toModel()
        }
        return null
    }

    override fun getTopVotes(monthDelta: Int): List<Pair<UUID, Int>> {
        val start = Calendar.getInstance()
        start.set(
            start[Calendar.YEAR],
            start[Calendar.MONTH] - monthDelta,
            0, 0, 0, 0
        )
        val end = Calendar.getInstance()
        end.set(
            end[Calendar.YEAR],
            end[Calendar.MONTH] - monthDelta + 1,
            0, 0, 0, 0
        )

        DaoUtils.executeInsideTransaction { session ->
            val query = session.createQuery("SELECT p.playerID, count(*) FROM PlayerVoteEntity p WHERE p.date BETWEEN :start AND :endd GROUP BY p.playerID ORDER BY count(*) DESC LIMIT 10",
                Array::class.java)
            query.setParameter("start", Timestamp.from(start.toInstant()))
            query.setParameter("endd", Timestamp.from(end.toInstant()))
            return query.resultList.map { it[0] as UUID to it[1] as Int }
        }
        return emptyList()
    }

    override fun countVotesBetween(start: Instant, end: Instant): Int {
        DaoUtils.executeInsideTransaction { session ->
            val query = session.createQuery("SELECT count(*) FROM PlayerVoteEntity vote WHERE vote.date BETWEEN :start AND :end", Int::class.java)
            query.setParameter("start", Timestamp.from(start))
            query.setParameter("end", Timestamp.from(end))
            return query.uniqueResult()
        }
        return 0
    }

    override fun getVotesBetween(
        start: Instant,
        end: Instant,
    ): Collection<PlayerVote> {
        DaoUtils.executeInsideTransaction { session ->
            val query = session.createQuery("FROM PlayerVoteEntity vote WHERE vote.date BETWEEN :start AND :end", PlayerVoteEntity::class.java)
            query.setParameter("start", Timestamp.from(start))
            query.setParameter("end", Timestamp.from(end))
            return query.resultList.map { it.toModel() }
        }
        return emptyList()
    }

    override fun getPlayerVoteCount(playerID: UUID?, since: Instant?, serviceName: String?): Long {
        DaoUtils.executeInsideJpaTransaction { session ->
            val builder = session.criteriaBuilder
            val query = builder.createQuery(Long::class.java)

            val root = query.from(PlayerVoteEntity::class.java)
            query.select(builder.count(root))
            if (playerID != null)
                query.where(builder.equal(root.get<String>("playerID"), playerID.toString()))
            if (since != null)
                query.where(builder.between(root.get<Timestamp>("date"), Timestamp.from(since), Timestamp.from(Instant.now())))
            if (serviceName != null)
                query.where(builder.equal(root.get<String>("serviceName"), serviceName))

            return session.createQuery(query).singleResult
        }
        return 0
    }
}