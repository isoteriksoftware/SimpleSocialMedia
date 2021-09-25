package week8.assessment.encentral.dao;

import com.querydsl.jpa.impl.JPAQuery;
import week8.assessment.encentral.entities.Comment;
import week8.assessment.encentral.entities.Post;
import week8.assessment.encentral.entities.QComment;
import week8.assessment.encentral.utils.HibernateUtil;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class CommentDao {
    public static Comment getComment(int id) {
        AtomicReference<Comment> reference = new AtomicReference<>();

        HibernateUtil.doTransaction(session -> {
            JPAQuery<Comment> query = new JPAQuery<>(session.getEntityManagerFactory().createEntityManager());
            QComment qComment = QComment.comment;
            Comment comment = query.select(qComment)
                    .from(qComment)
                    .where(qComment.id.eq(id))
                    .fetchOne();

            reference.set(comment);
        });

        return reference.get();
    }

    public static boolean addComment(Comment comment) {
        return HibernateUtil.doTransaction(session -> session.save(comment));
    }

    public static List<Comment> getAllComments() {
        AtomicReference<List<Comment>> comments = new AtomicReference<>();

        HibernateUtil.doTransaction(session -> {
            JPAQuery<Comment> query = new JPAQuery<>(session.getEntityManagerFactory().createEntityManager());
            QComment qComment = QComment.comment;
            comments.set(query.select(qComment)
                    .from(qComment)
                    .fetch());
        });

        return comments.get();
    }

    public static List<Comment> getAllComments(Post post) {
        AtomicReference<List<Comment>> comments = new AtomicReference<>();

        HibernateUtil.doTransaction(session -> {
            JPAQuery<Comment> query = new JPAQuery<>(session.getEntityManagerFactory().createEntityManager());
            QComment qComment = QComment.comment;
            comments.set(query.select(qComment)
                    .from(qComment)
                    .where(qComment.post.id.eq(post.getId()))
                    .fetch());
        });

        return comments.get();
    }
}
