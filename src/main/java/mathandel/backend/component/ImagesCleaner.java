package mathandel.backend.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

@Component
public class ImagesCleaner implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(ImagesCleaner.class);

    @Override
    public void run(ApplicationArguments args) {

        String path = "src/main/resources/images";

        if(!Files.exists(Paths.get(path))) {
            new File(path).mkdir();
        }
        logger.info("Server ready to go...");
    }
}
