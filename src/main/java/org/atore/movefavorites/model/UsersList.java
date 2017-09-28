package org.atore.movefavorites.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.hateoas.ResourceSupport;

import javax.persistence.*;
import java.util.List;

@Entity
public class UsersList extends ResourceSupport {

    private static final int HASH_INITIAL_ODD_NUMBER = 17;
    private static final int HASH_MULTIPLIER_ODD_NUMBER = 37;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long listId;

    private String name;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<Movie> movies;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    public Long getListId() {
        return listId;
    }

    public void setListId(Long listId) {
        this.listId = listId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public void setMovies(List<Movie> listOfMovies) {
        this.movies = listOfMovies;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        UsersList usersList = (UsersList) o;

        return new EqualsBuilder()
                .appendSuper(super.equals(o))
                .append(listId, usersList.listId)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(HASH_INITIAL_ODD_NUMBER, HASH_MULTIPLIER_ODD_NUMBER)
                .appendSuper(super.hashCode())
                .append(listId)
                .toHashCode();
    }
}
