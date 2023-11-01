package com.example.service;

import com.example.exception.UserException;
import com.example.model.User;

public interface UserService {
	
	public User findUserById(Long userId) throws UserException;
	
	public User findUserProfileByJwt(String jwt) throws UserException;
}
