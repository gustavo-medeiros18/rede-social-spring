package authproject.controllers;

import authproject.dtos.UserDto;
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
  public ResponseEntity<User> create(@RequestBody UserDto userDto) {
    return ResponseEntity.ok(service.create(userDto));
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
  public ResponseEntity<User> update(@PathVariable("id") Long id, @RequestBody UserDto userDto) {
    User updatedUser = service.update(id, userDto);

    if (updatedUser == null) return ResponseEntity.notFound().build();
    else return ResponseEntity.ok(updatedUser);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
    service.delete(id);
    return ResponseEntity.noContent().build();
  }
}
