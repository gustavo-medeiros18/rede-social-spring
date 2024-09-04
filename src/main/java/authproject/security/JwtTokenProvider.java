package authproject.security;

import authproject.dtos.TokenDto;
import authproject.exceptions.InvalidJwtAuthenticationException;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.security.core.Authentication;

import java.util.Base64;
import java.util.Date;
import java.util.List;

@Service
public class JwtTokenProvider {
  @Value("${security.jwt.token.secret-key: secret}")
  private String secretKey = "secret";

  @Value("${security.jwt.token.expire-length: 3600000}")
  private long validityInMilliseconds = 3600000;

  private UserDetailsService userDetailsService;
  Algorithm algorithm = null;

  @Autowired
  public JwtTokenProvider(UserDetailsService userDetailsService) {
    this.userDetailsService = userDetailsService;
  }

  @PostConstruct
  protected void init() {
    secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    algorithm = Algorithm.HMAC256(secretKey.getBytes());
  }

  public TokenDto createAccessToken(String username, List<String> roles) {
    Date now = new Date();
    Date validity = new Date(now.getTime() + validityInMilliseconds);

    var accessToken = getAccessToken(username, roles, now, validity);
    var refreshToken = getRefreshToken(username, roles, now);

    return new TokenDto(username, true, now, validity, accessToken, refreshToken);
  }

  public TokenDto refreshToken(String refreshToken) {
    if (refreshToken.contains("Bearer "))
      refreshToken = refreshToken.substring("Bearer ".length());

    JWTVerifier verifier = JWT.require(algorithm).build();
    DecodedJWT decodedJWT = verifier.verify(refreshToken);

    String username = decodedJWT.getSubject();
    List<String> roles = decodedJWT.getClaim("roles").asList(String.class);

    return createAccessToken(username, roles);
  }

  private String getAccessToken(String username, List<String> roles, Date now, Date vality) {
    String issueUrl = ServletUriComponentsBuilder
        .fromCurrentContextPath()
        .build()
        .toUriString();

    return JWT.create()
        .withClaim("roles", roles)
        .withIssuedAt(now)
        .withExpiresAt(vality)
        .withSubject(username)
        .withIssuer(issueUrl)
        .sign(algorithm)
        .strip();
  }

  private String getRefreshToken(String username, List<String> roles, Date now) {
    // The refresh token is valid for 3 hours.
    Date validityRefreshToken = new Date(now.getTime() + (validityInMilliseconds * 3));

    return JWT.create()
        .withClaim("roles", roles)
        .withIssuedAt(now)
        .withExpiresAt(validityRefreshToken)
        .withSubject(username)
        .sign(algorithm)
        .strip();
  }

  public Authentication getAuthentication(String token) {
    DecodedJWT decodedJWT = decodeToken(token);
    UserDetails userDetails = userDetailsService
        .loadUserByUsername(decodedJWT.getSubject());

    return new UsernamePasswordAuthenticationToken(
        userDetails,
        "",
        userDetails.getAuthorities()
    );
  }

  private DecodedJWT decodeToken(String token) {
    Algorithm alg = Algorithm.HMAC256(secretKey.getBytes());
    JWTVerifier verifier = JWT.require(alg).build();

    DecodedJWT decodedJWT = verifier.verify(token);
    return decodedJWT;
  }

  public String resolveToken(HttpServletRequest request) {
    String bearerToken = request.getHeader("Authorization");

    if (bearerToken != null && bearerToken.startsWith("Bearer "))
      return bearerToken.substring(7);

    return null;
  }

  public boolean validateToken(String token) throws InvalidJwtAuthenticationException {
    DecodedJWT decodedJWT = decodeToken(token);

    try {
      if (decodedJWT.getExpiresAt().before(new Date()))
        return false;
      return true;
    } catch (Exception e) {
      throw new InvalidJwtAuthenticationException("Expired or invalid token");
    }
  }
}
