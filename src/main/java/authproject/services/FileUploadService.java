package authproject.services;

import authproject.exceptions.InvalidDataInputException;
import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.UUID;
import java.util.logging.Logger;

@Service
public class FileUploadService {
  private static final org.slf4j.Logger log = LoggerFactory.getLogger(FileUploadService.class);
  Logger logger = Logger.getLogger(PhotoService.class.getName());

  @Value("${firebase.download_url}")
  private String DOWNLOAD_URL;

  @Value("${firebase.bucket}")
  private String BUCKET;

  @Value("${firebase.configuration_file_path}")
  private String CONFIGURATION_FILE_PATH;

  private void validateFile(MultipartFile multipartFile) {
    String contentType = multipartFile.getContentType();
    if (contentType == null || !contentType.startsWith("image/")) {
      throw new InvalidDataInputException("The file must be an image!");
    }

    long maxSize = 5 * 1024 * 1024; // 5MB
    if (multipartFile.getSize() > maxSize) {
      throw new InvalidDataInputException("The file must be smaller than 5MB!");
    }
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

  private String saveFileInFirebaseStorage(File file, String fileName) throws IOException {
    BlobId blobId = BlobId.of(BUCKET, fileName);
    BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("media").build();
    Credentials credentials = GoogleCredentials.fromStream(new FileInputStream(CONFIGURATION_FILE_PATH));

    Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
    storage.create(blobInfo, Files.readAllBytes(file.toPath()));

    logger.info("File uploaded successfully. File name: " + fileName);

    return String.format(DOWNLOAD_URL, URLEncoder.encode(fileName, StandardCharsets.UTF_8));
  }

  public String uploadFile(MultipartFile multipartFile) {
    logger.info("Uploading file to Firebase Storage");
    validateFile(multipartFile);

    try {
      String fileName = multipartFile.getOriginalFilename();
      fileName = UUID.randomUUID().toString().concat(this.getExtension(fileName));

      File tempLocalFile = this.convertToFile(multipartFile, fileName);
      String TEMP_URL = this.saveFileInFirebaseStorage(tempLocalFile, fileName);
      tempLocalFile.delete();

      return TEMP_URL;
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
    }
  }
}
