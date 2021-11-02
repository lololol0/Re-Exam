package com.example.ReExam.models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "usr")
@Data
public class User implements UserDetails {
    @Id
    private String id;

    private String username;

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "user_rooms",
            joinColumns = { @JoinColumn(name = "room_id") },
            inverseJoinColumns = { @JoinColumn(name = "user_id")}
    )
    private List<Room> rooms;

    public List<Room> getRooms() {
        if (rooms == null) {
            rooms = new ArrayList<>();
        }
        return rooms;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return Optional.ofNullable(username).orElse("guest");
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
