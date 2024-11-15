package onl.tesseract.core.cosmetics;

import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;

public enum ElytraTrails implements CosmeticWithMaterial {
    ENDER(ChatColor.DARK_PURPLE + "Ender", Material.ENDER_PEARL, 1, Particle.DRAGON_BREATH),
    FLAME(ChatColor.GOLD + "Flammes", Material.BLAZE_POWDER, 2, Particle.FLAME),
    CLOUD(ChatColor.GRAY + "Nuages", Material.PHANTOM_MEMBRANE, 3, Particle.CLOUD),
    LOVE(ChatColor.GRAY + "Amour", Material.APPLE, 4, Particle.HEART),
    MUSICAL(ChatColor.DARK_AQUA + "Musical", Material.NOTE_BLOCK, 5, Particle.NOTE),
    REDSTONE(ChatColor.DARK_RED + "Redstone", Material.REDSTONE, 6, Particle.DUST),
    SMOKE(ChatColor.DARK_GRAY + "Fumée noire", Material.CHARCOAL, 7, Particle.LARGE_SMOKE),
    GREEN(ChatColor.GREEN + "Verdoyant", Material.LILY_PAD, 10, Particle.HAPPY_VILLAGER),
    ANGER(ChatColor.DARK_RED + "Colère", Material.NETHER_WART, 11, Particle.ANGRY_VILLAGER),
    INCENDIARY(ChatColor.GOLD + "Incendiaire", Material.FIRE_CHARGE, 12, Particle.LAVA),
    NEBULOUS(ChatColor.WHITE + "Nébuleux", Material.FEATHER, 13, Particle.END_ROD),
    TOTEM(ChatColor.DARK_GREEN + "Totem", Material.TOTEM_OF_UNDYING, 14, Particle.TOTEM_OF_UNDYING),
    POTION(ChatColor.LIGHT_PURPLE + "Potion", Material.DRAGON_BREATH, 15, Particle.DRAGON_BREATH),
    SHINNING(ChatColor.WHITE + "Scintillant", Material.PRISMARINE_CRYSTALS, 16, Particle.FIREWORK),
    NONE(ChatColor.GRAY + "Sans sillage", Material.STRUCTURE_VOID, 0, null);

    final String name;
    final Material material;
    final int index;
    final Particle particle;

    ElytraTrails(String s, Material m, int i, Particle p)
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

    public String getName()
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
