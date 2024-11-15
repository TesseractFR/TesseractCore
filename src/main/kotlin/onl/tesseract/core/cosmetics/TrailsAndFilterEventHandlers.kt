package onl.tesseract.core.cosmetics

import com.destroystokyo.paper.ParticleBuilder
import onl.tesseract.core.TesseractCorePlugin
import onl.tesseract.core.boutique.BoutiqueService
import onl.tesseract.lib.service.ServiceContainer
import onl.tesseract.lib.util.Util
import org.bukkit.Color
import org.bukkit.Particle
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityToggleGlideEvent
import org.bukkit.event.player.PlayerToggleFlightEvent
import kotlin.math.cos
import kotlin.math.sin

class TrailsAndFilterEventHandlers : Listener {

    @EventHandler
    fun onGlide(event: EntityToggleGlideEvent) {
        if (event.entityType != EntityType.PLAYER) return

        val boutiqueService = ServiceContainer[BoutiqueService::class.java]
        val player = event.entity as Player

        val playerInfo = boutiqueService.getPlayerBoutiqueInfo(player.uniqueId)
        TesseractCorePlugin.instance.server.scheduler.runTaskTimer(TesseractCorePlugin.instance, { task ->
            if (!player.isOnline || !player.isGliding)
                task.cancel()
            val trail = playerInfo.activeTrail
            if (trail == ElytraTrails.NONE) return@runTaskTimer
            val builder = ParticleBuilder(trail.getParticle())
            if (trail == ElytraTrails.SHINNING)
                builder.count(1)
            else
                builder.count(2)
            builder.offset(.5, .5, .5)
            if (trail == ElytraTrails.POTION || trail == ElytraTrails.MUSICAL)
                builder.extra(0.2)
            else
                builder.extra(0.0)
            builder.location(Util.Locations.backward(event.getEntity().location, 2.0))
            builder.receivers(100)
            if (trail == ElytraTrails.REDSTONE)
                builder.color(Color.RED)
            builder.spawn()
        }, 0, 2)
    }

    @EventHandler
    fun onFly(event: PlayerToggleFlightEvent) {
        val player = event.player
        val boutiqueService = ServiceContainer[BoutiqueService::class.java]
        val playerInfo = boutiqueService.getPlayerBoutiqueInfo(player.uniqueId)
        TesseractCorePlugin.instance.server.scheduler.runTaskTimer(TesseractCorePlugin.instance, { task ->
            if (!player.isFlying) task.cancel()
            val selectedFilter = playerInfo.activeFlyFilter
            if (selectedFilter == FlyFilter.NONE) return@runTaskTimer
            var loc = player.location
            loc = loc.add((-cos(loc.yaw)).toDouble(), 0.0, -sin(loc.yaw.toDouble()))
            val particle = selectedFilter.getParticle()
            val extra: Double = if (event.getPlayer().isSprinting) 0.5 else 0.0
            if (selectedFilter == FlyFilter.REDSTONE)
                loc.getWorld()
                    .spawnParticle(
                        particle, loc, 15, .5, .5, .5, .5, Particle.DustOptions(
                            Color.RED,
                            1.2f
                        )
                    )
            else if (selectedFilter == FlyFilter.POTION || selectedFilter == FlyFilter.MUSICAL)
                loc.getWorld().spawnParticle(particle, loc, 15, .5, .5, .5, 0.2)
            else
                loc.getWorld().spawnParticle(particle, loc, 15, .5, .5, .5, extra)
        }, 0, 10)
    }
}