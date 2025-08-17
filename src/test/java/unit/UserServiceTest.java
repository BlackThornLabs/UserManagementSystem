package unit;

import com.github.BlackThornLabs.dao.UserDao;
import com.github.BlackThornLabs.model.User;
import com.github.BlackThornLabs.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserDao userDao;

    @InjectMocks
    private UserService userService;

    @Test
    void createUser_WithValidData_ReturnsUser() {
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        User validTestUser = userService.createUser("NameIsOkay", "test@test.com", 30);

        verify(userDao).save(userCaptor.capture());
        assertThat(userCaptor.getValue()).isSameAs(validTestUser);
        assertThat(validTestUser.getName()).isEqualTo("NameIsOkay");
    }

    @Test
    void createUser_WithInvalidEmail_ThrowsException() {
        assertThatThrownBy(() -> userService.createUser("Little Vanya", "vanya-top.mail.ru", 10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Недопустимый email");
    }

    @Test
    void updateUser_WithNonExistingId_ThrowsException() {
        when(userDao.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.updateUser(999L, "Arya Stark", "thegirl@havenoemail.com", 25))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Пользователь не найден");
    }
}