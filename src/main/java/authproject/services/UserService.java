package authproject.services;

import authproject.models.User;
import authproject.repositories.UserRepository;
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

  public List<User> findAll() {
    logger.info("Fetching all users");
    return repository.findAll();
  }
}
