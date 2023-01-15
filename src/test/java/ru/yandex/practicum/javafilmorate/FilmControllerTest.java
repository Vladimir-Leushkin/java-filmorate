
package ru.yandex.practicum.javafilmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.javafilmorate.controller.FilmController;
import ru.yandex.practicum.javafilmorate.controller.GenreController;
import ru.yandex.practicum.javafilmorate.controller.MpaController;
import ru.yandex.practicum.javafilmorate.controller.UserController;
import ru.yandex.practicum.javafilmorate.exeption.ValidationException;
import ru.yandex.practicum.javafilmorate.model.Film;
import ru.yandex.practicum.javafilmorate.model.Mpa;
import ru.yandex.practicum.javafilmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmControllerTest {
    private final FilmController filmController;
    private final GenreController genreController;
    private final UserController userController;
    private final MpaController mpaController;
    protected Film film = new Film();


    @BeforeEach
    void beforeEach() {
        film.setName("Name");
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(1995, 10, 20));
        film.setDuration(100);
        film.setMpa(new Mpa(1, "G"));
    }

    @Test
    void expectExceptionEmptyNameByCreateFilm() {
        //Подготовка
        film.setName("");
        //Исполнение
        //Проверка
        ValidationException ex = assertThrows(ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        filmController.createFilm(film);
                    }
                });

        assertEquals("Название фильма не может быть пустым", ex.getMessage());
    }

    @Test
    void expectExceptionDescriptionLonger200ByCreateFilm() {
        //Подготовка
        film.setDescription("Сотрудник страховой компании страдает хронической бессонницей и " +
                "отчаянно пытается вырваться из мучительно скучной жизни. Однажды в очередной " +
                "командировке он встречает некоего Тайлера Дёрдена — харизматического торговца " +
                "мылом с извращенной философией. Тайлер уверен, что самосовершенствование — " +
                "удел слабых, а единственное, ради чего стоит жить — саморазрушение.");
        //Исполнение
        //Проверка
        ValidationException ex = assertThrows(ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        filmController.createFilm(film);
                    }
                });

        assertEquals("Описание превышает 200 символов", ex.getMessage());
    }

    @Test
    void expectExceptionReleaseDateBefore1895_12_28ByCreateFilm() {
        //Подготовка
        film.setReleaseDate(LocalDate.of(1700, 12, 12));
        //Исполнение
        //Проверка
        ValidationException ex = assertThrows(ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        filmController.createFilm(film);
                    }
                });

        assertEquals("Дата релиза фильма не может быть до 28.12.1895", ex.getMessage());
    }

    @Test
    void expectExceptionDuration0ByCreateFilm() {
        //Подготовка
        film.setDuration(0);
        //Исполнение
        //Проверка
        ValidationException ex = assertThrows(ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        filmController.createFilm(film);
                    }
                });

        assertEquals("Продолжительность фильма не может быть нулевой", ex.getMessage());
    }

    @Test
    void expectExceptionEmptyNameByUpdateFilm() {
        //Подготовка
        film.setName("");
        //Исполнение
        //Проверка
        ValidationException ex = assertThrows(ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        filmController.updateFilm(film);
                    }
                });

        assertEquals("Название фильма не может быть пустым", ex.getMessage());
    }

    @Test
    void expectExceptionDescriptionLonger200ByUpdateFilm() {
        //Подготовка
        film.setDescription("Сотрудник страховой компании страдает хронической бессонницей и " +
                "отчаянно пытается вырваться из мучительно скучной жизни. Однажды в очередной " +
                "командировке он встречает некоего Тайлера Дёрдена — харизматического торговца " +
                "мылом с извращенной философией. Тайлер уверен, что самосовершенствование — " +
                "удел слабых, а единственное, ради чего стоит жить — саморазрушение.");
        //Исполнение
        //Проверка
        ValidationException ex = assertThrows(ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        filmController.updateFilm(film);
                    }
                });

        assertEquals("Описание превышает 200 символов", ex.getMessage());
    }

    @Test
    void expectExceptionReleaseDateBefore1895_12_28ByUpdateFilm() {
        //Подготовка
        film.setReleaseDate(LocalDate.of(1700, 12, 12));
        //Исполнение
        //Проверка
        ValidationException ex = assertThrows(ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        filmController.updateFilm(film);
                    }
                });

        assertEquals("Дата релиза фильма не может быть до 28.12.1895", ex.getMessage());
    }

    @Test
    void expectExceptionDuration0ByUpdateFilm() {
        //Подготовка
        film.setDuration(0);
        //Исполнение
        //Проверка
        ValidationException ex = assertThrows(ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        filmController.updateFilm(film);
                    }
                });

        assertEquals("Продолжительность фильма не может быть нулевой", ex.getMessage());
    }

    @Test
    void expectAddFilm() {
        //Подготовка
        //Исполнение
        filmController.createFilm(film);
        //Проверка
        assertEquals(1, filmController.findAll().get(0).getId());
        assertEquals("Name", filmController.findFilmById(1).getName());
    }

    @Test
    void expectAddFilmLike() {
        //Подготовка
        User user = new User();
        user.setName("Name");
        user.setEmail("Email@email");
        user.setLogin("Login");
        user.setBirthday(LocalDate.of(1995, 10, 20));
        userController.createUser(user);
        //Исполнение
        filmController.createFilm(film);
        filmController.addLikeFilmByUserId(1, 1, 6);
        //Проверка
        assertEquals(1, filmController.findAllByLikes(10).get(0).getId());
        assertEquals("Name", filmController.findAllByLikes(10).get(0).getName());

    }

    @Test
    void expectFindAllGenres() {
        //Подготовка
        //Исполнение
        //Проверка
        assertEquals(1, genreController.findAllGenres().get(0).getId());
        assertEquals("Комедия", genreController.findGenreById(1).getName());
        assertEquals(6, genreController.findAllGenres().size());
    }

    @Test
    void expectFindAllMpa() {
        //Подготовка
        //Исполнение
        //Проверка
        assertEquals(1, mpaController.findAllMpa().get(0).getId());
        assertEquals("G", mpaController.findMpaById(1).getName());
        assertEquals(5, mpaController.findAllMpa().size());
    }
}


