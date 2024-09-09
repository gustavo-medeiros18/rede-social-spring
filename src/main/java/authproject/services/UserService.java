package authproject.services;

import authproject.controllers.UserController;
import authproject.dtos.UserDto;
import authproject.exceptions.DuplicatedEntryException;
import authproject.exceptions.InvalidDataInputException;
import authproject.exceptions.ResourceNotFoundException;
import authproject.models.User;
import authproject.repositories.UserRepository;
import authproject.validators.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.stereotype.Service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Service
public class UserService implements UserDetailsService {
  private Logger logger = Logger.getLogger(UserService.class.getName());
  private UserRepository repository;

  @Autowired
  public UserService(UserRepository repository) {
    this.repository = repository;
  }

  public User create(UserDto userDto) {
    logger.info("Saving user...");

    verifyUserFields(userDto);
    userDto.setPassword(encodePassword(userDto.getPassword()));
    User user = new User(userDto);

    try {
      User createdUser = repository.save(user);

      createdUser.setPermissions(new ArrayList<>());
      createdUser.add(linkTo(methodOn(UserController.class).findSingle(createdUser.getId())).withSelfRel());
      return createdUser;
    } catch (Exception e) {
      throw new DuplicatedEntryException("Username or email already exists!");
    }
  }

  public List<User> findAll() {
    logger.info("Fetching all users");

    List<User> users = repository.findAll();

    for (User user : users)
      user.add(linkTo(methodOn(UserController.class).findSingle(user.getId())).withSelfRel());

    return users;
  }

  public User findSingle(Long id) {
    logger.info("Fetching user with id: " + id);

    User user = repository.findById(id).orElseThrow(() ->
        new ResourceNotFoundException("No records found for this ID!")
    );

    user.add(linkTo(methodOn(UserController.class).findSingle(id)).withSelfRel());
    return user;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    logger.info("Finding user by username: " + username);

    User user = repository.findByUsername(username);

    if (user != null)
      return user;
    else
      throw new UsernameNotFoundException("Username " + username + " not found");
  }

  public User update(Long id, UserDto userDto) {
    logger.info("Updating user with id: " + id);

    verifyUserFields(userDto);

    User existingUser = repository.findById(id).orElseThrow(
        () -> new ResourceNotFoundException("No records found for this ID!")
    );

    existingUser.setUsername(userDto.getUsername());
    existingUser.setEmail(userDto.getEmail());
    existingUser.setPassword(encodePassword(userDto.getPassword()));

    try {
      User updatedUser = repository.save(existingUser);

      updatedUser.add(linkTo(methodOn(UserController.class).findSingle(updatedUser.getId())).withSelfRel());
      return updatedUser;
    } catch (Exception e) {
      throw new DuplicatedEntryException("Username or email already exists!");
    }
  }

  public void delete(Long id) {
    logger.info("Deleting user with id: " + id);

    User existingUser = repository.findById(id).orElseThrow(
        () -> new ResourceNotFoundException("No records found for this ID!")
    );
    repository.delete(existingUser);
  }

  private void verifyUserFields(UserDto userDto) {
    if (
        userDto == null ||
            userDto.getUsername() == null ||
            userDto.getPassword() == null ||
            userDto.getEmail() == null
    )
      throw new InvalidDataInputException("User object has null attributes!");
    if (!UserValidator.usernameIsValid(userDto.getUsername()))
      throw new InvalidDataInputException("Username is invalid!");
    if (!UserValidator.passwordIsValid(userDto.getPassword()))
      throw new InvalidDataInputException("Password is invalid!");
    if (!UserValidator.emailIsValid(userDto.getEmail()))
      throw new InvalidDataInputException("Email is invalid!");
  }

  private String encodePassword(String password) {
    Pbkdf2PasswordEncoder pbkdf2Encoder = new Pbkdf2PasswordEncoder(
        "",
        8,
        185000,
        Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256
    );
    Map<String, PasswordEncoder> encoders = new HashMap<>();
    encoders.put("pbkdf2", pbkdf2Encoder);

    DelegatingPasswordEncoder passwordEncoder = new DelegatingPasswordEncoder("pbkdf2", encoders);
    passwordEncoder.setDefaultPasswordEncoderForMatches(pbkdf2Encoder);

    String encodedPassword = passwordEncoder.encode(password).replace("{pbkdf2}", "");
    return encodedPassword;
  }
}
