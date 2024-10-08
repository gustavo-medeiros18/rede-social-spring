//package authproject.unittests.services;
//
//import authproject.exceptions.DuplicatedEntryException;
//import authproject.exceptions.InvalidDataInputException;
//import authproject.exceptions.ResourceNotFoundException;
//import authproject.models.User;
//import authproject.repositories.UserRepository;
//import authproject.services.UserService;
//import authproject.unittests.mocks.MockUser;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.TestInstance;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.when;
//
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//@ExtendWith(MockitoExtension.class)
//public class UserServiceTest {
//  MockUser input;
//
//  @InjectMocks
//  private UserService service;
//
//  @Mock
//  UserRepository repository;
//
//  @BeforeEach
//  void setUpMocks() throws Exception {
//    input = new MockUser();
//    MockitoAnnotations.openMocks(this);
//  }
//
//  @Test
//  void testCreate() {
//    User entity = input.mockEntity(1);
//    User persisted = entity;
//
//    when(repository.save(entity)).thenReturn(persisted);
//    User savedUser = service.create(entity);
//
//    assertNotNull(savedUser);
//    assertNotNull(savedUser.getId());
//    assertNotNull(savedUser.getLinks());
//    assertTrue(savedUser.toString().contains("links: [</user/1>;rel=\"self\"]"));
//    assertEquals("username_1", savedUser.getUsername());
//    assertEquals("emailtest1@test.com", savedUser.getEmail());
//    assertEquals("Password1!", savedUser.getPassword());
//    assertEquals(true, savedUser.getEnabled());
//    assertEquals("2024-01-01T00:00:00Z", savedUser.getCreatedAt().toString());
//    assertEquals("2024-01-01T00:00:00Z", savedUser.getUpdatedAt().toString());
//  }
//
//  @Test
//  void testCreateWithNullInput() {
//    Exception exception = assertThrows(InvalidDataInputException.class, () -> {
//      service.create(null);
//    });
//
//    String expectedMessage = "User object has null attributes!";
//    String actualMessage = exception.getMessage();
//
//    assertTrue(actualMessage.contains(expectedMessage));
//  }
//
//  @Test
//  void testCreateWithDuplicatedEntry() {
//    User entity = input.mockEntity(1);
//
//    when(repository.save(entity)).thenThrow(new RuntimeException("Duplicated entry"));
//
//    Exception exception = assertThrows(DuplicatedEntryException.class, () -> {
//      service.create(entity);
//    });
//
//    String expectedMessage = "Username or email already exists!";
//    String actualMessage = exception.getMessage();
//
//    assertTrue(actualMessage.contains(expectedMessage));
//  }
//
//  @Test
//  void testCreateWithInvalidUsername() {
//    User entity = input.mockEntity(1);
//    entity.setUsername("mynameiswaytoolongandshouldnotbeaccepted");
//
//    Exception exception = assertThrows(InvalidDataInputException.class, () -> {
//      service.create(entity);
//    });
//
//    String expectedMessage = "Username is invalid!";
//    String actualMessage = exception.getMessage();
//
//    assertTrue(actualMessage.contains(expectedMessage));
//  }
//
//  @Test
//  void testCreateWithInvalidPassword() {
//    User entity = input.mockEntity(1);
//    entity.setPassword("password");
//
//    Exception exception = assertThrows(InvalidDataInputException.class, () -> {
//      service.create(entity);
//    });
//
//    String expectedMessage = "Password is invalid!";
//    String actualMessage = exception.getMessage();
//
//    assertTrue(actualMessage.contains(expectedMessage));
//  }
//
//  @Test
//  void testCreateWithInvalidEmail() {
//    User entity = input.mockEntity(1);
//    entity.setEmail("invalidemail");
//
//    Exception exception = assertThrows(InvalidDataInputException.class, () -> {
//      service.create(entity);
//    });
//
//    String expectedMessage = "Email is invalid!";
//    String actualMessage = exception.getMessage();
//
//    assertTrue(actualMessage.contains(expectedMessage));
//  }
//
//  @Test
//  void testFindAll() {
//    List<User> list = input.mockEntityList();
//    when(repository.findAll()).thenReturn(list);
//
//    List<User> returnedUsers = service.findAll();
//
//    assertNotNull(returnedUsers);
//    assertEquals(15, returnedUsers.size());
//
//    User userOne = returnedUsers.get(1);
//    assertNotNull(userOne);
//    assertNotNull(userOne.getId());
//    assertNotNull(userOne.getLinks());
//    assertTrue(userOne.toString().contains("links: [</user/1>;rel=\"self\"]"));
//    assertEquals("username_1", userOne.getUsername());
//    assertEquals("emailtest1@test.com", userOne.getEmail());
//    assertEquals("Password1!", userOne.getPassword());
//    assertEquals(true, userOne.getEnabled());
//    assertEquals("2024-01-01T00:00:00Z", userOne.getCreatedAt().toString());
//    assertEquals("2024-01-01T00:00:00Z", userOne.getUpdatedAt().toString());
//
//    User useFour = returnedUsers.get(4);
//    assertNotNull(useFour);
//    assertNotNull(useFour.getId());
//    assertNotNull(useFour.getLinks());
//    assertTrue(useFour.toString().contains("links: [</user/4>;rel=\"self\"]"));
//    assertEquals("username_4", useFour.getUsername());
//    assertEquals("emailtest4@test.com", useFour.getEmail());
//    assertEquals("Password4!", useFour.getPassword());
//    assertEquals(true, useFour.getEnabled());
//    assertEquals("2024-01-01T00:00:00Z", useFour.getCreatedAt().toString());
//    assertEquals("2024-01-01T00:00:00Z", useFour.getUpdatedAt().toString());
//
//    User userSeven = returnedUsers.get(7);
//    assertNotNull(userSeven);
//    assertNotNull(userSeven.getId());
//    assertNotNull(userSeven.getLinks());
//    assertTrue(userSeven.toString().contains("links: [</user/7>;rel=\"self\"]"));
//    assertEquals("username_7", userSeven.getUsername());
//    assertEquals("emailtest7@test.com", userSeven.getEmail());
//    assertEquals("Password7!", userSeven.getPassword());
//    assertEquals(true, userSeven.getEnabled());
//    assertEquals("2024-01-01T00:00:00Z", userSeven.getCreatedAt().toString());
//    assertEquals("2024-01-01T00:00:00Z", userSeven.getUpdatedAt().toString());
//  }
//
//  @Test
//  void testFindSingle() {
//    User entity = input.mockEntity(1);
//
//    when(repository.findById(1L)).thenReturn(Optional.of(entity));
//    User returnedUser = service.findSingle(1L);
//
//    assertNotNull(returnedUser);
//    assertNotNull(returnedUser.getId());
//    assertNotNull(returnedUser.getLinks());
//
//    assertTrue(returnedUser.toString().contains("links: [</user/1>;rel=\"self\"]"));
//
//    assertEquals("username_1", returnedUser.getUsername());
//    assertEquals("emailtest1@test.com", returnedUser.getEmail());
//    assertEquals("Password1!", returnedUser.getPassword());
//    assertEquals(true, returnedUser.getEnabled());
//    assertEquals("2024-01-01T00:00:00Z", returnedUser.getCreatedAt().toString());
//    assertEquals("2024-01-01T00:00:00Z", returnedUser.getUpdatedAt().toString());
//  }
//
//  @Test
//  void testFindSingleWithUnknownUser() {
//    when(repository.findById(1L)).thenReturn(Optional.empty());
//
//    Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
//      service.findSingle(1L);
//    });
//
//    String expectedMessage = "No records found for this ID!";
//    String actualMessage = exception.getMessage();
//
//    assertTrue(actualMessage.contains(expectedMessage));
//  }
//
//  @Test
//  void testUpdate() {
//    User entity = input.mockEntity(1);
//    User persisted = entity;
//
//    when(repository.findById(1L)).thenReturn(Optional.of(entity));
//    when(repository.save(entity)).thenReturn(persisted);
//
//    User savedUser = service.update(1L, entity);
//    assertNotNull(savedUser);
//    assertNotNull(savedUser.getId());
//    assertNotNull(savedUser.getLinks());
//    assertTrue(savedUser.toString().contains("links: [</user/1>;rel=\"self\"]"));
//    assertEquals("username_1", savedUser.getUsername());
//    assertEquals("emailtest1@test.com", savedUser.getEmail());
//    assertEquals("Password1!", savedUser.getPassword());
//    assertEquals(true, savedUser.getEnabled());
//    assertEquals("2024-01-01T00:00:00Z", savedUser.getCreatedAt().toString());
//    assertEquals("2024-01-01T00:00:00Z", savedUser.getUpdatedAt().toString());
//  }
//
//  @Test
//  void testUpdateWithNullInput() {
//    Exception exception = assertThrows(InvalidDataInputException.class, () -> {
//      service.update(1L, null);
//    });
//
//    String expectedMessage = "User object has null attributes!";
//    String actualMessage = exception.getMessage();
//
//    assertTrue(actualMessage.contains(expectedMessage));
//  }
//
//  @Test
//  void testUpdateWithInvalidUsername() {
//    User entity = input.mockEntity(1);
//    entity.setUsername("mynameiswaytoolongandshouldnotbeaccepted");
//
//    Exception exception = assertThrows(InvalidDataInputException.class, () -> {
//      service.update(1L, entity);
//    });
//
//    String expectedMessage = "Username is invalid!";
//    String actualMessage = exception.getMessage();
//
//    assertTrue(actualMessage.contains(expectedMessage));
//  }
//
//  @Test
//  void testUpdateWithInvalidPassword() {
//    User entity = input.mockEntity(1);
//    entity.setPassword("password");
//
//    Exception exception = assertThrows(InvalidDataInputException.class, () -> {
//      service.update(1L, entity);
//    });
//
//    String expectedMessage = "Password is invalid!";
//    String actualMessage = exception.getMessage();
//
//    assertTrue(actualMessage.contains(expectedMessage));
//  }
//
//  @Test
//  void testUpdateWithInvalidEmail() {
//    User entity = input.mockEntity(1);
//    entity.setEmail("invalidemail");
//
//    Exception exception = assertThrows(InvalidDataInputException.class, () -> {
//      service.update(1L, entity);
//    });
//
//    String expectedMessage = "Email is invalid!";
//    String actualMessage = exception.getMessage();
//
//    assertTrue(actualMessage.contains(expectedMessage));
//  }
//
//  @Test
//  void testUpdateWithUnknownUser() {
//    User entity = input.mockEntity(1);
//
//    when(repository.findById(1L)).thenReturn(Optional.empty());
//
//    Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
//      service.update(1L, entity);
//    });
//
//    String expectedMessage = "No records found for this ID!";
//    String actualMessage = exception.getMessage();
//
//    assertTrue(actualMessage.contains(expectedMessage));
//  }
//
//  @Test
//  void testUpdateWithDuplicatedEntry() {
//    User entity = input.mockEntity(1);
//    User persisted = entity;
//
//    when(repository.findById(1L)).thenReturn(Optional.of(entity));
//    when(repository.save(entity)).thenThrow(new RuntimeException("Duplicated entry"));
//
//    Exception exception = assertThrows(DuplicatedEntryException.class, () -> {
//      service.update(1L, entity);
//    });
//
//    String expectedMessage = "Username or email already exists!";
//    String actualMessage = exception.getMessage();
//
//    assertTrue(actualMessage.contains(expectedMessage));
//  }
//
//  @Test
//  void testDelete() {
//    User entity = input.mockEntity(1);
//    when(repository.findById(1L)).thenReturn(Optional.of(entity));
//
//    service.delete(1L);
//  }
//
//  @Test
//  void testDeleteWithUnknownUser() {
//    when(repository.findById(1L)).thenReturn(Optional.empty());
//
//    Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
//      service.delete(1L);
//    });
//
//    String expectedMessage = "No records found for this ID!";
//    String actualMessage = exception.getMessage();
//
//    assertTrue(actualMessage.contains(expectedMessage));
//  }
//}
