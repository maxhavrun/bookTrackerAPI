package com.havrun.bookTrackerAPI.Services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private static final String SECRET_KEY = "7jbJc2h8X6owgnMsnNLj8LNtNLY8mrBZEHNDFdMXBW8Q6QH7ecc6czTU394grAX2RhCECCyCyrptZeY3jeXMuepa7ML2hD8qJDfaCCtChFdFDRd5WwG8JqHSKT5FhrruWbRYyC4yrJwae9NETdPWsM92T2aWnkwwADCa2qRE7b3fZ5r4xGPRR7r8NhuusNezwnc7DWZS2YtM7LEJxju7yzKtAHmX4NEUp6hw8JSPbH94cz56AT55wDd4jSxhdbM4HkQbteG8X5xtdowkLchKSrmDpuWyeH3P8G46ymNnooFhZCS6cXWkSUD96KMuTT7d65WS2HeYMfaWc6SLBtuqq6PQKtFNoYTAHJ7bGPEHekPahQugLHNjNDGdAfcYt2tw9HKkZhjhZR859HYtHdBcEwoG6eru9GfE3NgydKzJeNNksMMhZToSK8CcwFGBREr9Ndcfhu8hp5n9hbdy7DppHbQ3kEdqHaarEQzoNbbq5bQXunnUmfgRENEsNSzQFgap";


    SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET_KEY.getBytes(),
            SignatureAlgorithm.HS512.getJcaName());

    public String getUsernameFromJwt(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver ) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails
    ) {
        return Jwts.builder()
                .claims(extraClaims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // 24 hours
                .signWith(getSigningKey())
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = getUsernameFromJwt(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }


    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
