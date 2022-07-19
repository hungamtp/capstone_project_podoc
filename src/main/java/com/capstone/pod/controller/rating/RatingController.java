package com.capstone.pod.controller.rating;

import com.capstone.pod.dto.rating.AddRatingDTO;
import com.capstone.pod.services.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("rating")
@RequiredArgsConstructor
public class RatingController {
    private final RatingService ratingService;

    @GetMapping("/{designId}")
    public ResponseEntity getAllRatingByDesignId(@PathVariable String designId, @RequestParam int page
        , @RequestParam int pageSize) {
        if (page < 0) {
            throw new IllegalArgumentException("PAGE_MUST_GREATER_THAN_0");
        }
        Pageable pageable = PageRequest.of(page, pageSize);
        return ResponseEntity.ok().body(ratingService.getAllRatingByDesignedProduct(designId, pageable));
    }

    @PutMapping()
    public ResponseEntity addRating(@RequestBody AddRatingDTO addRatingDTO) {
        return ResponseEntity.ok().body(ratingService.addComment(addRatingDTO));
    }
}
