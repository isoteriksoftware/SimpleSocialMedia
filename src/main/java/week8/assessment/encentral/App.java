package week8.assessment.encentral;

import week8.assessment.encentral.dao.CommentDao;
import week8.assessment.encentral.dao.PostDao;
import week8.assessment.encentral.dao.UserDao;
import week8.assessment.encentral.entities.Comment;
import week8.assessment.encentral.entities.Post;
import week8.assessment.encentral.entities.User;

import java.util.List;
import java.util.Scanner;

public class App {
    private static final int OPTION_REGISTER = 1;
    private static final int OPTION_SIGN_IN  = 2;
    private static final int OPTION_QUIT     = 3;

    private static final int OPTION_CREATE_POST   = 1;
    private static final int OPTION_VIEW_POSTS    = 2;
    private static final int OPTION_VIEW_MY_POSTS = 3;
    private static final int OPTION_COMMENT_POST  = 4;
    private static final int OPTION_SIGN_OUT      = 5;

    private static final Scanner scanner;

    private static boolean isUserLoggedIn = false;
    private static User loggedInUser;

    static {
        scanner = new Scanner(System.in);
        scanner.useDelimiter("\n");

        // This is just to make sure we're connected to the DB before accepting any user input
        // Subsequent transactions will be faster!
        UserDao.getAllUsers();
    }

    public static void main( String[] args ) {
        boolean shouldQuit = false;

        while (!shouldQuit) {
            System.out.println();
            int option = getMainMenuOption();
            System.out.println();

            if (option == OPTION_QUIT) {
                shouldQuit = true;
                quit();
            }
            else if (option == OPTION_REGISTER)
                registerUser();
            else if (option == OPTION_SIGN_IN)
                login();
            else
                System.out.println("Please choose a valid option");
        }
    }

    private static void quit() {
        System.out.println("Good bye!");
    }

    private static int getMainMenuOption() {
        int option;

        while (true) {
            System.out.printf("%d -> Create an Account%n", OPTION_REGISTER);
            System.out.printf("%d -> Sign in%n", OPTION_SIGN_IN);
            System.out.printf("%d -> Quit%n", OPTION_QUIT);

            System.out.print("Please choose one of the options to continue: ");
            String input = scanner.next();

            try {
                option = Integer.parseInt(input);
                break;
            } catch (Exception e) {
                System.out.println(input + " is not a valid option");
            }

            System.out.println();
        }

        return option;
    }

    private static int getLoggedInMenuOption() {
        int option;

        while (true) {
            System.out.printf("%d -> Create a post%n", OPTION_CREATE_POST);
            System.out.printf("%d -> Show all posts%n", OPTION_VIEW_POSTS);
            System.out.printf("%d -> Show my posts%n", OPTION_VIEW_MY_POSTS);
            System.out.printf("%d -> Comment a post%n", OPTION_COMMENT_POST);
            System.out.printf("%d -> Sign out%n", OPTION_SIGN_OUT);

            System.out.print("Please choose one of the options to continue: ");
            String input = scanner.next();

            try {
                option = Integer.parseInt(input);
                break;
            } catch (Exception e) {
                System.out.println(input + " is not a valid option");
            }

            System.out.println();
        }

        return option;
    }

    private static void registerUser() {
        String username;

        while (true) {
            System.out.print("Choose a username: ");
            username = scanner.next().trim();

            if (UserDao.isUserExists(username)) {
                System.out.println("Oops! This username is taken, choose another one.");
                continue;
            }

            break;
        }

        System.out.print("Choose a password: ");
        String password = scanner.next();

        User user = new User(username, password);

        if (UserDao.addUser(user))
            System.out.println("Registration successful!");
        else
            System.out.println("Registration failed!");
    }

    private static void login() {
        System.out.print("Enter your username: ");
        String username = scanner.next().trim();

        System.out.print("Enter your password: ");
        String password = scanner.next();

        User user = UserDao.getUser(username);
        if (user == null || !user.getPassword().equals(password)) {
            System.out.println("Invalid username or password!");
            return;
        }

        isUserLoggedIn = true;
        loggedInUser = user;
        System.out.println("You're now signed in!");
        processedLoggedInState();
    }

    private static void processedLoggedInState() {
        while (isUserLoggedIn) {
            System.out.println();
            int option = getLoggedInMenuOption();
            System.out.println();

            if (option == OPTION_CREATE_POST)
                createPost();
            else if (option == OPTION_VIEW_POSTS)
                showAllPosts();
            else if (option == OPTION_VIEW_MY_POSTS)
                showMyPosts();
            else if (option == OPTION_COMMENT_POST)
                commentPost();
            else if (option == OPTION_SIGN_OUT) {
                System.out.println("You're now signed out!");
                isUserLoggedIn = false;
            }
            else
                System.out.println("Please choose a valid option");
        }
    }

    private static void createPost() {
        System.out.print("Enter the post title: ");
        String title = scanner.next().trim();

        System.out.print("Enter the post contents: ");
        String content = scanner.next().trim();

        Post post = new Post(loggedInUser, title, content);

        if (PostDao.addPost(post))
            System.out.println("Post created successfully!");
        else
            System.out.println("Failed to create post.");
    }

    private static void showAllPosts() {
        List<Post> posts = PostDao.getAllPosts();

        if (posts == null || posts.isEmpty())
            System.out.println("No post yet!");
        else {
            posts.forEach(App::printPost);
        }
    }

    private static void showMyPosts() {
        List<Post> posts = PostDao.getAllPosts(loggedInUser);

        if (posts == null || posts.isEmpty())
            System.out.println("You have not added a post yet!");
        else {
            posts.forEach(App::printPost);
        }
    }

    private static void printPost(Post post) {
        System.out.println(post);

        List<Comment> comments = CommentDao.getAllComments(post);
        if (comments != null && !comments.isEmpty()) {
            System.out.println("\tComments:");
            comments.forEach(comment -> System.out.println("\t\t" + comment));
            System.out.println();
        }
    }

    private static void commentPost() {
        System.out.print("Enter the post title: ");
        String title = scanner.next().trim();

        Post post = PostDao.getPost(title);

        if (post == null)
            System.out.println("Post not found!");
        else {
            System.out.print("Enter your comment: ");
            String commentContent = scanner.next().trim();

            Comment comment = new Comment(loggedInUser, post, commentContent);

            if (CommentDao.addComment(comment))
                System.out.println("Comment added!");
            else
                System.out.println("Failed to add comment");
        }
    }
}