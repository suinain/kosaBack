package com.mycompany.demo.database.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.mycompany.demo.database.dao.ReviewDao;
import com.mycompany.demo.database.dto.Pager;
import com.mycompany.demo.database.entity.Review;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class ReviewService {
    private final ReviewDao reviewDao;

    public Review getReview(long reviewId) {
        return reviewDao.selectById(reviewId);
    }

    public void write(Review review) {
        reviewDao.insert(review);
    }

    public int pagingTotalRows(Map<String, Object> where) {
        return reviewDao.selectCount(where);
    }

    public List<Review> listReview(Pager pager, Map<String, Object> where) {
        return reviewDao.selectPage(pager, where);
    }

}
