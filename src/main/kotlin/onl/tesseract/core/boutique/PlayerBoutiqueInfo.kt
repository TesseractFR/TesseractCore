package onl.tesseract.core.boutique

import onl.tesseract.tesseractlib.cosmetics.Cosmetic
import onl.tesseract.tesseractlib.cosmetics.ElytraTrails
import onl.tesseract.tesseractlib.cosmetics.FlyFilter
import onl.tesseract.tesseractlib.cosmetics.TeleportationAnimation
import onl.tesseract.tesseractlib.cosmetics.familier.Pet
import java.util.UUID

data class PlayerBoutiqueInfo(
    val playerID: UUID,
    val activeTrail: ElytraTrails = ElytraTrails.NONE,
    val activeFlyFilter: FlyFilter = FlyFilter.NONE,
    val marketCurrency: Int = 0,
    val shopPoints: Int = 0,
    val elytraTrails: Collection<ElytraTrails> = emptyList(),
    val flyFilters: Collection<FlyFilter> = emptyList(),
    val pets: Collection<Pet> = emptyList(),
    val teleportationAnimations: Collection<TeleportationAnimation> = emptyList(),
)