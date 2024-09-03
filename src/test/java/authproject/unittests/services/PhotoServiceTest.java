package authproject.unittests.services;

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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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
}
