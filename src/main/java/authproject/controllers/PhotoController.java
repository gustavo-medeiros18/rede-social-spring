package authproject.controllers;

import authproject.dtos.PhotoDto;
import authproject.models.Photo;
import authproject.services.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/photo")
public class PhotoController {
  private PhotoService service;

  @Autowired
  public PhotoController(PhotoService service) {
    this.service = service;
  }

  @PostMapping(consumes = {"multipart/form-data"})
  public ResponseEntity<Photo> create(
      @RequestParam("description") String description,
      @RequestParam("userId") Long userId,
      @RequestParam("imageFile") MultipartFile imageFile
  ) {
    PhotoDto photoDto = new PhotoDto();
    photoDto.setDescription(description);
    photoDto.setUserId(userId);
    photoDto.setImageFile(imageFile);

    return ResponseEntity.ok(service.create(photoDto));
  }

  @GetMapping()
  public ResponseEntity<List<Photo>> findAll() {
    return ResponseEntity.ok(service.findAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<Photo> findSingle(@PathVariable Long id) {
    return ResponseEntity.ok(service.findSingle(id));
  }

  @PutMapping(path = "/{id}", consumes = {"multipart/form-data"})
  public ResponseEntity<Photo> update(
      @PathVariable Long id,
      @RequestParam("description") String description,
      @RequestParam("userId") Long userId,
      @RequestParam("imageFile") MultipartFile imageFile
  ) {
    PhotoDto photoDto = new PhotoDto();
    photoDto.setDescription(description);
    photoDto.setUserId(userId);
    photoDto.setImageFile(imageFile);

    Photo updatedPhoto = service.update(id, photoDto);

    if (updatedPhoto == null) return ResponseEntity.notFound().build();
    else return ResponseEntity.ok(service.update(id, photoDto));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    service.delete(id);
    return ResponseEntity.noContent().build();
  }
}
