package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.model.Cart;
import com.example.model.CartItem;
import com.example.model.Product;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
	@Query("SELECT ci FROM CartItem ci Where ci.cart=:cart And ci.product=:product And ci.size=:size And ci.userId=:userId")
	public CartItem isCartItemExist(@Param("cart")Cart cart,@Param("product") Product product,@Param("size")String size,
			@Param("userId")Long userId);
}
