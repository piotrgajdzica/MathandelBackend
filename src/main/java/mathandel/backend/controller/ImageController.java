package mathandel.backend.controller;

import mathandel.backend.service.ImageService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import static mathandel.backend.utils.UrlPaths.imagePath;

@Controller
public class ImageController {

    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    // documented
    @GetMapping(imagePath)
    public ResponseEntity<byte[]> getImage(@PathVariable String imageName) {

        return ResponseEntity
                .ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(imageService.getImage(imageName));
    }
}
