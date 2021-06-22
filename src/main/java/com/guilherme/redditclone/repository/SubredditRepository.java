package com.guilherme.redditclone.repository;

import java.util.Optional;

import com.guilherme.redditclone.model.Subreddit;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubredditRepository extends JpaRepository<Subreddit, Long> {
    Optional<Subreddit> findByName(String subredditName);
}
