package authproject.unittests.services;

import authproject.models.User;
import authproject.repositories.UserRepository;
import authproject.services.UserService;
import authproject.unittests.mocks.MockUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
  MockUser input;

  @InjectMocks
  private UserService service;

  @Mock
  UserRepository repository;

  @BeforeEach
  void setUpMocks() throws Exception {
    input = new MockUser();
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testFindSingle() {
    User entity = input.mockEntity(1);
    entity.setId(1L);

    when(repository.findById(1L)).thenReturn(Optional.of(entity));
    User returnedUser = service.findSingle(1L);

    assertNotNull(returnedUser);
    assertNotNull(returnedUser.getId());
    assertNotNull(returnedUser.getLinks());

    assertTrue(returnedUser.toString().contains("links: [</user/1>;rel=\"self\"]"));

    assertEquals("usernametest1", returnedUser.getUsername());
    assertEquals("emailtest1@test.com", returnedUser.getEmail());
    assertEquals("passwordtest1", returnedUser.getPassword());
    assertEquals(true, returnedUser.getEnabled());
    assertEquals("2024-01-01T00:00:00Z", returnedUser.getCreatedAt().toString());
    assertEquals("2024-01-01T00:00:00Z", returnedUser.getUpdatedAt().toString());
  }
}
