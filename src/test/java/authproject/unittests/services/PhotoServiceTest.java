package authproject.unittests.services;

import authproject.dtos.PhotoDto;
import authproject.exceptions.InvalidDataInputException;
import authproject.exceptions.ResourceNotFoundException;
import authproject.models.Photo;
import authproject.models.User;
import authproject.repositories.PhotoRepository;
import authproject.repositories.UserRepository;
import authproject.services.PhotoService;
import authproject.unittests.mocks.MockPhoto;
import authproject.unittests.mocks.MockUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class PhotoServiceTest {
  MockPhoto photoInput;
  MockUser userInput;

  @InjectMocks
  private PhotoService photoService;

  @Mock
  PhotoRepository photoRepository;

  @Mock
  UserRepository userRepository;

  @BeforeEach
  void setUpMocks() {
    photoInput = new MockPhoto();
    userInput = new MockUser();

    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testCreate() {
    PhotoDto dto = photoInput.mockDto(1);
    Photo entity = photoInput.mockEntity(1);
    Photo persisted = entity;
    User correspondingUser = userInput.mockEntity(1);

    when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(correspondingUser));
    doReturn(persisted).when(photoRepository).save(any(Photo.class));
    Photo savedPhoto = photoService.create(dto);

    assertNotNull(savedPhoto);
    assertNotNull(savedPhoto.getId());
    assertNotNull(savedPhoto.getLinks());
    assertTrue(savedPhoto.toString().contains("links: [</photo/1>;rel=\"self\"]"));
    assertEquals("http://test.com/photo_1.jpg", savedPhoto.getUrl());
    assertEquals("Description for photo 1", savedPhoto.getDescription());
    assertEquals("2024-01-01T00:00:00Z", savedPhoto.getCreatedAt().toString());
    assertEquals("2024-01-01T00:00:00Z", savedPhoto.getUpdatedAt().toString());
    assertEquals(correspondingUser, savedPhoto.getUser());
  }

  @Test
  void testCreateWithInvalidUrl() {
    PhotoDto dto = photoInput.mockDto(1);
    dto.setUrl("invalid-url");

    Exception exception = assertThrows(InvalidDataInputException.class, () -> {
      photoService.create(dto);
    });

    String expectedMessage = "URL is invalid!";
    String actualMessage = exception.getMessage();

    assertTrue(actualMessage.contains(expectedMessage));
  }

  @Test
  void testCreateWithInvalidDescription() {
    PhotoDto dto = photoInput.mockDto(1);
    dto.setDescription("");

    Exception exception = assertThrows(InvalidDataInputException.class, () -> {
      photoService.create(dto);
    });

    String expectedMessage = "Description is invalid!";
    String actualMessage = exception.getMessage();

    assertTrue(actualMessage.contains(expectedMessage));
  }

  @Test
  void testCreateWithNullInput() {
    PhotoDto dto = photoInput.mockDto();

    dto.setUrl(null);
    dto.setDescription(null);
    dto.setUserId(null);

    Exception exception = assertThrows(InvalidDataInputException.class, () -> {
      photoService.create(dto);
    });

    String expectedMessage = "Photo object has null attributes!";
    String actualMessage = exception.getMessage();

    assertTrue(actualMessage.contains(expectedMessage));
  }

  @Test
  void TestFindAll() {
    List<Photo> list = photoInput.mockEntityList();
    when(photoRepository.findAll()).thenReturn(list);

    List<Photo> returnedPhotos = photoService.findAll();

    assertNotNull(returnedPhotos);
    assertEquals(15, returnedPhotos.size());

    Photo photoOne = returnedPhotos.get(1);
    assertNotNull(photoOne);
    assertNotNull(photoOne.getId());
    assertNotNull(photoOne.getLinks());
    assertTrue(photoOne.toString().contains("links: [</photo/1>;rel=\"self\"]"));
    assertEquals("http://test.com/photo_1.jpg", photoOne.getUrl());
    assertEquals("Description for photo 1", photoOne.getDescription());
    assertEquals("2024-01-01T00:00:00Z", photoOne.getCreatedAt().toString());
    assertEquals("2024-01-01T00:00:00Z", photoOne.getUpdatedAt().toString());
    User photoOneUser = userInput.mockEntity(1);
    assertEquals(photoOneUser, photoOne.getUser());

    Photo photoFour = returnedPhotos.get(4);
    assertNotNull(photoFour);
    assertNotNull(photoFour.getId());
    assertNotNull(photoFour.getLinks());
    assertTrue(photoFour.toString().contains("links: [</photo/4>;rel=\"self\"]"));
    assertEquals("http://test.com/photo_4.jpg", photoFour.getUrl());
    assertEquals("Description for photo 4", photoFour.getDescription());
    assertEquals("2024-01-01T00:00:00Z", photoFour.getCreatedAt().toString());
    assertEquals("2024-01-01T00:00:00Z", photoFour.getUpdatedAt().toString());
    User photoFourUser = userInput.mockEntity(4);
    assertEquals(photoFourUser, photoFour.getUser());

    Photo photoSeven = returnedPhotos.get(7);
    assertNotNull(photoSeven);
    assertNotNull(photoSeven.getId());
    assertNotNull(photoSeven.getLinks());
    assertTrue(photoSeven.toString().contains("links: [</photo/7>;rel=\"self\"]"));
    assertEquals("http://test.com/photo_7.jpg", photoSeven.getUrl());
    assertEquals("Description for photo 7", photoSeven.getDescription());
    assertEquals("2024-01-01T00:00:00Z", photoSeven.getCreatedAt().toString());
    assertEquals("2024-01-01T00:00:00Z", photoSeven.getUpdatedAt().toString());
    User photoSevenUser = userInput.mockEntity(7);
    assertEquals(photoSevenUser, photoSeven.getUser());
  }

  @Test
  void testFindSingle() {
    Photo entity = photoInput.mockEntity(1);

    when(photoRepository.findById(1L)).thenReturn(Optional.of(entity));
    Photo returnedPhoto = photoService.findSingle(1L);

    assertNotNull(returnedPhoto);
    assertNotNull(returnedPhoto.getId());
    assertNotNull(returnedPhoto.getLinks());
    assertNotNull(returnedPhoto.getUser());

    assertTrue(returnedPhoto.toString().contains("links: [</photo/1>;rel=\"self\"]"));

    assertEquals("http://test.com/photo_1.jpg", returnedPhoto.getUrl());
    assertEquals("Description for photo 1", returnedPhoto.getDescription());
    assertEquals("2024-01-01T00:00:00Z", returnedPhoto.getCreatedAt().toString());
    assertEquals("2024-01-01T00:00:00Z", returnedPhoto.getUpdatedAt().toString());

    User mockedUser = userInput.mockEntity(1);
    assertEquals(mockedUser, returnedPhoto.getUser());
  }

  @Test
  void testFindSingleWithUnknownPhoto() {
    when(photoRepository.findById(1L)).thenReturn(Optional.empty());

    Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
      photoService.findSingle(1L);
    });

    String expectedMessage = "No records found for this ID!";
    String actualMessage = exception.getMessage();

    assertTrue(actualMessage.contains(expectedMessage));
  }

  @Test
  void testUpdate() {
    PhotoDto dto = photoInput.mockDto(1);
    Photo entity = photoInput.mockEntity(1);
    Photo persisted = entity;
    User correspondingUser = userInput.mockEntity(1);

    when(photoRepository.findById(1L)).thenReturn(Optional.of(entity));
    when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(correspondingUser));
    doReturn(persisted).when(photoRepository).save(any(Photo.class));

    Photo updatedPhoto = photoService.update(1L, dto);

    assertNotNull(updatedPhoto);
    assertNotNull(updatedPhoto.getId());
    assertNotNull(updatedPhoto.getLinks());
    assertNotNull(updatedPhoto.getUser());

    assertTrue(updatedPhoto.toString().contains("links: [</photo/1>;rel=\"self\"]"));

    assertEquals("http://test.com/photo_1.jpg", updatedPhoto.getUrl());
    assertEquals("Description for photo 1", updatedPhoto.getDescription());
    assertEquals("2024-01-01T00:00:00Z", updatedPhoto.getCreatedAt().toString());
    assertEquals("2024-01-01T00:00:00Z", updatedPhoto.getUpdatedAt().toString());

    assertEquals(correspondingUser, updatedPhoto.getUser());
  }

  @Test
  void testUpdateWithUnknownUser() {
    PhotoDto dto = photoInput.mockDto(1);
    dto.setUserId(2L);

    Photo entity = photoInput.mockEntity(1);

    when(photoRepository.findById(1L)).thenReturn(Optional.of(entity));
    when(userRepository.findById(2L)).thenReturn(Optional.empty());

    Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
      photoService.update(1L, dto);
    });

    String expectedMessage = "No records found for this User ID!";
    String actualMessage = exception.getMessage();

    assertTrue(actualMessage.contains(expectedMessage));
  }

  @Test
  void testUpdateWithUnknownPhoto() {
    PhotoDto dto = photoInput.mockDto(1);

    when(photoRepository.findById(1L)).thenReturn(Optional.empty());

    Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
      photoService.update(1L, dto);
    });

    String expectedMessage = "No records found for this Photo ID!";
    String actualMessage = exception.getMessage();

    assertTrue(actualMessage.contains(expectedMessage));
  }

  @Test
  void testDelete() {
    Photo entity = photoInput.mockEntity(1);
    when(photoRepository.findById(1L)).thenReturn(Optional.of(entity));

    photoService.delete(1L);
  }

  @Test
  void testDeleteWithUnknownPhoto() {
    when(photoRepository.findById(1L)).thenReturn(Optional.empty());

    Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
      photoService.delete(1L);
    });

    String expectedMessage = "No records found for this ID!";
    String actualMessage = exception.getMessage();

    assertTrue(actualMessage.contains(expectedMessage));
  }
}
