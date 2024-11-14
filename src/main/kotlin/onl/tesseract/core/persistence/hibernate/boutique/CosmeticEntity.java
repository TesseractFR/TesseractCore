package onl.tesseract.core.persistence.hibernate.boutique;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class CosmeticEntity  {
    private String cosmetic_type;
    private String cosmetic;

    public CosmeticEntity() {}

    public CosmeticEntity(String cosmetic_type, String cosmetic) {
        this.cosmetic_type = cosmetic_type;
        this.cosmetic = cosmetic;
    }
}
