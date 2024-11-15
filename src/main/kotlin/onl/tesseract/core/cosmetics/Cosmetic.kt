package onl.tesseract.core.cosmetics

import onl.tesseract.lib.MarketObject
import org.bukkit.Material

interface Cosmetic : MarketObject

interface CosmeticWithMaterial : Cosmetic {

    val material: Material
}
