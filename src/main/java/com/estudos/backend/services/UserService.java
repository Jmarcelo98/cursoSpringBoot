package com.estudos.backend.services;

import org.springframework.security.core.context.SecurityContextHolder;

import com.estudos.backend.security.UserSS;

public class UserService {

	public static UserSS autenticado() {
		try {
			return (UserSS) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		} catch (Exception e) {
			return null;
		}

	}

}
