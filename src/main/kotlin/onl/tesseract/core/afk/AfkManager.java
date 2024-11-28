package onl.tesseract.core.afk;

import io.papermc.paper.event.player.AsyncChatEvent;
import kotlin.Unit;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import onl.tesseract.lib.service.ServiceContainer;
import onl.tesseract.lib.task.TaskScheduler;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandSendEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AfkManager implements Listener {
    private static final @NotNull TextComponent MESSAGE_NOT_AFK_ANYMORE = Component.text("Vous n'êtes plus AFK", NamedTextColor.GRAY);

    private Set<UUID> afks = new HashSet<>();
    private HashMap<UUID, Instant> lastMessage = new HashMap<>();
    private HashMap<UUID, Instant> lastMove = new HashMap<>();
    private HashMap<UUID, Location> lastLocations = new HashMap<>();

    public void startTask() {
        ServiceContainer.get(TaskScheduler.class).runTimer(0L, 10 * 20L, () -> {
            Bukkit.getOnlinePlayers().forEach(player -> {
                if (lastLocations.containsKey(player.getUniqueId())) {
                    checkAndUpdateAFKState(player);
                }
                lastLocations.put(player.getUniqueId(), player.getLocation());
            });
           return Unit.INSTANCE;
        });
    }

    private void checkAndUpdateAFKState(Player player) {
        boolean isAFK = isAfkLocation(player) && isAfkChat(player);
        if (isAFK && !afks.contains(player.getUniqueId())) {
            afks.add(player.getUniqueId());
            player.sendMessage(Component.text("Vous êtes désormais AFK", NamedTextColor.GRAY));
        } else if (!isAFK && afks.contains(player.getUniqueId())) {
            afks.remove(player.getUniqueId());
            player.sendMessage(MESSAGE_NOT_AFK_ANYMORE);
        }
    }

    private boolean isAfkChat(Player player) {
        if (!lastMessage.containsKey(player.getUniqueId())) {
            return true;
        }
        Duration duration = Duration.between(lastMessage.get(player.getUniqueId()), Instant.now());
        return duration.toMillis() > 30_000;
    }

    private boolean isAfkLocation(Player player) {
        if (!lastLocations.containsKey(player.getUniqueId())) {
            lastLocations.put(player.getUniqueId(), player.getLocation());
            return true;
        }
        Location lastLocation = lastLocations.get(player.getUniqueId());
        Location playerLocation = player.getLocation();
        boolean isAfkLocation = isSameLocation(lastLocation, playerLocation);
        boolean isAfkEyes = isSameEyes(lastLocation, playerLocation);
        if (isAfkLocation || isAfkEyes) {
            if (!lastMove.containsKey(player.getUniqueId())) {
                lastMove.put(player.getUniqueId(), Instant.now());
            }
            Duration duration = Duration.between(lastMove.get(player.getUniqueId()), Instant.now());
            return duration.toMillis() > 60_000;
        }
        lastMove.put(player.getUniqueId(), Instant.now());
        return false;
    }

    private boolean isSameLocation(Location lastLocation, Location playerLocation) {
        return lastLocation.getWorld().equals(playerLocation.getWorld()) &&
                lastLocation.getX() == playerLocation.getX() &&
                lastLocation.getY() == playerLocation.getY() &&
                lastLocation.getZ() == playerLocation.getZ();
    }

    private boolean isSameEyes(Location lastLocation, Location playerLocation) {
        return lastLocation.getYaw() == playerLocation.getYaw() && lastLocation.getPitch() == playerLocation.getPitch();
    }

    public boolean isAfk(Player player) {
        return afks.contains(player.getUniqueId());
    }

    @EventHandler
    void onQuit(PlayerQuitEvent event) {
        afks.remove(event.getPlayer().getUniqueId());
        lastLocations.remove(event.getPlayer().getUniqueId());
        lastMessage.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler()
    void onChat(AsyncChatEvent event) {
        lastMessage.put(event.getPlayer().getUniqueId(), Instant.now());
        if (afks.remove(event.getPlayer().getUniqueId()))
            event.getPlayer().sendMessage(MESSAGE_NOT_AFK_ANYMORE);

    }

    @EventHandler
    void onCommand(PlayerCommandSendEvent event) {
        lastMessage.put(event.getPlayer().getUniqueId(), Instant.now());
        if (afks.remove(event.getPlayer().getUniqueId()))
            event.getPlayer().sendMessage(MESSAGE_NOT_AFK_ANYMORE);
    }
}
