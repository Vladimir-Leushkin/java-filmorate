package ru.yandex.practicum.javafilmorate.exeption;

public class ValidationException extends RuntimeException {
    public ValidationException(String e) {
        super(e);
    }
}
