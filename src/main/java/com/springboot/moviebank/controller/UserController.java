package com.springboot.moviebank.controller;

import static com.springboot.moviebank.security.config.SecurityConstants.EXPIRATION_TIME;
import static com.springboot.moviebank.security.config.SecurityConstants.HEADER_STRING;
import static com.springboot.moviebank.security.config.SecurityConstants.SECRET;
import static com.springboot.moviebank.security.config.SecurityConstants.TOKEN_PREFIX;

import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.springboot.moviebank.dao.UserRepository;
import com.springboot.moviebank.domain.ApplicationUser;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@PostMapping("/sign-up")
	public void signUp(HttpServletResponse res, @RequestBody ApplicationUser user) {
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

		String token = JWT.create().withSubject(user.getUsername())
				.withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
				.sign(Algorithm.HMAC512(SECRET.getBytes()));
		res.addHeader(HEADER_STRING, TOKEN_PREFIX + token);

		userRepository.save(user);
	}

}
