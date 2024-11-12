package onl.tesseract.core.persistence.hibernate.boutique

import onl.tesseract.core.boutique.BoutiqueRepository
import onl.tesseract.core.boutique.PlayerBoutiqueInfo
import onl.tesseract.core.persistence.hibernate.DaoUtils
import onl.tesseract.tesseractlib.entity.TPlayerInfo
import java.util.UUID

object BoutiqueHibernateRepository : BoutiqueRepository {

    override fun save(entity: PlayerBoutiqueInfo) {
        DaoUtils.executeInsideTransaction { session ->
            session.persist(entity)
        }
    }

    override fun getById(id: UUID): PlayerBoutiqueInfo? {
        DaoUtils.executeInsideTransaction { session ->
            return session.find(PlayerBoutiqueInfo::class.java, id)
        }
        return null
    }

    override fun idOf(entity: PlayerBoutiqueInfo): UUID = entity.playerID
}

fun TPlayerInfo.toModel(): PlayerBoutiqueInfo {
    return PlayerBoutiqueInfo(
        this.uuid,
        this.active_trail,
        this.active_fly_filter,
        this.market_currency,
        this.shop_point,
        this.elytraTrails,
        this.flyFilters,
        this.pets,
        this.teleportationAnimations,
    )
}