package authproject.services;

import authproject.exceptions.DuplicatedEntryException;
import authproject.exceptions.InvalidDataInputException;
import authproject.exceptions.ResourceNotFoundException;
import authproject.models.User;
import authproject.repositories.UserRepository;
import authproject.validators.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
      return repository.save(user);
    } catch (Exception e) {
      throw new DuplicatedEntryException("Username or email already exists!");
    }
  }

  public List<User> findAll() {
    logger.info("Fetching all users");
    return repository.findAll();
  }

  public User findSingle(Long id) {
    logger.info("Fetching user with id: " + id);
    return repository.findById(id).orElseThrow(() ->
        new ResourceNotFoundException("No records found for this ID!")
    );
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
      return repository.save(existingUser);
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
