package com.example.service;

import com.example.exception.ProductException;
import com.example.model.Cart;
import com.example.model.User;
import com.example.request.AddItemRequest;

public interface CartService {
	
	public Cart createCart(User user);
	public String addCartItem(Long userid,AddItemRequest req) throws ProductException;
	public Cart findUserCart(Long userId);

}
