package authproject.services;

import authproject.controllers.UserController;
import authproject.exceptions.DuplicatedEntryException;
import authproject.exceptions.InvalidDataInputException;
import authproject.exceptions.ResourceNotFoundException;
import authproject.models.User;
import authproject.repositories.UserRepository;
import authproject.validators.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.logging.Logger;

@Service
public class UserService {
  private Logger logger = Logger.getLogger(UserService.class.getName());
  private UserRepository repository;

  @Autowired
  public UserService(UserRepository repository) {
    this.repository = repository;
  }

  public User create(User user) {
    logger.info("Saving user: " + user);

    verifyUserFields(user);

    try {
      User createdUser = repository.save(user);

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

  public User update(Long id, User user) {
    logger.info("Updating user with id: " + id);

    verifyUserFields(user);

    User existingUser = repository.findById(id).orElseThrow(
        () -> new ResourceNotFoundException("No records found for this ID!")
    );

    existingUser.setUsername(user.getUsername());
    existingUser.setEmail(user.getEmail());
    existingUser.setPassword(user.getPassword());

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

  private void verifyUserFields(User user) {
    if (
        user == null ||
            user.getUsername() == null ||
            user.getPassword() == null ||
            user.getEmail() == null
    )
      throw new InvalidDataInputException("User object has null attributes!");
    if (!UserValidator.usernameIsValid(user.getUsername()))
      throw new InvalidDataInputException("Username is invalid!");
    if (!UserValidator.passwordIsValid(user.getPassword()))
      throw new InvalidDataInputException("Password is invalid!");
    if (!UserValidator.emailIsValid(user.getEmail()))
      throw new InvalidDataInputException("Email is invalid!");
  }
}
