package com.social.demo.services;

import com.social.demo.models.SocialUser;
import com.social.demo.repositories.SocialUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SocialService {

    @Autowired
    SocialUserRepository socialUserRepository;

    public List<SocialUser> getAllUsers() {
        return socialUserRepository.findAll();
    }

    public SocialUser saveUser(SocialUser socialUser) {
        // 🔑 ensure owning side of OneToOne is set
        if (socialUser.getSocialProfile() != null) {
            socialUser.getSocialProfile().setUser(socialUser);
        }
            return socialUserRepository.save(socialUser);
    }

    public SocialUser delete(Long id) {
        SocialUser socialUser=socialUserRepository.findById(id)
                .orElseThrow(()->new RuntimeException("User not found"));
         socialUserRepository.delete(socialUser);
         return socialUser;
    }
}
