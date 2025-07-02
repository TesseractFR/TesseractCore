package onl.tesseract.core.persistence.hibernate.vote

import jakarta.persistence.*
import onl.tesseract.core.vote.PlayerVote
import org.hibernate.annotations.JdbcTypeCode
import java.sql.Timestamp
import java.sql.Types
import java.util.*

@Entity
@Table(name = "t_vote")
class PlayerVoteEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int,
    @Column(name = "player_uuid", updatable = false, nullable = false, columnDefinition = "VARCHAR(36)")
    @JdbcTypeCode(
        Types.VARCHAR
    )
    val playerID: UUID,
    @Column(name = "`date`", nullable = false)
    val date: Timestamp,

    @ManyToOne(targetEntity = VoteSiteEntity::class, fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "service_name")
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