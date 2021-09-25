package week8.assessment.encentral.dao;

import com.querydsl.jpa.impl.JPAQuery;
import week8.assessment.encentral.entities.QUser;
import week8.assessment.encentral.entities.User;
import week8.assessment.encentral.utils.HibernateUtil;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class UserDao {
    public static User getUser(String username) {
        AtomicReference<User> reference = new AtomicReference<>();

        HibernateUtil.doTransaction(session -> {
            JPAQuery<User> query = new JPAQuery<>(session.getEntityManagerFactory().createEntityManager());
            QUser qUser = QUser.user;
            User user = query.select(qUser)
                    .from(qUser)
                    .where(qUser.username.eq(username))
                    .fetchOne();

            reference.set(user);
        });

        return reference.get();
    }

    public static boolean addUser(User user) {
        return HibernateUtil.doTransaction(session -> session.save(user));
    }

    public static List<User> getAllUsers() {
        AtomicReference<List<User>> users = new AtomicReference<>();

        HibernateUtil.doTransaction(session -> {
            JPAQuery<User> query = new JPAQuery<>(session.getEntityManagerFactory().createEntityManager());
            QUser qUser = QUser.user;
            users.set(query.select(qUser)
                    .from(qUser)
                    .fetch());
        });

        return users.get();
    }

    public static boolean isUserExists(String username) {
        return getUser(username) != null;
    }
}
