package onl.tesseract.core.persistence.hibernate.boutique

import onl.tesseract.core.boutique.BoutiqueRepository
import onl.tesseract.core.boutique.PlayerBoutiqueInfo
import onl.tesseract.core.persistence.hibernate.DaoUtils
import onl.tesseract.tesseractlib.entity.TPlayerInfo
import onl.tesseract.tesseractlib.player.Gender
import java.util.UUID

object BoutiqueHibernateRepository : BoutiqueRepository {

    override fun save(entity: PlayerBoutiqueInfo) {
        DaoUtils.executeInsideTransaction { session ->
            session.persist(fromModel(entity))
        }
    }

    override fun getById(id: UUID): PlayerBoutiqueInfo? {
        DaoUtils.executeInsideTransaction { session ->
            return session.find(TPlayerInfo::class.java, id).toModel()
        }
        return null
    }

    override fun idOf(entity: PlayerBoutiqueInfo): UUID = entity.playerID

    override fun addMarketCurrency(id: UUID, amount: Int) {
        DaoUtils.executeInsideTransaction {
            val info = it.find(TPlayerInfo::class.java, id) ?: TPlayerInfo(id)
            info.market_currency += amount
            it.persist(info)
        }
    }
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

private fun fromModel(model: PlayerBoutiqueInfo): TPlayerInfo {
    return TPlayerInfo(
        model.playerID,
        Gender.OTHER,
        model.activeTrail,
        model.activeFlyFilter,
        model.marketCurrency,
        model.shopPoints,
        mutableSetOf(),
        model.elytraTrails.toSet(),
        model.flyFilters.toSet(),
        model.pets.toSet(),
        model.teleportationAnimations.toSet(),
    )
}