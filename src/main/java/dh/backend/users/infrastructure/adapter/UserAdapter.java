package dh.backend.users.infrastructure.adapter;

import dh.backend.users.domain.model.user.User;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.exc.MismatchedInputException;

import tools.jackson.databind.ObjectMapper;

@Component
public class UserAdapter {
    public User jsonToUser(String jsonUser) throws MismatchedInputException{
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(jsonUser, User.class);
    }

}
