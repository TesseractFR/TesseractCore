package onl.tesseract.core.cosmetics.familier;

import net.kyori.adventure.text.Component;
import onl.tesseract.core.cosmetics.CosmeticManager;
import onl.tesseract.tesseractlib.util.ChatFormats;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class PetManager implements Listener {
    static public final Map<UUID, ArmorStand> invokedPets = new HashMap<>();


    public static void invokePet(Player p, Pet pet)
    {

        if (pet == null || invokedPets.containsKey(p.getUniqueId()) )
        {
            invokedPets.get(p.getUniqueId()).remove();
            invokedPets.remove(p.getUniqueId());
            if(pet == null)return;
        }

        if (CosmeticManager.hasCosmetic(p, Pet.getTypeName(),pet))
        {
            ArmorStand armorStand;
            armorStand = (ArmorStand) p.getWorld().spawnEntity(p.getLocation().add(+0, +1.5, +0.3),
                                                               EntityType.ARMOR_STAND);
            armorStand.setRotation(p.getLocation().getYaw(), p.getLocation().getPitch());
            armorStand.setCustomName("§6" + pet);
            Objects.requireNonNull(armorStand.getEquipment()).setHelmet(pet.getHead());
            armorStand.setCustomNameVisible(false);
            armorStand.setSmall(true);
            armorStand.setGravity(false);
            armorStand.setVisible(false);
            armorStand.setCollidable(true);
            armorStand.setMarker(true);
            armorStand.setDisabledSlots(EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.FEET,
                                        EquipmentSlot.HAND, EquipmentSlot.LEGS, EquipmentSlot.OFF_HAND);
            p.sendMessage(ChatFormats.PET_SUCCESS.append(Component.text("Vous avez invoqué " + pet)));
            invokedPets.put(p.getUniqueId(), armorStand);
        }
        else
        {
            p.sendMessage(ChatFormats.PET_ERROR.append(Component.text("Vous ne possédez pas ce familier")));
        }
    }

    public static boolean hasPetInvocked(Player p)
    {
        return invokedPets.containsKey(p.getUniqueId());
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e)
    {
        Player p = e.getPlayer();
        Yaw yaw = Yaw.getYaw(p);
        if (!invokedPets.containsKey(p.getUniqueId()) || invokedPets.get(p.getUniqueId()) == null)
            return;
        switch (yaw)
        {
            case NORTH -> {
                invokedPets.get(p.getUniqueId()).teleport(p.getLocation().add(0.3, +1.5, +0.3));
                invokedPets.get(p.getUniqueId()).setRotation(p.getLocation().getYaw(), p.getLocation().getPitch());
            }
            case EAST -> {
                invokedPets.get(p.getUniqueId()).teleport(p.getLocation().add(-0.3, +1.5, +0.3));
                invokedPets.get(p.getUniqueId()).setRotation(p.getLocation().getYaw(), p.getLocation().getPitch());
            }
            case WEST -> {
                invokedPets.get(p.getUniqueId()).teleport(p.getLocation().add(+0.3, +1.5, -0.3));
                invokedPets.get(p.getUniqueId()).setRotation(p.getLocation().getYaw(), p.getLocation().getPitch());
            }
            case SOUTH -> {
                invokedPets.get(p.getUniqueId()).teleport(p.getLocation().add(-0.3, +1.5, -0.3));
                invokedPets.get(p.getUniqueId()).setRotation(p.getLocation().getYaw(), p.getLocation().getPitch());
            }
            default -> {
            }
        }
    }

    @EventHandler
    public void OnLeave(PlayerQuitEvent e)
    {
        Player p = e.getPlayer();
        reset(p);
    }

    public void reset(Player p)
    {
        if (invokedPets.get(p.getUniqueId()) != null)
        {
            invokePet(p, null);
            invokedPets.remove(p.getUniqueId());
        }
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event)
    {
        Player player = event.getPlayer();
        if (invokedPets.containsKey(player.getUniqueId()))
        {
            invokedPets.get(player.getUniqueId()).teleport(event.getTo());
        }
    }
}
