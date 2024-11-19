package onl.tesseract.core.vote

import onl.tesseract.core.vote.goal.VoteGoal
import onl.tesseract.core.vote.goal.VoteGoalReward
import onl.tesseract.lib.repository.Repository
import java.time.Instant
import java.util.UUID

class VoteGoalService(
    val voteService: VoteService,
    private val goalRepository: VoteGoalRepository
) {

    /**
     * Get the amount of votes made during the period of time defined by the given vote goal
     */
    fun getVoteCount(goal: VoteGoal): Int {
        return voteService.countVotesBetween(goal.start, goal.end)
    }

    fun createVoteGoal(start: Instant, end: Instant, quantity: Int, reward: VoteGoalReward?) {
        goalRepository.create(VoteGoal(-1, start, end, quantity, reward))
    }

    /**
     * Get a collection of all players that contributed to the completion of the given vote goal
     */
    fun getContributors(goal: VoteGoal): Collection<UUID> {
        return voteService.getVotersBetween(goal.start, goal.end)
    }
}

interface VoteGoalRepository : Repository<VoteGoal, Int> {

    fun getAll(): Collection<VoteGoal>

    fun getCurrentGoals(): Collection<VoteGoal>

    fun create(goal: VoteGoal)
}
