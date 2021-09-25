package week8.assessment.encentral.dao;

import com.querydsl.jpa.impl.JPAQuery;
import week8.assessment.encentral.entities.Post;
import week8.assessment.encentral.entities.QPost;
import week8.assessment.encentral.entities.User;
import week8.assessment.encentral.utils.HibernateUtil;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class PostDao {
    public static Post getPost(int id) {
        AtomicReference<Post> reference = new AtomicReference<>();

        HibernateUtil.doTransaction(session -> {
            JPAQuery<Post> query = new JPAQuery<>(session.getEntityManagerFactory().createEntityManager());
            QPost qPost = QPost.post;
            Post post = query.select(qPost)
                    .from(qPost)
                    .where(qPost.id.eq(id))
                    .fetchOne();

            reference.set(post);
        });

        return reference.get();
    }

    public static Post getPost(String title) {
        AtomicReference<Post> reference = new AtomicReference<>();

        HibernateUtil.doTransaction(session -> {
            JPAQuery<Post> query = new JPAQuery<>(session.getEntityManagerFactory().createEntityManager());
            QPost qPost = QPost.post;
            Post post = query.select(qPost)
                    .from(qPost)
                    .where(qPost.title.eq(title))
                    .fetchOne();

            reference.set(post);
        });

        return reference.get();
    }

    public static boolean addPost(Post post) {
        return HibernateUtil.doTransaction(session -> session.save(post));
    }

    public static List<Post> getAllPosts() {
        AtomicReference<List<Post>> posts = new AtomicReference<>();

        HibernateUtil.doTransaction(session -> {
            JPAQuery<Post> query = new JPAQuery<>(session.getEntityManagerFactory().createEntityManager());
            QPost qPost = QPost.post;
            posts.set(query.select(qPost)
                    .from(qPost)
                    .fetch());
        });

        return posts.get();
    }

    public static List<Post> getAllPosts(User user) {
        AtomicReference<List<Post>> posts = new AtomicReference<>();

        HibernateUtil.doTransaction(session -> {
            JPAQuery<Post> query = new JPAQuery<>(session.getEntityManagerFactory().createEntityManager());
            QPost qPost = QPost.post;
            posts.set(query.select(qPost)
                    .from(qPost)
                    .where(qPost.author.id.eq(user.getId()))
                    .fetch());
        });

        return posts.get();
    }
}
