package onl.tesseract.core.persistence.hibernate.achievement;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "t_player_achievementes")
@AllArgsConstructor
@NoArgsConstructor
@IdClass(PlayerAchievementsEntity.IdComposite.class)
public class PlayerAchievementsEntity {

    @Id
    private UUID player;
    @Id
    private int achievementID;

    public record IdComposite(UUID player, int achievementID) {

    }
}
