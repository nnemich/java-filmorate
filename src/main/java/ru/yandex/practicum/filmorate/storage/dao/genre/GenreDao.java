package ru.yandex.practicum.filmorate.storage.dao.genre;

import ru.yandex.practicum.filmorate.model.film.Genre;

import java.util.Collection;

public interface GenreDao {
    /**
     * Меьтод возвращает жанр из хранилища
     * по его идентификатору.
     *
     * @param genreID идентификатор жанра.
     * @return жанр, принадлежащий
     * идентификатору.
     */
    Genre get(int genreID);

    /**
     * Метод возвращает все жанры из хранилища.
     *
     * @return коллекция из всех жанров в
     * хранилище.
     */
    Collection<Genre> getAll();

    /**
     * Метод проверяет содержится ли в хранилище
     * указанный жанр.
     *
     * @param genreID идентификатор жанра.
     * @return Логическое значение, true - если
     * элемент с указанным идентификатором содержится
     * в хранилище, и false - если нет.
     */
    boolean contains(int genreID);
}