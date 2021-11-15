package controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import service.ArrService;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@RestController
public class ArrController {

    @Autowired
    ArrService arrService;

    @GetMapping(value = "/results",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity arrManipulationResults(){
        try {
            Optional<Map<String, Object>> response = arrService.arrManipulationResults();
            if(!response.isEmpty()){
                return ResponseEntity.ok(response.get());
            }
            else {
                return ResponseEntity.noContent().build();
            }
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        } catch (ClassNotFoundException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
