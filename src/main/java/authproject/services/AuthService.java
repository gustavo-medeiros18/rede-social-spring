package authproject.services;

import authproject.dtos.CredentialsDto;
import authproject.dtos.TokenDto;
import authproject.repositories.UserRepository;
import authproject.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class AuthService {
  private Logger logger = Logger.getLogger(UserService.class.getName());

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private JwtTokenProvider tokenProvider;

  @Autowired
  private UserRepository repository;

  @SuppressWarnings("rawtypes")
  public ResponseEntity signin(CredentialsDto data) {
    logger.info("Signing in user " + data.getUsername());

    try {
      var username = data.getUsername();
      var password = data.getPassword();

      authenticationManager
          .authenticate(new UsernamePasswordAuthenticationToken(username, password));

      var user = repository.findByUsername(username);
      var tokenResponse = new TokenDto();

      if (user != null) {
        tokenResponse = tokenProvider.createAccessToken(username, user.getRoles());
      } else {
        throw new UsernameNotFoundException("Username " + username + " not found");
      }

      return ResponseEntity.ok(tokenResponse);
    } catch (Exception e) {
      throw new BadCredentialsException("Invalid username/password supplied");
    }
  }

  @SuppressWarnings("rawtypes")
  public ResponseEntity refreshToken(String username, String refreshToken) {
    var user = repository.findByUsername(username);
    var tokenResponse = new TokenDto();

    if (user != null) {
      tokenResponse = tokenProvider.refreshToken(refreshToken);
    } else
      throw new UsernameNotFoundException("Username " + username + " not found");

    return ResponseEntity.ok(tokenResponse);

  }
}
