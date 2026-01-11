package com.social.demo.repositories;

import com.social.demo.models.Groups;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<Groups,Long> {
}
