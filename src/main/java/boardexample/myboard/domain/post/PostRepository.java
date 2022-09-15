package boardexample.myboard.domain.post;

import boardexample.myboard.web.post.PostResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    @Query("select new boardexample.myboard.web.post.PostResponseDto(p.id, p.title, p.content, p.date, p.updateTable, u.email) " +
            "from Post p join p.user u where p.id = :id")
    public PostResponseDto findOnePostDto(@Param("id") Long postId);

    @Query("select new boardexample.myboard.web.post.PostResponseDto(p.id, p.title, p.content, p.date, p.updateTable, u.email) " +
            "from Post p join p.user u")
    public Page<PostResponseDto> findAllPostDto(Pageable pageable);
}
