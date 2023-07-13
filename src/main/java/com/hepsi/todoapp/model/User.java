package com.hepsi.todoapp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.hepsi.todoapp.enums.ERole;
import com.hepsi.todoapp.enums.RegisterType;
import lombok.*;
import org.hibernate.annotations.Where;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Collections;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "USERS")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Inheritance(strategy = InheritanceType.JOINED)
@EqualsAndHashCode
@Where(clause = "is_deleted = false")
public class User extends BaseModel implements Serializable, UserDetails {

    private static final long serialVersionUID = -6911002390558341442L;

    private String emailAddress;

    private String password;

    private String name;
    private ERole role;

    private RegisterType registerType;

    @Builder.Default
    private Boolean locked = false;

    private Timestamp lockedAt;

    @Builder.Default
    private Boolean enabled = false;

    @Builder.Default
    private Boolean reactivated = false;

    @OneToOne(mappedBy = "user")
    @JsonManagedReference
    private ConfirmationToken confirmationToken;

    @OneToOne(mappedBy = "user")
    @JsonManagedReference
    private ResetPasswordToken resetPasswordToken;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        final SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(role.name());
        return Collections.singletonList(simpleGrantedAuthority);
    }

    @Override
    public String getUsername() {
        return emailAddress;
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
        return enabled;
    }
}
