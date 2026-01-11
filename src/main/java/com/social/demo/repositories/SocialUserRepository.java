package com.social.demo.repositories;

import com.social.demo.models.SocialUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SocialUserRepository extends JpaRepository<SocialUser,Long> {
}
