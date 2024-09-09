package authproject.dtos;

import org.springframework.web.multipart.MultipartFile;

public class PhotoDto {
  private MultipartFile imageFile;
  private String description;
  private Long userId;

  public PhotoDto() {

  }

  public MultipartFile getImageFile() {
    return imageFile;
  }

  public void setImageFile(MultipartFile imageFile) {
    this.imageFile = imageFile;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }
}
