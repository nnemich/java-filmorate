package ru.yandex.practicum.filmorate.exception.storage.user;

public class UserAlreadyExistsException extends RuntimeException {
    public static final String USER_ALREADY_EXISTS = "Пользователь userID_%d уже был добавлен ранее";

    public UserAlreadyExistsException(String message) {
        super(message);
    }
}