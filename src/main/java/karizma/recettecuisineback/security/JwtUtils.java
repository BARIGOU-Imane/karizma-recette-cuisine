package karizma.recettecuisineback.security;


import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import karizma.recettecuisineback.beans.Utilisateur;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
    @Value("${karizma.app.jwtSecret}")
    private String jwtSecret;
    @Value("${karizma.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    public JwtUtils() {
    }

    public String generateJwtToken(Authentication authentication) {
        Utilisateur userPrincipal = (Utilisateur) authentication.getPrincipal();
        return Jwts.builder().setIssuedAt(new Date()).setExpiration(new Date((new Date()).getTime() + (long)this.jwtExpirationMs)).signWith(this.key(), SignatureAlgorithm.HS256).compact();
    }

    private Key key() {
        return Keys.hmacShaKeyFor((byte[]) Decoders.BASE64.decode(this.jwtSecret));
    }

    public String getUserNameFromJwtToken(String token) {
        return ((Claims)Jwts.parser().setSigningKey(this.key()).parseClaimsJws(token).getBody()).getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(this.key()).parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException var3) {
            logger.error("Invalide JWT token: {}", var3.getMessage());
        } catch (ExpiredJwtException var4) {
            logger.error("JWT token est expiré: {}", var4.getMessage());
        } catch (UnsupportedJwtException var5) {
            logger.error("JWT token est insupportable: {}", var5.getMessage());
        } catch (IllegalArgumentException var6) {
            logger.error("JWT claims string est vide: {}", var6.getMessage());
        }

        return false;
    }
}
