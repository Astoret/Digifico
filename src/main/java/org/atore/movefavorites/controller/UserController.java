package org.atore.movefavorites.controller;

import org.atore.movefavorites.model.User;
import org.atore.movefavorites.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "{userId}", method = RequestMethod.GET)
    public HttpEntity<User> getUser(@PathVariable("userId") Long userId) throws Exception {
        User user = userService.findOne(userId);
        user.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(UserController.class).getUser(userId)).withSelfRel());
        user.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(UsersListController.class).list()).withRel("list"));
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    public HttpEntity<User> addUser(@RequestParam("userName") String userName,
                                    @RequestParam("password") String password) throws Exception {
        User user = userService.add(userName, password);
        user.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(UserController.class).getUser(user.getUserId())).withSelfRel());
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

}
