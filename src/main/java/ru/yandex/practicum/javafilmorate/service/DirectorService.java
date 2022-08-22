package ru.yandex.practicum.javafilmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.javafilmorate.dao.DirectorStorage;
import ru.yandex.practicum.javafilmorate.exeption.NotFoundException;
import ru.yandex.practicum.javafilmorate.model.Director;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class DirectorService {
    private final DirectorStorage directorStorage;

    public List<Director> findAllDirectors() {
        return directorStorage.getDirectorList();
    }

    public Director findDirectorById(Integer id) {
        try {
            return directorStorage.findDirectorById(id);
        }  catch (Exception exception) {
            throw new NotFoundException("Director not found");
        }
    }

    public Director addDirector(Director director) {
        if (director.getName() == null || director.getName().isEmpty() || director.getName().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.resolve(500), "");
        }

        directorStorage.addDirector(director);
        return director;
    }

    public void deleteDirectorById(Integer id){
        Integer result = directorStorage.deleteDirectorById(id);
        if (result == 0) {
            throw new NotFoundException("Director not found");
        }
    }

    public Director updateDirector(Director director){
        Integer result = directorStorage.updateDirector(director);
        if (result == 0) {
            throw new NotFoundException("Director not found");
        }  else {
            return director;
        }
    }

}
