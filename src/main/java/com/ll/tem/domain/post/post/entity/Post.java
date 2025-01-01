package com.ll.tem.domain.post.post.entity;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Post {
    private static long lastId;

    @Builder.Default
    private Long Id = ++lastId;
    private String title;
    private String content;

}

