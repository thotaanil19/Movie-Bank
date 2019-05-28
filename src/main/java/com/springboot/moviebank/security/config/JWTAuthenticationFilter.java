package com.springboot.moviebank.security.config;

import static com.springboot.moviebank.security.config.SecurityConstants.EXPIRATION_TIME;
import static com.springboot.moviebank.security.config.SecurityConstants.HEADER_STRING;
import static com.springboot.moviebank.security.config.SecurityConstants.SECRET;
import static com.springboot.moviebank.security.config.SecurityConstants.TOKEN_PREFIX;

import java.io.IOException;
import java.security.Key;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.GenericFilterBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.moviebank.domain.ApplicationUser;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JWTAuthenticationFilter extends  UsernamePasswordAuthenticationFilter   /*  OncePerRequestFilter */ {

	private List list = Arrays.asList("/sign-up", "/login");

	private AuthenticationManager authenticationManager;
	
	private UserDetailsService userDetailsService;
	
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	

	public JWTAuthenticationFilter(AuthenticationManager authenticationManager, UserDetailsService userDetailsService,
			BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.authenticationManager = authenticationManager;
		this.userDetailsService = userDetailsService;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

	public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
			throws AuthenticationException {
		try {
			
			String token = req.getHeader(HEADER_STRING);
			if (token != null) {
				Claims i = Jwts.parser().setSigningKey(SECRET.getBytes()).parseClaimsJws(token).getBody();
				String subject = (String)i.get("sub");
				UserDetails u = userDetailsService.loadUserByUsername(subject);
								
				return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(u.getUsername(),
						u.getPassword(), new ArrayList<>()));
			} else {
			
			ApplicationUser creds = new ObjectMapper().readValue(req.getInputStream(), ApplicationUser.class);

			return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(creds.getUsername(),
					creds.getPassword(), new ArrayList<>()));
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain,
			Authentication auth) throws IOException, ServletException {

		String username = ((User) auth.getPrincipal()).getUsername();
		String token = createJWT(username, username, username, EXPIRATION_TIME);

		res.addHeader(HEADER_STRING, TOKEN_PREFIX + token);
	}

	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String uri = request.getRequestURI();
		
		if ("/login".contentEquals(uri)) {
			attemptAuthentication(request, response);
		}

		if (!list.contains(uri)) {
			filterChain.doFilter(request, response);


			String token = request.getHeader(HEADER_STRING);

			Jwts.parser().setSigningKey(SECRET.getBytes()).parseClaimsJws(token).getBody();

			System.out.println("Hi");
		}
		filterChain.doFilter(request, response);

	}
	
	private void validateToken (String token) {
		Jwts.parser().setSigningKey(SECRET.getBytes()).parseClaimsJws(token).getBody();
	}

	private String createJWT(String id, String issuer, String subject, long ttlMillis) {

		// The JWT signature algorithm we will be using to sign the token
		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

		long nowMillis = System.currentTimeMillis();
		Date now = new Date(nowMillis);

		// We will sign our JWT with our ApiKey secret
		byte[] apiKeySecretBytes = SECRET.getBytes();
		Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

		// Let's set the JWT Claims
		JwtBuilder builder = Jwts.builder().setId(id).setIssuedAt(now).setSubject(subject).signWith(signatureAlgorithm, signingKey);

		// if it has been specified, let's add the expiration
		if (ttlMillis > 0) {
			long expMillis = nowMillis + ttlMillis;
			Date exp = new Date(expMillis);
			builder.setExpiration(exp);
		}

		// Builds the JWT and serializes it to a compact, URL-safe string
		return builder.compact();
	}

	public void doFilter2(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		 HttpServletRequest request = (HttpServletRequest) req;
	        HttpServletResponse response = (HttpServletResponse) res;
	        String token = request.getHeader(HEADER_STRING);
	        if (token != null) {
	            try {
	                validateToken(token) ;
	            } catch (JwtException | IllegalArgumentException e) {
	                response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"Invalid JWT token");
	                throw new CustomException("Invalid JWT token",HttpStatus.UNAUTHORIZED);
	            }
	            Authentication auth = token != null ? getAuthentication(token) : null;
	            //setting auth in the context.
	            SecurityContextHolder.getContext().setAuthentication(auth);
	        }
	        chain.doFilter(req, res);
		
	}
	
	public Authentication getAuthentication(String token) {
        //using data base: uncomment when you want to fetch data from data base
        UserDetails userDetails = userDetailsService.loadUserByUsername(getUsername(token));
        //from token take user value. comment below line for changing it taking from data base
        //UserDetails userDetails = getUserDetails(token);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }
	
	public String getUsername(String token) {
        return Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody().getSubject();
    }

}