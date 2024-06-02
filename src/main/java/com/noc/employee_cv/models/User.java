package com.noc.employee_cv.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.noc.employee_cv.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Collection;

@Entity
@Table(name = "_user")
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@Builder
@ToString
public class User implements UserDetails, Principal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(unique = true)
    private String username;
    private String password;
    private String firstname;
    private String lastname;
    private String imageName;
    private String imagePath;
    @Column(unique = true)
    private String email;
    private boolean accountLocked;
    private boolean enabled;

    @Enumerated(EnumType.STRING)
    private Role role;
//    @ManyToMany(fetch = FetchType.EAGER)
//    private Set<Role> roles;
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;
    @Column(nullable = false)
    private LocalDateTime updatedDate;
    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        createdDate = now;
        updatedDate = now;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedDate = LocalDateTime.now();
    }


    @JsonIgnore
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
    private Employee employee;



    @Override
    public String getName() {
        return this.email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getAuthorities();
    }
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return this.roles
//                .stream()
//                .map(r->new SimpleGrantedAuthority(r.getName()))
//                .collect(Collectors.toList());
//    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !accountLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public String getFullName() {
        return firstname + " " + lastname;
    }
}
