package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film {
    private long id; // идентификатор
    @NotBlank(message = "Имя не может быть пустым")
    private String name; // название
    @Size(max = 200, message = "Описание не должно больше 200 символов")
    private String description; // описание
    @PastOrPresent
    private LocalDate releaseDate; // дата релиза
    @Positive(message = "Продолжительность фильма не может быть отрицательной")
    private int duration; // продолжительность фильма
    @JsonIgnore
    private Set<Long> likes = new HashSet<>();
}