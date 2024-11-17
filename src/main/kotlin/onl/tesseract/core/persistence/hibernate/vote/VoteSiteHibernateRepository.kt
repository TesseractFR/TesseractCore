package onl.tesseract.core.persistence.hibernate.vote

import onl.tesseract.core.persistence.hibernate.DaoUtils
import onl.tesseract.core.vote.VoteSite
import onl.tesseract.core.vote.VoteSiteRepository

object VoteSiteHibernateRepository : VoteSiteRepository {

    override fun getAll(): List<VoteSite> {
        DaoUtils.executeInsideTransaction { session ->
            val list = session.createQuery("FROM VoteSiteEntity", VoteSiteEntity::class.java).resultList
            return list.map { it.toModel() }
        }
        return emptyList()
    }

    override fun getById(id: String): VoteSite? {
        DaoUtils.executeInsideTransaction { session ->
            return session.find(VoteSiteEntity::class.java, id)?.toModel()
        }
        return null
    }

    override fun idOf(entity: VoteSite): String = entity.serviceName
}