package onl.tesseract.core.cosmetics.familier;

import onl.tesseract.lib.menu.ItemBuilder;
import onl.tesseract.lib.profile.PlayerProfileService;
import onl.tesseract.lib.service.ServiceContainer;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum PetCategory {
    COCHON(new ArrayList<>(Arrays.asList(Pet.PIGGY, Pet.BOARY, Pet.WILDY)), PetHead.PIG_EGG_HEAD),
    OISEAU(new ArrayList<>(Arrays.asList(Pet.BIRDY, Pet.OWLY, Pet.OLDY)), PetHead.BIRD_EGG_HEAD),
    CHIEN(new ArrayList<>(Arrays.asList(Pet.DOGGY, Pet.PUGGY, Pet.BROWNY)), PetHead.DOG_EGG_HEAD),
    CHAT(new ArrayList<>(Arrays.asList(Pet.CATTY, Pet.NYANCAT, Pet.TIGER)), PetHead.CAT_EGG_HEAD),
    VACHE(new ArrayList<>(Arrays.asList(Pet.MOOSHY, Pet.SPARCOW, Pet.COWWY)), PetHead.COW_EGG_HEAD),
    ABEILLE(new ArrayList<>(Arrays.asList(Pet.BEEBEE, Pet.MAGENBEE, Pet.BOOP)), PetHead.BEE_EGG_HEAD),
    ESCARGOT(new ArrayList<>(Arrays.asList(Pet.SNAILY, Pet.GARY, Pet.SLOWY)), PetHead.SNAIL_EGG_HEAD),
    POISSON(new ArrayList<>(Arrays.asList(Pet.FISHY, Pet.PUFFER, Pet.MAGIKARP)), PetHead.FISH_EGG_HEAD),
    MOUTON(new ArrayList<>(Arrays.asList(Pet.SHEEPY, Pet.BLUEPY, Pet.TEEPY)), PetHead.SHEEP_EGG_HEAD);

    private final List<Pet> pets;
    private final PetHead head;

    PetCategory(List<Pet> pets, PetHead head)
    {
        this.pets = pets;
        this.head = head;
    }

    public List<Pet> getPets()
    {
        return pets;
    }

    public ItemStack getHead()
    {
        return new ItemBuilder(Material.PLAYER_HEAD, null ,null)
                .customHead(head.data, head.signature)
                .build(ServiceContainer.get(PlayerProfileService.class));
    }


}
