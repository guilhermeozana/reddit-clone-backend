package com.guilherme.redditclone.service;

import java.util.List;

import com.guilherme.redditclone.dto.SubredditDto;
import com.guilherme.redditclone.exception.RedditCloneException;
import com.guilherme.redditclone.mapper.SubredditMapper;
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
    private final SubredditMapper subredditMapper;
    
    @Transactional
    public Subreddit create(SubredditDto subredditDto){
        Subreddit subreddit = subredditMapper.mapDtoToSubreddit(subredditDto);
        return subredditRepository.save(subreddit);
    }

    @Transactional(readOnly = true)
    public List<Subreddit> getAll(){
        return subredditRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Subreddit getById(Long id){
        return subredditRepository.findById(id).orElseThrow(() -> new RedditCloneException("No subreddit found with id "+id));
    }
}
