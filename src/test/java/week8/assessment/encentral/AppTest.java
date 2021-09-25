package week8.assessment.encentral;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;
import week8.assessment.encentral.dao.CommentDao;
import week8.assessment.encentral.dao.PostDao;
import week8.assessment.encentral.dao.UserDao;
import week8.assessment.encentral.entities.Comment;
import week8.assessment.encentral.entities.Post;
import week8.assessment.encentral.entities.User;

import java.util.List;

public class AppTest {
    @BeforeClass
    public static void setup() {
        UserDao.addUser(new User("encentral", "secret"));
        UserDao.addUser(new User("emris", "hidden"));
    }

    @Test
    public void testUserAdded() {
        List<User> users = UserDao.getAllUsers();

        assertNotNull(users);
        assertEquals(2, users.size());
    }

    @Test
    public void testAddPostsAndComments() {
        User user = UserDao.getUser("encentral");
        assertNotNull(user);

        Post post = new Post(user, "Life at Encentral", "Well, how do I start :)");
        PostDao.addPost(post);

        List<Post> posts = PostDao.getAllPosts();
        assertNotNull(posts);
        assertEquals(1, posts.size());

        post = PostDao.getPost(1);
        Comment comment = new Comment(user, post, "I love this post!");
        CommentDao.addComment(comment);

        List<Comment> comments = CommentDao.getAllComments();
        assertNotNull(comments);
        assertEquals(1, comments.size());

        comment = comments.get(0);
        assertNotNull(comment);
        assertEquals("encentral", comment.getAuthor().getUsername());
        assertEquals("Life at Encentral", comment.getPost().getTitle());
    }
}
