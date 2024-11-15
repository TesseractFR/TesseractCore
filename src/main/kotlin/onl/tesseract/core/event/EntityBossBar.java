package onl.tesseract.core.event;

import onl.tesseract.core.TesseractCorePlugin;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Displays a bossBar for entities damaged by players
 */
public class EntityBossBar implements Listener {
    static final HashMap<UUID, BossBar> map = new HashMap<>();
    static final Map<UUID, BukkitTask> tasks = new HashMap<>();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDamage(EntityDamageEvent event)
    {
        if (event.isCancelled()) return;
        if (! (event.getEntity() instanceof LivingEntity living))
            return;

        if (event.getEntityType() == EntityType.ENDER_DRAGON || event.getEntityType() == EntityType.WITHER || event.getEntityType() == EntityType.ARMOR_STAND)
            return;

        // Update the bar
        if (! map.containsKey(event.getEntity().getUniqueId())) return;
        map.get(event.getEntity().getUniqueId()).setProgress(Math.max(0, (living.getHealth() - event.getFinalDamage())) / living.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());

        // Remove the bossBar after 5 seconds
        if (tasks.containsKey(event.getEntity().getUniqueId()))
            tasks.get(event.getEntity().getUniqueId()).cancel();
        tasks.put(event.getEntity().getUniqueId(), new BukkitRunnable() {
            @Override
            public void run()
            {
                BossBar b = map.get(event.getEntity().getUniqueId());
                b.removeAll();
                tasks.remove(event.getEntity().getUniqueId());
                if (b.getPlayers().size() == 0)
                    map.remove(event.getEntity().getUniqueId());

            }
        }.runTaskLater(TesseractCorePlugin.instance, 150));
    }

    @EventHandler (priority = EventPriority.HIGH)
    public void onEntityDamaged(EntityDamageByEntityEvent event)
    {
        if (event.isCancelled()) return;
        if (event.getEntity() instanceof LivingEntity)
        {
            if (event.getEntityType() == EntityType.ENDER_DRAGON || event.getEntityType() == EntityType.WITHER || event.getEntityType() == EntityType.ARMOR_STAND)
                return;
            // If the entity is damaged by a player
            if (event.getDamager().getType() == EntityType.PLAYER)
            {
                Player player = (Player) event.getDamager();
                showBossBar(event.getEntity(), player);
            }
            else if (event.getDamager() instanceof Projectile projectile)
            {
                if (projectile.getShooter() instanceof Player player)
                {
                    showBossBar(event.getEntity(), player);
                }
            }
        }
    }

    private void showBossBar(final Entity damaged, final Player player)
    {
        if (!map.containsKey(damaged.getUniqueId()))
        {
            BossBar created = Bukkit.createBossBar(damaged.getName(), BarColor.GREEN, BarStyle.SEGMENTED_10);
            map.put(damaged.getUniqueId(), created);
            created.addPlayer(player);
        }
        else
        {
            // Display the bossBar to the player
            BossBar bar = map.get(damaged.getUniqueId());
            bar.addPlayer(player);
        }
    }
}