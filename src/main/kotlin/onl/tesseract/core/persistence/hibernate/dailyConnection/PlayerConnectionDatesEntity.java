package onl.tesseract.core.persistence.hibernate.dailyConnection;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;

import java.sql.Types;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "t_player_connection_dates")
public class PlayerConnectionDatesEntity {

    @EmbeddedId
    private CompositeId id;
    private LocalDateTime lastConnection;
    private LocalDateTime firstConnection;

    @Embeddable
    public record CompositeId(
            @Column(columnDefinition = "VARCHAR(36)")
            @JdbcTypeCode(Types.VARCHAR)
            UUID playerID,
            String server) {
    }

    public PlayerConnectionDatesEntity() {
    }

    public PlayerConnectionDatesEntity(CompositeId id, LocalDateTime firstConnection, LocalDateTime lastConnection) {
        this.firstConnection = firstConnection;
        this.id = id;
        this.lastConnection = lastConnection;
    }

    public LocalDateTime getFirstConnection() {
        return firstConnection;
    }

    public CompositeId getId() {
        return id;
    }

    public LocalDateTime getLastConnection() {
        return lastConnection;
    }

    public PlayerConnectionDatesEntity setLastConnection(LocalDateTime lastConnection) {
        this.lastConnection = lastConnection;
        return this;
    }
}
