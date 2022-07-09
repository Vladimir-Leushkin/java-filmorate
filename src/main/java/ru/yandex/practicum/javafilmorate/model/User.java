package ru.yandex.practicum.javafilmorate.model;

import lombok.Data;
import org.springframework.web.servlet.tags.form.SelectTag;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {
    private Integer id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
    private Set<Integer> friends = new HashSet<>();

    public void addFriend(Integer id) {
        friends.add(id);
    }
}
