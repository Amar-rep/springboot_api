package com.codewithmosh.store.services;

import com.codewithmosh.store.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {
    @Value("${spring.jwt.secret}")
    private String secret;
    public String generateToken(User user)
    {
        return Jwts.builder().subject(user.getId().toString()).claim("email",user.getEmail()).claim("name",user.getName()).issuedAt(new Date()).expiration(new Date(System.currentTimeMillis()+1000*60*60*10))
                .signWith(Keys.hmacShaKeyFor(secret.getBytes())).compact();

    }
    public boolean validateToken(String token)
    {
        try {
            var claims = getClaims(token);
            return  claims.getExpiration().after(new Date());
        }catch (Exception e)
        {
                return false;
        }
    }

    private Claims getClaims(String token) {
        var claims=Jwts.parser().verifyWith(Keys.hmacShaKeyFor(secret.getBytes())).build()
                .parseSignedClaims(token).getPayload();
        return claims;
    }

    public Long getUserIdFromToken(String token)
   {
       return Long.valueOf(getClaims(token).getSubject());

   }
}
