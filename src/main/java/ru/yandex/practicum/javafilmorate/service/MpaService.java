package ru.yandex.practicum.javafilmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import ru.yandex.practicum.javafilmorate.dao.MpaStorage;
import ru.yandex.practicum.javafilmorate.model.Mpa;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MpaService {
    private final MpaStorage mpaStorage;

    public List<Mpa> findAllMpa() {
        return mpaStorage.getMpaList();
    }

    public Mpa findMpaById(@PathVariable Integer id) {
        return mpaStorage.findMpaById(id);
    }

}
