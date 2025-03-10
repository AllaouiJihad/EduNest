package com.jihad.edunest.service.implimentations;

import com.jihad.edunest.domaine.entities.Member;
import com.jihad.edunest.domaine.enums.UserRole;
import com.jihad.edunest.exception.user.RooleNotFoundException;
import com.jihad.edunest.exception.user.UserNotFoundException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    private final String SECRET ;
    private final UserService userService;



    public JwtService(@Value("${jwt.secret.key}") String SECRET, UserService userService) {
        this.SECRET = SECRET;
        this.userService = userService;
    }

    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String generateToken(String email) {
        return generateToken(new HashMap<>(), email);
    }
    public String generateToken(Map<String, Object> claims, String username) {
        claims.put("role", extractRole(username));
        // put permissions
        claims.put("permissions", extractRole(username).getAuthorities());
        claims.put("username", username);
        userService.findByUsername(username).ifPresent(user -> claims.put("id", user.getId()));

        return Jwts
                .builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24   )) // 1 minute
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(String username) {
        return generateRefreshToken(new HashMap<>(), username);
    }

    public String generateRefreshToken(Map<String, Object> claims, String username) {
        return Jwts
                .builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7)) // 1 week
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims getClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isTokenValid(String token, String email) {
        final String extractedEmail = extractEmail(token); // En réalité, extrait l'email
        return (extractedEmail.equals(email)) && !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public UserRole extractRole(String username)  {
        Member user = userService.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        UserRole role = user.getRole();
        if (role == null) {
            throw new RooleNotFoundException("Role not found");
        }
        return role;
    }


    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public boolean validateToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
