package com.example.forumapp.domain;

import com.example.forumapp.domain.dto.UserView;
import com.example.forumapp.domain.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserView toUserView(User user) {
        UserView userView = new UserView();
        userView.setId(user.getId());
        userView.setEmail(userView.getEmail());
        userView.setUsername(userView.getUsername());
        userView.setCreatedDate(user.getCreatedDate());
        userView.setEnabled(user.isEnabled());
        return userView;
    }
}
