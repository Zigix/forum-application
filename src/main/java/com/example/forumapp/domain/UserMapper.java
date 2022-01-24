package com.example.forumapp.domain;

import com.example.forumapp.domain.dto.UserView;
import com.example.forumapp.domain.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserView toUserView(User user) {
        UserView userView = new UserView();
        userView.setId(user.getId());
        userView.setEmail(user.getEmail());
        userView.setUsername(user.getUsername());
        userView.setCreatedDate(user.getCreatedDate());
        userView.setEnabled(user.isEnabled());
        return userView;
    }
}
