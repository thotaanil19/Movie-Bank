package com.springboot.moviebank.controller;

import static com.springboot.moviebank.constants.SecurityConstants.AUTHORIZATION;
import static com.springboot.moviebank.constants.SecurityConstants.EXPIRATION_TIME;
import static com.springboot.moviebank.constants.SecurityConstants.SECRET;
import static com.springboot.moviebank.constants.SecurityConstants.TOKEN_PREFIX;

import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.springboot.moviebank.dao.JwtTokenRepository;
import com.springboot.moviebank.dao.UserRepository;
import com.springboot.moviebank.domain.AppUser;
import com.springboot.moviebank.domain.JwtToken;
import com.springboot.moviebank.dto.LoginRequestDTO;
import com.springboot.moviebank.dto.LoginSuccessDTO;
import com.springboot.moviebank.dto.SignUpRequestDTO;
import com.springboot.moviebank.exceptions.UserAlreadyExistsException;
import com.springboot.moviebank.util.JwtTokenUtil;

@RestController
@RequestMapping("/user")
public class LoginController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenRepository jwtTokenRepository;

	@PostMapping("/register")
	public ResponseEntity<LoginSuccessDTO> register(HttpServletResponse res,
			@RequestBody SignUpRequestDTO signUpRequestDTO) {

		AppUser userInDB = userRepository.findByUsername(signUpRequestDTO.getUsername());

		if (userInDB != null) {
			throw new UserAlreadyExistsException(signUpRequestDTO.getUsername());
		}

		AppUser user = new AppUser();
		user.setUsername(signUpRequestDTO.getUsername());
		user.setPassword(bCryptPasswordEncoder.encode(signUpRequestDTO.getPassword()));
		user.setRole(signUpRequestDTO.getRole());
		user.setActive(true);

		String token = JWT.create().withSubject(user.getUsername())
				.withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
				.sign(Algorithm.HMAC512(SECRET.getBytes()));
		res.addHeader(AUTHORIZATION, TOKEN_PREFIX + token);

		userRepository.save(user);

		LoginSuccessDTO loginSuccessDTO = new LoginSuccessDTO();
		loginSuccessDTO.setToken(token);
		loginSuccessDTO.setName(user.getUsername());

		ResponseEntity<LoginSuccessDTO> response = new ResponseEntity<LoginSuccessDTO>(loginSuccessDTO, HttpStatus.OK);
		return response;// ResponseEntity.ok(jwt);

	}

	@PostMapping("/un-register")
	public ResponseEntity<String> un_register(HttpServletResponse res, @RequestHeader(AUTHORIZATION) String token) {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		AppUser user = userRepository.findByUsername(auth.getName());
		user.setActive(false);
		userRepository.save(user);

		ResponseEntity<String> response = new ResponseEntity<String>("Un-register is success", HttpStatus.OK);
		return response;

	}

	@PostMapping("/login")
	public ResponseEntity<LoginSuccessDTO> login(HttpServletResponse res, @RequestBody LoginRequestDTO user) {
		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);

		String jwt = jwtTokenUtil.createJWT(user.getUsername(), user.getUsername(), user.getUsername(),
				EXPIRATION_TIME);
		res.addHeader(AUTHORIZATION, TOKEN_PREFIX + jwt);

		LoginSuccessDTO loginSuccessDTO = new LoginSuccessDTO();
		loginSuccessDTO.setToken(jwt);
		loginSuccessDTO.setName(user.getUsername());

		// Save generated JWT token into DB
		jwtTokenRepository.save(new JwtToken(jwt));

		ResponseEntity<LoginSuccessDTO> response = new ResponseEntity<LoginSuccessDTO>(loginSuccessDTO, HttpStatus.OK);
		return response;// ResponseEntity.ok(jwt);
	}

	@PostMapping("/signout")
	@ResponseBody
	public ResponseEntity<String> logout(@RequestHeader(AUTHORIZATION) String token) {
		HttpHeaders headers = new HttpHeaders();
		boolean flag = jwtTokenRepository.existsById(token);
		if (flag) {
			jwtTokenRepository.delete(new JwtToken(token));
			return new ResponseEntity<String>("Logout success", headers, HttpStatus.OK);
		}

		return new ResponseEntity<String>("Invalid Token", headers, HttpStatus.UNAUTHORIZED);
	}

}
