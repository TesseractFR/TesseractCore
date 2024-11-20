package onl.tesseract.core.persistence.hibernate.boutique;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class CosmeticEntity  {
    @Column(name = "cosmetic_type")
    private String cosmeticType;
    private String cosmetic;

    public CosmeticEntity() {}

    public CosmeticEntity(String cosmeticType, String cosmetic) {
        this.cosmeticType = cosmeticType;
        this.cosmetic = cosmetic;
    }
}
