package onl.tesseract.core.cosmetics;

import lombok.Getter;
import onl.tesseract.core.persistence.hibernate.boutique.TPlayerInfo;
import onl.tesseract.core.persistence.hibernate.boutique.TPlayerInfoService;
import onl.tesseract.core.TesseractCorePlugin;
import onl.tesseract.core.cosmetics.familier.Pet;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Set;
import java.util.UUID;

public class CosmeticManager {
    @Getter
    private static final CosmeticManager instance = new CosmeticManager();

    public static void giveCosmetic(UUID uuid, Cosmetic cosmetic) {

        var runnable = new BukkitRunnable() {
            final TPlayerInfo tPlayerInfo = TPlayerInfoService.getInstance().get(uuid);

            @Override
            public void run() {
                if (cosmetic instanceof FlyFilter flyFilter) {
                    tPlayerInfo.getFlyFilters().add(flyFilter);
                } else if (cosmetic instanceof Pet pet) {
                    tPlayerInfo.getPets().add(pet);
                } else if (cosmetic instanceof ElytraTrails ellyTrails) {
                    tPlayerInfo.getElytraTrails().add(ellyTrails);
                } else if (cosmetic instanceof TeleportationAnimation teleportationAnimation) {
                    tPlayerInfo.getTeleportationAnimations().add(teleportationAnimation);
                }
                TPlayerInfoService.getInstance().save(tPlayerInfo);
            }
        };
        runnable.runTaskAsynchronously(TesseractCorePlugin.instance);
    }


    public static void removeCosmetic(UUID uuid, Cosmetic cosmetic) {
        final TPlayerInfo tPlayerInfo = TPlayerInfoService.getInstance().get(uuid);
        var runnable = new BukkitRunnable() {
            @Override
            public void run() {
                if (cosmetic instanceof FlyFilter flyFilter) {
                    tPlayerInfo.getFlyFilters().remove(flyFilter);
                } else if (cosmetic instanceof Pet pet) {
                    tPlayerInfo.getPets().remove(pet);
                } else if (cosmetic instanceof ElytraTrails ellyTrails) {
                    tPlayerInfo.getElytraTrails().remove(ellyTrails);
                } else if (cosmetic instanceof TeleportationAnimation teleportationAnimation) {
                    tPlayerInfo.getTeleportationAnimations().remove(teleportationAnimation);
                }
            }
        };
        runnable.runTaskAsynchronously(TesseractCorePlugin.instance);
    }
    public static boolean hasCosmetic(Player player, String type, Cosmetic cosmetic) {
        return hasCosmetic(player.getUniqueId(), type, cosmetic);
    }
    public static boolean hasCosmetic(UUID uuid, String type, Cosmetic cosmetic) {
        final TPlayerInfo tPlayerInfo = TPlayerInfoService.getInstance().get(uuid);
        if (cosmetic instanceof FlyFilter flyFilter) {
            return tPlayerInfo.getFlyFilters().contains(flyFilter);
        } else if (cosmetic instanceof Pet pet) {
            return tPlayerInfo.getPets().contains(pet);
        } else if (cosmetic instanceof ElytraTrails ellyTrails) {
            return tPlayerInfo.getElytraTrails().contains(ellyTrails);
        } else if (cosmetic instanceof TeleportationAnimation teleportationAnimation) {
            return tPlayerInfo.getTeleportationAnimations().contains(teleportationAnimation);
        }
        return false;
    }

    public static Cosmetic stringToCosmetic(String type, String cosmetic) {
        if (ElytraTrails.getTypeName().equals(type)) {
            return ElytraTrails.valueOf(cosmetic);
        } else if (FlyFilter.getTypeName().equals(type)) {
            return FlyFilter.valueOf(cosmetic);
        } else if (TeleportationAnimation.getTypeName().equals(type)) {
            return TeleportationAnimation.valueOf(cosmetic);
        } else if (Pet.getTypeName().equals(type)) {
            return Pet.valueOf(cosmetic);
        }
        throw new IllegalArgumentException("Unknow cosmetic " + type + " " + cosmetic);
    }

    public static int getTotalPossessed(UUID uuid, String type) {
        final TPlayerInfo tPlayerInfo = TPlayerInfoService.getInstance().get(uuid);
        if (ElytraTrails.getTypeName().equals(type)) {
            return tPlayerInfo.getElytraTrails().size();
        } else if (FlyFilter.getTypeName().equals(type)) {
            return tPlayerInfo.getFlyFilters().size();
        } else if (TeleportationAnimation.getTypeName().equals(type)) {
            return tPlayerInfo.getTeleportationAnimations().size();
        } else if (Pet.getTypeName().equals(type)) {
            return tPlayerInfo.getPets().size();
        }
        return 0;
    }


    public static Set<String> getTypes() {
        return Set.of(TeleportationAnimation.getTypeName(), FlyFilter.getTypeName(), Pet.getTypeName(), ElytraTrails.getTypeName());
    }

    public static Set<Cosmetic> getCosmetics(String arg) {
        if (ElytraTrails.getTypeName().equals(arg)) {
            return Set.of(ElytraTrails.values());
        } else if (FlyFilter.getTypeName().equals(arg)) {
            return Set.of(FlyFilter.values());
        } else if (TeleportationAnimation.getTypeName().equals(arg)) {
            return Set.of(TeleportationAnimation.values());
        } else if (Pet.getTypeName().equals(arg)) {
            return Set.of(Pet.values());
        }
        return Set.of();
    }
}
