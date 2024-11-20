package onl.tesseract.core.event;

import onl.tesseract.core.sit.SeatBlock;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Makes a player sit down when he right-clicks a slab or stairs
 */
public class PlayerSit implements Listener {
    static final Map<Player, Pig> map = new HashMap<>();

    @EventHandler
    public void onSit(PlayerInteractEvent event)
    {
        if (!isRightClickOnBlock(event))
            return;

        Block clickedBlock = Objects.requireNonNull(event.getClickedBlock());
        SeatBlock.getEntries().stream()
                .filter(seat -> seat.matches(event.getClickedBlock()))
                .findAny()
                .ifPresent(seat -> handleSit(seat, event.getPlayer(), clickedBlock));
    }

    private boolean isRightClickOnBlock(PlayerInteractEvent event) {
        return event.hasBlock() && event.getClickedBlock() != null && !event.hasItem() && event.getAction() == Action.RIGHT_CLICK_BLOCK;
    }

    private void handleSit(SeatBlock seat, Player player, Block block) {
        PlayerSitEvent event = new PlayerSitEvent(player, seat.getSitLocation(block), seat.getSitRotation(block, player));
        if (!event.callEvent())
            return;
        sit(event.getPlayer(), event.getLocation(), event.getRotation());
    }

    @EventHandler
    public void onStandUp(VehicleExitEvent event)
    {
        if (event.getExited() instanceof Player player && map.containsKey(player))
            standUp(player);
    }
    @EventHandler
    public void onLeave(PlayerQuitEvent event)
    {
        if (map.containsKey(event.getPlayer()))
            standUp(event.getPlayer());
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onSitHandler(PlayerSitEvent event)
    {
        if (!event.isCancelled())
            sit(event.getPlayer(), event.getLocation(), event.getRotation());
    }

    public static void sit(Player player, Location location, float rotation)
    {
        Pig pig = (Pig) player.getWorld().spawnEntity(location, EntityType.PIG);
        pig.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 99999, 0, false, false));
        pig.setInvulnerable(true);
        pig.setAI(false);
        pig.setSilent(true);
        pig.setGravity(false);
        pig.setRotation(rotation, 0);
        pig.addPassenger(player);

        map.put(player, pig);
    }

    public static void standUp(Player player)
    {
        var pig = map.get(player);
        player.teleport(pig.getLocation().add(0, 1, 0));
        pig.remove();
        map.remove(player);
    }
}