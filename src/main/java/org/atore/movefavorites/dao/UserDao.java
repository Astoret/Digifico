package org.atore.movefavorites.dao;


import org.atore.movefavorites.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends CrudRepository<User, Long> {

    User findFirstByUserName(String userName);

}
