package onl.tesseract.core.persistence.hibernate.boutique;

import lombok.Getter;
import onl.tesseract.core.persistence.hibernate.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.MutationQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class TPlayerInfoDAO {
    @Getter
    static private final TPlayerInfoDAO instance = new TPlayerInfoDAO();

    Logger log = LoggerFactory.getLogger(this.getClass());

    public void refresh(TPlayerInfo tPlayerInfo) {
        try {
            DaoUtils.executeInsideTransaction(session -> session.refresh(tPlayerInfo));
        } catch (Exception e) {
            log.error("Erreur lors du refresh du TPlayerInfo {}", tPlayerInfo.getUuid(), e);
        }
        try (Session session = HibernateUtil.INSTANCE.getSessionFactory().openSession()) {
            session.refresh(tPlayerInfo);
        }
    }


    public TPlayerInfo get(UUID uuid) {
        try (Session session = HibernateUtil.INSTANCE.getSessionFactory().openSession()) {
            return session.find(TPlayerInfo.class, uuid);
        }
    }

    @SuppressWarnings("deprecation")
    public void save(TPlayerInfo tPlayerInfo) {
        try {
            DaoUtils.executeInsideTransaction(session -> session.update(tPlayerInfo));
        } catch (Exception e) {
            log.error("Erreur lors du save du TPlayerInfo {}", tPlayerInfo.getUuid(), e);
        }
    }

    @SuppressWarnings("unused")
    public void delete(TPlayerInfo tPlayerInfo) {
        log.error("Erreur lors du delete du TPlayerInfo, la suppression n'est pas autorisée");
    }


    public void addMarketCurrency(TPlayerInfo tPlayerInfo, int amount) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.INSTANCE.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            String hql = "UPDATE TPlayerInfo SET market_currency = market_currency + :amount WHERE uuid = :uuid ";
            MutationQuery query = session.createMutationQuery(hql);
            query.setParameter("amount", amount);
            query.setParameter("uuid", tPlayerInfo.getUuid());
            query.executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            log.error("Erreur lors du add market currency {}", tPlayerInfo, e);
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    public void addShopPoint(TPlayerInfo tPlayerInfo, int amount) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.INSTANCE.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            String hql = "UPDATE TPlayerInfo SET shop_point = shop_point + :amount WHERE uuid = :uuid ";
            MutationQuery query = session.createMutationQuery(hql);
            query.setParameter("amount", amount);
            query.setParameter("uuid", tPlayerInfo.getUuid());
            query.executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            log.error("Erreur lors du add shop point {}", tPlayerInfo, e);
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    public void create(TPlayerInfo tPlayerInfo) {
        try {
            DaoUtils.executeInsideTransaction(session -> session.merge(tPlayerInfo));
        } catch (Exception e) {
            log.error("Erreur lors du save du TPlayerInfo {}", tPlayerInfo.getUuid(), e);
        }
    }
}
