package onl.tesseract.core.persistence.hibernate.boutique

import onl.tesseract.core.boutique.BoutiqueRepository
import onl.tesseract.core.boutique.PlayerBoutiqueInfo
import onl.tesseract.core.persistence.hibernate.DaoUtils
import onl.tesseract.tesseractlib.cosmetics.ElytraTrails
import onl.tesseract.tesseractlib.cosmetics.FlyFilter
import onl.tesseract.tesseractlib.cosmetics.TeleportationAnimation
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

    override fun setActiveTrail(id: UUID, trail: ElytraTrails) {
        DaoUtils.executeInsideTransaction {
            val info = it.find(TPlayerInfo::class.java, id) ?: TPlayerInfo(id)
            info.active_trail = trail
            it.persist(info)
        }
    }

    override fun setActiveFilter(id: UUID, filter: FlyFilter) {
        DaoUtils.executeInsideTransaction {
            val info = it.find(TPlayerInfo::class.java, id) ?: TPlayerInfo(id)
            info.active_fly_filter = filter
            it.persist(info)
        }
    }

    override fun setActiveTpAnimation(id: UUID, animation: TeleportationAnimation) {
        DaoUtils.executeInsideTransaction {
            val info = it.find(TPlayerInfo::class.java, id) ?: TPlayerInfo(id)
            info.active_tp_animation = animation
            it.persist(info)
        }
    }
}

fun TPlayerInfo.toModel(): PlayerBoutiqueInfo {
    return PlayerBoutiqueInfo(
        this.uuid,
        this.active_trail,
        this.active_fly_filter,
        this.active_tp_animation,
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
        model.activeTpAnimation,
        model.marketCurrency,
        model.shopPoints,
        mutableSetOf(),
        model.elytraTrails.toSet(),
        model.flyFilters.toSet(),
        model.pets.toSet(),
        model.teleportationAnimations.toSet(),
    )
}