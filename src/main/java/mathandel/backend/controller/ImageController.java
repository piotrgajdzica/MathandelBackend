package mathandel.backend.controller;

import mathandel.backend.client.response.ApiResponse;
import mathandel.backend.security.CurrentUser;
import mathandel.backend.security.UserPrincipal;
import mathandel.backend.service.ImageService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static mathandel.backend.utils.UrlPaths.imagePath;
import static mathandel.backend.utils.UrlPaths.productImagePath;

@Controller
public class ImageController {

    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping(productImagePath)
    @PreAuthorize("hasRole('USER')")
    public @ResponseBody ApiResponse addImage(@CurrentUser UserPrincipal current,
                                              @PathVariable Long productId,
                                              @RequestParam("image") MultipartFile multipartFile) {
        return imageService.addImage(current.getId(), productId, multipartFile);
    }

//    @GetMapping(imagePath)
//    public @ResponseBody byte[] getImage(@PathVariable String imageName) {
//        return imageService.getImage(imageName);
//    }
}
