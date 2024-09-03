package authproject.unittests.mocks;

import authproject.models.Photo;
import authproject.models.User;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class MockPhoto {
  public Photo mockEntity() {
    return mockEntity(0);
  }

  public Photo mockEntity(Integer number) {
    Photo photo = new Photo();

    photo.setId(number.longValue());
    photo.setUrl(String.format("http://test.com/photo_%d.jpg", number));
    photo.setDescription("Description for photo " + number);
    photo.setCreatedAt(Instant.parse("2024-01-01T00:00:00Z"));
    photo.setUpdatedAt(Instant.parse("2024-01-01T00:00:00Z"));

    User user = new MockUser().mockEntity(number);
    photo.setUser(user);

    return photo;
  }

  public List<Photo> mockEntityList() {
    List<Photo> photos = new ArrayList<Photo>();
    for (int i = 0; i < 15; i++)
      photos.add(mockEntity(i));

    return photos;
  }
}
