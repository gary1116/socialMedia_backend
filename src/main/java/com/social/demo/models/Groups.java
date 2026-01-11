package com.social.demo.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Groups {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany(mappedBy = "groups")
    private Set<SocialUser> socialUsers= new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<SocialUser> getSocialUsers() {
        return socialUsers;
    }

    public void setSocialUsers(Set<SocialUser> socialUsers) {
        this.socialUsers = socialUsers;
    }
}
