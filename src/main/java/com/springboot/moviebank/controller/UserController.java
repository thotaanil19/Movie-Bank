package com.springboot.moviebank.controller;

import static com.springboot.moviebank.constants.SecurityConstants.AUTHORIZATION;
import static com.springboot.moviebank.constants.SecurityConstants.EXPIRATION_TIME;
import static com.springboot.moviebank.constants.SecurityConstants.SECRET;
import static com.springboot.moviebank.constants.SecurityConstants.TOKEN_PREFIX;

import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.springboot.moviebank.dao.UserRepository;
import com.springboot.moviebank.domain.AppUser;
import com.springboot.moviebank.exceptions.UserAlreadyExistsException;
import com.springboot.moviebank.util.JwtTokenUtil;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Autowired
    private AuthenticationManager authenticationManager;

	@PostMapping("/sign-up")
	public void signUp(HttpServletResponse res, @RequestBody AppUser user) {
		
		AppUser userInDB = userRepository.findByUsername(user.getUsername());
		
		if (userInDB != null) {
			throw new UserAlreadyExistsException(user.getUsername());
		}
		
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

		String token = JWT.create().withSubject(user.getUsername())
				.withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
				.sign(Algorithm.HMAC512(SECRET.getBytes()));
		res.addHeader(AUTHORIZATION, TOKEN_PREFIX + token);

		userRepository.save(user);
	}
	
	
	@PostMapping("/login")
	public ResponseEntity<String> log(HttpServletResponse res, @RequestBody AppUser user) {
		/*
		 * Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		 * String username = ((User) auth.getPrincipal()).getUsername(); String token =
		 * jwtTokenUtil.createJWT(username, username, username, EXPIRATION_TIME);
		 * 
		 * res.addHeader(AUTHORIZATION, TOKEN_PREFIX + token);
		 */
		
		Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                		user.getUsername(),
                        user.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtTokenUtil.createJWT(user.getUsername(), user.getUsername(), user.getUsername(), EXPIRATION_TIME);
        res.addHeader(AUTHORIZATION, TOKEN_PREFIX + jwt);
        return ResponseEntity.ok(jwt);
	}

}
