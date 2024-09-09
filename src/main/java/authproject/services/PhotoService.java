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
import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

@Service
public class PhotoService {
  Logger logger = Logger.getLogger(PhotoService.class.getName());
  private PhotoRepository photoRepository;
  private UserRepository userRepository;
  private final String DOWNLOAD_URL = "https://firebasestorage.googleapis.com/v0/b/rede-social-73c41.appspot.com/o/%s?alt=media";

  @Autowired
  public PhotoService(PhotoRepository photoRepository, UserRepository userRepository) {
    this.photoRepository = photoRepository;
    this.userRepository = userRepository;
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

    photo.setUrl(this.upload(photoDto.getImageFile()));

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

//    existentPhoto.setUrl(photoDto.getUrl());
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
//    if (!PhotoValidator.urlIsValid(photoDto.getUrl()))
//      throw new InvalidDataInputException("URL is invalid!");
    if (!PhotoValidator.descriptionIsValid(photoDto.getDescription()))
      throw new InvalidDataInputException("Description is invalid!");
    if (!PhotoValidator.userIdIsValid(photoDto.getUserId()))
      throw new InvalidDataInputException("User ID is invalid!");
  }

  private String getExtension(String fileName) {
    return fileName.substring(fileName.lastIndexOf("."));
  }

  private File convertToFile(MultipartFile multipartFile, String fileName) throws IOException {
    File tempFile = new File(fileName);

    try (FileOutputStream fos = new FileOutputStream(tempFile)) {
      fos.write(multipartFile.getBytes());
    }
    return tempFile;
  }

  private String uploadFile(File file, String fileName) throws IOException {
    BlobId blobId = BlobId.of("rede-social-73c41.appspot.com", fileName);
    BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("media").build();
    Credentials credentials = GoogleCredentials.fromStream(new FileInputStream("src/main/resources/rede-social-73c41-firebase-adminsdk-meocm-59905394be.json"));

    Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
    storage.create(blobInfo, Files.readAllBytes(file.toPath()));

    return String.format(DOWNLOAD_URL, URLEncoder.encode(fileName, StandardCharsets.UTF_8));
  }

  public String upload(MultipartFile multipartFile) {
    try {
      String fileName = multipartFile.getOriginalFilename();
      fileName = UUID.randomUUID().toString().concat(this.getExtension(fileName));

      File file = this.convertToFile(multipartFile, fileName);
      String TEMP_URL = this.uploadFile(file, fileName);
      file.delete();

      return TEMP_URL;
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
    }

  }
}
