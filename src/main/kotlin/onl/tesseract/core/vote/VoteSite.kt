package onl.tesseract.core.vote

import java.time.Duration
import java.time.Instant
import java.util.UUID

data class VoteSite(
    val serviceName: String,
    val address: String,
    val delay: Duration,
)

data class PlayerVote(
    val playerID: UUID,
    val date: Instant,
    val serviceName: String,
)