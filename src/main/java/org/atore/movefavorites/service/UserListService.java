package org.atore.movefavorites.service;

import org.atore.movefavorites.constant.Constants;
import org.atore.movefavorites.dao.MovieDao;
import org.atore.movefavorites.dao.UserDao;
import org.atore.movefavorites.dao.UserListDao;
import org.atore.movefavorites.exception.BadRequestException;
import org.atore.movefavorites.model.Movie;
import org.atore.movefavorites.model.User;
import org.atore.movefavorites.model.UsersList;
import org.atore.movefavorites.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserListService {

    @Autowired
    private UserListDao listDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private MovieDao movieDao;

    @Autowired
    private SecurityUtils securityUtils;


    public List<UsersList> findListByUser(User user) {
        return listDao.findAllByUser(user);
    }

    public UsersList findOne(Long id) throws BadRequestException {
        UsersList usersList = listDao.findOne(id);
        if (usersList == null) {
            throw new BadRequestException(Constants.ERROR_LIST_NOT_FOUND);
        }
        return usersList;
    }

    public UsersList getByUserAndListId(User user, Long listId) throws BadRequestException {
        UsersList list = listDao.findFirstByUserAndListId(user, listId);
        if (list == null) {
            throw new BadRequestException(Constants.ERROR_LIST_NOT_FOUND);
        }
        return list;
    }

    public UsersList creatList(User user, String name) throws BadRequestException {
        UsersList  usersList = listDao.findFirstByUserAndName(user, name);
        if (usersList != null) {
            throw new BadRequestException(Constants.ERROR_LIST_NAME_TAKEN);
        }
        UsersList newUsersList = new UsersList();
        newUsersList.setUser(user);
        newUsersList.setName(name);
        return listDao.save(newUsersList);
    }

    public void delete(User user, Long listId) throws BadRequestException {
        UsersList list = listDao.findFirstByUserAndListId(user, listId);
        if (list == null) {
            throw new BadRequestException(Constants.ERROR_LIST_NOT_FOUND);
        }
        listDao.delete(list);
    }

    public UsersList update(UsersList usersList) {
        return listDao.save(usersList);
    }

    public UsersList rename(User user, Long listId, String newName) throws BadRequestException {
        UsersList list = getByUserAndListId(user, listId);
        list.setName(newName);
        return listDao.save(list);
    }

    public UsersList removeMove(User user, Long listId, Long movieId) throws BadRequestException {
        UsersList list = getByUserAndListId(user, listId);
        List<Movie>  movies = list.getMovies().stream()
                .filter(p -> !movieId.equals(p.getMovieId())).collect(Collectors.toList());
        list.setMovies(movies);
        return listDao.save(list);
    }

    public UsersList moveMove(User user, Long newListId, Long movieId) throws BadRequestException {
        Movie movie = movieDao.findOne(movieId);
        if (movie == null) {
            throw new BadRequestException(Constants.ERROR_MOVIE_NOT_FOUND);
        }
        UsersList list = listDao.findFirstByUserAndMovies(user, movie);
        if (list == null) {
            throw new BadRequestException(Constants.ERROR_LIST_NOT_FOUND);
        }
        UsersList newList = getByUserAndListId(user, newListId);
        list.getMovies().remove(movie);
        newList.getMovies().add(movie);
        listDao.save(list);
        listDao.save(newList);
        return list;
    }


}
