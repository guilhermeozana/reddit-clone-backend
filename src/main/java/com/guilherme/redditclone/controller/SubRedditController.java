package com.guilherme.redditclone.controller;

import java.util.List;

import com.guilherme.redditclone.dto.SubredditDto;
import com.guilherme.redditclone.model.Subreddit;
import com.guilherme.redditclone.service.SubredditService;

import org.hibernate.validator.constraints.pl.REGON;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/subreddit")
@RequiredArgsConstructor
@Log4j2
public class SubRedditController {
    private final SubredditService subredditService;
    
    @PostMapping
    public ResponseEntity<Subreddit> createSubreddit(@RequestBody SubredditDto subredditDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(subredditService.save(subredditDto));
    }

    @GetMapping()
    public ResponseEntity<List<Subreddit>> getAllSubreddits(){
        return ResponseEntity.status(HttpStatus.OK)
                .body(subredditService.getAll());
    }
    
}
