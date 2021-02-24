package database;

import bean.Pair;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class DataAccessObject<T> {
    private final Class<T> type;
    private final String tableName;

    public DataAccessObject(Class<T> type, String tableName) {
        this.type = type;
        this.tableName = tableName;
    }

    public T findById(long id) {
        return HibernateSessionFactory.getSessionFactory().openSession().get(type, id);
    }

    public void save(T user) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.save(user);
        transaction.commit();
        session.close();
    }

    public void update(T user) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.update(user);
        transaction.commit();
        session.close();
    }

    public void remove(T user) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.remove(user);
        transaction.commit();
        session.close();
    }

    public List<T> findAll() {
        //noinspection unchecked
        return HibernateSessionFactory.getSessionFactory().openSession().createQuery("From " + tableName).list();
    }

    public List<T> findByConstraints(List<Pair<String, Object>> constraints) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(type);
        Root<T> root = criteriaQuery.from(type);
        for (Pair<String, Object> constraint : constraints) {
            criteriaQuery.select(root).where(criteriaBuilder.equal(root.get(constraint.first), constraint.second));
        }
        Query<T> query = session.createQuery(criteriaQuery);
        return query.getResultList();
    }

    public long size() {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<T> root = criteriaQuery.from(type);
        criteriaQuery.select(criteriaBuilder.count(root));
        return session.createQuery(criteriaQuery).getSingleResult();
    }
}
