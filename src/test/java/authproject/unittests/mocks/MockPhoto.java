package authproject.unittests.mocks;

import authproject.models.Photo;
import authproject.models.User;

import java.time.Instant;

public class MockPhoto {
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
}
