package onl.tesseract.core.cosmetics;

import net.kyori.adventure.text.Component;
import onl.tesseract.lib.animation.*;
import onl.tesseract.tesseractlib.TesseractLib;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.function.BiFunction;

public enum TeleportationAnimation implements Cosmetic {
    WATER("Eau", Material.WATER_BUCKET, (location, duration) -> {
        return new SmoothCylinder()
                .setParticle(Particle.DRIPPING_WATER)
                .setLocation(location)
                .setRadius(1)
                .setHeight(3)
                .setDelay(duration / 30f)
                .build();
    }),
    LAVA("Lave", Material.LAVA_BUCKET, (location, duration) -> {
        return new SmoothCylinder()
                .setParticle(Particle.DRIPPING_LAVA)
                .setLocation(location)
                .setRadius(1)
                .setHeight(3)
                .setDelay(duration / 30f)
                .build();
    }),
    HONEY("Miel", Material.HONEYCOMB, (location, duration) -> {
        return new SmoothCylinder()
                .setParticle(Particle.DRIPPING_HONEY)
                .setLocation(location)
                .setRadius(1)
                .setHeight(3)
                .setDelay(duration / 30f)
                .build();
    }),
    SPIRAL("Spiral", Material.ENDER_EYE, (location, duration) -> {
        return new Circle(Particle.DRAGON_BREATH, new AnimationTarget(location))
                .setRadius(1)
                .setSpacing(0.05f)
                .setDelay(duration / 250f)
                .setOriginCount(2)
                .setRotationCount(2)
                .setOnDraw(circle -> circle.setOrigin(new AnimationTarget(location.add(0, 0.03, 0))))
                .build();
    }),
    FIRE_SPIRAL("Spiral de feu", Material.LANTERN, (location, duration) -> {
        return new Circle(Particle.FLAME, new AnimationTarget(location))
                .setRadius(1)
                .setSpacing(0.05f)
                .setDelay(duration / 250f)
                .setOriginCount(2)
                .setRotationCount(2)
                .setOnDraw(circle -> circle.setOrigin(new AnimationTarget(location.add(0, 0.03, 0))))
                .build();
    }),
    SPHERE("Sphère", Material.HEART_OF_THE_SEA, (location, duration) -> {
        return new Sphere()
                .setParticle(Particle.DUST)
                .setColor(Color.RED)
                .setOrigin(new AnimationTarget(location.add(0, 1, 0)))
                .setRadius(1.5f)
                .setDelay((float) (duration / 250f))
                .setRotationCount(2)
                .build();
    }),
    DIVIN("Divin", Material.BEACON, (location, duration) -> {
        return new Cylinder().setParticle(Particle.HAPPY_VILLAGER)
                .setLocation(location)
                .setRadius(1)
                .setHeight(20)
                .setRevert(true)
                .setDelay(duration / 66.7f)
                .build();
    }),
    SMOKE("Fumée", Material.CHARCOAL, (location, duration) ->
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
                }.runTaskTimer(TesseractLib.instance, 0, 5);
            }),
    DESINTEGRATION("Désintégration", Material.COCOA_BEANS, (location, duration) ->
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
                }.runTaskTimer(TesseractLib.instance, 0, 5);
            }),
    RAIN("Pluie", Material.SPLASH_POTION, (location, duration) ->
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
                }.runTaskTimer(TesseractLib.instance, 0, 2);
            }),
    FLAME("Flame", Material.FLINT_AND_STEEL, (location, duration) ->
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
                }.runTaskTimer(TesseractLib.instance, 0, 5);
            }),
    INCENDIARY("Incendiaire", Material.FIRE_CHARGE, (location, duration) ->
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
                }.runTaskTimer(TesseractLib.instance, 0, 5);
            }),
    THUNDER("Éclair", Material.TRIDENT, (location, duration) -> {
        return new Circle(Particle.LARGE_SMOKE, new AnimationTarget(location))
                .setRadius(1)
                .setOriginCount(2)
                .setRotationCount(3)
                .setDelay(duration / 377f)
                .setOnFinish(a -> location.getWorld().strikeLightningEffect(location))
                .build();
    }),
    DEMONIC("Démoniaque", Material.NETHER_STAR, (location, duration) ->
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
                            new Line(Particle.DUST, points[k], points[(k + 2) % 5]).setColor(Color.RED).setDelay(0)
                                    .draw();
                            new Line(Particle.DUST, points[k], points[(k + 3) % 5]).setColor(Color.RED).setDelay(0)
                                    .draw();
                        }

                        if (++i >= duration)
                            this.cancel();
                    }
                }.runTaskTimer(TesseractLib.instance, 0, 1);
            }),
    ROSETTE("Rosace", Material.END_ROD, (location, duration) ->
            () -> {
                new CollapsingRosette().location(location.add(0, 0.2, 0))
                        .speed(0.25)
                        .count(75)
                        .radius(3.5)
                        .particle(Particle.END_ROD)
                        .time(duration + 5)
                        .draw();
                new CollapsingRosette().location(location.add(0, 6, 0))
                        .speed(0.2)
                        .count(75)
                        .radius(3.5)
                        .particle(Particle.TOTEM_OF_UNDYING)
                        .time(duration + 5)
                        .draw();
            }),
    ;

    String name;
    Material icon;
    BiFunction<Location, Double, Animation> animation;

    TeleportationAnimation(String name, Material icon, BiFunction<Location, Double, Animation> animation)
    {
        this.name = name;
        this.icon = icon;
        this.animation = animation;
    }

    public static String getTypeName()
    {
        return "TPAnimation";
    }

    public void animate(Location location, double duration)
    {
        animation.apply(location.clone(), duration).draw();
    }

    public Animation getAnimation(final Location location, final double delay)
    {
        return animation.apply(location, delay);
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

    public String getName()
    {
        return name;
    }

    public Material getIcon()
    {
        return icon;
    }
}
