package com.example.service;

import java.util.List;

import com.example.exception.ProductException;
import com.example.model.Review;
import com.example.model.User;
import com.example.request.ReviewRequest;

public interface ReviewService {
	public Review createReview(ReviewRequest req,User user) throws ProductException;
	public List<Review> getAllReview(Long productId);
}
