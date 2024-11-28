package onl.tesseract.core.vote

import onl.tesseract.lib.repository.ReadRepository
import onl.tesseract.lib.repository.Repository
import java.time.Duration
import java.time.Instant
import java.util.*

class VoteService(
    private val siteRepository: VoteSiteRepository,
    private val playerVoteRepository: PlayerVoteRepository,
    private val playerVotePointsRepository: PlayerVotePointsRepository,
) {

    enum class VotePeriod { YEARLY, MONTHLY, WEEKLY, DAILY, TOTAL }

    fun getVoteSites(): List<VoteSite> {
        return siteRepository.getAll()
    }

    fun getRemainingTimeUntilVote(playerID: UUID): Map<VoteSite, Duration> {
        return getVoteSites()
                .associateWith { site -> getRemainingTimeUntilVote(playerID, site) }
    }

    fun getRemainingTimeUntilVote(playerID: UUID, site: VoteSite): Duration {
        val lastVoteDate = playerVoteRepository.getLastVote(playerID, site.serviceName)?.date ?: return Duration.ZERO
        val remainingDelay = Duration.between(Instant.now(), lastVoteDate.plus(site.delay))
        return if (remainingDelay.isPositive) remainingDelay else Duration.ZERO
    }

    fun getTop(monthDelta: Int = 0): List<Pair<UUID, Int>> {
        return playerVoteRepository.getTopVotes(monthDelta)
    }

    fun getPlayerVotePoints(playerID: UUID): Int {
        return playerVotePointsRepository.getById(playerID)?.points ?: 0
    }

    fun remotePlayerVotePoints(playerID: UUID, amount: Int) {
        playerVotePointsRepository.removePoints(playerID, amount)
    }

    fun countVotesBetween(start: Instant, end: Instant): Int {
        return playerVoteRepository.countVotesBetween(start, end)
    }

    fun getVotersBetween(start: Instant, end: Instant): Collection<UUID> {
        return playerVoteRepository.getVotesBetween(start, end).map { it.playerID }
    }

    fun countVotesDuringPeriod(period: VotePeriod, playerID: UUID? = null, site: VoteSite? = null): Long {
        return playerVoteRepository.getPlayerVoteCount(playerID, periodToStartDate(period), site?.serviceName)
    }

    private fun periodToStartDate(period: VotePeriod): Instant {
        if (period == VotePeriod.TOTAL) return Instant.EPOCH
        val start = Calendar.getInstance()
        if (period == VotePeriod.YEARLY)
            start[Calendar.MONTH] = 0
        if (period == VotePeriod.MONTHLY)
            start[Calendar.DAY_OF_MONTH] = 0
        if (period == VotePeriod.WEEKLY)
            start[Calendar.DAY_OF_WEEK] = 0
        start[Calendar.HOUR_OF_DAY] = 0
        start[Calendar.MINUTE] = 0
        start[Calendar.SECOND] = 0
        return start.toInstant()
    }
}

interface VoteSiteRepository : ReadRepository<VoteSite, String> {

    fun getAll(): List<VoteSite>
}

interface PlayerVoteRepository {

    fun getLastVote(playerID: UUID, site: String): PlayerVote?

    fun getTopVotes(monthDelta: Int): List<Pair<UUID, Int>>

    fun countVotesBetween(start: Instant, end: Instant): Int
    fun getVotesBetween(start: Instant, end: Instant): Collection<PlayerVote>
    fun getPlayerVoteCount(playerID: UUID?, since: Instant?, serviceName: String?): Long
}

interface PlayerVotePointsRepository : Repository<PlayerVotePoints, UUID> {

    fun removePoints(playerID: UUID, amount: Int)
}