package authproject.controllers;

import authproject.dtos.CredentialsDto;
import authproject.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
  @Autowired
  AuthService authServices;

  @SuppressWarnings("rawtypes")
  @PostMapping(value = "/signin")
  public ResponseEntity signin(@RequestBody CredentialsDto data) {
    if (checkIfParamsIsNotNull(data))
      return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid credentials");

    var token = authServices.signin(data);
    if (token == null)
      return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid credentials");

    return token;
  }

  @SuppressWarnings("rawtypes")
  @PutMapping(value = "/refresh/{username}")
  public ResponseEntity refreshToken(
      @PathVariable("username") String username,
      @RequestHeader("Authorization") String refreshToken
  ) {
    if (checkIfParamsIsNotNull(username, refreshToken))
      return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid credentials");

    var token = authServices.refreshToken(username, refreshToken);
    if (token == null)
      return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid credentials");

    return token;
  }

  private static boolean checkIfParamsIsNotNull(String username, String refreshToken) {
    return refreshToken == null ||
        refreshToken.isBlank() ||
        username == null ||
        username.isBlank();
  }

  private static boolean checkIfParamsIsNotNull(CredentialsDto data) {
    return data == null ||
        data.getUsername() == null ||
        data.getUsername().isBlank() ||
        data.getPassword() == null ||
        data.getPassword().isBlank();
  }
}