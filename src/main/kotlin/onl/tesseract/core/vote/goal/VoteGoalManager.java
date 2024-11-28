package onl.tesseract.core.vote.goal;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import onl.tesseract.core.TesseractCorePlugin;
import onl.tesseract.core.persistence.hibernate.vote.goal.VoteGoalHibernateRepository;
import onl.tesseract.core.vote.VoteGoalService;
import onl.tesseract.core.vote.VoteService;
import onl.tesseract.lib.service.ServiceContainer;
import onl.tesseract.lib.util.ChatFormats;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

/**
 * Manages all current vote goals. Once {@link VoteGoalManager#startLoops()} is called, the manager regularly requests the database to get current
 * vote goals
 */
public class VoteGoalManager {
    private static final Collection<VoteGoal> goals = new HashSet<>();
    private static final Map<VoteGoal, BossBar> bossBars = new HashMap<>();
    private static final int REFRESH_RATE = 30;

    private VoteGoalManager() {
    }

    /**
     * Start the auto-update loop. Calls to {@link VoteGoalManager#update()} are made every {@value REFRESH_RATE} seconds
     */
    public static void startLoops()
    {
        TesseractCorePlugin.instance.getLogger().info("Starting VoteGoalManager");
        new BukkitRunnable() {
            int step = 0;

            @Override
            public void run()
            {
                if (step == 0)
                    displayAll();
                else
                    hideAll();
                update();

                // Display boss bars for 30 seconds every 3 minutes
                step = (step + 1) % 6;
            }
        }.runTaskTimerAsynchronously(TesseractCorePlugin.instance, REFRESH_RATE * 20L, REFRESH_RATE * 20L);
    }

    public static void update()
    {
        Collection<VoteGoal> currentGoals = VoteGoalHibernateRepository.INSTANCE.getCurrentGoals();

        // Determine newly started goals
        for (VoteGoal currentGoal : currentGoals)
        {
            int voteCount = ServiceContainer.get(VoteService.class).countVotesBetween(currentGoal.start(), currentGoal.end());
            if (!goals.contains(currentGoal) && voteCount < currentGoal.requiredQuantity())
            {
                goals.add(currentGoal);
                onNewGoal(currentGoal);
                display(currentGoal);
            }
        }

        for (Iterator<VoteGoal> iterator = goals.iterator(); iterator.hasNext(); )
        {
            VoteGoal goal = iterator.next();
            int voteCount = ServiceContainer.get(VoteService.class).countVotesBetween(goal.start(), goal.end());
            // If vote goal expired
            if (!currentGoals.contains(goal))
            {
                iterator.remove();
                if (voteCount >= goal.requiredQuantity())
                    onGoalCompleted(goal);
                else
                    onGoalFailed();
            }
            else
            {
                // If goal not expired, but enough votes anyway
                if (voteCount >= goal.requiredQuantity())
                {
                    iterator.remove();
                    onGoalCompleted(goal);
                }
            }
        }
    }

    public static void displayAll()
    {
        for (VoteGoal goal : goals)
        {
            display(goal);
        }
    }

    private static void display(VoteGoal goal)
    {
        bossBars.putIfAbsent(goal, Bukkit.createBossBar(" ", BarColor.GREEN, BarStyle.SEGMENTED_10));
        BossBar bar = bossBars.get(goal);
        int voteCount = ServiceContainer.get(VoteGoalService.class).getVoteCount(goal);
        String title = ChatColor.GOLD + String.format("VOTE GOAL | %s - %d/%d", goal.getPrintableRemainingDuration(), voteCount, goal.requiredQuantity());
        bar.setTitle(title);
        double progress = voteCount / (float) goal.requiredQuantity();
        bar.setProgress(Math.min(1d, progress));

        // Show to all players
        bossBars.values().forEach(b -> b.setVisible(true));
        Bukkit.getOnlinePlayers().forEach(bar::addPlayer);
    }

    public static void hideAll()
    {
        bossBars.values().forEach(bar -> bar.setVisible(false));
    }


    private static void onNewGoal(final VoteGoal goal)
    {
        TesseractCorePlugin.instance.getLogger().info("Detected new vote goal of duration " + goal.getPrintableDuration() + ", goal: " + goal.requiredQuantity());

        String duration = goal.getPrintableDuration();
        Component[] components = new Component[] {
                Component.text("                                                                       ", NamedTextColor.RED, TextDecoration.STRIKETHROUGH),
                Component.text("                     ")
                         .append(Component.text("lll", NamedTextColor.WHITE, TextDecoration.OBFUSCATED))
                         .append(Component.text(" VOTE GOAL " + duration, NamedTextColor.GOLD))
                        .append(Component.text(" lll", NamedTextColor.WHITE, TextDecoration.OBFUSCATED)),
                Component.empty(),
                Component.text("                     Objectif : ", NamedTextColor.YELLOW)
                         .append(Component.text(goal.requiredQuantity(), NamedTextColor.GOLD))
                        .append(Component.text(" votes", NamedTextColor.YELLOW)),
                Component.empty(),
                Component.text("                     → ", NamedTextColor.RED, TextDecoration.BOLD)
                         .append(Component.text("/vote", NamedTextColor.YELLOW, TextDecoration.BOLD))
                         .append(Component.text(" ← ", NamedTextColor.RED, TextDecoration.BOLD))
                        .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "/vote")),
                Component.text("                                                                       ", NamedTextColor.RED, TextDecoration.STRIKETHROUGH),
                };

        Bukkit.getOnlinePlayers().forEach(p -> {
            for (var component : components)
            {
                p.sendMessage(component);
                p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 0.9f);
            }
        });
    }

    private static void onGoalCompleted(final VoteGoal goal)
    {
        TesseractCorePlugin.instance.getLogger().info("Vote goal completed");
        Component component = ChatFormats.VOTE.append(Component.text("Le vote goal a été atteint !"));
        Bukkit.getOnlinePlayers().forEach(p -> {
            p.sendMessage(component);
            p.playSound(p.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1, 0.9f);
        });

        final VoteGoalReward reward = goal.reward();
        reward.giveAll();
        Collection<UUID> contributors = ServiceContainer.get(VoteGoalService.class).getContributors(goal);
        contributors.forEach(reward::give);
    }

    private static void onGoalFailed()
    {
        TesseractCorePlugin.instance.getLogger().info("Vote goal failed");
        Component component = ChatFormats.VOTE.append(Component.text("Le vote goal n'a pas été atteint ='("));
        Bukkit.getOnlinePlayers().forEach(p -> p.sendMessage(component));
    }

    public static Collection<VoteGoal> getGoals()
    {
        return goals;
    }
}
