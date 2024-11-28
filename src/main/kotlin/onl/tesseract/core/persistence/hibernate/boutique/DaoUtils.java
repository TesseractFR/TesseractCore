package onl.tesseract.core.persistence.hibernate.boutique;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import lombok.extern.slf4j.Slf4j;
import onl.tesseract.core.persistence.hibernate.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.function.Consumer;
@Slf4j
public class DaoUtils {
    private DaoUtils() {
    }

    public static void executeInsideTransaction(Consumer<Session> action) {

        Transaction transaction = null;
        try (Session session = HibernateUtil.INSTANCE.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            action.accept(session);
            transaction.commit();
        } catch (Exception e) {
            log.error("Error executing inside transaction", e);
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }
    public static <T> List<T> loadAll(Class<T> type, Session session) {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<T> criteria = builder.createQuery(type);
        criteria.from(type);
        return session.createQuery(criteria).getResultList();
    }

}
