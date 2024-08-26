package authproject.controllers;

import authproject.models.User;
import authproject.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
  private UserService service;

  @Autowired
  public UserController(UserService service) {
    this.service = service;
  }

  @GetMapping()
  public ResponseEntity<List<User>> findAll() {
    return ResponseEntity.ok(service.findAll());
  }
}
