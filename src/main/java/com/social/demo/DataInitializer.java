package com.social.demo;

import com.social.demo.repositories.PostRepository;
import com.social.demo.repositories.SocialProfileRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.social.demo.models.Post;
import com.social.demo.models.Groups;
import com.social.demo.models.SocialProfile;
import com.social.demo.models.SocialUser;
import com.social.demo.repositories.GroupRepository;
import com.social.demo.repositories.SocialUserRepository;

@Configuration
public class DataInitializer {

//    initialized every repository
    private final SocialUserRepository userRepository;
    private final GroupRepository groupRepository;
    private final SocialProfileRepository socialProfileRepository;
    private final PostRepository postRepository;

//    initialized them with help of constructor-> constructor injection
    public DataInitializer(SocialUserRepository userRepository, GroupRepository groupRepository, SocialProfileRepository socialProfileRepository, PostRepository postRepository) {
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
        this.socialProfileRepository = socialProfileRepository;
        this.postRepository = postRepository;
    }

    // created a bean of CommandLineRunner this bean gets executed when applicationContext gets initialized
    @Bean
    public CommandLineRunner initializeData() {
        return args -> {
            // Create some users
            SocialUser user1 = new SocialUser();
            SocialUser user2 = new SocialUser();
            SocialUser user3 = new SocialUser();

            // Save users to the database
            userRepository.save(user1);
            userRepository.save(user2);
            userRepository.save(user3);

            // Create some groups
            Groups group1 = new Groups();
            Groups group2 = new Groups();

            // Add users to groups
//            user1, user2 gettig added in group1
            group1.getSocialUsers().add(user1);
            group1.getSocialUsers().add(user2);

//            user 2, and user 3 gtting added in group2
            group2.getSocialUsers().add(user2);
            group2.getSocialUsers().add(user3);

            // Save groups to the database
            groupRepository.save(group1);
            groupRepository.save(group2);

            // Associate users with groups
            user1.getGroups().add(group1);
            user2.getGroups().add(group1);
            user2.getGroups().add(group2);
            user3.getGroups().add(group2);

            // Save users back to database to update associations
            userRepository.save(user1);
            userRepository.save(user2);
            userRepository.save(user3);


            // Create some posts
            Post post1 = new Post();
            Post post2 = new Post();
            Post post3 = new Post();

            // Associate posts with users
            post1.setSocialUser(user1);
            post2.setSocialUser(user2);
            post3.setSocialUser(user3);

            // Save posts to the database (assuming you have a PostRepository)
            postRepository.save(post1);
            postRepository.save(post2);
            postRepository.save(post3);

            // Create some social profiles
            SocialProfile profile1 = new SocialProfile();
            SocialProfile profile2 = new SocialProfile();
            SocialProfile profile3 = new SocialProfile();

            // Associate profiles with users
            profile1.setUser(user1);
            profile2.setUser(user2);
            profile3.setUser(user3);

            // Save profiles to the database (assuming you have a SocialProfileRepository)
            socialProfileRepository.save(profile1);
            socialProfileRepository.save(profile2);
            socialProfileRepository.save(profile3);
        };
    }
}
