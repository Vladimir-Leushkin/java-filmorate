package ru.yandex.practicum.javafilmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import ru.yandex.practicum.javafilmorate.controller.FilmController;
import ru.yandex.practicum.javafilmorate.exeption.ValidationException;
import ru.yandex.practicum.javafilmorate.model.Film;
import ru.yandex.practicum.javafilmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FilmControllerTest extends FilmController {

    protected Film film = new Film();

    @BeforeEach
    void beforeEach() {
        film.setName("Name");
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(1995, 10, 20));
        film.setDuration(100);
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
                        createFilm(film);
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
                        createFilm(film);
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
                        createFilm(film);
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
                        createFilm(film);
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
                        updateFilm(film);
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
                        updateFilm(film);
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
                        updateFilm(film);
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
                        updateFilm(film);
                    }
                });

        assertEquals("Продолжительность фильма не может быть нулевой", ex.getMessage());
    }

    @Test
    void expectCreateFilmByUpdateFilm() {
        //Подготовка
        //Исполнение
        //Проверка
        assertEquals(film, createFilm(film));
        assertEquals(1, getFilms().size());
    }

    @Test
    void expectCreateFilmByGetFilm() {
        //Подготовка
        //Исполнение
        createFilm(film);
        final List<Film> usersList = new ArrayList<>(getFilms().values());
        //Проверка
        assertEquals(1, getFilms().size());
        assertEquals(usersList, findAll());
    }
}
