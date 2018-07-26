package mathandel.backend.service;

import mathandel.backend.payload.request.AddEditionRequest;
import mathandel.backend.payload.response.ApiResponse;
import mathandel.backend.repository.EditionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EditionService {

    @Autowired
    EditionRepository editionRepository;


    public ApiResponse createEdition(AddEditionRequest addEditionRequest, Long userId){
        return null;
    }

}
