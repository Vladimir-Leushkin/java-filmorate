package ru.yandex.practicum.javafilmorate.dao;

import ru.yandex.practicum.javafilmorate.model.Mpa;

import java.util.List;

public interface MpaStorage {

    List<Mpa> getMpaList();

    Mpa findMpaById(Integer id);
}
