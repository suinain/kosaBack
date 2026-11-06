package com.mycompany.demo.database.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.mycompany.demo.database.dto.Pager;
import com.mycompany.demo.database.dto.ReviewDetailResponse;
import com.mycompany.demo.database.dto.ReviewDetailRequest;
import com.mycompany.demo.database.dto.ReviewListItemResponse;
import com.mycompany.demo.database.dto.ReviewWriteRequest;
import com.mycompany.demo.database.dto.ReviewWriteResponse;
import com.mycompany.demo.database.entity.Review;
import com.mycompany.demo.database.service.ReviewService;

import lombok.extern.slf4j.Slf4j;

@RestController 
@RequestMapping("/review")
@Slf4j 
public class ReviewController {
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/list")
    public Map<String, Object> listBoard(
        @RequestParam(value="pageNo", defaultValue = "1") int pageNo
    ) {
        Map<String, Object> where = null;
        int totalRows = reviewService.pagingTotalRows(where);
        Pager pager = new Pager(5, 5, totalRows, pageNo);
        List<Review> list = reviewService.listReview(pager, where);
        List<ReviewListItemResponse> listReviewResponse = new ArrayList<>();
        for(Review review: list) {
            ReviewListItemResponse riviewListItemResponse = new ReviewListItemResponse();
            riviewListItemResponse.setReviewId(review.getReviewId());
            riviewListItemResponse.setTitle(review.getTitle());
            riviewListItemResponse.setWriter(review.getWriter());
            riviewListItemResponse.setWritedAt(review.getWritedAt());
            riviewListItemResponse.setProductId(review.getProductId());
            riviewListItemResponse.setFileName(review.getFileName());
            listReviewResponse.add(riviewListItemResponse);
        } 
        Map<String, Object> map = Map.of(
            "pager", pager,
            "data", listReviewResponse
        );
        return map;
    }

    @PostMapping("/write")
    public ReviewWriteResponse write(ReviewWriteRequest request) throws IOException {
        // Review 엔티티로 변환
        Review review = new Review();
        review.setTitle(request.getTitle());
        review.setContent(request.getContent());
        review.setWriter(request.getWriter());
        review.setProductId(request.getProductId());

        if (request.getFile() != null && !request.getFile().isEmpty()) {
            review.setFileName(request.getFile().getOriginalFilename());
            review.setFileType(request.getFile().getContentType());
            review.setFileData(request.getFile().getBytes());
        }

        // 서비스로 엔티티를 전달해서 글쓰기 처리
        reviewService.write(review);

        ReviewWriteResponse  reviewWriteResponse = new ReviewWriteResponse();
        reviewWriteResponse.setState("success");

        return reviewWriteResponse;
    }

    @GetMapping("/image")
    public ResponseEntity<byte[]> image(@ModelAttribute ReviewDetailRequest request) {
        Review review = reviewService.getReview(request.getReviewId());
        if (review == null || review.getFileData() == null || review.getFileData().length == 0) {
            return ResponseEntity.notFound().build();
        }
        String fileType = review.getFileType();
        if (fileType == null || !List.of("image/jpeg", "image/png", "image/gif", "image/webp", "image/avif", "image/bmp").contains(fileType.toLowerCase(java.util.Locale.ROOT))) {
            return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).build();
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(fileType))
                .header("X-Content-Type-Options", "nosniff")
                .body(review.getFileData());
    }

    @GetMapping("/detail")
    public ReviewDetailResponse detail(@RequestParam("reviewId") long reviewId) {

        Review review = reviewService.getReview(reviewId);

        ReviewDetailResponse response = new ReviewDetailResponse();
        response.setReviewId(review.getReviewId());
        response.setTitle(review.getTitle());
        response.setContent(review.getContent());
        response.setWriter(review.getWriter());
        response.setProductId(review.getProductId());
        response.setFileName(review.getFileName());
        response.setWritedAt(review.getWritedAt());

        
        return response;

    }
    
}
