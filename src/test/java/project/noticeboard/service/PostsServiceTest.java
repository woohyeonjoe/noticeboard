package project.noticeboard.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import project.noticeboard.domain.posts.Posts;
import project.noticeboard.domain.posts.PostsRepository;
import project.noticeboard.service.posts.PostsService;
import project.noticeboard.web.dto.PostsResponseDto;
import project.noticeboard.web.dto.PostsSaveRequestDto;
import project.noticeboard.web.dto.PostsUpdateRequestDto;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
@SpringBootTest
public class PostsServiceTest {
    @Autowired
    private PostsRepository postRepository;

    @Autowired
    private PostsService postService;

    @AfterEach
    void clean() {
        postRepository.deleteAll();
    }

    @DisplayName("글이 저장된다.")
    @Test
    void addTest() {
        //given
        PostsSaveRequestDto addPost = PostsSaveRequestDto.builder()
                .title("제목")
                .content("내용")
                .author("작성자")
                .build();

        //when
        postService.save(addPost);

        //then
        assertThat(postRepository.findAll().size()).isEqualTo(1);
        assertThat(postRepository.findAll().get(0).getTitle()).isEqualTo("제목");
        assertThat(postRepository.findAll().get(0).getContent()).isEqualTo("내용");
        assertThat(postRepository.findAll().get(0).getAuthor()).isEqualTo("작성자");
    }

    @DisplayName("id로 글이 조회된다.")
    @Test
    void findById() {
        //given
        Posts post = Posts.builder()
                .title("제목")
                .content("내용")
                .author("작성자")
                .build();

        postRepository.save(post);

        //when
        PostsResponseDto findPost = postService.findById(post.getId());

        //then
        assertThat(findPost.getId()).isEqualTo(post.getId());
        assertThat(findPost.getTitle()).isEqualTo("제목");
        assertThat(findPost.getContent()).isEqualTo("내용");
        assertThat(findPost.getAuthor()).isEqualTo("작성자");

    }


    @DisplayName("글 수정 테스트")
    @Test
    void editPost() {
        Posts post = Posts.builder()
                .title("제목")
                .content("내용")
                .build();

        postRepository.save(post);

        post.update("제목 수정", "내용 수정");

        assertThat(post.getTitle()).isEqualTo("제목 수정");
        assertThat(post.getContent()).isEqualTo("내용 수정");
    }

    @DisplayName("글 삭제 테스트")
    @Test
    void deletePost() {
        Posts post = Posts.builder()
                .title("제목")
                .content("내용")
                .build();

        postRepository.save(post);

        postService.delete(post.getId());

        assertThat(postRepository.findAll().size()).isEqualTo(0);
        assertThatThrownBy(() -> postService.findById(post.getId()));
    }

}
