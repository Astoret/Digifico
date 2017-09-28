package org.atore.movefavorites.dao;


import org.atore.movefavorites.model.Movie;
import org.atore.movefavorites.model.User;
import org.atore.movefavorites.model.UsersList;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserListDao extends CrudRepository<UsersList, Long> {

    List<UsersList> findAllByUser(User user);

    UsersList findFirstByUserAndListId(User user, Long listId);

    UsersList findFirstByUserAndName(User user, String name);

    UsersList findFirstByUserAndMovies(User user, Movie movie);
}
