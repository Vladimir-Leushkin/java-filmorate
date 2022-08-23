package ru.yandex.practicum.javafilmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.javafilmorate.model.Review;
import ru.yandex.practicum.javafilmorate.service.ReviewService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/reviews")
public class
ReviewController {
    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    public Review addNewReview(@Valid @RequestBody Review review) {
        return reviewService.addReview(review);
    }

    @PutMapping
    public Review changeReview(@RequestBody Review review) {
        return reviewService.changeReview(review);
    }

    @DeleteMapping("/{idReview}")
    public void deleteReview(@PathVariable("idReview") int idReview) {
        reviewService.deleteReview(idReview);
    }

    @GetMapping("/{idReview}")
    public Review findReviewById(@PathVariable("idReview") int idReview) {
        return reviewService.findReviewById(idReview);
    }

    @GetMapping
    public List<Review> getAllReviewByIdFilm(
            @RequestParam(value = "filmId", defaultValue = "-1", required = false) int filmId,
            @RequestParam(value = "count", defaultValue = "10", required = false) int count) {
        if (filmId == -1) {
            return reviewService.getAllReview();
        }
        return reviewService.getAllReviewByIdFilm(filmId, count);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLikeReview(@PathVariable("id") int id, @PathVariable("userId") int userId) {
        reviewService.addLikeReview(id, userId);
    }

    @PutMapping("/{id}/dislike/{userId}")
    public void addDislikeReview(@PathVariable("id") Integer id, @PathVariable("userId") Integer userId) {
        reviewService.addDislikeReview(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLikeReview(@PathVariable("id") int id, @PathVariable("userId") int userId) {
        reviewService.deleteLikeReview(id, userId);
    }

    @DeleteMapping("/{id}/dislike/{userId}")
    public void deleteDislikeReview(@PathVariable("id") int id, @PathVariable("userId") int userId) {
        reviewService.deleteLikeReview(id, userId);
    }
}
