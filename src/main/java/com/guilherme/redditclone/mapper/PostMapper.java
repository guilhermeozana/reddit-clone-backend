package com.guilherme.redditclone.mapper;

import com.guilherme.redditclone.dto.PostRequest;
import com.guilherme.redditclone.model.Post;
import com.guilherme.redditclone.model.Subreddit;
import com.guilherme.redditclone.model.User;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PostMapper {
    
    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    @Mapping(target = "subreddit", source = "postRequest.subreddit")
    @Mapping(target = "user", source = "postRequest.user")
    @Mapping(target = "description", source = "postRequest.description")
    Post mapDtoToPost(PostRequest postRequest, Subreddit subreddit, User user);
}
