package org.atore.movefavorites.controller;

import org.atore.movefavorites.dao.MovieDao;
import org.atore.movefavorites.model.Movie;
import org.atore.movefavorites.model.SearchResponse;
import org.atore.movefavorites.utils.TmdbRestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/movie")
public class MovieController {

    private static final String FIRST_PAGE = "1";

    @Value( "${web.itemsPerPage:5}" )
    private Integer itemsPerPage;

    @Autowired
    private TmdbRestClient tmdbRestClient;

    @Autowired
    private MovieDao movieDao;

    @RequestMapping(value = "{movieId}", method = RequestMethod.GET)
    public HttpEntity<Movie> getDetails(@PathVariable("movie_id") Long movieId) throws Exception {
        return new ResponseEntity<>(tmdbRestClient.getMovie(movieId), HttpStatus.OK);
    }

    /**
     * The method adds movie from external database to internal database.
     * @param externalId external movie id
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public HttpEntity<Movie> add(@RequestParam("external_id") Long externalId) throws Exception {
        Movie movie = tmdbRestClient.getMovie(externalId);
        return new ResponseEntity<>(movieDao.save(movie), HttpStatus.OK);
    }

    @RequestMapping(value = "search" ,method = RequestMethod.POST)
    public HttpEntity<SearchResponse> search(
            @RequestParam("query") String query,
            @RequestParam(value = "page", required = false, defaultValue = FIRST_PAGE) Integer page)
            throws Exception {

        return new ResponseEntity<>(tmdbRestClient.getMovie(query, page, itemsPerPage), HttpStatus.OK);
    }


}
