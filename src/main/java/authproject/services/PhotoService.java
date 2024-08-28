package authproject.services;

import authproject.dtos.PhotoDto;
import authproject.exceptions.ResourceNotFoundException;
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
    logger.info("Fetching all photos");
    return photoRepository.findAll();
  }

  public Photo findSingle(Long id) {
    logger.info("Fetching photo with id: " + id);
    return photoRepository.findById(id).orElseThrow(() ->
        new ResourceNotFoundException("No records found for this ID!")
    );
  }

  public Photo update(Long id, PhotoDto photoDto) {
    logger.info("Updating photo with id: " + id);

    Photo existentPhoto = photoRepository.findById(id).orElseThrow(
        () -> new ResourceNotFoundException("No records found for this Photo ID!")
    );

    existentPhoto.setUrl(photoDto.getUrl());
    existentPhoto.setDescription(photoDto.getDescription());

    User user = userRepository.findById(photoDto.getUserId()).orElseThrow(
        () -> new ResourceNotFoundException("No records found for this User ID!")
    );
    existentPhoto.setUser(user);

    return photoRepository.save(existentPhoto);
  }

  public void delete(Long id) {
    logger.info("Deleting photo with id: " + id);

    Photo existentPhoto = photoRepository.findById(id).orElseThrow(
        () -> new ResourceNotFoundException("No records found for this ID!")
    );
    photoRepository.delete(existentPhoto);
  }
}
