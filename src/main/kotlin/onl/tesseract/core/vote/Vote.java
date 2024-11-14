package onl.tesseract.core.vote;

import java.time.Instant;
import java.util.UUID;

public record Vote(int id, UUID playerUUID, Instant date, VoteSite site) {
}
