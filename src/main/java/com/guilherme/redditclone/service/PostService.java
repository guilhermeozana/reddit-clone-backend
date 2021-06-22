package com.guilherme.redditclone.service;

import java.util.List;
import java.util.Optional;

import com.guilherme.redditclone.dto.PostRequest;
import com.guilherme.redditclone.exception.RedditCloneException;
import com.guilherme.redditclone.exception.SubredditNotFoundException;
import com.guilherme.redditclone.exception.UserNotFoundException;
import com.guilherme.redditclone.mapper.PostMapper;
import com.guilherme.redditclone.model.Post;
import com.guilherme.redditclone.model.Subreddit;
import com.guilherme.redditclone.model.User;
import com.guilherme.redditclone.repository.PostRepository;
import com.guilherme.redditclone.repository.SubredditRepository;
import com.guilherme.redditclone.repository.UserRepository;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {
    private final PostRepository postRepository;
    private final SubredditRepository subredditRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final PostMapper postMapper;

    @Transactional
    public Post createPost(PostRequest postRequest){
        String subredditName = postRequest.getSubredditName();
        Subreddit subreddit = subredditRepository.findByName(subredditName).orElseThrow(() -> 
                new RedditCloneException("No subreddit "+subredditName+"found"));

        User user = authService.getCurrentUser();

        return postMapper.mapDtoToPost(postRequest, subreddit, user);
    }

    @Transactional(readOnly = true)
	public List<Post> getAllPosts() {
		return postRepository.findAll();
	}

    @Transactional(readOnly = true)
    public Post getPost(Long id) {
        return postRepository.findById(id).orElseThrow(()-> new RedditCloneException("No post found with id "+id));
    }

    @Transactional(readOnly = true)
    public List<Post> getPostsBySubreddit(Long subredditId) {
        Subreddit subreddit = subredditRepository.findById(subredditId).orElseThrow(() -> 
                new SubredditNotFoundException(subredditId.toString()));

        return postRepository.findBySubreddit(subreddit);
    }

    @Transactional(readOnly = true)
    public List<Post> getPostsByUsername(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
        return postRepository.findByUser(user);
    }
    
}
