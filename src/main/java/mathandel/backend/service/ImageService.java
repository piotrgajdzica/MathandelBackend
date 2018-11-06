package mathandel.backend.service;

import mathandel.backend.client.response.ApiResponse;
import mathandel.backend.exception.AppException;
import mathandel.backend.exception.BadRequestException;
import mathandel.backend.exception.ResourceNotFoundException;
import mathandel.backend.model.client.ImageTO;
import mathandel.backend.model.server.Image;
import mathandel.backend.model.server.Product;
import mathandel.backend.model.server.User;
import mathandel.backend.repository.ImageRepository;
import mathandel.backend.repository.ProductRepository;
import mathandel.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import static mathandel.backend.utils.ImageTypeMap.getExtension;
import static mathandel.backend.utils.ServerToClientDataConverter.mapImage;

@Service
public class ImageService {

    @Value("${maximum-file-size}")
    private long maxSize;

    @Value("${maximum-images-per-product}")
    private int maxImagesPerProduct;

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;

    public ImageService(ProductRepository productRepository, UserRepository userRepository, ImageRepository imageRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.imageRepository = imageRepository;
    }

    public ImageTO addImage(Long userId, Long productId, MultipartFile file) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException("User doesn't exist."));
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));
        String basePath = new File("").getAbsolutePath();
        String extension = getExtension(file.getContentType());
        String name = "a" + generateName(userId, productId) + "." + extension;

        if (!product.getUser().equals(user)) {
            throw new BadRequestException("You are not the owner of this product");
        }
        if (product.getImages().size() == maxImagesPerProduct) {
            throw new BadRequestException("Product already has 3 images");
        }
        if (file.getSize() > maxSize) {
            throw new BadRequestException("File cannot exceed 5mb");
        }
        if (!Objects.requireNonNull(file.getContentType()).startsWith("image/")) {
            throw new BadRequestException("You can only upload an image");
        }
        if (extension == null) {
            throw new BadRequestException("File type not supported");
        }

        try {
            byte[] bytes = file.getBytes();
            Path path = Paths.get(basePath + "\\src\\main\\resources\\images\\" + name);
            Files.write(path, bytes);
        } catch (IOException e) {
            e.printStackTrace();
            throw new AppException("Error occurred when trying to save file");
        }

        Image image = new Image()
                .setName(name);

        product.getImages().add(image);

        Image uploadedImage = imageRepository.save(image);
        return mapImage(uploadedImage);
    }

    private String generateName(Long userId, Long productId) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS");
        String dateString = format.format(new Date());
        return userId.toString() + "_" + productId + "_" + dateString;
    }

    public byte[] getImage(String imageName) {
        try {
            String basePath = new File("").getAbsolutePath();
            return Files.readAllBytes(Paths.get(basePath + "\\src\\main\\resources\\images\\" + imageName));
        } catch (IOException e) {
            throw new BadRequestException("Requested file does not exist");
        }
    }

    public ApiResponse deleteImage(Long userId, Long productId, String imageName) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));
        Image image = imageRepository.findByName(imageName).orElseThrow(() -> new ResourceNotFoundException("Image", "name", imageName));
        String basePath = new File("").getAbsolutePath();

        if(!product.getUser().getId().equals(userId)) {
            throw new BadRequestException("You are not the owner of this product");
        }
        if(!product.getImages().contains(image)) {
            throw new BadRequestException("This image does not belong to your this product");
        }

        product.getImages().remove(image);
        imageRepository.delete(image);
        File file = new File(basePath + "\\src\\main\\resources\\images\\" + imageName);
        if(!file.delete()) {
           throw new AppException("Could not delete file - server internal error");
        }
        return new ApiResponse("Image deleted successfully");
    }
}
