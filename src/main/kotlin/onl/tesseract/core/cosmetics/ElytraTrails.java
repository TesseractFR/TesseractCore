package onl.tesseract.core.cosmetics;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;

public enum ElytraTrails implements CosmeticWithMaterial {
    ENDER(Component.text("Ender", NamedTextColor.DARK_PURPLE), Material.ENDER_PEARL, 1, Particle.DRAGON_BREATH),
    FLAME(Component.text("Flammes", NamedTextColor.GOLD), Material.BLAZE_POWDER, 2, Particle.FLAME),
    CLOUD(Component.text("Nuages", NamedTextColor.GRAY), Material.PHANTOM_MEMBRANE, 3, Particle.CLOUD),
    LOVE(Component.text("Amour", NamedTextColor.GRAY), Material.APPLE, 4, Particle.HEART),
    MUSICAL(Component.text("Musical", NamedTextColor.DARK_AQUA), Material.NOTE_BLOCK, 5, Particle.NOTE),
    REDSTONE(Component.text("Redstone", NamedTextColor.DARK_RED), Material.REDSTONE, 6, Particle.DUST),
    SMOKE(Component.text("Fumée noire", NamedTextColor.DARK_GRAY), Material.CHARCOAL, 7, Particle.LARGE_SMOKE),
    GREEN(Component.text("Verdoyant", NamedTextColor.GREEN), Material.LILY_PAD, 10, Particle.HAPPY_VILLAGER),
    ANGER(Component.text("Colère", NamedTextColor.DARK_RED), Material.NETHER_WART, 11, Particle.ANGRY_VILLAGER),
    INCENDIARY(Component.text("Incendiaire", NamedTextColor.GOLD), Material.FIRE_CHARGE, 12, Particle.LAVA),
    NEBULOUS(Component.text("Nébuleux", NamedTextColor.WHITE), Material.FEATHER, 13, Particle.END_ROD),
    TOTEM(Component.text("Totem", NamedTextColor.DARK_GREEN), Material.TOTEM_OF_UNDYING, 14, Particle.TOTEM_OF_UNDYING),
    POTION(Component.text("Potion", NamedTextColor.LIGHT_PURPLE), Material.DRAGON_BREATH, 15, Particle.DRAGON_BREATH),
    SHINNING(Component.text("Scintillant", NamedTextColor.WHITE), Material.PRISMARINE_CRYSTALS, 16, Particle.FIREWORK),
    NONE(Component.text("Sans sillage", NamedTextColor.GRAY), Material.STRUCTURE_VOID, 0, null);

    final Component name;
    final Material material;
    final int index;
    final Particle particle;

    ElytraTrails(Component s, Material m, int i, Particle p)
    {
        name = s;
        material = m;
        index = i;
        particle = p;
    }

    public int getIndex()
    {
        return index;
    }

    public Material getMaterial()
    {
        return material;
    }

    public Component getName()
    {
        return name;
    }

    public Particle getParticle()
    {
        return particle;
    }

    @Override
    public Component getObtainMessage()
    {
        return Component.text("Vous avez obtenu le sillage d'ailes "+toString().charAt(0)+toString().substring(1).toLowerCase());
    }
    public static String getTypeName()
    {
        return "ElytraTrails";
    }
}
