package onl.tesseract.core.persistence.hibernate.vote

import jakarta.persistence.*
import onl.tesseract.core.vote.PlayerVote
import java.sql.Timestamp
import java.util.*

@Entity
class PlayerVoteEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int,
    @Column(name = "player_uuid", nullable = false)
    val playerID: UUID,
    @Column(name = "`date`", nullable = false)
    val date: Timestamp,
    @ManyToOne(targetEntity = VoteSiteEntity::class, fetch = FetchType.EAGER)
    val site: VoteSiteEntity,
) {

    fun toModel(): PlayerVote {
        return PlayerVote(
            playerID,
            date.toInstant(),
            site.serviceName
        )
    }
}