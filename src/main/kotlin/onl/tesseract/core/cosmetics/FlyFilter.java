package onl.tesseract.core.cosmetics;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;

public enum FlyFilter implements CosmeticWithMaterial {
    AMOUR(Component.text("Amour", NamedTextColor.GRAY), Material.APPLE, 2, Particle.HEART),
    COLERE(Component.text("Colère", NamedTextColor.DARK_RED), Material.NETHER_WART, 3, Particle.ANGRY_VILLAGER),
    CONNAISSANCE(Component.text("Connaissance", NamedTextColor.BLUE), Material.BOOK,4,Particle.ENCHANT),
    MUSICAL(Component.text("Musical", NamedTextColor.DARK_AQUA), Material.NOTE_BLOCK, 5, Particle.NOTE),
    ENDER(Component.text("Ender", NamedTextColor.DARK_PURPLE), Material.ENDER_PEARL,6, Particle.DRAGON_BREATH),
    INCENDIAIRE(Component.text("Incendiaire", NamedTextColor.GOLD), Material.FIRE_CHARGE, 11, Particle.LAVA),
    REDSTONE(Component.text("Redstone", NamedTextColor.DARK_RED), Material.REDSTONE, 12, Particle.DUST),
    POTION(Component.text("Potion", NamedTextColor.LIGHT_PURPLE), Material.DRAGON_BREATH, 13, Particle.DRAGON_BREATH),
    FUMEE(Component.text("Fumée noire", NamedTextColor.DARK_GRAY), Material.CHARCOAL, 14, Particle.LARGE_SMOKE),
    NEBULEUX(Component.text("Nébuleux", NamedTextColor.WHITE), Material.FEATHER, 15, Particle.END_ROD),
    NONE(Component.text("Flammes", NamedTextColor.GOLD), Material.BLAZE_POWDER, 2, Particle.FLAME);

    final Component name;
    final Material material;
    final int index;
    final Particle particle;

    FlyFilter(Component s, Material m, int i, Particle p)
    {
        name = s;
        material = m;
        index = i;
        particle = p;
    }

    public static String getTypeName()
    {
        return "FlyFilter";
    }

    @Override
    public Component getObtainMessage()
    {
        return Component.text("Vous avez obtenu le filtre de vol ").append(getName());
    }

    @Override
    public Component getName()
    {
        return name;
    }

    public Particle getParticle()
    {
        return particle;
    }


    public int getIndex()
    {
        return index;
    }

    public Material getMaterial()
    {
        return material;
    }
}
