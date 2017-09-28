package org.atore.movefavorites.security;

import org.atore.movefavorites.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils {

    @Autowired
    private UserService userService;

    public org.atore.movefavorites.model.User getUser() {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        return (org.atore.movefavorites.model.User) userService.loadUserByUsername(userName);
    }

    public User getPrincipalUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}
