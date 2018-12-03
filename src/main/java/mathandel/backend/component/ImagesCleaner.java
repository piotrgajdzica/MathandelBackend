package mathandel.backend.component;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Component
public class ImagesCleaner implements ApplicationRunner {

    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String databaseCommand;

    private static final Logger logger = LoggerFactory.getLogger(ImagesCleaner.class);

    @Override
    public void run(ApplicationArguments args) throws IOException {

        String path = "src/main/resources/images";

        if(databaseCommand.equals("update") || databaseCommand.equals("none")) {
            if(!Files.exists(Paths.get(path))) {
                new File(path).mkdir();
            }
        } else {
            if(Files.exists(Paths.get(path))) {
                FileUtils.cleanDirectory(new File(path));
            } else {
                new File(path).mkdir();
            }
        }
        logger.info("Server ready to go...");
    }
}
