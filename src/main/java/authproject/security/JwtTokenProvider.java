package authproject.security;

import com.auth0.jwt.algorithms.Algorithm;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Base64;

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
}
