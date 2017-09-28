package org.atore.movefavorites.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.atore.movefavorites.constant.Constants;
import org.atore.movefavorites.exception.BadRequestException;
import org.atore.movefavorites.model.Movie;
import org.atore.movefavorites.model.SearchResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Component
public class TmdbRestClient {

    private static final String API_KEY_PARAM = "api_key";
    private static final String QUERY_PARAM = "query";
    private static final String PAGE_PARAM = "page";
    private static final String MOVIE_ID_PARAM = "movie_id";

    @Value("${tmdb.scheme:https}")
    private String imdbScheme;

    @Value("${tmdb.host}")
    private String imdbHost;

    @Value("${tmdb.path.movie}")
    private String imdbMoviePath;

    @Value("${tmdb.path.search}")
    private String imdbSearchPath;

    @Value("${tmdb.itemsPerPage:20}")
    private int imdbItemsPerPage;

    @Value("${tmdb.apiKey}")
    private String imdbApiKeyValue;

    private final CloseableHttpClient httpclient = HttpClients.createDefault();

    /**
     *
     * See https://developers.themoviedb.org/3/movies
     * @param externalId external movie id
     * @return
     * @throws Exception
     */
    public Movie getMovie(Long externalId) throws Exception {
        URI uri = new URIBuilder()
                .setScheme(imdbScheme)
                .setHost(imdbHost)
                .setPath(Paths.get(imdbMoviePath, String.valueOf(externalId)).toString())
                .setParameter(API_KEY_PARAM, imdbApiKeyValue)
                .build();

        return execGetRequest(uri, Movie.class);
    }

    /**
     * See https://developers.themoviedb.org/3/search
     * @param query
     * @param page
     * @param itemPerPage
     * @return
     * @throws Exception
     */
    public SearchResponse getMovie(String query, Integer page, Integer itemPerPage)
            throws Exception {

        int firstTmdbPage = findFirstTmdbPage(page, itemPerPage);
        int offset = getPageOffset(page, itemPerPage);
        SearchResponse response = getSearchResponse(query, firstTmdbPage);
        int totalPages = (int) Math.ceil((double) response.getTotalResults() / itemPerPage);
        if (page > totalPages) {
            throw new BadRequestException(Constants.ERROR_OUT_OF_PAGE);
        }
        int size = getSizeOfItems(itemPerPage, response.getResults());
        List<Movie> listOfMovies = new ArrayList<>();
        listOfMovies.addAll(response.getResults().subList(offset, size + offset));
        while (listOfMovies.size() < itemPerPage && listOfMovies.size() < response.getResults().size()) {
            firstTmdbPage++;
            response = getSearchResponse(query, firstTmdbPage);
            size = getSizeOfItems(itemPerPage, response.getResults());
            listOfMovies.addAll(response.getResults().subList(0, size));
        }
        SearchResponse result = new SearchResponse();
        result.setPage(page);
        result.setTotalResults(response.getTotalResults());
        result.setResults(listOfMovies);
        result.setTotalPages(totalPages);
        return result;
    }

    private int getSizeOfItems(int itemPerPage, List<Movie> listOfMovies) {
        if (listOfMovies.size() < itemPerPage) {
            return listOfMovies.size();
        }
        return itemPerPage;
    }

    /**
     * The method finds offset for themoviedb page.
     * @param page the application page starting from 1
     * @param itemPerPage count of items per page
     * @return
     */
    int getPageOffset(Integer page, Integer itemPerPage) {
        int tmdbPage = findFirstTmdbPage(page, itemPerPage);
        return Math.abs((page - 1) * itemPerPage - (tmdbPage - 1) * imdbItemsPerPage);
    }

    /**
     * The method finds first page that will be used for search on themoviedb server.
     * We need this method because themoviedb don't support items per page parameters.
     * @param page the application page starting from 1
     * @param itemPerPage count of items per page
     * @return themoviedb page number started from 1
     */
    int findFirstTmdbPage(int page, int itemPerPage) {
        return (int) Math.ceil((page - 1) * itemPerPage / imdbItemsPerPage) + 1;
    }

    /**
     * Searches for movies on themoviedb server.
     * @param query
     * @param tmdbPage page number on themoviedb
     * @return
     * @throws URISyntaxException
     * @throws IOException
     */
    private SearchResponse getSearchResponse(String query, int tmdbPage)
            throws Exception {
        URI uri = new URIBuilder()
                .setScheme(imdbScheme)
                .setHost(imdbHost)
                .setPath(imdbSearchPath)
                .setParameter(API_KEY_PARAM, imdbApiKeyValue)
                .setParameter(QUERY_PARAM, URLEncoder.encode(query, StandardCharsets.UTF_8.name()))
                .setParameter(PAGE_PARAM, String.valueOf(tmdbPage))
                .build();

        return execGetRequest(uri, SearchResponse.class);
    }

    private <T> T execGetRequest(URI uri, Class<T> clazz) throws IOException {
        try (CloseableHttpResponse response1 = httpclient.execute(new HttpGet(uri))) {
            HttpEntity entity = response1.getEntity();
            ObjectMapper mapper = new ObjectMapper();
            T searchResponse = mapper.readValue(IOUtils.toString(entity.getContent(),
                    StandardCharsets.UTF_8), clazz);
            EntityUtils.consume(entity);
            return searchResponse;
        }
    }

    public void setImdbItemsPerPage(int imdbItemsPerPage) {
        this.imdbItemsPerPage = imdbItemsPerPage;
    }
}
