package org.atore.movefavorites.service;

import org.atore.movefavorites.dao.MovieDao;
import org.atore.movefavorites.model.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MovieService {

    @Autowired
    private MovieDao movieDao;

    public Movie saveOrFind(Movie inMovie) {
        Movie movieEntity = movieDao.findFirstByExternalId(inMovie.getExternalId());
        if (movieEntity == null) {
            movieEntity = movieDao.save(inMovie);
        }
        return movieEntity;
    }

}
