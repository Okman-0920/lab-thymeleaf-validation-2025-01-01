package com.ll.tem.domain.post.post;

import com.ll.tem.domain.post.post.entity.Post;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/posts")
public class PostController {
        private List<Post> posts = new ArrayList<>() {
                {
                        add(
                                        Post.builder()
                                                        .title("제목1")
                                                        .content("내용1")
                                                        .build());

                        add(
                                        Post.builder()
                                                        .title("제목2")
                                                        .content("내용2")
                                                        .build());

                        add(
                                        Post.builder()
                                                        .title("제목3")
                                                        .content("내용3")
                                                        .build());
                }
        };

        private String getFormHtml(String errorMessage, String title, String content) {
                return """
                                <div>%s<div>
                                <form method="POST">
                                    <input type="text" name="title" placeholder="제목" value="%s">
                                    <br>
                                    <textarea name="content" placeholder="내용">%s</textarea>
                                    <br>
                                    <button type="submit">글쓰기</button>
                                </form>
                                """.formatted(errorMessage, title, content);
        }

        @GetMapping
        @ResponseBody
        public String showList() {
                String ul = "<ul>" + posts
                                .reversed()
                                .stream()
                                .map(post -> "<li>%s</li>".formatted(post.getTitle()))
                                .collect(Collectors.joining()) + "</ul>";

                String body = """
                                <h1>글 목록</h1>

                                %s

                                <a href="/post/write">글쓰기</a>
                                """.formatted(ul);
                // ul(unordered list): 순서가 중요하지 않은 목록
                // ol(ordered list): 순서가 중요한 목록
                // li(list item): 목록 안의 요소들

                return body;
        }

        @GetMapping("/write")
        @ResponseBody
        public String showWrite() {
                return getFormHtml("", "", "");
        }

        // 다른곳에서 이거 사용 못하게 private
        private record PostWriteForm(
                        @NotBlank(message = "01-제목을 입력해주세요.") @Length(min = 5, message = "02-제목을 5자이상 입력해주세요") String title,
                        @NotBlank(message = "03-내용을 입력해주세요") @Length(min = 10, message = "04-내용을 10자이상 입력해주세요") String content) {
        }

        @PostMapping("/write")
        @ResponseBody
        public String write(@Valid PostWriteForm form, BindingResult bindingResult) {
                if (bindingResult.hasErrors()) {
                        String errorMessage = bindingResult.getAllErrors()
                                        .stream()
                                        .map(error -> error.getDefaultMessage())
                                        .sorted()
                                        .map(message -> message.split("-", 2)[1])
                                        // split: 문자열을 특정 구분자로 나누어 배열로 반환 (기준으로 반으로 쪼갠다는 의미)
                                        // "-" : - 를 구분자로 지정
                                        // 2 : - 구분자를 기준으로 2개로 나눈다는 의미
                                        // 1 : - 그중 1번째 내용을 출력 (배열은 0부터 시작이어서 1임)
                                        .collect(Collectors.joining("<br>"));

                        return getFormHtml(
                                        errorMessage,
                                        form.title(),
                                        form.content());
                }
                return """
                                <h1> 글쓰기 완료 </h1>
                                <div>
                                    <h2>%s</h2>
                                    <p>%s</p>
                                </div>
                                """.formatted(form.title(), form.content());
        }
}
