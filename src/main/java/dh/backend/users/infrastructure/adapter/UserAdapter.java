package dh.backend.users.infrastructure.adapter;

import dh.backend.users.domain.model.user.User;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Component
public class UserAdapter {
    public User jsonToUser(String jsonUser){
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(jsonUser, User.class);
    }

}
