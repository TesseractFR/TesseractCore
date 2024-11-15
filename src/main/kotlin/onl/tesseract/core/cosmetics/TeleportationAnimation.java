package onl.tesseract.core.cosmetics;

import net.kyori.adventure.text.Component;
import onl.tesseract.lib.animation.*;
import onl.tesseract.lib.util.TriFunction;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

public enum TeleportationAnimation implements CosmeticWithMaterial {
    WATER("Eau", Material.WATER_BUCKET, (plugin, location, duration) -> {
        return new SmoothCylinder(plugin)
                .setParticle(Particle.DRIPPING_WATER)
                .setLocation(location)
                .setRadius(1)
                .setHeight(3)
                .setDelay(duration / 30f)
                .build();
    }),
    LAVA("Lave", Material.LAVA_BUCKET, (plugin, location, duration) -> {
        return new SmoothCylinder(plugin)
                .setParticle(Particle.DRIPPING_LAVA)
                .setLocation(location)
                .setRadius(1)
                .setHeight(3)
                .setDelay(duration / 30f)
                .build();
    }),
    HONEY("Miel", Material.HONEYCOMB, (plugin, location, duration) -> {
        return new SmoothCylinder(plugin)
                .setParticle(Particle.DRIPPING_HONEY)
                .setLocation(location)
                .setRadius(1)
                .setHeight(3)
                .setDelay(duration / 30f)
                .build();
    }),
    SPIRAL("Spiral", Material.ENDER_EYE, (plugin, location, duration) -> {
        return new Circle(Particle.DRAGON_BREATH, new AnimationTarget(location), plugin)
                .setRadius(1)
                .setSpacing(0.05f)
                .setDelay(duration / 250f)
                .setOriginCount(2)
                .setRotationCount(2)
                .setOnDraw(circle -> circle.setOrigin(new AnimationTarget(location.add(0, 0.03, 0))))
                .build();
    }),
    FIRE_SPIRAL("Spiral de feu", Material.LANTERN, (plugin, location, duration) -> {
        return new Circle(Particle.FLAME, new AnimationTarget(location), plugin)
                .setRadius(1)
                .setSpacing(0.05f)
                .setDelay(duration / 250f)
                .setOriginCount(2)
                .setRotationCount(2)
                .setOnDraw(circle -> circle.setOrigin(new AnimationTarget(location.add(0, 0.03, 0))))
                .build();
    }),
    SPHERE("Sphère", Material.HEART_OF_THE_SEA, (plugin, location, duration) -> {
        return new Sphere(plugin)
                .setParticle(Particle.DUST)
                .setColor(Color.RED)
                .setOrigin(new AnimationTarget(location.add(0, 1, 0)))
                .setRadius(1.5f)
                .setDelay((float) (duration / 250f))
                .setRotationCount(2)
                .build();
    }),
    DIVIN("Divin", Material.BEACON, (plugin, location, duration) -> {
        return new Cylinder(plugin).setParticle(Particle.HAPPY_VILLAGER)
                .setLocation(location)
                .setRadius(1)
                .setHeight(20)
                .setRevert(true)
                .setDelay(duration / 66.7f)
                .build();
    }),
    SMOKE("Fumée", Material.CHARCOAL, (plugin, location, duration) ->
            () -> {
                new BukkitRunnable() {
                    int count = (int) (duration / 5);
                    final Location loc = location.add(0, 2, 0);

                    @Override
                    public void run()
                    {
                        location.getWorld().spawnParticle(Particle.CLOUD, loc, 15, 0.5, 0.5, 0.5, 0);
                        if (count-- <= 0)
                            this.cancel();
                    }
                }.runTaskTimer(plugin, 0, 5);
            }),
    DESINTEGRATION("Désintégration", Material.COCOA_BEANS, (plugin, location, duration) ->
            () -> {
                new BukkitRunnable() {
                    int count = (int) (duration / 5);
                    final Location loc = location.add(0, 1, 0);

                    @Override
                    public void run()
                    {
                        location.getWorld().spawnParticle(Particle.CRIMSON_SPORE, loc, 30, 0.2, 0.2, 0.2);
                        if (count-- <= 0)
                            this.cancel();
                    }
                }.runTaskTimer(plugin, 0, 5);
            }),
    RAIN("Pluie", Material.SPLASH_POTION, (plugin, location, duration) ->
            () -> {
                new BukkitRunnable() {
                    int count = (int) (duration / 2);
                    final Location loc = location.add(0, 3, 0);

                    @Override
                    public void run()
                    {
                        location.getWorld().spawnParticle(Particle.FALLING_WATER, loc, 30, 0.5, 0.5, 0.5);
                        if (count-- <= 0)
                            this.cancel();
                    }
                }.runTaskTimer(plugin, 0, 2);
            }),
    FLAME("Flame", Material.FLINT_AND_STEEL, (plugin, location, duration) ->
            () -> {
                new BukkitRunnable() {
                    int count = (int) (duration / 5);
                    final Location loc = location.add(0, 1, 0);

                    @Override
                    public void run()
                    {
                        location.getWorld().spawnParticle(Particle.FLAME, loc, 20, 0.2, 0.2, 0.2, 0.1);
                        if (count-- <= 0)
                            this.cancel();
                    }
                }.runTaskTimer(plugin, 0, 5);
            }),
    INCENDIARY("Incendiaire", Material.FIRE_CHARGE, (plugin, location, duration) ->
            () -> {
                new BukkitRunnable() {
                    int count = (int) (duration / 5);
                    final Location loc = location.add(0, 2, 0);

                    @Override
                    public void run()
                    {
                        location.getWorld().spawnParticle(Particle.LAVA, loc, 20, 0.2, 0.2, 0.2, 0.1);
                        if (count-- <= 0)
                            this.cancel();
                    }
                }.runTaskTimer(plugin, 0, 5);
            }),
    THUNDER("Éclair", Material.TRIDENT, (plugin, location, duration) -> {
        return new Circle(Particle.LARGE_SMOKE, new AnimationTarget(location), plugin)
                .setRadius(1)
                .setOriginCount(2)
                .setRotationCount(3)
                .setDelay(duration / 377f)
                .setOnFinish(a -> location.getWorld().strikeLightningEffect(location))
                .build();
    }),
    DEMONIC("Démoniaque", Material.NETHER_STAR, (plugin, location, duration) ->
            () -> {
                new BukkitRunnable() {
                    int i = 0;

                    @Override
                    public void run()
                    {
                        Location[] points = new Location[5];
                        for (int k = 0; k < 5; k++)
                            points[k] = location.clone().add(2.1 * Math.cos(k * Math.PI * 0.4), 0,
                                    2.1 * Math.sin(k * Math.PI * 0.4));
                        for (int k = 0; k < 5; k++)
                        {
                            new Line(Particle.DUST, points[k], points[(k + 2) % 5], plugin).setColor(Color.RED).setDelay(0)
                                    .draw();
                            new Line(Particle.DUST, points[k], points[(k + 3) % 5], plugin).setColor(Color.RED).setDelay(0)
                                    .draw();
                        }

                        if (++i >= duration)
                            this.cancel();
                    }
                }.runTaskTimer(plugin, 0, 1);
            }),
    ROSETTE("Rosace", Material.END_ROD, (plugin, location, duration) ->
            () -> {
                new CollapsingRosette(plugin).location(location.add(0, 0.2, 0))
                        .speed(0.25)
                        .count(75)
                        .radius(3.5)
                        .particle(Particle.END_ROD)
                        .time(duration + 5)
                        .draw();
                new CollapsingRosette(plugin).location(location.add(0, 6, 0))
                        .speed(0.2)
                        .count(75)
                        .radius(3.5)
                        .particle(Particle.TOTEM_OF_UNDYING)
                        .time(duration + 5)
                        .draw();
            }),
    ;

    private final Component name;
    private final Material icon;
    private final TriFunction<Plugin, Location, Double, Animation> animation;

    TeleportationAnimation(String name, Material icon, TriFunction<Plugin, Location, Double, Animation> animation)
    {
        this.name = Component.text(name);
        this.icon = icon;
        this.animation = animation;
    }

    public static String getTypeName()
    {
        return "TPAnimation";
    }

    public void animate(Plugin plugin, Location location, double duration)
    {
        animation.call(plugin, location.clone(), duration).draw();
    }

    public Animation getAnimation(Plugin plugin, final Location location, final double delay)
    {
        return animation.call(plugin, location, delay);
    }

    @Override
    public Component getObtainMessage()
    {
        return Component
                .text("Vous avez obtenu l'effet de téléportation " + toString().charAt(0) + toString().substring(1)
                        .toLowerCase());
    }

    @Override
    public int getPrice()
    {
        return 300;
    }

    public Component getName()
    {
        return name;
    }

    public Material getIcon()
    {
        return icon;
    }

    @Override
    public @NotNull Material getMaterial() {
        return icon;
    }
}
