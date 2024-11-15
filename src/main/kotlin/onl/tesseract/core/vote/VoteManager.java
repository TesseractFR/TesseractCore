package onl.tesseract.core.vote;

import onl.tesseract.core.persistence.hibernate.vote.VoteRepository;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class VoteManager {
    private static VoteManager INSTANCE;

    public static VoteManager getInstance()
    {
        if (INSTANCE == null)
            INSTANCE = new VoteManager();
        return INSTANCE;
    }

    final Collection<VoteSite> voteSites = new ArrayList<>();

    public void init()
    {
        loadVoteSites();
    }

    private void loadVoteSites()
    {
        voteSites.clear();
        voteSites.addAll(VoteRepository.getVoteSites());
    }

    public Collection<VoteSite> getVoteSites()
    {
        return voteSites;
    }

    public VoteSite getVoteSite(final String voteSiteName)
    {
        for (VoteSite voteSite : voteSites)
        {
            if (voteSite.serviceName().equalsIgnoreCase(voteSiteName))
                return voteSite;
        }
        throw new IllegalArgumentException("Unknown vote site");
    }

    public Map<VoteSite, Duration> getRemainingTimeUntilVote(final UUID player)
    {
        Map<VoteSite, Duration> map = new HashMap<>();
        voteSites.forEach(voteSite -> map.put(voteSite, getRemainingTimeUntilVote(player, voteSite)));
        return map;
    }

    public Duration getRemainingTimeUntilVote(final UUID player, final VoteSite voteSite)
    {
        return VoteRepository.getLastVote(player, voteSite.serviceName())
                             .map(vote -> {
                                 Instant unlockInstant = vote.date().plus(voteSite.delay());
                                 return Duration.between(Instant.now(), unlockInstant);
                             })
                             .orElse(Duration.ZERO);
    }
}
