package ru.yandex.practicum.filmorate.storage.dao.mpa;

import ru.yandex.practicum.filmorate.model.film.Mpa;

import java.util.Collection;

public interface MpaDao {
    /**
     * Метод возвращает рейтинг MPA по его идентификатору.
     *
     * @param mpaID идентификатор рейтинга MPA.
     * @return Рейтинг MPA.
     */
    Mpa get(int mpaID);

    /**
     * Метод возвращает все рейтинги MPA.
     *
     * @return Коллекция из всех рейтингов MPA.
     */
    Collection<Mpa> getAll();

    /**
     * Метод проверяет содержится ли в хранилище
     * элемент с указанным индентификатором.
     *
     * @param mpaID идентификатор элемента.
     * @return Логическое значение, true - если
     * элемент с указанным идентификатором содержится
     * в хранилище, и false - если нет.
     */
    boolean contains(int mpaID);
}