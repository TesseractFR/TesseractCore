package onl.tesseract.core.persistence.hibernate.vote.goal

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import onl.tesseract.core.vote.goal.VoteGoal
import onl.tesseract.core.vote.goal.VoteGoalManager
import onl.tesseract.core.vote.goal.VoteGoalRewardManager
import java.sql.Timestamp

@Entity
@Table(name = "t_vote_goal")
class VoteGoalEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int,
    val start: Timestamp,
    val end: Timestamp,
    val quantity: Int,
    @Column(name = "reward_type")
    val rewardType: String,
    @Column(name = "reward_raw")
    val rewardRaw: String,
) {

    fun toModel(): VoteGoal {
        return VoteGoal(
            id,
            start.toInstant(),
            end.toInstant(),
            quantity,
            VoteGoalRewardManager.getRewardType(rewardType).deserialize(rewardRaw)
        )
    }
}