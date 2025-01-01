package com.ll.tem.domain.post.post.controller;

import com.ll.tem.domain.post.post.entity.Post;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/posts")
public class PostController {
    private List<Post> posts = new ArrayList<>() {{
        add(
                Post.builder()
                        .title("제목1")
                        .content("내용1")
                        .build()
        );

        add(
                Post.builder()
                        .title("제목2")
                        .content("내용2")
                        .build()
        );

        add(
                Post.builder()
                        .title("제목3")
                        .content("내용3")
                        .build()
        );
    }};

    @GetMapping
//  @ResponseBody // list.html 사용 전
    public String showList(Model model) {
        model.addAttribute("posts", posts.reversed());
/*        String ul = "<ul>" + posts
                .reversed()
                .stream()
                .map(post -> "<li>%s</li>".formatted(post.getTitle()))
                .collect(Collectors.joining()) + "</ul>";

        String body = """
                <h1>글 목록</h1>
                
                %s
                
                <a href="/posts/write">글쓰기</a>
                """.formatted(ul);
        // ul(unordered list): 순서가 중요하지 않은 목록
        // ol(ordered list): 순서가 중요한 목록
        // li(list item): 목록 안의 요소들

        return body;*/// list.html 사용 전
        return "domain/post/post/list";
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
        /* String errorMessage = bindingResult.getAllErrors()
                    .stream()
                    .map(error -> error.getDefaultMessage())
                    .sorted()
                    .map(message -> message.split("-", 2)[1])
                    // split: 문자열을 특정 구분자로 나누어 배열로 반환 (기준으로 반으로 쪼갠다는 의미)
                    // "-" : - 를 구분자로 지정
                    // 2 : - 구분자를 기준으로 2개로 나눈다는 의미
                    // 1 : - 그중 1번째 내용을 출력 (배열은 0부터 시작이어서 1임)
                    .collect(Collectors.joining("<br>"));

            model.addAttribute("errorMessage", errorMessage);
            */// 암기: 스트림으로 에러메시지 표현
            return "domain/post/post/write";

        }
        posts.add(
                Post.builder()
                        .title(form.title)
                        .content(form.content)
                        .build()
        );

        return "redirect:/posts";
    }
}