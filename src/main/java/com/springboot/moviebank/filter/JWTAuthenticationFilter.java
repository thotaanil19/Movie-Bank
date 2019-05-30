package com.springboot.moviebank.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.moviebank.domain.AppUser;
import com.springboot.moviebank.exceptions.SecurityException;
import com.springboot.moviebank.util.JwtTokenUtil;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private UserDetailsService userDetailsService;

	private BCryptPasswordEncoder bCryptPasswordEncoder;

	private JwtTokenUtil jwtTokenUtil;

	public JWTAuthenticationFilter(UserDetailsService userDetailsService,
			BCryptPasswordEncoder bCryptPasswordEncoder,
			JwtTokenUtil jwtTokenUtil) {
		this.userDetailsService = userDetailsService;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
		this.jwtTokenUtil = jwtTokenUtil;
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;

		String uri = request.getRequestURI();
		if ("/user/login".equals(uri)) {
			AppUser json = new ObjectMapper().readValue(req.getInputStream(), AppUser.class);
			String username = json.getUsername();
			String password = json.getPassword();
			UserDetails userDetails = userDetailsService.loadUserByUsername(username);
			if (null != userDetails && bCryptPasswordEncoder.matches(password, userDetails.getPassword())) {
				Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, "",
						userDetails.getAuthorities());
				SecurityContextHolder.getContext().setAuthentication(auth);
			}
		} else {
			String token = jwtTokenUtil.resolveToken((HttpServletRequest) req);
			if (token != null) {
				try {
					jwtTokenUtil.validateToken(token);
				} catch (Exception e) {
					response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token");
					throw new SecurityException("Invalid JWT token", HttpStatus.UNAUTHORIZED);
				}
				Authentication auth = token != null ? jwtTokenUtil.getAuthentication(token) : null;
				SecurityContextHolder.getContext().setAuthentication(auth);
			}
		}
		filterChain.doFilter(req, res);
	}

}