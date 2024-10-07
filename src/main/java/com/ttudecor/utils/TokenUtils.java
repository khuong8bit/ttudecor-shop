package com.ttudecor.utils;

import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class TokenUtils {
	static final long EXPIRATIONTIME = 69 * 24 * 60 * 60; // 69 days
    
    static final String SECRET = "cvbnjhn783hjib35io56pzeqjkfbhjb234j87mfgbd8293fgsjbvjsdbv8u9wbvwef";
    
    static final String TOKEN_PREFIX = "Bearer";
    
    static final String HEADER_STRING = "Authorization";
    
    public static String generateToken(String userId) {
    	String JWT = Jwts.builder()
    			.setSubject(userId)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATIONTIME))
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
    	
    	return JWT;
    }
    
    public static String validateToken(String token) {
    	if(token == null || token.isBlank()) return "TOKEN_EMPTY";
    	token = token.replace(TokenUtils.TOKEN_PREFIX, "");
    	Claims claims = Jwts.parser()
    			.setSigningKey(SECRET)
    			.parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
    			.getBody();
    	String userId = claims.getSubject();
    	Date expire = claims.getExpiration();
    	if(userId == null) return "USER_NOT_FOUND";
    	else if(expire.before(new Date())) return "TOKEN_EXPIRED";
    	else return "OK";
    }
    
    public static void setTokenResponse(HttpServletResponse response, String token) {
    	response.addHeader(HEADER_STRING, TOKEN_PREFIX + " " + token);
    }
}
