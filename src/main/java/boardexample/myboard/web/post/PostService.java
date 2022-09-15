package boardexample.myboard.web.post;

import boardexample.myboard.domain.post.Post;
import boardexample.myboard.domain.post.PostRepository;
import boardexample.myboard.domain.user.User;
import boardexample.myboard.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    //view
    @Transactional
    public Long save(Long userId, postSaveForm form){
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalStateException("해당 회원이 없습니다."));

        form.setUser(user);
        return postRepository.save(form.toEntity()).getId();
    }

    @Transactional
    public void updatePost(Long postId, Long userId,postUpdateForm form){
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalStateException("해당 회원이 없습니다."));

        Post findPost = findPost(postId);
        validateUser(user, findPost);

        findPost.updatePost(form.getTitle(), form.getContent(), form.getUpdateTable());
    }

    public Post findPost(Long postId){
       return postRepository.findById(postId)
                .orElseThrow(() -> new IllegalStateException("찾는 게시물이 없습니다."));
    }

    public List<Post> findAllPosts(){
        return postRepository.findAll();
    }

    public Page<Post> postList(Pageable pageable){
        return postRepository.findAll(pageable);
    }


    @Transactional
    public void delete(Long postId){
        Post post = findPost(postId);
        postRepository.delete(post);
    }

    //restApi

    public PostResponseDto findByPostDto(Long postId){
        return postRepository.findOnePostDto(postId);
    }

    public Page<PostResponseDto> allPostDto(Pageable pageable){
        return postRepository.findAllPostDto(pageable);
    }

    //
    private void validateUser(User user, Post findPost) {
        if(user.getId() != findPost.getUser().getId()){
            throw new IllegalStateException("작성자와 수정자가 다릅니다.");
        }
    }


}
