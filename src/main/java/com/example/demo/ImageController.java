package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/image")
@RestController
public class ImageController {

    @Autowired
    private ImageService imageService;
    @PostMapping
    public ResponseEntity<ImageEntity> addImage(@ModelAttribute ImageEntity image){
        ImageEntity response = imageService.addImage(image);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ImageEntity>> getAllImage() {
        List<ImageEntity> response = imageService.getAllImage();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
