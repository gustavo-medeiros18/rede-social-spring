package authproject.services;

import authproject.dtos.PhotoDto;
import authproject.models.Photo;
import authproject.models.User;
import authproject.repositories.PhotoRepository;
import authproject.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

@Service
public class PhotoService {
  Logger logger = Logger.getLogger(PhotoService.class.getName());
  private PhotoRepository photoRepository;
  private UserRepository userRepository;

  @Autowired
  public PhotoService(PhotoRepository photoRepository, UserRepository userRepository) {
    this.photoRepository = photoRepository;
    this.userRepository = userRepository;
  }

  public Photo create(PhotoDto photoDto) {
    logger.info("Creating photo for user with id: " + photoDto.getUserId());

    Photo photo = new Photo();
    photo.setUrl(photoDto.getUrl());
    photo.setDescription(photoDto.getDescription());

    User user = userRepository.findById(photoDto.getUserId()).get();
    photo.setUser(user);

    return photoRepository.save(photo);
  }

  public List<Photo> findAll() {
    return photoRepository.findAll();
  }
}
