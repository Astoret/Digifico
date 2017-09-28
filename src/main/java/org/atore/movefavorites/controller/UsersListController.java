package org.atore.movefavorites.controller;


import org.atore.movefavorites.model.Movie;
import org.atore.movefavorites.model.User;
import org.atore.movefavorites.model.UsersList;
import org.atore.movefavorites.security.SecurityUtils;
import org.atore.movefavorites.service.MovieService;
import org.atore.movefavorites.service.UserListService;
import org.atore.movefavorites.utils.TmdbRestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;


@RestController
@RequestMapping("/list")
public class UsersListController {

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private TmdbRestClient tmdbRestClient;

    @Autowired
    private UserListService userListService;

    @Autowired
    private MovieService movieService;

    @RequestMapping(method = RequestMethod.GET)
    public HttpEntity<List<UsersList>> list() {
        User user = securityUtils.getUser();
        return new ResponseEntity<>(userListService.findListByUser(user), HttpStatus.OK);
    }

    @RequestMapping("{listId}")
    public HttpEntity<UsersList> show(@PathVariable("listId") Long listId) throws Exception {
        User user = securityUtils.getUser();
        UsersList usersList = userListService.getByUserAndListId(user, listId);
        return new ResponseEntity<>(usersList, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    public HttpEntity<UsersList> create(@RequestParam("name") String name) throws Exception {
        User user = securityUtils.getUser();
        UsersList usersList = userListService.creatList(user, name);
        return new ResponseEntity<>(usersList, HttpStatus.OK);
    }

    @RequestMapping(value = "{listId}", method = RequestMethod.DELETE)
    public HttpEntity<UsersList> delete(@PathVariable("listId") Long listId) throws Exception {
        User user = securityUtils.getUser();
        userListService.delete(user, listId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "{listId}/rename", method = RequestMethod.POST)
    public HttpEntity<UsersList> rename(@PathVariable("listId") Long listId,
                                        @RequestParam("newName") String newName) throws Exception {
        User user = securityUtils.getUser();
        return new ResponseEntity<>(userListService.rename(user, listId, newName), HttpStatus.OK);
    }

    @RequestMapping(value = "{listId}/movie", method = RequestMethod.POST)
    public HttpEntity<UsersList> addMovie(@PathVariable("listId") Long listId,
                                         @RequestParam("external_id") Long externalId) throws Exception {
        User user = securityUtils.getUser();
        UsersList usersList = userListService.getByUserAndListId(user, listId);
        Movie movie = movieService.saveOrFind(tmdbRestClient.getMovie(externalId));
        usersList.getMovies().add(movie);
        return new ResponseEntity<>(userListService.update(usersList), HttpStatus.OK);
    }

    @RequestMapping(value = "{listId}/movie/{movieId}", method = RequestMethod.DELETE)
    public HttpEntity<UsersList> removeMovie(@PathVariable("listId") Long listId,
                                            @PathVariable("movieId") Long movieId) throws Exception {
        User user = securityUtils.getUser();
        return new ResponseEntity<>(userListService.removeMove(user, listId, movieId), HttpStatus.OK);
    }

    @Transactional
    @RequestMapping(value = "move/{newListId}", method = RequestMethod.POST)
    public HttpEntity<UsersList> moveMovie(@PathVariable("newListId") Long newListId,
                                          @RequestParam("moveId") Long moveId) throws Exception {
        User user = securityUtils.getUser();
        return new ResponseEntity<>(userListService.moveMove(user, newListId, moveId), HttpStatus.OK);
    }
}
