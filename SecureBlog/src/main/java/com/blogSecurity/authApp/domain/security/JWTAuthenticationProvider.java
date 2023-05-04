package com.blogSecurity.authApp.domain.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Component
public class JWTAuthenticationProvider {
    private static final String jwtSecret = "2A462D4A614E645267556B58703272357538782F413F4428472B4B6250655368";
    private static final int jwtExpirationDateInMs = 604800000;

    public String generateToken(Authentication authentication){
        String username = authentication.getName();

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+ jwtExpirationDateInMs))
                .signWith(getSignatureKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    private Key getSignatureKey(){
        byte [] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Claims extractCLaims(String jwtToken){
        return Jwts.parserBuilder()
                .setSigningKey(getSignatureKey())
                .build()
                .parseClaimsJws(jwtToken)
                .getBody();
    }

    public <T> T extractClaim(String jwtToken, Function<Claims, T> claimsResolver){
        final Claims claims = extractCLaims(jwtToken);
        return claimsResolver.apply(claims);
    }

    private Date extractExpirationDate(String jwtToken){
        return extractClaim(jwtToken, Claims::getExpiration);
    }
    public String extractUsername(String jwtToken){
        return extractClaim(jwtToken, Claims::getSubject);
    }
    private boolean isTokenExpired(String jwtToken){
        return extractExpirationDate(jwtToken).before(new Date());
    }

    public boolean isTokenValid(String jwtToken, UserDetails userDetails){
        String username = extractUsername(jwtToken);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(jwtToken));
    }
}
