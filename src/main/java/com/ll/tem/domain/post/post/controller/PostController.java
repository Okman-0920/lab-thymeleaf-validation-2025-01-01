package com.ll.tem.domain.post.post.controller;

import com.ll.tem.domain.post.post.entity.Post;
import com.ll.tem.domain.post.post.repository.PostService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    public String showList(Model model){
        List<Post> posts = postService.findAllByOrderByIdDesc();
        model.addAttribute("posts", posts);

        return "domain/post/post/list";
    }

    @GetMapping("{id}")
    public String showDetail(Model model, @PathVariable long id) {
        Post post = postService.findById(id).get();
        model.addAttribute("post", post);
        // 글 하나 보여주니까 post (맥락으로 선택한 내용임)

        return "domain/post/post/detail";
    }

    // 다른곳에서 이거 사용 못하게 private
    private record PostWriteForm(
            @NotBlank(message = "제목을 입력해주세요.")
            @Length(min = 2, message = "제목을 5자 이상 입력해주세요.")
            String title,
            @NotBlank(message = "내용을 입력해주세요.")
            @Length(min = 2, message = "내용을 10자 이상 입력해주세요.")
            String content
    ) { }

    @GetMapping("/write")
    public String showWrite(
            PostWriteForm form
    ) {
        return "domain/post/post/write";
    }

    @PostMapping("/write")
    public String write(
            @ModelAttribute("form") @Valid PostWriteForm form,
            // 암기: @Model~ : 요청 데이터를 Java객체로 변환하는 어노테이션
            // 암기: 위처럼 하면 타임리프 form이라는 변수로 가능함 (생략 가능)
            BindingResult bindingResult,
            // 암기: 사용자의 입력값에 대한 결과를 보관
            Model model
            // 암기: 사용자가 입력한 데이터를 보관
    ) {
        if (bindingResult.hasErrors()) {
            return "domain/post/post/write";

        }
        postService.write(form.title, form.content);

        return "redirect:/posts";
    }
}