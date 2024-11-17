package onl.tesseract.core.vote

import onl.tesseract.lib.repository.ReadRepository
import java.time.Duration
import java.time.Instant
import java.util.*

class VoteService(
    private val siteRepository: VoteSiteRepository,
    private val playerVoteRepository: PlayerVoteRepository,
) {

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
}

interface VoteSiteRepository : ReadRepository<VoteSite, String> {

    fun getAll(): List<VoteSite>
}

interface PlayerVoteRepository {

    fun getLastVote(playerID: UUID, site: String): PlayerVote?

    fun getTopVotes(monthDelta: Int): List<Pair<UUID, Int>>
}