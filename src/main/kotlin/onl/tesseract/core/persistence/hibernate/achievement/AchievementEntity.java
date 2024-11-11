package onl.tesseract.core.persistence.hibernate.achievement;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import onl.tesseract.core.achievement.Achievement;

@Getter
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Table(name = "t_achievement",
        uniqueConstraints = {@UniqueConstraint(columnNames = "id")},
        indexes = @Index(name = "idx_id", columnList = "id"))
public class AchievementEntity {

    @Column(columnDefinition = "VARCHAR(255)")
    String condition;
    String name;
    @Id
    int id;
    @Column(name = "text")
    String displayName;


    public AchievementEntity(int id, String name, String displayName, String condition) {
        this.id = id;
        this.name = name;
        this.displayName = displayName;
        this.condition = condition;
    }

    public Achievement toModel() {
        return new Achievement(condition, name, id, displayName);
    }

    public static AchievementEntity fromModel(Achievement model) {
        return new AchievementEntity(model.getId(), model.getName(), model.getDisplayName(), model.getCondition());
    }
}



