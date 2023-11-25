package karizma.recettecuisineback.service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import karizma.recettecuisineback.beans.Utilisateur;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Objects;


public class UserDetailsImpl implements UserDetails {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String username;

    @JsonIgnore
    private String password;

    public UserDetailsImpl(Long id , String username,String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public static UserDetailsImpl build(Utilisateur user) {
        return new UserDetailsImpl(user.getId(), user.getUsername(),user.getPassword());
    }


    public Long getId() {
        return this.id;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    public String getPassword() {
        return this.password;
    }

    public String getUsername() {
        return this.username;
    }

    public boolean isAccountNonExpired() {
        return true;
    }

    public boolean isAccountNonLocked() {
        return true;
    }

    public boolean isCredentialsNonExpired() {
        return true;
    }

    public boolean isEnabled() {
        return true;
    }



    public static long getSerialversionuid() {
        return 1L;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            UserDetailsImpl user = (UserDetailsImpl)o;
            return Objects.equals(this.id, user.id);
        } else {
            return false;
        }
    }

}
