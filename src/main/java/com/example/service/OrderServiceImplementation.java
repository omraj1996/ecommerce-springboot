package com.example.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.exception.OrderException;
import com.example.model.Address;
import com.example.model.Cart;
import com.example.model.CartItem;
import com.example.model.Order;
import com.example.model.OrderItem;
import com.example.model.User;
import com.example.repository.AddressRepository;
import com.example.repository.CartRepository;
import com.example.repository.OrderItemRepository;
import com.example.repository.OrderRepository;
import com.example.repository.UserRepository;

@Service
public class OrderServiceImplementation implements OrderService {
	private OrderRepository orderRepository;
	private CartService cartService;
	private AddressRepository addressRepository;
	private UserRepository userRepository;
	private OrderItemService orderItemService;
	private OrderItemRepository orderItemRepository;
	private CartRepository cartRepository;
	private ProductService productService;

	public OrderServiceImplementation(OrderRepository orderRepository, CartRepository cartRepository,
			CartService cartService, ProductService productService, AddressRepository addressRepository,
			UserRepository userRepository, OrderItemService orderItemService, OrderItemRepository orderItemRepository) {
		this.orderRepository=orderRepository;
		this.cartRepository=cartRepository;
		this.cartService = cartService;
		this.productService = productService;
		this.addressRepository=addressRepository;
		this.userRepository=userRepository;
		this.orderItemService=orderItemService;
		this.orderItemRepository=orderItemRepository;
	}

	@Override
	public Order createOrder(User user, Address shippAddress) {
		shippAddress.setUser(user);
		Address address=addressRepository.save(shippAddress);
		user.getAddress().add(address);
		userRepository.save(user);
		
		Cart cart=cartService.findUserCart(user.getId());
		List<OrderItem> orderItems=new ArrayList<>();
		
		for(CartItem item:cart.getCartItems()) {
			OrderItem orderItem= new OrderItem();
			
			orderItem.setPrice(item.getPrice());
			orderItem.setProduct(item.getProduct());
			orderItem.setQunatity(item.getQuantity());
			orderItem.setSize(item.getSize());
			orderItem.setUserId(item.getUserId());
			orderItem.setDiscountedPrice(item.getDiscountedPrice());
			
			OrderItem createdOrderItem=orderItemRepository.save(orderItem);
			
			orderItems.add(createdOrderItem);
		}
		
		Order createdOrder= new Order();
		createdOrder.setUser(user);
		createdOrder.setOrderItems(orderItems);
		createdOrder.setTotalPrice(cart.getTotalPrice());
		createdOrder.setTotalDiscountedPrice(cart.getTotalDiscountedPrice());
		createdOrder.setDiscount(cart.getDiscount());
		createdOrder.setTotalItem(cart.getTotalItem());
		createdOrder.setShippingAddress(address);
		createdOrder.setOrderDate(LocalDateTime.now());
		createdOrder.setOrderStatus("PENDING");
		createdOrder.getPaymentDetails().setStatus("PENDING");
		createdOrder.setCreatedAt(LocalDateTime.now());
		
		Order savedOrder=orderRepository.save(createdOrder);
		
		for(OrderItem item:orderItems) {
			item.setOrder(savedOrder);
			orderItemRepository.save(item);
		}
		
		return savedOrder;
	}

	@Override
	public Order findOrderById(Long orderId) throws OrderException {
		Optional<Order> opt=orderRepository.findById(orderId);
		
		if(opt.isPresent()) {
			return opt.get();
		}
		throw new OrderException("Order does not exist with id: "+orderId);
	}

	@Override
	public List<Order> usersOrderHistory(Long userId) {
		List<Order> orders= orderRepository.getUsersOrders(userId);
		return orders;
	}

	@Override
	public Order placeOrder(Long orderId) throws OrderException {
		Order order=findOrderById(orderId);
		order.setOrderStatus("PLACED");
		order.getPaymentDetails().setStatus("COMPLETED");
		return order ;
	}

	@Override
	public Order confirmedOrder(Long orderId) throws OrderException {
		Order order=findOrderById(orderId);
		order.setOrderStatus("CONFIRMED");
		return orderRepository.save(order);
	}

	@Override
	public Order shippedOrder(Long orderId) throws OrderException {
		Order order=findOrderById(orderId);
		order.setOrderStatus("SHIPPED");
		return orderRepository.save(order);
	}

	@Override
	public Order deliveredOrder(Long orderId) throws OrderException {
		Order order=findOrderById(orderId);
		order.setOrderStatus("DELIVERED");
		return orderRepository.save(order);
	}

	@Override
	public Order canceledOrder(Long orderId) throws OrderException {
		Order order=findOrderById(orderId);
		order.setOrderStatus("CANCELLED");
		return orderRepository.save(order);
	}

	@Override
	public List<Order> getAllOrders() {
		return orderRepository.findAll();
	}

	@Override
	public void deleteOrder(Long orderId) throws OrderException {
		Order order=findOrderById(orderId);
		
		orderRepository.deleteById(orderId);

	}

}
