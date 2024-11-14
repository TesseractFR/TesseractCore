package onl.tesseract.core.persistence.hibernate.boutique;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import onl.tesseract.core.cosmetics.ElytraTrails;
import onl.tesseract.core.cosmetics.FlyFilter;
import onl.tesseract.core.cosmetics.TeleportationAnimation;
import onl.tesseract.core.cosmetics.familier.Pet;
import onl.tesseract.lib.player.Gender;
import org.hibernate.annotations.JdbcTypeCode;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "t_player", uniqueConstraints = {@UniqueConstraint(columnNames = "uuid")}, indexes = @Index(name = "idx_uuid", columnList = "uuid"))
public class TPlayerInfo implements Serializable {
    @Id
    @Column(updatable = false, nullable = false, columnDefinition = "VARCHAR(36)", unique = true)
    @JdbcTypeCode(java.sql.Types.VARCHAR)
    UUID uuid;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    Gender genre = Gender.OTHER;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    ElytraTrails active_trail = ElytraTrails.NONE;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    FlyFilter active_fly_filter = FlyFilter.NONE;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    TeleportationAnimation active_tp_animation = TeleportationAnimation.WATER;

    @Column(nullable = false)
    int market_currency = 0;

    @Column(nullable = false)
    int shop_point = 0;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "t_player_cosmetics", joinColumns = @JoinColumn(name = "player_uuid"))
    @AttributeOverrides({@AttributeOverride(name = "cosmetic_type", column = @Column(name = "cosmetic_type")), @AttributeOverride(name = "cosmetic", column = @Column(name = "cosmetic"))})
    private Set<CosmeticEntity> cosmetics = new HashSet<>();

    @Transient
    private Set<ElytraTrails> elytraTrails = new HashSet<>();

    @Transient
    private Set<FlyFilter> flyFilters = new HashSet<>();

    @Transient
    private Set<Pet> pets = new HashSet<>();

    @Transient
    private Set<TeleportationAnimation> teleportationAnimations = new HashSet<>();

    public TPlayerInfo(UUID uuid) {
        this.uuid = uuid;
    }

    @PostLoad
    @PostUpdate
    private void populateCosmetics() {
        for (CosmeticEntity cosmetic : cosmetics) {
            if (ElytraTrails.getTypeName().equals(cosmetic.getCosmetic_type())) {
                elytraTrails.add(ElytraTrails.valueOf(cosmetic.getCosmetic()));
            } else if (FlyFilter.getTypeName().equals(cosmetic.getCosmetic_type())) {
                flyFilters.add(FlyFilter.valueOf(cosmetic.getCosmetic()));
            } else if (TeleportationAnimation.getTypeName().equals(cosmetic.getCosmetic_type())) {
                teleportationAnimations.add(TeleportationAnimation.valueOf(cosmetic.getCosmetic()));
            } else if (Pet.getTypeName().equals(cosmetic.getCosmetic_type())) {
                pets.add(Pet.valueOf(cosmetic.getCosmetic()));
            }
        }
    }

    @PrePersist
    @PreUpdate
    private void populateCosmeticEntities() {
        cosmetics.clear();
        for (ElytraTrails trail : elytraTrails) {
            cosmetics.add(new CosmeticEntity(ElytraTrails.getTypeName(), trail.name()));
        }
        for (FlyFilter filter : flyFilters) {
            cosmetics.add(new CosmeticEntity(FlyFilter.getTypeName(), filter.name()));
        }
        for (TeleportationAnimation animation : teleportationAnimations) {
            cosmetics.add(new CosmeticEntity(TeleportationAnimation.getTypeName(), animation.name()));
        }
        for (Pet pet : pets) {
            cosmetics.add(new CosmeticEntity(Pet.getTypeName(), pet.name()));
        }
    }

}
