package com.sso.springboot.JWT;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtTokenUtil  {
	public static final String ISS = "SSO";
	public static final String BEARER = "Bearer ";
	
	public static final long JWT_TOKEN_VALIDITY = 20*60;
	public static final long JWT_TOKEN_REFRESH_VALIDITY = 24*60*60;

	public String getUserIdFromToken(String token) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException, UnsupportedEncodingException {
		return getClaimFromToken(token, Claims::getSubject);
	}

	public Date getIssuedAtDateFromToken(String token) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException, UnsupportedEncodingException {
		return getClaimFromToken(token, Claims::getIssuedAt);
	}

	public Date getExpirationDateFromToken(String token) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException, UnsupportedEncodingException {
		return getClaimFromToken(token, Claims::getExpiration);
	}

	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException, UnsupportedEncodingException {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}

	private Claims getAllClaimsFromToken(String token) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException, UnsupportedEncodingException {
		String secret = System.getenv("PUBLIC_TOKEN_K");
		if (secret==null)secret="TEST";

		return Jwts.parser().setSigningKey(secret.getBytes("UTF-8")).parseClaimsJws(token).getBody();
	}

	private Boolean isTokenExpired(String token) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException, UnsupportedEncodingException {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}

	private Boolean ignoreTokenExpiration(String token) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException, UnsupportedEncodingException {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date(System.currentTimeMillis() + JWT_TOKEN_REFRESH_VALIDITY*1000));
	}
	
	
	public String refreshToken(String userId, Map<String, Object> claims) throws UnsupportedEncodingException {
		return doRefrehToken(claims, userId);
	}
	public String generateToken(String userId, Map<String, Object> claims) throws UnsupportedEncodingException {
		return doGenerateToken(claims, userId);
	}
	private String doRefrehToken(Map<String, Object> claims, String userId) throws UnsupportedEncodingException {
		String secret =System.getenv("PUBLIC_TOKEN_K");
		if (secret==null)secret="TEST";
		
		return Jwts.builder().setClaims(claims).setSubject(userId).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_REFRESH_VALIDITY*1000)).signWith(SignatureAlgorithm.HS512, secret.getBytes("UTF-8")).compact();
	}

	private String doGenerateToken(Map<String, Object> claims, String userId) throws UnsupportedEncodingException {
		String secret =System.getenv("PUBLIC_TOKEN_K");
		if (secret==null)secret="TEST";
		
		return Jwts.builder().setClaims(claims).setSubject(userId).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY*1000)).signWith(SignatureAlgorithm.HS512, secret.getBytes("UTF-8")).compact();
	}

	public Boolean canTokenBeRefreshed(String token) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException, UnsupportedEncodingException {
		return (!isTokenExpired(token) || ignoreTokenExpiration(token));
	}

	public Boolean validateToken(String token, UserDetails userDetails) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException, UnsupportedEncodingException {
		final String username = getUserIdFromToken(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}
}