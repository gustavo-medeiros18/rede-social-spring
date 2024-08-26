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
  private UserService service;

  @Autowired
  public UserController(UserService service) {
    this.service = service;
  }

  @PostMapping()
  public ResponseEntity<User> create(@RequestBody User user) {
    return ResponseEntity.ok(service.create(user));
  }

  @GetMapping()
  public ResponseEntity<List<User>> findAll() {
    return ResponseEntity.ok(service.findAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<User> findSingle(@PathVariable("id") Long id) {
    return ResponseEntity.ok(service.findSingle(id));
  }

  @PutMapping("/{id}")
  public ResponseEntity<User> update(@PathVariable("id") Long id, @RequestBody User user) {
    User updatedUser = service.update(id, user);

    if (updatedUser == null) return ResponseEntity.notFound().build();
    else return ResponseEntity.ok(updatedUser);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
    service.delete(id);
    return ResponseEntity.noContent().build();
  }
}
