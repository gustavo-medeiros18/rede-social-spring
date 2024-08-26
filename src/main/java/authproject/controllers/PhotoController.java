package authproject.controllers;

import authproject.dtos.PhotoDto;
import authproject.models.Photo;
import authproject.services.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

  @GetMapping()
  public ResponseEntity<List<Photo>> findAll() {
    return ResponseEntity.ok(service.findAll());
  }
}
