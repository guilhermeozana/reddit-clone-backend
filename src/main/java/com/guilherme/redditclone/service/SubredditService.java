package com.guilherme.redditclone.service;

import java.util.List;

import com.guilherme.redditclone.dto.SubredditDto;
import com.guilherme.redditclone.model.Subreddit;
import com.guilherme.redditclone.repository.SubredditRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class SubredditService {
    private final SubredditRepository subredditRepository;
    
    @Transactional
    public Subreddit save(SubredditDto subredditDto){
        Subreddit subreddit = Subreddit.builder()
            .subredditName(subredditDto.getName())
            .description(subredditDto.getDescription())
            .build();
        return subredditRepository.save(subreddit);
    }

    @Transactional(readOnly = true)
    public List<Subreddit> getAll(){
        return subredditRepository.findAll();
    }
}
