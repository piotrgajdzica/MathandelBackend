package mathandel.backend.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

// todo to be removed, now used for testing hitting endpoints with rest template
@Controller
public class TestControler {

    private static String WORKDIR;

    private static String REQUEST_TO_DESERIALIZE_PATH;


    @PostMapping("/api/solve")
    public @ResponseBody
    String solve(@RequestBody String data) throws IOException {
        System.out.println(data);
        WORKDIR = System.getProperty("user.dir");
        REQUEST_TO_DESERIALIZE_PATH = "/src/main/resources/exampleCalsServiceResponseData.json";
        return new String(Files.readAllBytes(Paths.get(WORKDIR + REQUEST_TO_DESERIALIZE_PATH)), StandardCharsets.UTF_8);

    }
}
