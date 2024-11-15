package onl.tesseract.core.cosmetics;

import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;

public enum FlyFilter implements CosmeticWithMaterial {
    AMOUR(ChatColor.GRAY + "Amour", Material.APPLE, 2, Particle.HEART),
    COLERE(ChatColor.DARK_RED + "Colère", Material.NETHER_WART, 3, Particle.ANGRY_VILLAGER),
    CONNAISSANCE(ChatColor.BLUE+"Connaissance",Material.BOOK,4,Particle.ENCHANT),
    MUSICAL(ChatColor.DARK_AQUA + "Musical", Material.NOTE_BLOCK, 5, Particle.NOTE),
    ENDER(ChatColor.DARK_PURPLE + "Ender", Material.ENDER_PEARL,6, Particle.DRAGON_BREATH),
    INCENDIAIRE(ChatColor.GOLD + "Incendiaire", Material.FIRE_CHARGE, 11, Particle.LAVA),
    REDSTONE(ChatColor.DARK_RED + "Redstone", Material.REDSTONE, 12, Particle.DUST),
    POTION(ChatColor.LIGHT_PURPLE + "Potion", Material.DRAGON_BREATH, 13, Particle.DRAGON_BREATH),
    FUMEE(ChatColor.DARK_GRAY + "Fumée noire", Material.CHARCOAL, 14, Particle.LARGE_SMOKE),
    NEBULEUX(ChatColor.WHITE + "Nébuleux", Material.FEATHER, 15, Particle.END_ROD),
    NONE(ChatColor.GOLD + "Flammes", Material.BLAZE_POWDER, 2, Particle.FLAME);

    final String name;
    final Material material;
    final int index;
    final Particle particle;

    FlyFilter(String s, Material m, int i, Particle p)
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
        return Component.text("Vous avez obtenu le filtre de vol " + getName());
    }

    @Override
    public String getName()
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
