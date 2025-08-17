import com.github.BlackThornLabs.dao.UserDao;
import com.github.BlackThornLabs.dao.UserDaoImpl;
import com.github.BlackThornLabs.model.User;
import com.github.BlackThornLabs.util.HibernateUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
class UserDaoImplTest {
    @SuppressWarnings("resource")
    @Container
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    private UserDao userDao;

    @BeforeAll
    static void beforeAll() {
        System.setProperty("hibernate.connection.url", postgres.getJdbcUrl());
        System.setProperty("hibernate.connection.username", postgres.getUsername());
        System.setProperty("hibernate.connection.password", postgres.getPassword());
    }

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl();
        try (var session = HibernateUtil.getSessionFactory().openSession()) {
            var transaction = session.beginTransaction();
            session.createMutationQuery("DELETE FROM User").executeUpdate();
            transaction.commit();
        }
    }

    @AfterAll
    static void afterAll() {
        HibernateUtil.shutdown();
    }

    @Test
    void shouldSaveAndFindUserById() {
        User user = new User("James Gosling", "java@rules.com", 70);

        userDao.save(user);
        Optional<User> foundUser = userDao.findById(user.getId());

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getName()).isEqualTo("James Gosling");
        assertThat(foundUser.get().getEmail()).isEqualTo("java@rules.com");
        assertThat(foundUser.get().getAge()).isEqualTo(70);
        assertThat(foundUser.get().getCreatedAt()).isBeforeOrEqualTo(LocalDateTime.now());
    }

    @Test
    void shouldReturnEmptyOptionalWhenUserNotFound() {
        Optional<User> foundUser = userDao.findById(999L);
        assertThat(foundUser).isEmpty();
    }

    @Test
    void shouldFindAllUsers() {
        userDao.save(new User("Ada Lovelace", "ada@forever.com", 36));
        userDao.save(new User("John Romero", "doom@guy.com", 57));

        List<User> users = userDao.findAll();
        assertThat(users).hasSize(2);
        assertThat(users).extracting(User::getName).containsExactly("Ada Lovelace", "John Romero");
    }

    @Test
    void shouldUpdateUser() {
        User user = new User("Alfonso John Romero", "doom@guy.com", 32);
        userDao.save(user);

        user.setName("John Romero");
        user.setEmail("free@guy.com");
        user.setAge(57);
        userDao.update(user);

        Optional<User> updatedUser = userDao.findById(user.getId());
        assertThat(updatedUser).isPresent();
        assertThat(updatedUser.get().getName()).isEqualTo("John Romero");
        assertThat(updatedUser.get().getEmail()).isEqualTo("free@guy.com");
        assertThat(updatedUser.get().getAge()).isEqualTo(57);
    }

    @Test
    void shouldDeleteUser() {
        User user = new User("To Delete", "delete@example.com", 50);
        userDao.save(user);
        assertThat(userDao.findById(user.getId())).isPresent();

        userDao.delete(user.getId());

        assertThat(userDao.findById(user.getId())).isEmpty();
    }
}
