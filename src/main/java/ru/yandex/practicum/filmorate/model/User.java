package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {
    private long id; // идентификатор
    @Email(message = "Некорректная почта")
    private String email; // электронная почта
    @NotBlank(message = "Логин не может быть пустым")
    @Pattern(regexp = "\\S+", message = "В логине не могут находиться пробелы")
    private String login; // логин пользователя
    private String name; // имя для отображения
    @PastOrPresent(message = "Дата рождения не может быть в будущем")
    private LocalDate birthday; // дата рождения
    @JsonIgnore
    private Set<Long> friends = new HashSet<>();
}