package io.swagger.api;

import io.swagger.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@javax.annotation.Generated(
        value = "io.swagger.codegen.v3.generators.java.SpringCodegen",
        date = "2025-11-28T19:19:02.746258252Z[GMT]"
)
@CrossOrigin(origins = "*")
@RestController
public class UsersApiController implements UsersApi {

    private static final Logger log = LoggerFactory.getLogger(UsersApiController.class);

    private final ObjectMapper objectMapper;
    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public UsersApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    @Override
    public ResponseEntity<List<User>> usersGet() {
        User u1 = new User();
        u1.setId(0);
        u1.setName("name");

        User u2 = new User();
        u2.setId(1);
        u2.setName("name2");

        List<User> users = new java.util.ArrayList<>();
        users.add(u1);
        users.add(u2);

        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> usersIdDelete(
            @Parameter(in = ParameterIn.PATH, description = "", required = true, schema = @Schema())
            @PathVariable("id") Object id) {

        // Stub generado: devolvemos 200 sin lógica real
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> usersIdGet(
            @Parameter(in = ParameterIn.PATH, description = "", required = true, schema = @Schema())
            @PathVariable("id") Object id) {

        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<>(
                        objectMapper.readValue("\"user " + id + "\"", String.class),
                        HttpStatus.OK
                );
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @Override
    public ResponseEntity<String> usersIdPut(
            @Parameter(in = ParameterIn.PATH, description = "", required = true, schema = @Schema())
            @PathVariable("id") Object id,
            @Parameter(in = ParameterIn.DEFAULT, description = "", schema = @Schema())
            @Valid @RequestBody String body) {

        // Stub: simulamos actualización
        return new ResponseEntity<>("updated " + id, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> usersPost(
            @Parameter(in = ParameterIn.DEFAULT, description = "", schema = @Schema())
            @Valid @RequestBody String body) {

        // Stub: simulamos creación
        return new ResponseEntity<>("created", HttpStatus.OK);
    }
}
