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
    Cochon(new ArrayList<>(Arrays.asList(Pet.PIGGY, Pet.BOARY, Pet.WILDY)), PetHead.PigEggHead),
    Oiseau(new ArrayList<>(Arrays.asList(Pet.BIRDY, Pet.OWLY, Pet.OLDY)), PetHead.BirdEggHead),
    Chien(new ArrayList<>(Arrays.asList(Pet.DOGGY, Pet.PUGGY, Pet.BROWNY)), PetHead.DogEggHead),
    Chat(new ArrayList<>(Arrays.asList(Pet.CATTY, Pet.NYANCAT, Pet.TIGER)), PetHead.CatEggHead),
    Vache(new ArrayList<>(Arrays.asList(Pet.MOOSHY, Pet.SPARCOW, Pet.COWWY)), PetHead.CowEggHead),
    Abeille(new ArrayList<>(Arrays.asList(Pet.BEEBEE, Pet.MAGENBEE, Pet.BOOP)), PetHead.BeeEggHead),
    Escargot(new ArrayList<>(Arrays.asList(Pet.SNAILY, Pet.GARY, Pet.SLOWY)), PetHead.SnailEggHead),
    Poisson(new ArrayList<>(Arrays.asList(Pet.FISHY, Pet.PUFFER, Pet.MAGIKARP)), PetHead.FishEggHead),
    Mouton(new ArrayList<>(Arrays.asList(Pet.SHEEPY, Pet.BLUEPY, Pet.TEEPY)), PetHead.SheepEggHead);

    final List<Pet> pets;
    final PetHead head;

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
