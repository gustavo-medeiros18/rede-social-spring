package authproject.controllers;

import authproject.models.User;
import authproject.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
  @Autowired
  private UserService userService;

  @PostMapping()
  public ResponseEntity<User> create(@RequestBody User user) {
    return ResponseEntity.ok(userService.create(user));
  }

  @GetMapping()
  public ResponseEntity<List<User>> findAll() {
    return ResponseEntity.ok(userService.findAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<User> findById(@PathVariable("id") Long id) {
    return ResponseEntity.ok(userService.findById(id));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteById(@PathVariable("id") Long id) {
    userService.deleteById(id);
    return ResponseEntity.noContent().build();
  }
}
