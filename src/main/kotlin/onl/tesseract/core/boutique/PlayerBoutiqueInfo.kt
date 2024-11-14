package onl.tesseract.core.boutique

import onl.tesseract.core.cosmetics.ElytraTrails
import onl.tesseract.core.cosmetics.FlyFilter
import onl.tesseract.core.cosmetics.TeleportationAnimation
import onl.tesseract.core.cosmetics.familier.Pet
import java.util.UUID

data class PlayerBoutiqueInfo(
    val playerID: UUID,
    val activeTrail: ElytraTrails = ElytraTrails.NONE,
    val activeFlyFilter: FlyFilter = FlyFilter.NONE,
    val activeTpAnimation: TeleportationAnimation = TeleportationAnimation.WATER,
    val marketCurrency: Int = 0,
    val shopPoints: Int = 0,
    val elytraTrails: Collection<ElytraTrails> = emptyList(),
    val flyFilters: Collection<FlyFilter> = emptyList(),
    val pets: Collection<Pet> = emptyList(),
    val teleportationAnimations: Collection<TeleportationAnimation> = emptyList(),
)