package org.atore.movefavorites.dao;


import org.atore.movefavorites.model.Movie;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieDao extends CrudRepository<Movie, Long> {

    Movie findFirstByExternalId(Long externalId);
}
