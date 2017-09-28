package org.atore.movefavorites.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.hateoas.ResourceSupport;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="movie",
        indexes = {@Index(name = "idx_movie_externalId",  columnList="externalId", unique = true)})
@JsonIgnoreProperties(ignoreUnknown = true)
public class Movie extends ResourceSupport {

    private static final int HASH_INITIAL_ODD_NUMBER = 17;
    private static final int HASH_MULTIPLIER_ODD_NUMBER = 37;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long movieId;

    @JsonProperty("id")
    @JsonSerialize(as=Long.class)
    @Column(nullable = false)
    private Long externalId;

    @Column(length = 1024)
    @JsonProperty("poster_path")
    private String poster_path;

    @Column(length = 16384)
    @JsonProperty("overview")
    private String overview;

    @JsonProperty("release_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date releaseDate;

    @Column(length = 2048)
    @JsonProperty("original_title")
    private String  originalTitle;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    private List<UsersList> usersLists;

    public Long getMovieId() {
        return movieId;
    }

    public void setMovieId(Long movieId) {
        this.movieId = movieId;
    }

    public Long getExternalId() {
        return externalId;
    }

    public void setExternalId(Long externalId) {
        this.externalId = externalId;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public List<UsersList> getUsersLists() {
        return usersLists;
    }

    public void setUsersLists(List<UsersList> usersLists) {
        this.usersLists = usersLists;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Movie movie = (Movie) o;

        return new EqualsBuilder()
                .appendSuper(super.equals(o))
                .append(movieId, movie.movieId)
                .append(externalId, movie.externalId)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(HASH_INITIAL_ODD_NUMBER, HASH_MULTIPLIER_ODD_NUMBER)
                .appendSuper(super.hashCode())
                .append(movieId)
                .append(externalId)
                .toHashCode();
    }
}
