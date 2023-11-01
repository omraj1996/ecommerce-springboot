package com.example.service;

import java.util.List;

import com.example.exception.ProductException;
import com.example.model.Rating;
import com.example.model.User;
import com.example.request.RatingRequest;

public interface RatingService {
	
	public Rating createRating(RatingRequest req,User user)throws ProductException;
	public List<Rating> getProductsRating(Long productId);
}
