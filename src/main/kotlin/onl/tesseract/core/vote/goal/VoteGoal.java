package onl.tesseract.core.vote.goal;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

@SuppressWarnings("unused")
public record VoteGoal(int id, Instant start, Instant end, int requiredQuantity, VoteGoalReward reward) {

    public Duration getDuration()
    {
        return Duration.between(start, end);
    }

    public Duration getRemainingDuration()
    {
        return Duration.between(Instant.now(), end);
    }

    public String getPrintableRemainingDuration()
    {
        return getPrintableDurationHelper(getRemainingDuration());
    }

    public String getPrintableDuration()
    {
        return getPrintableDurationHelper(getDuration());
    }

    private String getPrintableDurationHelper(final Duration duration)
    {
        long days = duration.toDays();
        if (days > 0)
            return String.format("%dj", duration.toHoursPart() < 12 ? days : days + 1);
        long hours = duration.toHours();
        long minutes = duration.toMinutesPart();
        if (minutes % 60 < 10)
        {
            return String.format("%dh", hours + (minutes < 10 ? 0 : 1));
        }
        return String.format("%dh%02dm", hours, minutes);
    }

    @Override
    public boolean equals(final Object o)
    {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        final VoteGoal voteGoal = (VoteGoal) o;
        return id == voteGoal.id;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(id);
    }
}
