package ru.yandex.practicum.filmorate.exception.storage.film;

public class FilmAlreadyExistsException extends RuntimeException {
    public static final String FILM_ALREADY_EXISTS = "Фильм filmID_%d уже был добавлен ранее";

    public FilmAlreadyExistsException(String message) {
        super(message);
    }
}