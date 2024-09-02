package authproject.unittests.mocks;

import authproject.models.User;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class MockUser {
  public User mockEntity() {
    return mockEntity(0);
  }

  public User mockEntity(Integer number) {
    User user = new User();

    user.setId(number.longValue());
    user.setUsername("username_" + number);
    user.setEmail("emailtest" + number + "@test.com");
    user.setPassword("Password" + number + "!");
    user.setEnabled(true);
    user.setCreatedAt(Instant.parse("2024-01-01T00:00:00Z"));
    user.setUpdatedAt(Instant.parse("2024-01-01T00:00:00Z"));

    return user;
  }

  public List<User> mockEntityList() {
    List<User> users = new ArrayList<User>();
    for (int i = 0; i < 15; i++)
      users.add(mockEntity(i));

    return users;
  }
}
