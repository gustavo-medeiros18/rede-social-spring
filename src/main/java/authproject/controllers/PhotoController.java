package authproject.controllers;

import authproject.dtos.PhotoDto;
import authproject.models.Photo;
import authproject.services.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/photo")
public class PhotoController {
  private PhotoService service;

  @Autowired
  public PhotoController(PhotoService service) {
    this.service = service;
  }

  @PostMapping()
  public ResponseEntity<Photo> create(@RequestBody PhotoDto photoDto) {
    return ResponseEntity.ok(service.create(photoDto));
  }
}
