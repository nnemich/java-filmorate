package ru.yandex.practicum.filmorate.storage.film;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.film.Genre;
import ru.yandex.practicum.filmorate.model.film.Mpa;
import ru.yandex.practicum.filmorate.storage.dao.genre.GenreDaoImpl;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.lang.String.format;
import static ru.yandex.practicum.filmorate.service.Service.DEPENDENCY_MESSAGE;

@Slf4j
@Component("FilmDbStorage")
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        log.debug("FilmDbStorage({}).", jdbcTemplate.getClass().getSimpleName());
        this.jdbcTemplate = jdbcTemplate;
        log.info(DEPENDENCY_MESSAGE, jdbcTemplate.getClass().getName());
    }

    @Override
    public Film add(@NonNull Film film) {
        log.debug("add({}).", film);
        jdbcTemplate.update(""
                        + "INSERT INTO films (name, description, release_date, duration_in_minutes, mpa_rating_id) "
                        + "VALUES(?, ?, ?, ?, ?)",
                film.getName(),
                film.getDescription(),
                Date.valueOf(film.getReleaseDate()),
                film.getDuration(),
                film.getMpa().getId());
        Film result = jdbcTemplate.queryForObject(format(""
                        + "SELECT film_id, name, description, release_date, duration_in_minutes, mpa_rating_id "
                        + "FROM films "
                        + "WHERE name='%s' "
                        + "AND description='%s' "
                        + "AND release_date='%s' "
                        + "AND duration_in_minutes=%d "
                        + "AND mpa_rating_id=%d",
                film.getName(),
                film.getDescription(),
                Date.valueOf(film.getReleaseDate()),
                film.getDuration(),
                film.getMpa().getId()), new FilmMapper());
        log.trace("В хранилище добавлен фильм: {}.", result);
        return result;
    }

    @Override
    public Film update(@NonNull Film film) {
        log.debug("update({}).", film);
        jdbcTemplate.update(""
                        + "UPDATE films "
                        + "SET name=?, description=?, release_date=?, duration_in_minutes=?, mpa_rating_id=?"
                        + "WHERE film_id=?",
                film.getName(),
                film.getDescription(),
                Date.valueOf(film.getReleaseDate()),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());
        Film result = get(film.getId());
        log.trace("Обновлён фильм: {}.", result);
        return result;
    }

    @Override
    public Film get(long filmID) {
        log.debug("get({}).", filmID);
        Film film = jdbcTemplate.queryForObject(format(""
                + "SELECT film_id, name, description, release_date, duration_in_minutes, mpa_rating_id FROM films "
                + "WHERE film_id=%d", filmID), new FilmMapper());
        log.trace("Возвращён фильм: {}", film);
        return film;
    }

    @Override
    public Collection<Film> getAll() {
        log.debug("getAll().");
        List<Film> films = jdbcTemplate.query(""
                + "SELECT film_id, name, description, release_date, duration_in_minutes, mpa_rating_id "
                + "FROM films", new FilmMapper());
        log.trace("Возвращены все фильмы: {}.", films);
        return films;
    }

    @Override
    public boolean contains(long filmID) {
        log.debug("contains({}).", filmID);
        try {
            get(filmID);
            log.trace("Найден фильм ID_{}.", filmID);
            return true;
        } catch (EmptyResultDataAccessException ex) {
            log.trace("Не найден фильм ID_{}.", filmID);
            return false;
        }
    }

    @Override
    public void addGenres(long filmID, Set<Genre> genres) {
        log.debug("add({}, {})", filmID, genres);
        for (Genre genre : genres) {
            jdbcTemplate.update(""
                    + "INSERT INTO film_genres (film_id, genre_id) "
                    + "VALUES (?, ?)", filmID, genre.getId());
            log.trace("Фильму ID_{} добавлен жанр ID_{}.", filmID, genre.getId());
        }
    }

    @Override
    public void updateGenres(long filmID, Set<Genre> genres) {
        log.debug("update({}, {})", filmID, genres);
        delete(filmID);
        addGenres(filmID, genres);
    }

    @Override
    public Set<Genre> getGenres(long filmID) {
        log.debug("getGenres({}).", filmID);
        Set<Genre> genres = new HashSet<>(jdbcTemplate.query(format(""
                + "SELECT f.genre_id, g.name "
                + "FROM film_genres AS f "
                + "LEFT OUTER JOIN genres AS g ON f.genre_id = g.genre_id "
                + "WHERE f.film_id=%d "
                + "ORDER BY g.genre_id", filmID), new GenreDaoImpl.GenreMapper()));
        log.trace("Возвращены все жанры для фильма ID_{}: {}.", filmID, genres);
        return genres;
    }

    /**
     * Метод удаляет все связи фильма с
     * жанрами.
     *
     * @param filmID идентификатор фильма.
     */
    private void delete(long filmID) {
        log.debug("delete({}).", filmID);
        jdbcTemplate.update(""
                + "DELETE "
                + "FROM film_genres "
                + "WHERE film_id=?", filmID);
        log.debug("Удалены все жанры у фильма {}.", filmID);
    }

    private static class FilmMapper implements RowMapper<Film> {
        @Override
        public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
            Mpa mpa = new Mpa();
            mpa.setId(rs.getInt("mpa_rating_id"));

            Film film = new Film();
            film.setId(rs.getLong("film_id"));
            film.setName(rs.getString("name"));
            film.setDescription(rs.getString("description"));
            film.setReleaseDate(rs.getDate("release_date").toLocalDate());
            film.setDuration(rs.getInt("duration_in_minutes"));
            film.setMpa(mpa);
            return film;
        }
    }
}