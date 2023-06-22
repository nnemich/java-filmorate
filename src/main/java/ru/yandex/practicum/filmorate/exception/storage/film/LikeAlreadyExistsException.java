package ru.yandex.practicum.filmorate.exception.storage.film;

public class LikeAlreadyExistsException extends RuntimeException {
    public static final String LIKE_ALREADY_EXISTS = "Пользователь userID_%d уже ставил лайк фильму filmID_%d";

    public LikeAlreadyExistsException(String message) {
        super(message);
    }
}