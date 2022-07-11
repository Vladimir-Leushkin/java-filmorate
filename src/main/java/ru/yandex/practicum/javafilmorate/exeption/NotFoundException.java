package ru.yandex.practicum.javafilmorate.exeption;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String e) {
        super(e);
    }
}
