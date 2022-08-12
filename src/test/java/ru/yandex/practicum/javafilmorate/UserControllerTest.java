
package ru.yandex.practicum.javafilmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.javafilmorate.controller.UserController;
import ru.yandex.practicum.javafilmorate.exeption.ValidationException;
import ru.yandex.practicum.javafilmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserControllerTest {
    private final UserController userController;
    protected User user = new User();


    @BeforeEach
    void beforeEach() {
        user.setName("Name");
        user.setEmail("Email@email");
        user.setLogin("Login");
        user.setBirthday(LocalDate.of(1995, 10, 20));
    }

    @Test
    void expectExceptionEmptyEmailByCreateUser() {
        //Подготовка
        user.setEmail("");
        //Исполнение
        //Проверка
        ValidationException ex = assertThrows(ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        userController.createUser(user);
                    }
                });

        assertEquals("Адрес электронной почты не может быть пустым", ex.getMessage());
    }

    @Test
    void expectExceptionEmptyLoginByCreateUser() {
        //Подготовка
        user.setLogin("");
        //Исполнение
        //Проверка
        ValidationException ex = assertThrows(ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        userController.createUser(user);
                    }
                });

        assertEquals("Логин не может быть пустым", ex.getMessage());
    }

    @Test
    void expectExceptionBirthDayAfterNowByCreateUser() {
        //Подготовка
        user.setBirthday(LocalDate.of(2040, 12, 12));
        //Исполнение
        //Проверка
        ValidationException ex = assertThrows(ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        userController.createUser(user);
                    }
                });

        assertEquals("Дата рождения не может быть больше " + LocalDate.now(), ex.getMessage());
    }

    @Test
    void expectExceptionEmptyEmailByUpdateUser() {
        //Подготовка
        user.setEmail("");
        //Исполнение
        //Проверка
        ValidationException ex = assertThrows(ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        userController.updateUser(user);
                    }
                });

        assertEquals("Адрес электронной почты не может быть пустым", ex.getMessage());
    }

    @Test
    void expectExceptionEmptyLoginByUpdateUser() {
        //Подготовка
        user.setLogin("");
        //Исполнение
        //Проверка
        ValidationException ex = assertThrows(ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        userController.updateUser(user);
                    }
                });

        assertEquals("Логин не может быть пустым", ex.getMessage());
    }

    @Test
    void expectExceptionBirthDayAfterNowByUpdateUser() {
        //Подготовка
        user.setBirthday(LocalDate.of(2040, 12, 12));
        //Исполнение
        //Проверка
        ValidationException ex = assertThrows(ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        userController.updateUser(user);
                    }
                });

        assertEquals("Дата рождения не может быть больше " + LocalDate.now(), ex.getMessage());
    }

    @Test
    void expectAddNewUserWithId1() {
        //Подготовка
        //Исполнение
        userController.createUser(user);
        //Проверка
        assertEquals(1, userController.findAll().get(0).getId());
        assertEquals("Name", userController.findUserById(1).getName());
    }

    @Test
    void expectAddNewUserFriend() {
        //Подготовка
        user.setName("Friend");
        user.setLogin("Friend");
        user.setEmail("friend@mail");
        user.setBirthday(LocalDate.of(1984, 04, 05));
        userController.createUser(user);
        //Исполнение
        userController.addFriend(1, 2);
        //Проверка
        assertEquals(2, userController.findAll().get(1).getId());
        assertEquals(2, userController.findAllFriends(1).get(0).getId());
    }
}



