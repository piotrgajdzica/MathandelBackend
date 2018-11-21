package mathandel.backend.controller;

import mathandel.backend.model.client.ImageTO;
import mathandel.backend.model.client.response.ApiResponse;
import mathandel.backend.security.CurrentUser;
import mathandel.backend.security.UserPrincipal;
import mathandel.backend.service.ImageService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static mathandel.backend.utils.UrlPaths.*;

@Controller
public class ImageController {

    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    // documented
    @PostMapping(itemImagesPath)
    @PreAuthorize("hasRole('USER')")
    public @ResponseBody
    ImageTO addImage(@CurrentUser UserPrincipal current,
                     @PathVariable Long itemId,
                     @RequestParam("image") MultipartFile multipartFile) {
        return imageService.addImage(current.getId(), itemId, multipartFile);
    }

    // documented
    @GetMapping(imagePath)
    public ResponseEntity<byte[]> getImage(@PathVariable String imageName) {

        return ResponseEntity
                .ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(imageService.getImage(imageName));
    }

    // documented
    @DeleteMapping(itemImagePath)
    @PreAuthorize("hasRole('USER')")
    public @ResponseBody
    ApiResponse deleteImage(@CurrentUser UserPrincipal current,
                            @PathVariable String imageName,
                            @PathVariable Long itemId) {
        return imageService.deleteImage(current.getId(), itemId, imageName);
    }
}
