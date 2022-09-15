package boardexample.myboard.web.post;


import boardexample.myboard.domain.post.Post;
import boardexample.myboard.web.dto.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/postapi")
public class PostRestController {
    private final PostService postService;

    @GetMapping("/{postId}")
    public PostResponseDto findById(@PathVariable("postId") Long id){
        return postService.findByPostDto(id);
    }

    @GetMapping
    public Result findAllPost(@PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable){
        Page<PostResponseDto> postResponseDtoList = postService.allPostDto(pageable);
        return new Result<>(postResponseDtoList);
    }
}
