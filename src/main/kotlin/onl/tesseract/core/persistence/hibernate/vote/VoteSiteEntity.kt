package onl.tesseract.core.persistence.hibernate.vote

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import onl.tesseract.core.vote.VoteSite
import java.time.Duration
import java.time.temporal.ChronoUnit

@Entity
@Table(name = "t_vote_site")
class VoteSiteEntity(
    @Id
    @Column(name = "service_name")
    val serviceName: String,
    val address: String,
    @Column(name = "delay_minutes")
    val delayMinutes: Int,
) {

    fun toModel(): VoteSite {
        return VoteSite(
            serviceName,
            address,
            Duration.of(delayMinutes.toLong(), ChronoUnit.MINUTES)
        )
    }

    fun VoteSite.toEntity(): VoteSiteEntity {
        return VoteSiteEntity(
            this.serviceName,
            this.address,
            this.delay.toMinutes().toInt(),
        )
    }
}