package boardexample.myboard.web.post;

import boardexample.myboard.domain.post.Post;
import boardexample.myboard.domain.user.User;
import boardexample.myboard.web.session.SessionConst;
import boardexample.myboard.web.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.Banner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {
    private final PostService postService;

    @GetMapping("/add")
    public String postAddForms(@ModelAttribute("form")postSaveForm form){
        return "post/postAddForm";
    }

    @PostMapping("/add")
    public String postAdd(@ModelAttribute("form")postSaveForm form, HttpServletRequest request){
        HttpSession session = request.getSession();
        User loginUser = (User) session.getAttribute(SessionConst.LOGIN_USER);

        postService.save(loginUser.getId(), form);

        return "redirect:/";
    }

    //@GetMapping("/list")
    public String postList(Model model){
        List<Post> allPosts = postService.findAllPosts();
        model.addAttribute("posts", allPosts);
        return "post/postList";
    }

    @GetMapping("/list")
    public String postPaging(@PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable, Model model){
        Page<Post> posts = postService.postList(pageable);
        int nowPage = posts.getPageable().getPageNumber() + 1;
        int startPage = Math.max(nowPage -4 , 1);
        int endPage = Math.min(nowPage + 5, posts.getTotalPages());
        model.addAttribute("posts", posts);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        return "post/postList";
    }


    @GetMapping("/{postId}")
    public String postView(@PathVariable("postId") Long postId, Model model){
        Post post = postService.findPost(postId);
        User user = post.getUser();
        model.addAttribute("post", post);
        model.addAttribute("user", user);

        return "post/postView";
    }


    @GetMapping("/{postId}/delete")
    public String postDelete(@PathVariable("postId") Long postId){
        postService.delete(postId);
        return "redirect:/post/list";
    }

    @GetMapping("/{postId}/edit")
    public String postUpdateForm(@PathVariable("postId") Long postId, Model model) {
        Post post = postService.findPost(postId);
        model.addAttribute("form", post);
        return "post/postEditForm";
    }

    @PostMapping("/{postId}/edit")
    public String postUpdate(@PathVariable("postId") Long postId, @ModelAttribute("form")postUpdateForm form,
                             HttpServletRequest request){
        HttpSession session = request.getSession();
        User loginUser =(User) session.getAttribute(SessionConst.LOGIN_USER);
        Long loginUserId = loginUser.getId();

        postService.updatePost(postId, loginUserId, form);
        return "redirect:/post/list";
    }

}
