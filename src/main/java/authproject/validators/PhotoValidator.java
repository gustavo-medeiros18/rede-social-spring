package authproject.validators;

import org.springframework.web.multipart.MultipartFile;

public class PhotoValidator {
  public static boolean imageFileIsValid(MultipartFile multipartFile) {
  String contentType = multipartFile.getContentType();
  long maxSize = 5 * 1024 * 1024; // 5MB

  return contentType != null &&
      contentType.startsWith("image/") &&
      multipartFile.getSize() <= maxSize;
}

  public static boolean descriptionIsValid(String description) {
    return description != null && !description.isEmpty();
  }

  public static boolean userIdIsValid(Long userId) {
    return userId != null;
  }
}