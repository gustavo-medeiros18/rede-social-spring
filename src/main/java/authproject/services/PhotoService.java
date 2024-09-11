package authproject.services;

import authproject.controllers.PhotoController;
import authproject.dtos.PhotoDto;
import authproject.exceptions.InvalidDataInputException;
import authproject.exceptions.ResourceNotFoundException;
import authproject.models.Photo;
import authproject.models.User;
import authproject.repositories.PhotoRepository;
import authproject.repositories.UserRepository;
import authproject.validators.PhotoValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.logging.Logger;

@Service
public class PhotoService {
  Logger logger = Logger.getLogger(PhotoService.class.getName());
  private PhotoRepository photoRepository;
  private UserRepository userRepository;
  private FileUploadService fileUploadService;
  private final String DOWNLOAD_URL = "https://firebasestorage.googleapis.com/v0/b/rede-social-73c41.appspot.com/o/%s?alt=media";

  @Autowired
  public PhotoService(PhotoRepository photoRepository, UserRepository userRepository, FileUploadService fileUploadService) {
    this.photoRepository = photoRepository;
    this.userRepository = userRepository;
    this.fileUploadService = fileUploadService;
  }

  public Photo create(PhotoDto photoDto) {
    logger.info("Creating photo for user with id: " + photoDto.getUserId());

    verifyPhotoFields(photoDto);

    Photo photo = new Photo();
    photo.setDescription(photoDto.getDescription());

    User user = userRepository.findById(photoDto.getUserId()).orElseThrow(() ->
        new ResourceNotFoundException("No records found for this User ID!")
    );
    photo.setUser(user);

    photo.setUrl(fileUploadService.uploadFile(photoDto.getImageFile()));

    Photo createdPhoto = photoRepository.save(photo);
    createdPhoto.add(linkTo(methodOn(PhotoController.class).findSingle(createdPhoto.getId())).withSelfRel());

    return createdPhoto;
  }

  public List<Photo> findAll() {
    logger.info("Fetching all photos");

    List<Photo> photos = photoRepository.findAll();
    for (Photo photo : photos)
      photo.add(linkTo(methodOn(PhotoController.class).findSingle(photo.getId())).withSelfRel());

    return photos;
  }

  public Photo findSingle(Long id) {
    logger.info("Fetching photo with id: " + id);

    Photo photo = photoRepository.findById(id).orElseThrow(() ->
        new ResourceNotFoundException("No records found for this ID!")
    );

    photo.add(linkTo(methodOn(PhotoController.class).findSingle(id)).withSelfRel());
    return photo;
  }

  public Photo update(Long id, PhotoDto photoDto) {
    logger.info("Updating photo with id: " + id);

    verifyPhotoFields(photoDto);

    Photo existentPhoto = photoRepository.findById(id).orElseThrow(
        () -> new ResourceNotFoundException("No records found for this Photo ID!")
    );

    existentPhoto.setUrl(fileUploadService.uploadFile(photoDto.getImageFile()));
    existentPhoto.setDescription(photoDto.getDescription());

    User user = userRepository.findById(photoDto.getUserId()).orElseThrow(
        () -> new ResourceNotFoundException("No records found for this User ID!")
    );
    existentPhoto.setUser(user);

    Photo updatedPhoto = photoRepository.save(existentPhoto);

    if (updatedPhoto.getLinks().isEmpty())
      updatedPhoto.add(linkTo(methodOn(PhotoController.class).findSingle(updatedPhoto.getId())).withSelfRel());

    return updatedPhoto;
  }

  public void delete(Long id) {
    logger.info("Deleting photo with id: " + id);

    Photo existentPhoto = photoRepository.findById(id).orElseThrow(
        () -> new ResourceNotFoundException("No records found for this ID!")
    );
    photoRepository.delete(existentPhoto);
  }

  private void verifyPhotoFields(PhotoDto photoDto) {
    if (
        photoDto == null ||
            photoDto.getImageFile() == null ||
            photoDto.getDescription() == null ||
            photoDto.getUserId() == null
    )
      throw new InvalidDataInputException("Photo object has null attributes!");
    if (!PhotoValidator.imageFileIsValid(photoDto.getImageFile()))
      throw new InvalidDataInputException("File must be an image and have a maximum size of 5MB!");
    if (!PhotoValidator.descriptionIsValid(photoDto.getDescription()))
      throw new InvalidDataInputException("Description is invalid!");
    if (!PhotoValidator.userIdIsValid(photoDto.getUserId()))
      throw new InvalidDataInputException("User ID is invalid!");
  }
}
