package mathandel.backend.controller;

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

import static mathandel.backend.utils.UrlPaths.imagePath;
import static mathandel.backend.utils.UrlPaths.productImagePath;
import static mathandel.backend.utils.UrlPaths.productImagesPath;

@Controller
public class ImageController {

    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping(productImagesPath)
    @PreAuthorize("hasRole('USER')")
    public @ResponseBody
    ApiResponse addImage(@CurrentUser UserPrincipal current,
                         @PathVariable Long productId,
                         @RequestParam("image") MultipartFile multipartFile) {
        return imageService.addImage(current.getId(), productId, multipartFile);
    }


    @GetMapping(imagePath)
    public ResponseEntity<byte[]> getImage(@PathVariable String imageName) {

        return ResponseEntity
                .ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(imageService.getImage(imageName));
    }

    @DeleteMapping(productImagePath)
    @PreAuthorize("hasRole('USER')")
    public @ResponseBody ApiResponse deleteImage(@CurrentUser UserPrincipal current,
                                                 @PathVariable String imageName,
                                                 @PathVariable Long productId) {
        return imageService.deleteImage(current.getId(), productId, imageName);
    }
}
