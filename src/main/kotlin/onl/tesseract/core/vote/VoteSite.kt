package onl.tesseract.core.vote;

import java.time.Duration;

public record VoteSite(String serviceName, String address, Duration delay) {
}
