package week8.assessment.encentral.utils;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HibernateUtil {
    private static StandardServiceRegistry registry;
    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                registry = new StandardServiceRegistryBuilder().configure().build();

                MetadataSources sources = new MetadataSources(registry);
                Metadata metadata = sources.getMetadataBuilder().build();

                sessionFactory = metadata.getSessionFactoryBuilder().build();
            } catch (Exception e) {
                e.printStackTrace();
                if (registry != null)
                    StandardServiceRegistryBuilder.destroy(registry);
            }
        }

        return sessionFactory;
    }

    public static void shutDown() {
        if (registry != null)
            StandardServiceRegistryBuilder.destroy(registry);
    }

    public static boolean doTransaction(TransactionTask task) {
        Transaction transaction = null;

        try (Session session = getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            task.run(session);
            transaction.commit();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null && transaction.isActive())
                transaction.rollback();

            return false;
        }
    }

    public interface TransactionTask {
        void run(Session session);
    }
}
