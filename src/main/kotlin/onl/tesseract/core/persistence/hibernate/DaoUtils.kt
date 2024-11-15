package onl.tesseract.core.persistence.hibernate

import jakarta.persistence.criteria.CriteriaBuilder
import org.hibernate.Session
import org.hibernate.Transaction
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.lang.Exception

object DaoUtils {
    val log: Logger = LoggerFactory.getLogger(DaoUtils::class.java)

    inline fun executeInsideTransaction(action: (Session) -> Unit) {
        var transaction: Transaction? = null
        try {
            HibernateUtil.sessionFactory.openSession().use { session ->
                transaction = session.beginTransaction()
                action(session)
                transaction.commit()
            }
        } catch (e: Exception) {
            log.error("Error executing inside transaction", e)
            transaction?.rollback()
        }
    }

    fun <T> loadAll(type: Class<T?>?, session: Session): MutableList<T?>? {
        val builder: CriteriaBuilder = session.getCriteriaBuilder()
        val criteria = builder.createQuery<T?>(type)
        criteria.from<T?>(type)
        val data = session.createQuery<T?>(criteria).getResultList()
        return data
    }
}
