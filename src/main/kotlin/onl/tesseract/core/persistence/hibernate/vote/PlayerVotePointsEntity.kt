package onl.tesseract.core.persistence.hibernate.vote

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import onl.tesseract.core.vote.PlayerVotePoints
import org.hibernate.annotations.JdbcTypeCode
import java.util.*

@Entity
@Table(name = "t_vote_points")
class PlayerVotePointsEntity(
    @Id
    @Column(name = "player_uuid", columnDefinition = "VARCHAR(36)", unique = true, nullable = false)
    @JdbcTypeCode(java.sql.Types.VARCHAR)
    val playerID: UUID,
    var amount: Int = 0,
) {

    fun toModel(): PlayerVotePoints {
        return PlayerVotePoints(playerID, amount)
    }
}

fun PlayerVotePoints.toEntity(): PlayerVotePointsEntity {
    return PlayerVotePointsEntity(
        this.playerID,
        this.points
    )
}