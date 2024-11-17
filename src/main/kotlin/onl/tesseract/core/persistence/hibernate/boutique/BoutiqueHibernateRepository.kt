package onl.tesseract.core.persistence.hibernate.boutique

import onl.tesseract.core.boutique.BoutiqueRepository
import onl.tesseract.core.boutique.PlayerBoutiqueInfo
import onl.tesseract.core.persistence.hibernate.DaoUtils
import onl.tesseract.core.cosmetics.ElytraTrails
import onl.tesseract.core.cosmetics.FlyFilter
import onl.tesseract.core.cosmetics.TeleportationAnimation
import onl.tesseract.lib.player.Gender
import java.util.UUID

object BoutiqueHibernateRepository : BoutiqueRepository {

    override fun save(entity: PlayerBoutiqueInfo) {
        DaoUtils.executeInsideTransaction { session ->
            session.merge(fromModel(entity))
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
            it.merge(info)
        }
    }

    override fun setActiveTrail(id: UUID, trail: ElytraTrails) {
        DaoUtils.executeInsideTransaction {
            val info = it.find(TPlayerInfo::class.java, id) ?: TPlayerInfo(id)
            info.active_trail = trail
            it.merge(info)
        }
    }

    override fun setActiveFilter(id: UUID, filter: FlyFilter) {
        DaoUtils.executeInsideTransaction {
            val info = it.find(TPlayerInfo::class.java, id) ?: TPlayerInfo(id)
            info.active_fly_filter = filter
            it.merge(info)
        }
    }

    override fun setActiveTpAnimation(id: UUID, animation: TeleportationAnimation) {
        DaoUtils.executeInsideTransaction {
            val info = it.find(TPlayerInfo::class.java, id) ?: TPlayerInfo(id)
            info.active_tp_animation = animation
            it.merge(info)
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