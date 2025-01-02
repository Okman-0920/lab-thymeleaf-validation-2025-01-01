package com.ll.tem.global.jpa.entity.initData;

import com.ll.tem.domain.post.post.entity.Post;
import com.ll.tem.domain.post.post.repository.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@RequiredArgsConstructor
public class BaseInitData {
    private final PostService postService;

    @Autowired
    @Lazy
    private BaseInitData self;

    @Bean
    public ApplicationRunner baseInitDataApplicationRunner() {
        return args -> {
            self.work1();
        };
    }

    @Transactional
    public void work1() {
        if (postService.count() > 0) return;

        Post post1 = postService.write("글1", "내용1");
        Post post2 = postService.write("글2", "내용2");
        Post post3 = postService.write("글3", "내용3");
    }
}
