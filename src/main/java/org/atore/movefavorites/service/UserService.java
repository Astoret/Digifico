package org.atore.movefavorites.service;

import org.atore.movefavorites.constant.Constants;
import org.atore.movefavorites.dao.UserDao;
import org.atore.movefavorites.exception.BadRequestException;
import org.atore.movefavorites.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    @Value("${security.admin.name}")
    private String adminName;

    @Autowired
    private UserDao userDao;

    @Autowired
    private PasswordEncoder encoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.findFirstByUserName(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return user;
    }

    public User add(String username, String password) throws BadRequestException {
        if (adminName.equals(username)) {
            throw new BadRequestException(Constants.ERROR_USER_USERNAME_TAKEN);
        }
        User user = userDao.findFirstByUserName(username);
        if (user != null && user.getUsername().equals(username)) {
            throw new BadRequestException(Constants.ERROR_USER_USERNAME_TAKEN);
        }
        User newUser = new User();
        newUser.setUserName(username);
        newUser.setPassword(encoder.encode(password));
        return userDao.save(newUser);
    }

    public User findOne(Long userId) throws BadRequestException {
        User user = userDao.findOne(userId);
        if (user == null) {
            throw new BadRequestException(Constants.ERROR_USER_NOT_FOUND);
        }
        return user;
    }
}
