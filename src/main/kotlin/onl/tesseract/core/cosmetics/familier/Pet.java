package onl.tesseract.core.cosmetics.familier;


import net.kyori.adventure.text.Component;
import onl.tesseract.lib.menu.ItemBuilder;
import onl.tesseract.lib.profile.PlayerProfileService;
import onl.tesseract.lib.service.ServiceContainer;
import onl.tesseract.core.cosmetics.Cosmetic;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum Pet implements Cosmetic {
    PIGGY(PetHead.PIGGY_HEAD),
    BOARY(PetHead.BOARY_HEAD),
    WILDY(PetHead.WILDY_HEAD),
    OWLY(PetHead.OWLY_HEAD),
    BIRDY(PetHead.BIRDY_HEAD),
    OLDY(PetHead.OLDY_HEAD),
    DOGGY(PetHead.DOGGY_HEAD),
    PUGGY(PetHead.PUGGY_HEAD),
    BROWNY(PetHead.BROWNY_HEAD),
    CATTY(PetHead.CATTY_HEAD),
    TIGER(PetHead.TIGER_HEAD),
    NYANCAT(PetHead.NYAN_CAT_HEAD),
    MOOSHY(PetHead.MOOSHY_HEAD),
    SPARCOW(PetHead.SPAR_COW_HEAD),
    COWWY(PetHead.COWWY_HEAD),
    BEEBEE(PetHead.BEE_BEE_HEAD, "BeeBee"),
    MAGENBEE(PetHead.MAGEN_BEE_HEAD),
    BOOP(PetHead.BOOP_HEAD),
    SNAILY(PetHead.SNAILY_HEAD),
    GARY(PetHead.GARY_HEAD),
    SLOWY(PetHead.SLOWY_HEAD),
    FISHY(PetHead.FISHY_HEAD),
    PUFFER(PetHead.PUFFER_HEAD),
    MAGIKARP(PetHead.MAGIKARP_HEAD),
    SHEEPY(PetHead.SHEEPY_HEAD),
    BLUEPY(PetHead.BLUEPY_HEAD),
    TEEPY(PetHead.TEEPY_HEAD);


    final PetHead head;
    final Component name;

    Pet(PetHead head)
    {
        this.head = head;
        name = Component.text(toString().charAt(0) + toString().substring(1).toLowerCase());
    }

    Pet(PetHead head, String name)
    {
        this.name = Component.text(name);
        this.head = head;
    }

    public static String getTypeName()
    {
        return "Pet";
    }

    public Component getname()
    {
        return name;
    }

    public ItemStack getHead()
    {
        return new ItemBuilder(Material.PLAYER_HEAD, null, null)
                .customHead(head.data, head.signature)
                .build(ServiceContainer.get(PlayerProfileService.class));
    }

    @Override
    public Component getObtainMessage()
    {
        return Component.text("Vous avez obtenu le familier "+getname());
    }

    @Override
    public Component getDisplayName()
    {
        return name;
    }
}
