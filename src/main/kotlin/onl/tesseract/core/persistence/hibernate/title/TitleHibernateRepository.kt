package onl.tesseract.core.persistence.hibernate.title

import onl.tesseract.core.persistence.hibernate.DaoUtils
import onl.tesseract.core.title.Title
import onl.tesseract.core.title.TitleRepository

object TitleHibernateRepository : TitleRepository {

    override fun save(entity: Title) {
        DaoUtils.executeInsideTransaction {
            it.merge(TitleEntity.fromModel(entity))
        }
    }

    override fun getById(id: String): Title? {
        DaoUtils.executeInsideTransaction {
            return it[TitleEntity::class.java, id].toModel()
        }
        return null
    }

    override fun idOf(entity: Title): String = entity.id
}