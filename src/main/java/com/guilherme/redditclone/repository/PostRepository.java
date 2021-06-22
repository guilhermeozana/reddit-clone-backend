package com.guilherme.redditclone.repository;

import java.util.List;

import com.guilherme.redditclone.model.Post;
import com.guilherme.redditclone.model.Subreddit;
import com.guilherme.redditclone.model.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findBySubreddit(Subreddit subreddit);

    List<Post> findByUser(User user);

}
