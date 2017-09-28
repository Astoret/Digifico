package org.atore.movefavorites.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.atore.movefavorites.constant.Constants;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name="user",
        indexes = {@Index(name = "idx_user_userName",  columnList="userName", unique = true)})
public class User extends ResourceSupport implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userId;

    @Column(nullable = false)
    private String userName;

    @JsonIgnore
    private String password;

    @OneToMany(fetch = FetchType.LAZY)
    private List<UsersList> usersLists;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<UsersList> getUsersLists() {
        return usersLists;
    }

    public void setUsersLists(List<UsersList> usersLists) {
        this.usersLists = usersLists;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        final List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(Constants.SECURITY_USER_ROLE));
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getUsername() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
