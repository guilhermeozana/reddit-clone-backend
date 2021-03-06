package com.guilherme.redditclone.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubredditDto {
    private String name;
    private String description;
    private Integer numberOfPosts;
}
