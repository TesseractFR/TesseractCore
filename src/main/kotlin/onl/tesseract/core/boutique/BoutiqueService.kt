package onl.tesseract.core.boutique

import onl.tesseract.lib.repository.Repository
import java.util.UUID

class BoutiqueService(private val repository: BoutiqueRepository)  {

    fun getPlayerBoutiqueInfo(playerID: UUID): PlayerBoutiqueInfo {
        return repository.getById(playerID) ?: PlayerBoutiqueInfo(playerID)
    }
}

interface BoutiqueRepository : Repository<PlayerBoutiqueInfo, UUID> {

}