package mathandel.backend.utils;

import mathandel.backend.client.response.ApiResponse;
import org.springframework.http.ResponseEntity;

public class ResponseCreator {

    public static ResponseEntity createResponse(ApiResponse apiResponse) {
        return apiResponse.getSuccess() ? ResponseEntity.ok(apiResponse) : ResponseEntity.badRequest().body(apiResponse);
    }

}
